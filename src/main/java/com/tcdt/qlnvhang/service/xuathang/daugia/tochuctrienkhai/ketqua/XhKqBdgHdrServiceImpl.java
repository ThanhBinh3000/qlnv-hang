package com.tcdt.qlnvhang.service.xuathang.daugia.tochuctrienkhai.ketqua;

import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.pheduyet.XhQdPdKhBdg;
import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.pheduyet.XhQdPdKhBdgDtl;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.xuathang.daugia.hopdong.XhHopDongHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.pheduyet.XhQdPdKhBdgDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.tochuctrienkhai.ketqua.XhKqBdgHdrRepository;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.tochuctrienkhai.ketqua.XhKqBdgHdrReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.entities.xuathang.daugia.tochuctrienkhai.ketqua.XhKqBdgHdr;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.table.report.ReportTemplateRequest;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import fr.opensagres.xdocreport.core.XDocReportException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@Service
public class XhKqBdgHdrServiceImpl extends BaseServiceImpl implements XhKqBdgHdrService {

  @Autowired
  private XhKqBdgHdrRepository xhKqBdgHdrRepository;

  @Autowired
  private XhHopDongHdrRepository xhHopDongHdrRepository;

  @Autowired
  private XhQdPdKhBdgDtlRepository xhQdPdKhBdgDtlRepository;

  @Autowired
  private FileDinhKemService fileDinhKemService;

  @Override
  public Page<XhKqBdgHdr> searchPage(XhKqBdgHdrReq req) throws Exception {
    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit(), Sort.by("id").descending());
    Page<XhKqBdgHdr> page = xhKqBdgHdrRepository.search(req, pageable);
    Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    Map<String,String> mapHinhThuDg = getListDanhMucChung("HINH_THUC_DG");
    Map<String,String> mapPhuongThucDg = getListDanhMucChung("PHUONG_THUC_DG");
    page.getContent().forEach(f -> {
      try {
        f.setTenDvi(StringUtils.isEmpty(f.getMaDvi()) ? null : mapDmucDvi.get(f.getMaDvi()));
        f.setTenLoaiVthh(StringUtils.isEmpty(f.getLoaiVthh()) ? null : mapDmucVthh.get(f.getLoaiVthh()));
        f.setTenCloaiVthh(StringUtils.isEmpty(f.getCloaiVthh()) ? null : mapDmucVthh.get(f.getCloaiVthh()));
        f.setTenHinhThucDauGia(StringUtils.isEmpty(f.getHinhThucDauGia()) ? null : mapHinhThuDg.get(f.getHinhThucDauGia()));
        f.setTenPthucDauGia(StringUtils.isEmpty(f.getPthucDauGia())? null : mapPhuongThucDg.get(f.getPthucDauGia()));
      }catch (Exception e){
        throw new RuntimeException(e);
      }
    });
    return page;
  }

  @Override
  public XhKqBdgHdr create(XhKqBdgHdrReq req) throws Exception {
    UserInfo userInfo = SecurityContextService.getUser();
    if (userInfo == null)
      throw new Exception("Bad request.");

    if (!StringUtils.isEmpty(req.getSoQdKq())) {
      Optional<XhKqBdgHdr> qOptional = xhKqBdgHdrRepository.findBySoQdKq(req.getSoQdKq());
      if (qOptional.isPresent()) {
        throw new Exception("Số quyết định phê duyệt " + req.getSoQdKq() + " đã tồn tại");
      }
    }
    XhKqBdgHdr data = new XhKqBdgHdr();
    BeanUtils.copyProperties(req, data, "id");

    data.setNam(LocalDate.now().getYear());
    data.setNguoiTaoId(userInfo.getId());
    data.setNgayTao(new Date());
    data.setMaDvi(userInfo.getDvql());
    data.setTrangThaiHd(NhapXuatHangTrangThaiEnum.CHUA_THUC_HIEN.getId());
    data.setTrangThaiXh(NhapXuatHangTrangThaiEnum.CHUA_THUC_HIEN.getId());
    XhKqBdgHdr byMaThongBao = xhKqBdgHdrRepository.findByMaThongBao(req.getMaThongBao());
    if (!ObjectUtils.isEmpty(byMaThongBao)) {
      throw new Exception("Mã thông báo này đã được quyết định kết quả bán đấu giá, xin vui lòng chọn mã thông báo khác");
    }
    XhKqBdgHdr created = xhKqBdgHdrRepository.save(data);

    if (!DataUtils.isNullOrEmpty(req.getFileDinhKem())) {
      List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKem(), created.getId(), XhKqBdgHdr.TABLE_NAME+ "_BAN_HANH");
      created.setFileDinhKem(fileDinhKem);
    }
    if (!DataUtils.isNullOrEmpty(req.getFileDinhKems())) {
      List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), created.getId(), XhKqBdgHdr.TABLE_NAME);
      created.setFileDinhKems(fileDinhKems);
    }

    return created;
  }

  @Override
  public XhKqBdgHdr update(XhKqBdgHdrReq req) throws Exception {
    if (ObjectUtils.isEmpty(req.getId())) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    Optional<XhKqBdgHdr> byId = xhKqBdgHdrRepository.findById(req.getId());
    if (!byId.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    XhKqBdgHdr data = byId.get();
    BeanUtils.copyProperties(req, data, "id");
    data.setNgaySua(new Date());
    data.setNguoiSuaId(getUser().getId());
    data.setTrangThaiHd(NhapXuatHangTrangThaiEnum.CHUA_THUC_HIEN.getId());
    data.setTrangThaiXh(NhapXuatHangTrangThaiEnum.CHUA_THUC_HIEN.getId());
    XhKqBdgHdr created = xhKqBdgHdrRepository.save(data);

    fileDinhKemService.delete(data.getId(), Collections.singleton(XhKqBdgHdr.TABLE_NAME + "_BAN_HANH"));
    List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKem(), created.getId(), XhKqBdgHdr.TABLE_NAME + "_BAN_HANH");
    created.setFileDinhKem(fileDinhKem);

    fileDinhKemService.delete(data.getId(), Collections.singleton(XhKqBdgHdr.TABLE_NAME));
    List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), created.getId(), XhKqBdgHdr.TABLE_NAME);
    created.setFileDinhKems(fileDinhKems);

    return created;
  }

  @Override
  public XhKqBdgHdr detail(Long id) throws Exception {
    if (ObjectUtils.isEmpty(id)) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    Optional<XhKqBdgHdr> byId = xhKqBdgHdrRepository.findById(id);
    if (!byId.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    XhKqBdgHdr data = byId.get();

    Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    Map<String,String> mapLoaiHinhNx = getListDanhMucChung("LOAI_HINH_NHAP_XUAT");
    Map<String,String> mapKieuNx = getListDanhMucChung("KIEU_NHAP_XUAT");

    data.setTenDvi(StringUtils.isEmpty(data.getMaDvi()) ? null : mapDmucDvi.get(data.getMaDvi()));
    data.setTenLoaiVthh(StringUtils.isEmpty(data.getLoaiVthh()) ? null : mapDmucVthh.get(data.getLoaiVthh()));
    data.setTenCloaiVthh(StringUtils.isEmpty(data.getCloaiVthh()) ? null : mapDmucVthh.get(data.getCloaiVthh()));
    data.setTenLoaiHinhNx(StringUtils.isEmpty(data.getLoaiHinhNx())? null : mapLoaiHinhNx.get(data.getLoaiHinhNx()));
    data.setTenKieuNx(StringUtils.isEmpty(data.getKieuNx())? null : mapKieuNx.get(data.getKieuNx()));
    data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));

    data.setListHopDong(xhHopDongHdrRepository.findAllBySoQdKq(data.getSoQdKq()));
    Map<String, String> listDanhMucDvi = getListDanhMucDvi("2", null, "01");
    data.setTenDvi(listDanhMucDvi.get(data.getMaDvi()));
    List<FileDinhKem> fileDinhKem = fileDinhKemService.search(data.getId(), Arrays.asList(XhKqBdgHdr.TABLE_NAME+ "_BAN_HANH"));
    data.setFileDinhKem(fileDinhKem);
    List<FileDinhKem> fileDinhKems = fileDinhKemService.search(data.getId(), Arrays.asList(XhKqBdgHdr.TABLE_NAME));
    data.setFileDinhKems(fileDinhKems);
    return data;
  }

  @Override
  public XhKqBdgHdr approve(XhKqBdgHdrReq req) throws Exception {
    UserInfo userInfo = SecurityContextService.getUser();
    if (userInfo == null) {
      throw new Exception("Bad request.");
    }

    if (StringUtils.isEmpty(req.getId())) {
      throw new Exception("Không tìm thấy dữ liệu");
    }

    Optional<XhKqBdgHdr> optional = xhKqBdgHdrRepository.findById(req.getId());
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    }

    XhKqBdgHdr data = optional.get();
    String status = req.getTrangThai() + data.getTrangThai();
    if (req.getTrangThai().equals(NhapXuatHangTrangThaiEnum.DA_HOAN_THANH.getId())
        && data.getTrangThaiHd().equals(NhapXuatHangTrangThaiEnum.DANG_THUC_HIEN.getId())) {
      data.setTrangThaiHd(req.getTrangThai());
    } else {
      switch (status) {
        case Contains.CHODUYET_TP + Contains.DUTHAO:
        case Contains.CHODUYET_TP + Contains.TUCHOI_TP:
        case Contains.CHODUYET_TP + Contains.TUCHOI_LDC:
          data.setNguoiGuiDuyetId(userInfo.getId());
          data.setNgayGuiDuyet(getDateTimeNow());
        case Contains.TUCHOI_TP + Contains.CHODUYET_TP:
        case Contains.TUCHOI_LDC + Contains.CHODUYET_LDC:
          data.setNguoiPduyetId(userInfo.getId());
          data.setNgayPduyet(getDateTimeNow());
          data.setLyDoTuChoi(req.getLyDoTuChoi());
          break;
        case Contains.CHODUYET_LDC + Contains.CHODUYET_TP:
        case Contains.BAN_HANH + Contains.CHODUYET_LDC:
          data.setNguoiPduyetId(userInfo.getId());
          data.setNgayPduyet(getDateTimeNow());
          break;
        default:
          throw new Exception("Phê duyệt không thành công");
      }
      data.setTrangThai(req.getTrangThai());
      if (req.getTrangThai().equals(Contains.BAN_HANH)) {
        Optional<XhQdPdKhBdgDtl> qdPdKhBttDtl = xhQdPdKhBdgDtlRepository.findById(data.getIdPdKhDtl());
        if (qdPdKhBttDtl.isPresent()){
          qdPdKhBttDtl.get().setSoQdPdKqBdg(data.getSoQdKq());
          qdPdKhBttDtl.get().setNgayKyQdPdKqBdg(data.getNgayKy());
          qdPdKhBttDtl.get().setIdQdPdKqBdg(data.getId());
          xhQdPdKhBdgDtlRepository.save(qdPdKhBttDtl.get());
        }
      }
    }
    return xhKqBdgHdrRepository.save(data);
  }

  @Override
  public void delete(Long id) throws Exception {
    if (StringUtils.isEmpty(id)) {
      throw new Exception("Xóa thất bại không tìm thấy dữ liệu");
    }
    Optional<XhKqBdgHdr> byId = xhKqBdgHdrRepository.findById(id);
    if (!byId.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    if (!byId.get().getTrangThai().equals(NhapXuatHangTrangThaiEnum.DUTHAO.getId())) {
      throw new Exception("Chỉ được xóa bản ghi với trang thái là dự thảo");
    }
    xhKqBdgHdrRepository.delete(byId.get());
    fileDinhKemService.delete(byId.get().getId(), Collections.singleton(XhKqBdgHdr.TABLE_NAME));
    fileDinhKemService.delete(byId.get().getId(), Collections.singleton(XhKqBdgHdr.TABLE_NAME + "_BAN_HANH"));

  }

  @Override
  public void deleteMulti(List<Long> listMulti) throws Exception {
    if (StringUtils.isEmpty(listMulti)) {
      throw new Exception("Xóa thất bại, không tìm thấy dữ liệu");
    }

    List<XhKqBdgHdr> list = xhKqBdgHdrRepository.findByIdIn(listMulti);
    if (list.isEmpty()) {
      throw new Exception("Không tìm thấy dữ liệu cần xóa");
    }

    for (XhKqBdgHdr hdr : list) {
      this.delete(hdr.getId());
    }
  }

  @Override
  public void export(XhKqBdgHdrReq objReq, HttpServletResponse response) throws Exception {
    PaggingReq paggingReq = new PaggingReq();
    paggingReq.setPage(0);
    paggingReq.setLimit(Integer.MAX_VALUE);
    objReq.setPaggingReq(paggingReq);
    Page<XhKqBdgHdr> page = this.searchPage(objReq);
    List<XhKqBdgHdr> data = page.getContent();

    String title = "Danh sách quyết định phê duyệt kết quả đấu giá";
    String[] rowsName = new String[]{"STT", "Năm Kế hoạch", "Số QĐ PD KQ BĐG", "Ngày ký", "Trích yếu",
        "Ngày tổ chức BĐG", "Số QĐ PD KH BĐG", "Mã thông báo BĐG", "Hình thức đấu giá", "Phướng thức đấu giá",
        "Số TB đấu giá không thành", "Số biên bản đấu giá", "Trạng thái"};
    String fileName = "danh-sach-quyet-dinh-phe-duyet-ket-qua-dau-gia.xlsx";
    List<Object[]> dataList = new ArrayList<Object[]>();
    Object[] objs = null;
    for (int i = 0; i < data.size(); i++) {
      XhKqBdgHdr hdr = data.get(i);
      objs = new Object[rowsName.length];
      objs[0] = i;
      objs[1] = hdr.getNam();
      objs[2] = hdr.getSoQdKq();
      objs[3] = hdr.getNgayKy();
      objs[4] = hdr.getTrichYeu();
      objs[5] = hdr.getNgayTao();
      objs[6] = hdr.getSoQdPd();
      objs[7] = hdr.getMaThongBao();
      objs[8] = hdr.getHinhThucDauGia();
      objs[9] = hdr.getPthucDauGia();
      objs[10] = hdr.getSoTbKhongThanh();
      objs[11] = hdr.getSoBienBan();
      objs[12] = hdr.getTenTrangThai();
      dataList.add(objs);
    }
    ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
    ex.export();
  }

  @Override
  public void exportQdHd(XhKqBdgHdrReq objReq, HttpServletResponse response) throws Exception {
    PaggingReq paggingReq = new PaggingReq();
    paggingReq.setPage(0);
    paggingReq.setLimit(Integer.MAX_VALUE);
    objReq.setPaggingReq(paggingReq);
    Page<XhKqBdgHdr> page = this.searchPage(objReq);
    List<XhKqBdgHdr> data = page.getContent();

    String title = "QUẢN LÝ KÝ HỢP ĐỒNG BÁN HÀNG DTQG THEO PHƯƠNG THỨC BÁN ĐẤU GIÁ";
    String[] rowsName = new String[]{"STT", "Năm Kế hoạch", "QĐ PD KHBĐG", "QĐ PD KQBĐG", "Tổng sô ĐV tài sản",
        "Số ĐVTS ĐG thành công", "SL HĐ đã ký", "Thời hạn thanh toán", "Chủng loại hành hóa", "ĐV thực hiện",
         "Tổng SL xuất bán", "Thành tiền(đ)", "Trạng thái HĐ", "Trạng thái XH"};
    String fileName = "quan-ly-ky-hop-dong-ban-hang-dtqg-theo-phuong-thuc-ban-dau-gia.xlsx";
    List<Object[]> dataList = new ArrayList<Object[]>();
    Object[] objs = null;
    for (int i = 0; i < data.size(); i++) {
      XhKqBdgHdr hdr = data.get(i);
      objs = new Object[rowsName.length];
      objs[0] = i;
      objs[1] = hdr.getNam();
      objs[2] = hdr.getSoQdPd();
      objs[3] = hdr.getSoQdKq();
      objs[4] = hdr.getTongDvts();
      objs[5] = hdr.getSoDvtsDgTc();
      objs[6] = hdr.getSlHdDaKy();
      objs[7] = hdr.getThoiHanTt();
      objs[8] = hdr.getTenDvi();
      objs[10] = hdr.getTongSlXuat();
      objs[11] = hdr.getThanhTien();
      objs[12] = hdr.getTenTrangThaiHd();
      objs[13] = hdr.getTenTrangThaiXh();
      dataList.add(objs);
    }
    ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
    ex.export();
  }
  @Override
  public ReportTemplateResponse preview(HashMap<String, Object> body) throws Exception {
    try {
//      ReportTemplateRequest reportTemplateRequest = new ReportTemplateRequest();
//      reportTemplateRequest.setFileName(DataUtils.safeToString(body.get("tenBaoCao")));
//      ReportTemplate model = findByTenFile(reportTemplateRequest);
//      byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
//      ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
      FileInputStream inputStream = new FileInputStream("src/main/resources/reports/bandaugia/Quyết định phê duyệt kết quả.docx");
      XhKqBdgHdr detail = this.detail(DataUtils.safeToLong(body.get("id")));
      return docxToPdfConverter.convertDocxToPdf(inputStream, detail);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (XDocReportException e) {
      e.printStackTrace();
    }
    return null;
  }
}

