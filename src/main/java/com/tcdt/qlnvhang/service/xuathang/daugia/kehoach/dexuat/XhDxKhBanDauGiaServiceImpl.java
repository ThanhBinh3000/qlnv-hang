package com.tcdt.qlnvhang.service.xuathang.daugia.kehoach.dexuat;

import com.tcdt.qlnvhang.common.DocxToPdfConverter;
import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.dexuat.XhDxKhBanDauGia;
import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.dexuat.XhDxKhBanDauGiaDtl;
import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.dexuat.XhDxKhBanDauGiaPhanLo;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.dexuat.XhDxKhBanDauGiaDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.dexuat.XhDxKhBanDauGiaPhanLoRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.dexuat.XhDxKhBanDauGiaRepository;
import com.tcdt.qlnvhang.request.CountKhlcntSlReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.kehoachbdg.dexuat.XhDxKhBanDauGiaReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.table.report.ReportTemplateRequest;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.ConverterTypeVia;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.velocity.tools.generic.DateTool;
import org.apache.velocity.tools.generic.NumberTool;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class XhDxKhBanDauGiaServiceImpl extends BaseServiceImpl implements XhDxKhBanDauGiaService {
  @Autowired
  private XhDxKhBanDauGiaRepository xhDxKhBanDauGiaRepository;

  @Autowired
  private XhDxKhBanDauGiaDtlRepository xhDxKhBanDauGiaDtlRepository;

  @Autowired
  private XhDxKhBanDauGiaPhanLoRepository xhDxKhBanDauGiaPhanLoRepository;

  @Autowired
  private FileDinhKemService fileDinhKemService;

  @Autowired
  DocxToPdfConverter docxToPdfConverter;
  @Override
  public Page<XhDxKhBanDauGia> searchPage(XhDxKhBanDauGiaReq req) throws Exception {
    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(),
        req.getPaggingReq().getLimit(), Sort.by("id").descending());
    Page<XhDxKhBanDauGia> data = xhDxKhBanDauGiaRepository.searchPage(
        req,
        pageable);
    Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");

    data.getContent().forEach(f -> {
      f.setTenDvi(StringUtils.isEmpty(f.getMaDvi()) ? null : mapDmucDvi.get(f.getMaDvi()));
      f.setTenLoaiVthh(StringUtils.isEmpty(f.getLoaiVthh()) ? null : mapDmucVthh.get(f.getLoaiVthh()));
      f.setTenCloaiVthh(StringUtils.isEmpty(f.getCloaiVthh()) ? null : mapDmucVthh.get(f.getCloaiVthh()));
      f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
      if (DataUtils.isNullObject(f.getIdThop())) {
        f.setTenTrangThaiTh("Chưa tổng hợp");
      } else {
        f.setTenTrangThaiTh(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThaiTh()));
      }
    });
    return data;
  }

  @Override
  public XhDxKhBanDauGia create(XhDxKhBanDauGiaReq req) throws Exception {
    if (req == null) return null;

    UserInfo userInfo = SecurityContextService.getUser();
    if (userInfo == null) throw new Exception("Bad request.");

    if (!StringUtils.isEmpty(req.getSoDxuat())) {
      Optional<XhDxKhBanDauGia> optional = xhDxKhBanDauGiaRepository.findBySoDxuat(req.getSoDxuat());
      if (optional.isPresent()) throw new Exception("Số đề xuất " + req.getSoDxuat() + " đã tồn tại");
    }

    XhDxKhBanDauGia data = new XhDxKhBanDauGia();
    BeanUtils.copyProperties(req, data, "id");

    int slDviTsan = data.getChildren().stream()
        .flatMap(item -> item.getChildren().stream())
        .map(XhDxKhBanDauGiaPhanLo::getMaDviTsan).collect(Collectors.toSet()).size();
    data.setSlDviTsan(DataUtils.safeToInt(slDviTsan));

    data.setMaDvi(userInfo.getDvql());
    data.setNguoiTaoId(userInfo.getId());
    data.setTrangThai(Contains.DU_THAO);
    data.setTrangThaiTh(Contains.CHUATONGHOP);

    XhDxKhBanDauGia created = xhDxKhBanDauGiaRepository.save(data);
    if (!DataUtils.isNullOrEmpty(req.getFileDinhKems())) {
      List<FileDinhKem> fileDinhKemList = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), created.getId(), XhDxKhBanDauGia.TABLE_NAME);
      data.setFileDinhKems(fileDinhKemList);
    }
    this.saveDetail(req, created.getId());
    return created;
  }

  void saveDetail(XhDxKhBanDauGiaReq req, Long idHdr) {
    xhDxKhBanDauGiaDtlRepository.deleteAllByIdHdr(idHdr);
    for (XhDxKhBanDauGiaDtl dataDtlReq : req.getChildren()) {
      XhDxKhBanDauGiaDtl dataDtl = new XhDxKhBanDauGiaDtl();
      BeanUtils.copyProperties(dataDtlReq, dataDtl, "id");
      dataDtl.setIdHdr(idHdr);
      xhDxKhBanDauGiaDtlRepository.save(dataDtl);
      xhDxKhBanDauGiaPhanLoRepository.deleteAllByIdDtl(dataDtlReq.getId());
      for (XhDxKhBanDauGiaPhanLo dataDtlPhanLoReq : dataDtlReq.getChildren()) {
        XhDxKhBanDauGiaPhanLo dataDtlPhanLo = new XhDxKhBanDauGiaPhanLo();
        BeanUtils.copyProperties(dataDtlPhanLoReq, dataDtlPhanLo, "id");
        dataDtlPhanLo.setIdDtl(dataDtl.getId());
        xhDxKhBanDauGiaPhanLoRepository.save(dataDtlPhanLo);
      }
    }
  }

  @Override
  public XhDxKhBanDauGia update(XhDxKhBanDauGiaReq req) throws Exception {
    if (req == null) return null;

    UserInfo userInfo = SecurityContextService.getUser();
    if (userInfo == null) throw new Exception("Bad request.");

    Optional<XhDxKhBanDauGia> optional = xhDxKhBanDauGiaRepository.findById(req.getId());
    if (!optional.isPresent()) throw new Exception("Kế hoạch bán đấu giá không tồn tại");

    if (!StringUtils.isEmpty(req.getSoDxuat())) {
      Optional<XhDxKhBanDauGia> qOptional = xhDxKhBanDauGiaRepository.findBySoDxuat(req.getSoDxuat());
      if (qOptional.isPresent()) {
        if (!qOptional.get().getId().equals(req.getId())) {
          throw new Exception("Số đề xuất " + req.getSoDxuat() + " đã tồn tại");
        }
      }
    }

    XhDxKhBanDauGia data = optional.get();
    BeanUtils.copyProperties(req, data, "id", "trangThaiTh");

    int slDviTsan = data.getChildren().stream()
        .flatMap(item -> item.getChildren().stream())
        .map(XhDxKhBanDauGiaPhanLo::getMaDviTsan).collect(Collectors.toSet()).size();
    data.setSlDviTsan(DataUtils.safeToInt(slDviTsan));

    data.setNgaySua(LocalDate.now());
    data.setNguoiSuaId(userInfo.getId());

    XhDxKhBanDauGia created = xhDxKhBanDauGiaRepository.save(data);
    fileDinhKemService.delete(created.getId(), Collections.singleton(XhDxKhBanDauGia.TABLE_NAME));
    List<FileDinhKem> fileDinhKemList = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), created.getId(), XhDxKhBanDauGia.TABLE_NAME);
    data.setFileDinhKems(fileDinhKemList);

    this.saveDetail(req, created.getId());
    return created;
  }

  @Override
  public XhDxKhBanDauGia detail(Long id) throws Exception {
    UserInfo userInfo = SecurityContextService.getUser();
    if (userInfo == null) throw new Exception("Bad request.");

    Optional<XhDxKhBanDauGia> optional = xhDxKhBanDauGiaRepository.findById(id);
    if (!optional.isPresent()) throw new UnsupportedOperationException("Kế hoạch bán đấu giá không tồn tại");

    XhDxKhBanDauGia data = optional.get();

    Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");

    List<XhDxKhBanDauGiaDtl> dataDtlList = xhDxKhBanDauGiaDtlRepository.findAllByIdHdr(data.getId());
    for (XhDxKhBanDauGiaDtl dataDtl : dataDtlList) {
      List<XhDxKhBanDauGiaPhanLo> dataPhanLoList = xhDxKhBanDauGiaPhanLoRepository.findByIdDtl(dataDtl.getId());
      dataPhanLoList.forEach(phanLo -> {
        phanLo.setTenDiemKho(StringUtils.isEmpty(phanLo.getMaDiemKho()) ? null : mapDmucDvi.get(phanLo.getMaDiemKho()));
        phanLo.setTenNhaKho(StringUtils.isEmpty(phanLo.getMaNhaKho()) ? null : mapDmucDvi.get(phanLo.getMaNhaKho()));
        phanLo.setTenNganKho(StringUtils.isEmpty(phanLo.getMaNganKho()) ? null : mapDmucDvi.get(phanLo.getMaNganKho()));
        phanLo.setTenLoKho(StringUtils.isEmpty(phanLo.getMaLoKho()) ? null : mapDmucDvi.get(phanLo.getMaLoKho()));
        phanLo.setTenLoaiVthh(StringUtils.isEmpty(phanLo.getLoaiVthh()) ? null : mapDmucVthh.get(phanLo.getLoaiVthh()));
        phanLo.setTenCloaiVthh(StringUtils.isEmpty(phanLo.getCloaiVthh()) ? null : mapDmucVthh.get(phanLo.getCloaiVthh()));
        this.donGiaDuocDuyet(data, phanLo);
      });
      dataDtl.setTenDvi(StringUtils.isEmpty(dataDtl.getMaDvi()) ? null : mapDmucDvi.get(dataDtl.getMaDvi()));
      dataDtl.setChildren(dataPhanLoList);
    }
    data.setTenDvi(StringUtils.isEmpty(data.getMaDvi()) ? null : mapDmucDvi.get(data.getMaDvi()));
    data.setTenLoaiVthh(StringUtils.isEmpty(data.getLoaiVthh()) ? null : mapDmucVthh.get(data.getLoaiVthh()));
    data.setTenCloaiVthh(StringUtils.isEmpty(data.getCloaiVthh()) ? null : mapDmucVthh.get(data.getCloaiVthh()));
    data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
    data.setChildren(dataDtlList);
    List<FileDinhKem> fileDinhKems = fileDinhKemService.search(data.getId(), Arrays.asList(XhDxKhBanDauGia.TABLE_NAME));
    if (!CollectionUtils.isEmpty(fileDinhKems)) data.setFileDinhKems(fileDinhKems);
    return data;
  }

  void donGiaDuocDuyet(XhDxKhBanDauGia data, XhDxKhBanDauGiaPhanLo phanLo) {
    BigDecimal donGiaDuocDuyet = BigDecimal.ZERO;
    if (data.getLoaiVthh().startsWith("02")) {
      donGiaDuocDuyet = xhDxKhBanDauGiaPhanLoRepository.getDonGiaVatVt(data.getCloaiVthh(), data.getNamKh());
      if (!DataUtils.isNullObject(donGiaDuocDuyet)) {
        phanLo.setDonGiaDuocDuyet(donGiaDuocDuyet);
        BigDecimal tongTienGiaKdTheoDgiaDd = data.getTongSoLuong().multiply(donGiaDuocDuyet);
        BigDecimal tongKhoanTienDtTheoDgiaDd = data.getTongSoLuong().multiply(donGiaDuocDuyet).multiply(data.getKhoanTienDatTruoc()).divide(BigDecimal.valueOf(100));
        data.setTongTienGiaKdTheoDgiaDd(tongTienGiaKdTheoDgiaDd);
        data.setTongKhoanTienDtTheoDgiaDd(tongKhoanTienDtTheoDgiaDd);
      }
    } else {
      donGiaDuocDuyet = xhDxKhBanDauGiaPhanLoRepository.getDonGiaVatLt(data.getCloaiVthh(), data.getMaDvi(), data.getNamKh());
      if (!DataUtils.isNullObject(donGiaDuocDuyet)) {
        phanLo.setDonGiaDuocDuyet(donGiaDuocDuyet);
        BigDecimal tongTienGiaKdTheoDgiaDd = data.getTongSoLuong().multiply(donGiaDuocDuyet);
        BigDecimal tongKhoanTienDtTheoDgiaDd = data.getTongSoLuong().multiply(donGiaDuocDuyet).multiply(data.getKhoanTienDatTruoc()).divide(BigDecimal.valueOf(100));
        data.setTongTienGiaKdTheoDgiaDd(tongTienGiaKdTheoDgiaDd);
        data.setTongKhoanTienDtTheoDgiaDd(tongKhoanTienDtTheoDgiaDd);
      }
    }
  }

  @Override
  public XhDxKhBanDauGia approve(XhDxKhBanDauGiaReq req) throws Exception {
    UserInfo userInfo = SecurityContextService.getUser();
    if (userInfo == null) throw new Exception("Bad request.");

    Optional<XhDxKhBanDauGia> optional = xhDxKhBanDauGiaRepository.findById(req.getId());
    if (!optional.isPresent()) throw new Exception("Kế hoạch bán đấu giá không tồn tại");

    XhDxKhBanDauGia data = optional.get();
    String status = req.getTrangThai() + data.getTrangThai();
    switch (status) {
      case Contains.CHODUYET_TP + Contains.DUTHAO:
      case Contains.CHODUYET_TP + Contains.TUCHOI_TP:
      case Contains.CHODUYET_TP + Contains.TUCHOI_LDC:
      case Contains.CHODUYET_TP + Contains.TU_CHOI_CBV:
        data.setNguoiGuiDuyetId(userInfo.getId());
        data.setNgayGuiDuyet(LocalDate.now());
      case Contains.TUCHOI_TP + Contains.CHODUYET_TP:
      case Contains.TUCHOI_LDC + Contains.CHODUYET_LDC:
      case Contains.TU_CHOI_CBV + Contains.DADUYET_LDC:
        data.setNguoiPduyetId(userInfo.getId());
        data.setNgayPduyet(LocalDate.now());
        data.setLyDoTuChoi(req.getLyDoTuChoi());
        break;
      case Contains.CHODUYET_LDC + Contains.CHODUYET_TP:
      case Contains.DADUYET_LDC + Contains.CHODUYET_LDC:
      case Contains.DA_DUYET_CBV + Contains.DADUYET_LDC:
        data.setNguoiPduyetId(userInfo.getId());
        data.setNgayPduyet(LocalDate.now());
        break;
      default:
        throw new Exception("Phê duyệt không thành công");
    }
    data.setTrangThai(req.getTrangThai());
    return xhDxKhBanDauGiaRepository.save(data);
  }

  @Override
  public void delete(Long id) throws Exception {
    UserInfo userInfo = SecurityContextService.getUser();
    if (userInfo == null) throw new Exception("Bad request.");

    if (StringUtils.isEmpty(id)) throw new Exception("Xóa thất bại không tìm thấy dữ liệu");

    Optional<XhDxKhBanDauGia> optional = xhDxKhBanDauGiaRepository.findById(id);
    if (!optional.isPresent()) throw new Exception("Không tìm thấy dữ liệu cần xóa");

    if (!optional.get().getTrangThai().equals(Contains.DUTHAO)
        && !optional.get().getTrangThai().equals(Contains.TU_CHOI_TP)
        && !optional.get().getTrangThai().equals(Contains.TUCHOI_LDC)) {
      throw new Exception("Chỉ thực hiện xóa với kế hoạch ở trạng thái bản nháp hoặc từ chối");
    }

    List<XhDxKhBanDauGiaDtl> allByIdHdr = xhDxKhBanDauGiaDtlRepository.findAllByIdHdr(id);
    for (XhDxKhBanDauGiaDtl dtl : allByIdHdr) {
      xhDxKhBanDauGiaPhanLoRepository.deleteAllByIdDtl(dtl.getId());
    }

    xhDxKhBanDauGiaDtlRepository.deleteAllByIdHdr(id);
    xhDxKhBanDauGiaRepository.delete(optional.get());
    fileDinhKemService.delete(optional.get().getId(), Collections.singleton(XhDxKhBanDauGia.TABLE_NAME));
  }

  @Override
  public void deleteMulti(List<Long> listMulti) throws Exception {
    UserInfo userInfo = SecurityContextService.getUser();
    if (userInfo == null) throw new Exception("Bad request.");

    if (StringUtils.isEmpty(listMulti)) throw new Exception("Xóa thất bại, không tìm thấy dữ liệu");

    List<XhDxKhBanDauGia> listDg = xhDxKhBanDauGiaRepository.findByIdIn(listMulti);
    if (listDg.isEmpty()) throw new Exception("Không tìm thấy dữ liệu cần xóa");

    for (XhDxKhBanDauGia dg : listDg) {
      this.delete(dg.getId());
    }
  }

  @Override
  public void export(XhDxKhBanDauGiaReq req, HttpServletResponse response) throws Exception {
    PaggingReq paggingReq = new PaggingReq();
    paggingReq.setPage(0);
    paggingReq.setLimit(Integer.MAX_VALUE);
    req.setPaggingReq(paggingReq);
    Page<XhDxKhBanDauGia> page = this.searchPage(req);
    List<XhDxKhBanDauGia> data = page.getContent();
    String title = "Danh sách đề xuất kế hoạch bán đấu giá";
    String[] rowsName = new String[]{"STT", "Năm KH", "Số KH/tờ trình", "Ngày lập KH", "Ngày duyệt KH", "Số QĐ duyệt KH bán ĐG", "Ngày ký QĐ", "Trích yếu", "Loại hàng hóa", "Chủng loại hàng hóa", "Số ĐV tài sản", "SL HĐ đã ký", "Số QĐ giao chỉ tiêu", "Trạng thái"};
    String filename = "danh-sach-dx-kh-ban-dau-gia.xlsx";
    List<Object[]> dataList = new ArrayList<Object[]>();
    Object[] objs = null;
    for (int i = 0; i < data.size(); i++) {
      XhDxKhBanDauGia hdr = data.get(i);
      objs = new Object[rowsName.length];
      objs[0] = i;
      objs[1] = hdr.getNamKh();
      objs[2] = hdr.getSoDxuat();
      objs[3] = hdr.getNgayTao();
      objs[4] = hdr.getNgayPduyet();
      objs[5] = hdr.getSoQdPd();
      objs[6] = hdr.getNgayKyQd();
      objs[7] = hdr.getTrichYeu();
      objs[8] = hdr.getTenLoaiVthh();
      objs[9] = hdr.getTenCloaiVthh();
      objs[10] = hdr.getSlDviTsan();
      objs[11] = null;
      objs[12] = hdr.getSoQdCtieu();
      objs[13] = hdr.getTenTrangThai();
      dataList.add(objs);
    }
    ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
    ex.export();
  }

  public BigDecimal countSoLuongKeHoachNam(CountKhlcntSlReq req) {
    return xhDxKhBanDauGiaRepository.countSLDalenKh(req.getYear(), req.getLoaiVthh(), req.getMaDvi(), req.getLastest());
  }

  @Override
  public BigDecimal getGiaBanToiThieu(String cloaiVthh, String maDvi, Integer namKh) {
    if (cloaiVthh.startsWith("02")) {
      return xhDxKhBanDauGiaRepository.getGiaBanToiThieuVt(cloaiVthh, namKh);
    } else {
      return xhDxKhBanDauGiaRepository.getGiaBanToiThieuLt(cloaiVthh, maDvi, namKh);
    }
  }

  @Override
  public ReportTemplateResponse preview(String tenBaoCao) throws Exception {
    try {
      ReportTemplateRequest reportTemplateRequest = new ReportTemplateRequest();
//      reportTemplateRequest.setFileName("de-xuat-ke-hoach-ban-dau-gia.docx");
      reportTemplateRequest.setFileName(tenBaoCao);
      ReportTemplate model = findByTenFile(reportTemplateRequest);
      byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
      ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
      XhDxKhBanDauGia detail = this.detail(4122l);
      return docxToPdfConverter.convertDocxToPdf(inputStream,detail);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (XDocReportException e) {
      e.printStackTrace();
    }
    return null;
  }
}