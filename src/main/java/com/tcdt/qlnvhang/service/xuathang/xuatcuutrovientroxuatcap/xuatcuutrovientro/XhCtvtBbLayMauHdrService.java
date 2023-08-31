package com.tcdt.qlnvhang.service.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtBbLayMauDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtBbLayMauHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtQdGiaoNvXhDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtQdGiaoNvXhHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.SearchXhCtvtBbLayMau;
import com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtBbLayMauHdrReq;
import com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtBbLayMauDtlReq;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.*;
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
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class XhCtvtBbLayMauHdrService extends BaseServiceImpl {


  @Autowired
  private XhCtvtBbLayMauHdrRepository xhCtvtBbLayMauHdrRepository;

  @Autowired
  private XhCtvtBbLayMauDtlRepository xhCtvtBbLayMauDtlRepository;

  @Autowired
  private XhCtvtQdGiaoNvXhHdrRepository xhCtvtQdGiaoNvXhHdrRepository;

  @Autowired
  private XhCtvtQdGiaoNvXhDtlRepository xhCtvtQdGiaoNvXhDtlRepository;

  @Autowired
  private UserInfoRepository userInfoRepository;

  @Autowired
  private FileDinhKemService fileDinhKemService;

  public Page<XhCtvtBbLayMauHdr> searchPage(CustomUserDetails currentUser, SearchXhCtvtBbLayMau req) throws Exception {
    req.setDvql(currentUser.getDvql());
    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
    Page<XhCtvtBbLayMauHdr> search = xhCtvtBbLayMauHdrRepository.search(req, pageable);
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
      if (s.getNguoiPduyetId() != null) {
        s.setTenThuKho(ObjectUtils.isEmpty(s.getNguoiPduyetId()) ? null : userInfoRepository.findById(s.getNguoiPduyetId()).get().getFullName());
      }
      s.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(s.getTrangThai()));
    });
    return search;
  }

  @Transactional
  public XhCtvtBbLayMauHdr save(CustomUserDetails currentUser, XhCtvtBbLayMauHdrReq objReq) throws Exception {
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    Optional<XhCtvtBbLayMauHdr> optional = xhCtvtBbLayMauHdrRepository.findBySoBienBan(objReq.getSoBienBan());
    if (optional.isPresent()) {
      throw new Exception("số biên bản đã tồn tại");
    }
    XhCtvtBbLayMauHdr data = new XhCtvtBbLayMauHdr();
    BeanUtils.copyProperties(objReq, data);
    data.setMaDvi(currentUser.getUser().getDepartment());
    data.setTrangThai(Contains.DUTHAO);
    XhCtvtBbLayMauHdr created = xhCtvtBbLayMauHdrRepository.save(data);

    List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), created.getId(), XhCtvtBbLayMauHdr.TABLE_NAME);
    created.setFileDinhKems(fileDinhKems);
    List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(objReq.getCanCu(), created.getId(), XhCtvtBbLayMauHdr.TABLE_NAME + "_CAN_CU");
    created.setCanCu(canCu);
    List<FileDinhKem> niemPhong = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKemNiemPhong(), created.getId(), XhCtvtBbLayMauHdr.TABLE_NAME + "_NIEM_PHONG");
    created.setFileDinhKemNiemPhong(niemPhong);

    this.saveCtiet(created.getId(), objReq);

    if (!DataUtils.isNullOrEmpty(created.getSoQdGiaoNvXh())) {
      Optional<XhCtvtQdGiaoNvXhHdr> qdGiaoNvXhHdr = xhCtvtQdGiaoNvXhHdrRepository.findById(created.getIdQdGiaoNvXh());
      if (qdGiaoNvXhHdr.isPresent()) {
        XhCtvtQdGiaoNvXhHdr qd = qdGiaoNvXhHdr.get();
        qd.setTenTrangThaiXh(Contains.DANG_THUC_HIEN);
        xhCtvtQdGiaoNvXhHdrRepository.save(qd);
      }
      List<XhCtvtQdGiaoNvXhDtl> qdGiaoNvXhDtl = xhCtvtQdGiaoNvXhDtlRepository.findByIdHdr(created.getIdQdGiaoNvXh());
      qdGiaoNvXhDtl.forEach(qdDtl -> {
            qdDtl.setTrangThai(Contains.DANG_THUC_HIEN);
            xhCtvtQdGiaoNvXhDtlRepository.save(qdDtl);
          }
      );
    }

    return created;
  }

  @Transactional()
  public void saveCtiet(Long idHdr, XhCtvtBbLayMauHdrReq objReq) {
    for (XhCtvtBbLayMauDtlReq nguoiLienQuanReq : objReq.getNguoiLienQuan()) {
      XhCtvtBbLayMauDtl nguoiLienQuan = new XhCtvtBbLayMauDtl();
      BeanUtils.copyProperties(nguoiLienQuanReq, nguoiLienQuan);
      nguoiLienQuan.setId(null);
      nguoiLienQuan.setIdHdr(idHdr);
      xhCtvtBbLayMauDtlRepository.save(nguoiLienQuan);
    }
  }

  @Transactional
  public XhCtvtBbLayMauHdr update(CustomUserDetails currentUser, XhCtvtBbLayMauHdrReq objReq) throws Exception {
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    Optional<XhCtvtBbLayMauHdr> optional = xhCtvtBbLayMauHdrRepository.findById(objReq.getId());
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu cần sửa");
    }
    Optional<XhCtvtBbLayMauHdr> soDx = xhCtvtBbLayMauHdrRepository.findBySoBienBan(objReq.getSoBienBan());
    if (soDx.isPresent()) {
      if (!soDx.get().getId().equals(objReq.getId())) {
        throw new Exception("số biên bản đã tồn tại");
      }
    }
    XhCtvtBbLayMauHdr data = optional.get();
    BeanUtils.copyProperties(objReq, data);
    XhCtvtBbLayMauHdr created = xhCtvtBbLayMauHdrRepository.save(data);

    fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(XhCtvtBbLayMauHdr.TABLE_NAME));
    List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), created.getId(), XhCtvtBbLayMauHdr.TABLE_NAME);
    created.setFileDinhKems(fileDinhKems);
    fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(XhCtvtBbLayMauHdr.TABLE_NAME + "_CAN_CU"));
    List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(objReq.getCanCu(), created.getId(), XhCtvtBbLayMauHdr.TABLE_NAME + "_CAN_CU");
    created.setCanCu(canCu);
    fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(XhCtvtBbLayMauHdr.TABLE_NAME + "_NIEM_PHONG"));
    List<FileDinhKem> niemPhong = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKemNiemPhong(), created.getId(), XhCtvtBbLayMauHdr.TABLE_NAME + "_NIEM_PHONG");
    created.setFileDinhKemNiemPhong(niemPhong);

    List<XhCtvtBbLayMauDtl> listnguoiLienQuan = xhCtvtBbLayMauDtlRepository.findByIdHdr(objReq.getId());
    xhCtvtBbLayMauDtlRepository.deleteAll(listnguoiLienQuan);
    this.saveCtiet(created.getId(), objReq);
    return created;
  }


  public List<XhCtvtBbLayMauHdr> detail(List<Long> ids) throws Exception {
    if (DataUtils.isNullOrEmpty(ids))
      throw new Exception("Tham số không hợp lệ.");
    List<XhCtvtBbLayMauHdr> optional = xhCtvtBbLayMauHdrRepository.findByIdIn(ids);
    if (DataUtils.isNullOrEmpty(optional)) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    Map<String, Map<String, Object>> mapDmucDvi = getListDanhMucDviObject(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    List<XhCtvtBbLayMauHdr> allById = xhCtvtBbLayMauHdrRepository.findAllById(ids);
    allById.forEach(data -> {
      if (mapDmucDvi.containsKey(data.getMaDvi())) {
        data.setTenDvi(mapDmucDvi.get(data.getMaDvi()).get("tenDvi").toString());
        data.setDiaChiDvi(mapDmucDvi.get(data.getMaDvi()).get("diaChi").toString());
        data.setTenDviCha(mapDmucDvi.get(data.getMaDvi().substring(0, data.getMaDvi().length() - 2)).get("tenDvi").toString());
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
        data.setTenThuKho(ObjectUtils.isEmpty(data.getNguoiPduyetId()) ? null : userInfoRepository.findById(data.getNguoiPduyetId()).get().getFullName());
      }
      data.setTenLoaiVthh(mapVthh.get(data.getLoaiVthh()));
      data.setTenCloaiVthh(mapVthh.get(data.getCloaiVthh()));
      data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));

      List<FileDinhKem> fileDinhKems = fileDinhKemService.search(data.getId(), Arrays.asList(XhCtvtBbLayMauHdr.TABLE_NAME));
      List<FileDinhKem> canCu = fileDinhKemService.search(data.getId(), Arrays.asList(XhCtvtBbLayMauHdr.TABLE_NAME + "_CAN_CU"));
      List<FileDinhKem> niemPhong = fileDinhKemService.search(data.getId(), Arrays.asList(XhCtvtBbLayMauHdr.TABLE_NAME + "_NIEM_PHONG"));
      data.setFileDinhKems(fileDinhKems);
      data.setCanCu(canCu);
      data.setFileDinhKemNiemPhong(niemPhong);

      List<XhCtvtBbLayMauDtl> list = xhCtvtBbLayMauDtlRepository.findByIdHdr(data.getId());
      data.setNguoiLienQuan(list);
    });

    return allById;
  }

  @Transient
  public void delete(IdSearchReq idSearchReq) throws Exception {
    Optional<XhCtvtBbLayMauHdr> optional = xhCtvtBbLayMauHdrRepository.findById(idSearchReq.getId());
    if (!optional.isPresent()) {
      throw new Exception("Bản ghi không tồn tại");
    }
    XhCtvtBbLayMauHdr data = optional.get();
    List<XhCtvtBbLayMauDtl> list = xhCtvtBbLayMauDtlRepository.findByIdHdr(data.getId());
    xhCtvtBbLayMauDtlRepository.deleteAll(list);
    fileDinhKemService.delete(data.getId(), Lists.newArrayList(XhCtvtBbLayMauHdr.TABLE_NAME));
    fileDinhKemService.delete(data.getId(), Lists.newArrayList(XhCtvtBbLayMauHdr.TABLE_NAME + "_CAN_CU"));
    fileDinhKemService.delete(data.getId(), Lists.newArrayList(XhCtvtBbLayMauHdr.TABLE_NAME + "_NIEM_PHONG"));

    if (!DataUtils.isNullOrEmpty(data.getSoQdGiaoNvXh())) {
      List<XhCtvtBbLayMauHdr> listQd = xhCtvtBbLayMauHdrRepository.findByIdQdGiaoNvXh(data.getIdQdGiaoNvXh());
      if (listQd.size() == 1){
        Optional<XhCtvtQdGiaoNvXhHdr> qdGiaoNvXhHdr = xhCtvtQdGiaoNvXhHdrRepository.findById(data.getIdQdGiaoNvXh());
        if (qdGiaoNvXhHdr.isPresent()) {
          XhCtvtQdGiaoNvXhHdr qd = qdGiaoNvXhHdr.get();
          qd.setTenTrangThaiXh(Contains.CHUA_THUC_HIEN);
          xhCtvtQdGiaoNvXhHdrRepository.save(qd);
        }
      }else {
        Optional<XhCtvtQdGiaoNvXhHdr> qdGiaoNvXhHdr = xhCtvtQdGiaoNvXhHdrRepository.findById(data.getIdQdGiaoNvXh());
        if (qdGiaoNvXhHdr.isPresent()) {
          XhCtvtQdGiaoNvXhHdr qd = qdGiaoNvXhHdr.get();
          qd.setTenTrangThaiXh(Contains.DANG_THUC_HIEN);
          xhCtvtQdGiaoNvXhHdrRepository.save(qd);
        }
      }

      List<XhCtvtQdGiaoNvXhDtl> qdGiaoNvXhDtl = xhCtvtQdGiaoNvXhDtlRepository.findByIdHdr(data.getIdQdGiaoNvXh());
      qdGiaoNvXhDtl.forEach(qdDtl -> {
            qdDtl.setTrangThai(Contains.CHUA_THUC_HIEN);
            xhCtvtQdGiaoNvXhDtlRepository.save(qdDtl);
          }
      );
    }

    xhCtvtBbLayMauHdrRepository.delete(data);
  }

  @Transactional
  public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
    List<XhCtvtBbLayMauHdr> list = xhCtvtBbLayMauHdrRepository.findAllByIdIn(idSearchReq.getIdList());

    if (list.isEmpty()) {
      throw new Exception("Bản ghi không tồn tại");
    }
    List<Long> listId = list.stream().map(XhCtvtBbLayMauHdr::getId).collect(Collectors.toList());
    List<XhCtvtBbLayMauDtl> listNguoiLienQuan = xhCtvtBbLayMauDtlRepository.findAllByIdHdrIn(listId);
    xhCtvtBbLayMauDtlRepository.deleteAll(listNguoiLienQuan);
    fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(XhCtvtBbLayMauHdr.TABLE_NAME));
    fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(XhCtvtBbLayMauHdr.TABLE_NAME + "_CAN_CU"));
    fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(XhCtvtBbLayMauHdr.TABLE_NAME + "_NIEM_PHONG"));
    xhCtvtBbLayMauHdrRepository.deleteAll(list);
  }

  @Transient
  public XhCtvtBbLayMauHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {

    if (StringUtils.isEmpty(statusReq.getId())) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    Optional<XhCtvtBbLayMauHdr> optional = xhCtvtBbLayMauHdrRepository.findById(Long.valueOf(statusReq.getId()));
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
    XhCtvtBbLayMauHdr created = xhCtvtBbLayMauHdrRepository.save(optional.get());
    return created;
  }

  public void export(CustomUserDetails currentUser, SearchXhCtvtBbLayMau objReq, HttpServletResponse response) throws Exception {
    PaggingReq paggingReq = new PaggingReq();
    paggingReq.setPage(0);
    paggingReq.setLimit(Integer.MAX_VALUE);
    objReq.setPaggingReq(paggingReq);
    Page<XhCtvtBbLayMauHdr> page = this.searchPage(currentUser, objReq);
    List<XhCtvtBbLayMauHdr> data = page.getContent();

    String title = "Danh sách biên bản lấy mẫu bàn giao mẫu ";
    String[] rowsName = new String[]{"STT", "Số QĐ giao nhiệm vụ XH", "Năm KH", "Thời hạn XH trước ngày", "Số BB LM/BGM", "Ngày lấy mẫu", "Điểm Kho",
        "Lô kho", "Số BB tịnh kho", "Ngày xuất dốc kho", "Số BB hao dôi", "Trạng thái"};
    String fileName = "danh-sach-bien-ban-lay-mau-ban-giao-mau.xlsx";
    List<Object[]> dataList = new ArrayList<Object[]>();
    Object[] objs = null;
    for (int i = 0; i < data.size(); i++) {
      XhCtvtBbLayMauHdr dx = data.get(i);
      objs = new Object[rowsName.length];
      objs[0] = i;
      objs[1] = dx.getSoQdGiaoNvXh();
      objs[2] = dx.getNam();
      objs[3] = dx.getNgayQdGiaoNvXh();
      objs[4] = dx.getSoBienBan();
      objs[5] = dx.getNgayLayMau();
      objs[6] = dx.getTenDiemKho();
      objs[7] = dx.getTenLoKho();
      objs[8] = dx.getSoBbTinhKho();
      objs[9] = dx.getNgayXuatDocKho();
      objs[10] = dx.getSoBbHaoDoi();
      objs[11] = dx.getTenTrangThai();
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
      List<XhCtvtBbLayMauHdr> detail = this.detail(Arrays.asList(DataUtils.safeToLong(body.get("id"))));
      return docxToPdfConverter.convertDocxToPdf(inputStream, detail.get(0));
    } catch (IOException e) {
      e.printStackTrace();
    } catch (XDocReportException e) {
      e.printStackTrace();
    }
    return null;
  }
}

