package com.tcdt.qlnvhang.service.xuathang.xuatkhac.ktluongthuc;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.ktluongthuc.XhXkLtBbLayMauHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.ktluongthuc.XhXkTongHopRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktluongthuc.XhXkLtBbLayMauRequest;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.table.report.ReportTemplateRequest;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.kthanghoa.XhXkTongHopDtl;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.kthanghoa.XhXkTongHopHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktluongthuc.XhXkLtBbLayMauHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktluongthuc.XhXkLtPhieuKnClHdr;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import fr.opensagres.xdocreport.core.XDocReportException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class XhXkLtBbLayMauService extends BaseServiceImpl {


  @Autowired
  private XhXkLtBbLayMauHdrRepository xhXkLtBbLayMauHdrRepository;

  @Autowired
  private XhXkTongHopRepository xhXkTongHopRepository;

  @Autowired
  private FileDinhKemService fileDinhKemService;

  public Page<XhXkLtBbLayMauHdr> searchPage(CustomUserDetails currentUser, XhXkLtBbLayMauRequest req) throws Exception {
    req.setDvql(currentUser.getDvql());
    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
    Page<XhXkLtBbLayMauHdr> search = xhXkLtBbLayMauHdrRepository.search(req, pageable);
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    search.getContent().forEach(s -> {
      s.setMapDmucDvi(mapDmucDvi);
      s.setMapVthh(mapVthh);
      s.setTenTrangThai(TrangThaiAllEnum.getLabelById(s.getTrangThai()));

    });
    return search;
  }

  @Transactional
  public XhXkLtBbLayMauHdr save(CustomUserDetails currentUser, XhXkLtBbLayMauRequest objReq) throws Exception {
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    Optional<XhXkLtBbLayMauHdr> optional = xhXkLtBbLayMauHdrRepository.findBySoBienBan(objReq.getSoBienBan());
    if (optional.isPresent()) {
      throw new Exception("số biên bản đã tồn tại");
    }
    XhXkLtBbLayMauHdr data = new XhXkLtBbLayMauHdr();
    BeanUtils.copyProperties(objReq, data);
    data.setMaDvi(currentUser.getUser().getDepartment());
    data.setTrangThai(Contains.DUTHAO);
    data.getBbLayMauDtl().forEach(s -> s.setBbLayMauHdr(data));
    XhXkLtBbLayMauHdr created = xhXkLtBbLayMauHdrRepository.save(data);
    this.updateTongHopDtl(created, false);
    List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), created.getId(), XhXkLtBbLayMauHdr.TABLE_NAME);
    created.setFileDinhKems(fileDinhKems);
    List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(objReq.getCanCu(), created.getId(), XhXkLtBbLayMauHdr.TABLE_NAME + "_CAN_CU");
    created.setCanCu(canCu);
    List<FileDinhKem> niemPhong = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKemNiemPhong(), created.getId(), XhXkLtBbLayMauHdr.TABLE_NAME + "_NIEM_PHONG");
    created.setFileDinhKemNiemPhong(niemPhong);
    return created;
  }


  @Transactional
  public XhXkLtBbLayMauHdr update(CustomUserDetails currentUser, XhXkLtBbLayMauRequest objReq) throws Exception {
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    Optional<XhXkLtBbLayMauHdr> optional = xhXkLtBbLayMauHdrRepository.findById(objReq.getId());
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu cần sửa");
    }
    Optional<XhXkLtBbLayMauHdr> soDx = xhXkLtBbLayMauHdrRepository.findBySoBienBan(objReq.getSoBienBan());
    if (soDx.isPresent()) {
      if (!soDx.get().getId().equals(objReq.getId())) {
        throw new Exception("số biên bản đã tồn tại");
      }
    }
    XhXkLtBbLayMauHdr data = optional.get();
    BeanUtils.copyProperties(objReq, data, "id", "maDvi");
    data.getBbLayMauDtl().forEach(s -> {
      s.setBbLayMauHdr(data);
    });
    XhXkLtBbLayMauHdr created = xhXkLtBbLayMauHdrRepository.save(data);
    this.updateTongHopDtl(created, false);
    fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(XhXkLtBbLayMauHdr.TABLE_NAME));
    List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), created.getId(), XhXkLtBbLayMauHdr.TABLE_NAME);
    created.setFileDinhKems(fileDinhKems);
    fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(XhXkLtBbLayMauHdr.TABLE_NAME + "_CAN_CU"));
    List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(objReq.getCanCu(), created.getId(), XhXkLtBbLayMauHdr.TABLE_NAME + "_CAN_CU");
    created.setCanCu(canCu);
    fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(XhXkLtBbLayMauHdr.TABLE_NAME + "_NIEM_PHONG"));
    List<FileDinhKem> niemPhong = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKemNiemPhong(), created.getId(), XhXkLtBbLayMauHdr.TABLE_NAME + "_NIEM_PHONG");
    created.setFileDinhKemNiemPhong(niemPhong);
    return created;
  }


  public List<XhXkLtBbLayMauHdr> detail(List<Long> ids) throws Exception {
    if (DataUtils.isNullOrEmpty(ids)) throw new Exception("Tham số không hợp lệ.");
    List<XhXkLtBbLayMauHdr> optional = xhXkLtBbLayMauHdrRepository.findByIdIn(ids);
    if (DataUtils.isNullOrEmpty(optional)) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    List<XhXkLtBbLayMauHdr> allById = xhXkLtBbLayMauHdrRepository.findAllById(ids);
    allById.forEach(data -> {
      data.setMapDmucDvi(mapDmucDvi);
      data.setTenDvi(mapDmucDvi.get(data.getMaDvi()));
      data.setMapVthh(mapVthh);
      data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
      List<FileDinhKem> fileDinhKems = fileDinhKemService.search(data.getId(), Arrays.asList(XhXkLtBbLayMauHdr.TABLE_NAME));
      List<FileDinhKem> canCu = fileDinhKemService.search(data.getId(), Arrays.asList(XhXkLtBbLayMauHdr.TABLE_NAME + "_CAN_CU"));
      List<FileDinhKem> niemPhong = fileDinhKemService.search(data.getId(), Arrays.asList(XhXkLtBbLayMauHdr.TABLE_NAME + "_NIEM_PHONG"));
      data.setFileDinhKems(fileDinhKems);
      data.setCanCu(canCu);
      data.setFileDinhKemNiemPhong(niemPhong);
    });

    return allById;
  }

  @Transient
  public void delete(IdSearchReq idSearchReq) throws Exception {
    Optional<XhXkLtBbLayMauHdr> optional = xhXkLtBbLayMauHdrRepository.findById(idSearchReq.getId());
    if (!optional.isPresent()) {
      throw new Exception("Bản ghi không tồn tại");
    }
    XhXkLtBbLayMauHdr data = optional.get();
    fileDinhKemService.delete(data.getId(), Lists.newArrayList(XhXkLtBbLayMauHdr.TABLE_NAME));
    fileDinhKemService.delete(data.getId(), Lists.newArrayList(XhXkLtBbLayMauHdr.TABLE_NAME + "_CAN_CU"));
    fileDinhKemService.delete(data.getId(), Lists.newArrayList(XhXkLtBbLayMauHdr.TABLE_NAME + "_NIEM_PHONG"));
    this.updateTongHopDtl(data, true);
    xhXkLtBbLayMauHdrRepository.delete(data);
  }

  @Transactional
  public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
    List<XhXkLtBbLayMauHdr> list = xhXkLtBbLayMauHdrRepository.findAllByIdIn(idSearchReq.getIdList());

    if (list.isEmpty()) {
      throw new Exception("Bản ghi không tồn tại");
    }
    fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(XhXkLtBbLayMauHdr.TABLE_NAME));
    fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(XhXkLtBbLayMauHdr.TABLE_NAME + "_CAN_CU"));
    fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(XhXkLtBbLayMauHdr.TABLE_NAME + "_NIEM_PHONG"));
    xhXkLtBbLayMauHdrRepository.deleteAll(list);
  }

  @Transient
  public XhXkLtBbLayMauHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {

    if (StringUtils.isEmpty(statusReq.getId())) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    Optional<XhXkLtBbLayMauHdr> optional = xhXkLtBbLayMauHdrRepository.findById(Long.valueOf(statusReq.getId()));
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    }

    String status = statusReq.getTrangThai() + optional.get().getTrangThai();
    switch (status) {
      case Contains.CHODUYET_LDCC + Contains.DUTHAO:
      case Contains.CHODUYET_LDCC + Contains.TUCHOI_LDCC:
        optional.get().setNguoiGduyetId(currentUser.getUser().getId());
        optional.get().setNgayGduyet(LocalDate.now());
        break;
      case Contains.TUCHOI_LDCC + Contains.CHODUYET_LDCC:
        optional.get().setNguoiPduyetId(currentUser.getUser().getId());
        optional.get().setNgayPduyet(LocalDate.now());
        optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
        break;
      case Contains.DADUYET_LDCC + Contains.CHODUYET_LDCC:
        optional.get().setNguoiPduyetId(currentUser.getUser().getId());
        optional.get().setLanhDaoChiCuc(currentUser.getUser().getFullName());
        optional.get().setNgayPduyet(LocalDate.now());
        break;
      default:
        throw new Exception("Phê duyệt không thành công");
    }
    optional.get().setTrangThai(statusReq.getTrangThai());
    XhXkLtBbLayMauHdr created = xhXkLtBbLayMauHdrRepository.save(optional.get());
    this.updateTongHopDtl(created, false);
    return created;
  }

  public void export(CustomUserDetails currentUser, XhXkLtBbLayMauRequest objReq, HttpServletResponse response) throws Exception {
    PaggingReq paggingReq = new PaggingReq();
    paggingReq.setPage(0);
    paggingReq.setLimit(Integer.MAX_VALUE);
    objReq.setPaggingReq(paggingReq);
    Page<XhXkLtBbLayMauHdr> page = this.searchPage(currentUser, objReq);
    List<XhXkLtBbLayMauHdr> data = page.getContent();

    String title = "Danh sách biên bản lấy mẫu bàn giao mẫu ";
    String[] rowsName = new String[]{"STT", "Năm KH", "Mã DS LT <= 6 tháng hết hạn lưu kho", "Điểm Kho",  "Lô kho","Tồn kho","SL hết hạn (<= 6 tháng)",
        "DVT","Thời hạn lưu kho (tháng)", "Số BB LM/BGM", "Ngày lấy mẫu", "Trạng thái"};
    String fileName = "danh-sach-bien-ban-lay-mau-ban-giao-mau.xlsx";
    List<Object[]> dataList = new ArrayList<Object[]>();
    Object[] objs = null;
    for (int i = 0; i < data.size(); i++) {
      XhXkLtBbLayMauHdr dx = data.get(i);
      objs = new Object[rowsName.length];
      objs[0] = i;
      objs[1] = dx.getNam();
      objs[2] = dx.getMaDanhSach();
      objs[3] = dx.getTenDiemKho();
      objs[4] = (dx.getTenLoKho() != null && !dx.getTenLoKho().isEmpty()) ? (dx.getTenLoKho() +' '+dx.getTenNganKho()): dx.getTenNganKho();
      objs[5] = dx.getSlTon();
      objs[6] = dx.getSlHetHan();
      objs[7] = dx.getDonViTinh();
      objs[8] = dx.getThoiHanLk();
      objs[9] = dx.getSoBienBan();
      objs[10] = dx.getNgayLayMau();
      objs[11] = dx.getTenTrangThai();
      dataList.add(objs);
    }
    ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
    ex.export();
  }

  public void updateTongHopDtl(XhXkLtBbLayMauHdr bienBan, boolean xoa) {
    if (!DataUtils.isNullObject(bienBan.getIdTongHop())) {
      Optional<XhXkTongHopHdr> listTongHop = xhXkTongHopRepository.findById(bienBan.getIdTongHop());
      if (listTongHop.isPresent()) {
        XhXkTongHopHdr item = listTongHop.get();
        List<XhXkTongHopDtl> tongHopDtlList = item.getTongHopDtl();
        for (XhXkTongHopDtl f : tongHopDtlList) {
          if (f.getMaDiaDiem().equals(bienBan.getMaDiaDiem())) {
            if (xoa) {
              f.setIdBienBan(null);
              f.setSoBienBan(null);
              f.setNgayLayMau(null);
              f.setTrangThaiBienBan(null);
              f.setIdNguoiTaoBb(null);
            } else {
              f.setIdBienBan(bienBan.getId());
              f.setSoBienBan(bienBan.getSoBienBan());
              f.setNgayLayMau(bienBan.getNgayLayMau());
              f.setTrangThaiBienBan(bienBan.getTrangThai());
              f.setIdNguoiTaoBb(bienBan.getNguoiTaoId());
            }

          }
        }
        xhXkTongHopRepository.save(item);
      }
    }
  }

  public ReportTemplateResponse preview(HashMap<String, Object> body) throws Exception {
    try {
      String fileName = DataUtils.safeToString(body.get("tenBaoCao"));
      String fileTemplate = "xuatkhac/luongthuc/" + fileName;
      FileInputStream inputStream = new FileInputStream(baseReportFolder + fileTemplate);
      List<XhXkLtBbLayMauHdr>  detail = this.detail(Arrays.asList(DataUtils.safeToLong(body.get("id"))));
      String chiTieuData = detail.get(0).getChiTieuKiemTra();
      String phuongPhapData = detail.get(0).getPpLayMau();
      List<Map<String, Object>> chiTieuList = parseData(chiTieuData);
      List<Map<String, Object>> phuongPhapList = parseData(phuongPhapData);
      return docxToPdfConverter.convertDocxToPdf(inputStream, detail.get(0) ,phuongPhapList ,chiTieuList);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (XDocReportException e) {
      e.printStackTrace();
    }
    return null;
  }
}

