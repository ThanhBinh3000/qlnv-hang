package com.tcdt.qlnvhang.service.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.fasterxml.jackson.databind.JsonNode;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtBangKeDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtBangKeHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtPhieuXuatKhoRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.SearchXhCtvtBangKeReq;
import com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtBangKeReq;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.table.report.ReportTemplateRequest;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtBangKeDtl;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtBangKeHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtPhieuKnClHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtPhieuXuatKho;
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
import org.springframework.util.ObjectUtils;
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
public class XhCtvtBangKeService extends BaseServiceImpl {


  @Autowired
  private XhCtvtBangKeHdrRepository xhCtvtBangKeHdrRepository;
  @Autowired
  private XhCtvtBangKeDtlRepository xhCtvtBangKeDtlRepository;

  @Autowired
  private XhCtvtPhieuXuatKhoRepository xhCtvtPhieuXuatKhoRepository ;
  @Autowired
  private UserInfoRepository userInfoRepository;

  @Autowired
  private FileDinhKemService fileDinhKemService;

  public Page<XhCtvtBangKeHdr> searchPage(CustomUserDetails currentUser, SearchXhCtvtBangKeReq req) throws Exception {
    req.setDvql(currentUser.getDvql());
    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
    Page<XhCtvtBangKeHdr> search = xhCtvtBangKeHdrRepository.search(req, pageable);
    Map<String, Map<String, Object>> mapDmucDvi = getListDanhMucDviObject(null, null, "01");

    Map<String, String> mapVthh = getListDanhMucHangHoa();
    search.getContent().forEach(s -> {
      if (mapDmucDvi.containsKey((s.getMaDvi()))) {
        Map<String, Object> objDonVi = mapDmucDvi.get(s.getMaDvi());
        s.setTenDvi(objDonVi.get("tenDvi").toString());
      }
      if (mapDmucDvi.containsKey(s.getMaDiemKho())) {
        s.setTenDiemKho(mapDmucDvi.get(s.getMaDiemKho()).get("tenDvi").toString());
      }
      if (mapDmucDvi.containsKey(s.getMaNhaKho())) {
        s.setTenNhaKho(mapDmucDvi.get(s.getMaNhaKho()).get("tenDvi").toString());
      }
      if (mapDmucDvi.containsKey(s.getMaNganKho())) {
        s.setTenNganKho(mapDmucDvi.get(s.getMaNganKho()).get("tenDvi").toString());
      }
      if (mapDmucDvi.containsKey(s.getMaLoKho())) {
        s.setTenLoKho(mapDmucDvi.get(s.getMaLoKho()).get("tenDvi").toString());
      }
      if (mapVthh.get((s.getLoaiVthh())) != null) {
        s.setTenLoaiVthh(mapVthh.get(s.getLoaiVthh()));
      }
      if (mapVthh.get((s.getCloaiVthh())) != null) {
        s.setTenCloaiVthh(mapVthh.get(s.getCloaiVthh()));
      }
      s.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(s.getTrangThai()));
    });
    return search;
  }

  @Transactional
  public XhCtvtBangKeHdr save(CustomUserDetails currentUser, XhCtvtBangKeReq objReq) throws Exception {
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    Optional<XhCtvtBangKeHdr> optional = xhCtvtBangKeHdrRepository.findBySoBangKe(objReq.getSoBangKe());
    if (optional.isPresent()) {
      throw new Exception("số biên bản đã tồn tại");
    }
    XhCtvtBangKeHdr data = new XhCtvtBangKeHdr();
    BeanUtils.copyProperties(objReq, data);
    data.setId(DataUtils.safeToLong(data.getSoBangKe().split("/")[0]));
    data.setMaDvi(currentUser.getUser().getDepartment());
    data.setTrangThai(Contains.DUTHAO);
    XhCtvtBangKeHdr created = xhCtvtBangKeHdrRepository.save(data);
    if (!DataUtils.isNullOrEmpty(created.getSoPhieuXuatKho())) {
      Optional<XhCtvtPhieuXuatKho> phieuXuatKho = xhCtvtPhieuXuatKhoRepository.findById(created.getIdPhieuXuatKho());
      if (phieuXuatKho.isPresent()) {
        XhCtvtPhieuXuatKho phieu = phieuXuatKho.get();
        phieu.setSoBangKeCh(created.getSoBangKe());
        phieu.setIdBangKeCh(created.getId());
        xhCtvtPhieuXuatKhoRepository.save(phieu);
      }
    }
    //dtl
    data.getBangKeDtl().forEach(s -> {
      s.setIdHdr(created.getId());
    });
    xhCtvtBangKeDtlRepository.saveAll(data.getBangKeDtl());
    return created;
  }

  @Transactional
  public XhCtvtBangKeHdr update(CustomUserDetails currentUser, XhCtvtBangKeReq objReq) throws Exception {
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    Optional<XhCtvtBangKeHdr> optional = xhCtvtBangKeHdrRepository.findById(objReq.getId());
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu cần sửa");
    }
    Optional<XhCtvtBangKeHdr> soDx = xhCtvtBangKeHdrRepository.findBySoBangKe(objReq.getSoBangKe());
    if (soDx.isPresent()) {
      if (!soDx.get().getId().equals(objReq.getId())) {
        throw new Exception("số biên bản đã tồn tại");
      }
    }
    XhCtvtBangKeHdr data = optional.get();
    BeanUtils.copyProperties(objReq, data);
    XhCtvtBangKeHdr created = xhCtvtBangKeHdrRepository.save(data);

    xhCtvtBangKeDtlRepository.deleteAllByIdHdr(created.getId());
    data.getBangKeDtl().forEach(s -> {
      s.setIdHdr(created.getId());
    });
    xhCtvtBangKeDtlRepository.saveAll(data.getBangKeDtl());
    return created;
  }


  public List<XhCtvtBangKeHdr> detail(List<Long> ids) throws Exception {
    if (DataUtils.isNullOrEmpty(ids)) throw new Exception("Tham số không hợp lệ.");
    List<XhCtvtBangKeHdr> allById = xhCtvtBangKeHdrRepository.findByIdIn(ids);
    if (DataUtils.isNullOrEmpty(allById)) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    Map<String, Map<String, Object>> mapDmucDvi = getListDanhMucDviObject(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    allById.forEach(data -> {
      if (mapDmucDvi.containsKey(data.getMaDvi())) {
        //lay ten chi cuc nen lui lai 1 cap
        String maChiCuc = data.getMaDvi().substring(0, 8);
        data.setTenDvi(mapDmucDvi.get(DataUtils.safeToString(maChiCuc)).get("tenDvi").toString());
        data.setDiaChiDvi(mapDmucDvi.get(data.getMaDvi()).get("diaChi").toString());
      }
      if (mapDmucDvi.containsKey(data.getMaDiemKho())) {
        data.setTenDiemKho(mapDmucDvi.get(data.getMaDiemKho()).get("tenDvi").toString());
      }
      if (mapDmucDvi.containsKey(data.getMaNhaKho())) {
        data.setTenNhaKho(mapDmucDvi.get(data.getMaNhaKho()).get("tenDvi").toString());
      }
      if (mapDmucDvi.containsKey(data.getMaNganKho())) {
        data.setTenNganKho(mapDmucDvi.get(data.getMaNganKho()).get("tenDvi").toString());
      }
      if (mapDmucDvi.containsKey(data.getMaLoKho())) {
        data.setTenLoKho(mapDmucDvi.get(data.getMaLoKho()).get("tenDvi").toString());
      }
      if (data.getNguoiPduyetId() != null) {
        data.setNguoiPduyet(ObjectUtils.isEmpty(data.getNguoiPduyetId()) ? null : userInfoRepository.findById(data.getNguoiPduyetId()).get().getFullName());
      }
      data.setTenLoaiVthh(mapVthh.get(data.getLoaiVthh()));
      data.setTenCloaiVthh(mapVthh.get(data.getCloaiVthh()));
      data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));

      List<XhCtvtBangKeDtl> allDtl = xhCtvtBangKeDtlRepository.findAllByIdHdr(data.getId());
      data.setBangKeDtl(allDtl);

    });

    return allById;
  }

  @Transient
  public void delete(IdSearchReq idSearchReq) throws Exception {
    Optional<XhCtvtBangKeHdr> optional = xhCtvtBangKeHdrRepository.findById(idSearchReq.getId());
    if (!optional.isPresent()) {
      throw new Exception("Bản ghi không tồn tại");
    }
    XhCtvtBangKeHdr data = optional.get();
    xhCtvtBangKeHdrRepository.delete(data);
    if (!DataUtils.isNullOrEmpty(data.getSoPhieuXuatKho())) {
      Optional<XhCtvtPhieuXuatKho> phieuXuatKho = xhCtvtPhieuXuatKhoRepository.findById(data.getIdPhieuXuatKho());
      if (phieuXuatKho.isPresent()) {
        XhCtvtPhieuXuatKho phieu = phieuXuatKho.get();
        phieu.setSoBangKeCh(null);
        xhCtvtPhieuXuatKhoRepository.save(phieu);
      }
    }
  }

  @Transactional
  public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
    List<XhCtvtBangKeHdr> list = xhCtvtBangKeHdrRepository.findAllByIdIn(idSearchReq.getIdList());

    if (list.isEmpty()) {
      throw new Exception("Bản ghi không tồn tại");
    }
    List<Long> listID= list.stream().map(XhCtvtBangKeHdr::getId).collect(Collectors.toList());
    List<XhCtvtPhieuXuatKho> phieuXuatKho = xhCtvtPhieuXuatKhoRepository.findByIdIn(listID);
    xhCtvtBangKeHdrRepository.deleteAll(list);
    if (!DataUtils.isNullOrEmpty(phieuXuatKho)) {
     for (XhCtvtPhieuXuatKho phieu : phieuXuatKho){
       phieu.setSoBangKeCh(null);
       xhCtvtPhieuXuatKhoRepository.save(phieu);
     }
    }
  }

  @Transient
  public XhCtvtBangKeHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {

    if (StringUtils.isEmpty(statusReq.getId())) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    Optional<XhCtvtBangKeHdr> optional = xhCtvtBangKeHdrRepository.findById(Long.valueOf(statusReq.getId()));
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
        optional.get().setNgayPduyet(LocalDate.now());
        break;
      default:
        throw new Exception("Phê duyệt không thành công");
    }
    optional.get().setTrangThai(statusReq.getTrangThai());
    XhCtvtBangKeHdr created = xhCtvtBangKeHdrRepository.save(optional.get());
    return created;
  }

  public void export(CustomUserDetails currentUser, SearchXhCtvtBangKeReq objReq, HttpServletResponse response) throws Exception {
    PaggingReq paggingReq = new PaggingReq();
    paggingReq.setPage(0);
    paggingReq.setLimit(Integer.MAX_VALUE);
    objReq.setPaggingReq(paggingReq);
    Page<XhCtvtBangKeHdr> page = this.searchPage(currentUser, objReq);
    List<XhCtvtBangKeHdr> data = page.getContent();

    String title = "Danh sách phiếu xuất kho ";
    String[] rowsName = new String[]{"STT", "Số QĐ giao nhiệm vụ XH", "Năm KH", "Thời hạn XH trước ngày", "Điểm kho", "Lô kho", "Số bảng kê", "Số phiếu XK", "Ngày XK", "Trạng thái"};
    String fileName = "danh-sach-bang-ke-can-hang.xlsx";
    List<Object[]> dataList = new ArrayList<Object[]>();
    Object[] objs = null;
    for (int i = 0; i < data.size(); i++) {
      XhCtvtBangKeHdr dx = data.get(i);
      objs = new Object[rowsName.length];
      objs[0] = i;
      objs[1] = dx.getSoQdGiaoNvXh();
      objs[2] = dx.getNam();
      objs[3] = dx.getNgayQdGiaoNvXh();
      objs[4] = dx.getTenDiemKho();
      objs[5] = dx.getTenLoKho()+'-'+dx.getTenNganKho();
      objs[6] = dx.getSoBangKe();
      objs[7] = dx.getSoPhieuXuatKho();
      objs[8] = dx.getNgayXuat();
      objs[9] = dx.getTenTrangThai();
      dataList.add(objs);
    }
    ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
    ex.export();
  }

  public ReportTemplateResponse preview(HashMap<String, Object> body) throws Exception {
    try {
      JsonNode reportTemplateRequest1 = objectMapper.readTree(DataUtils.safeToString(body.get("reportTemplateRequest")));
      String fileName = reportTemplateRequest1.get("fileName").textValue();
      String fileTemplate = "xuatcuutrovientro/" + fileName;
      FileInputStream inputStream = new FileInputStream(baseReportFolder + fileTemplate);
      List<XhCtvtBangKeHdr> detail = this.detail(Arrays.asList(DataUtils.safeToLong(body.get("id"))));
      return docxToPdfConverter.convertDocxToPdf(inputStream, detail.get(0));
    } catch (IOException e) {
      e.printStackTrace();
    } catch (XDocReportException e) {
      e.printStackTrace();
    }
    return null;
  }
}
