package com.tcdt.qlnvhang.service.xuathang.kiemtrachatluong;

import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.QlnvDmVattuRepository;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.kiemtrachatluong.XhPhieuKnclRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.xuatcuutrovientro.XhPhieuKnclHdrPreview;
import com.tcdt.qlnvhang.request.xuathang.kiemtrachatluong.SearchPhieuKnclReq;
import com.tcdt.qlnvhang.request.xuathang.kiemtrachatluong.XhPhieuKnclReq;
import com.tcdt.qlnvhang.response.xuatcuutrovientro.XhPhieuKnclDtlDto;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.xuathang.kiemtrachatluong.phieukncl.XhPhieuKnclDtl;
import com.tcdt.qlnvhang.table.xuathang.kiemtrachatluong.phieukncl.XhPhieuKnclHdr;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import lombok.var;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.FileInputStream;
import java.time.LocalDate;
import java.util.*;

@Service
public class XhPhieuKnclService extends BaseServiceImpl {

  @Autowired
  private XhPhieuKnclRepository xhPhieuKnclRepository;

  @Autowired
  private UserInfoRepository userInfoRepository;
  @Autowired
  private QlnvDmVattuRepository qlnvDmVattuRepository;

  public Page<XhPhieuKnclHdr> searchPage(CustomUserDetails currentUser, SearchPhieuKnclReq req) throws Exception {
    String dvql = currentUser.getDvql();
    if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CHI_CUC)) {
      req.setDvql(dvql.substring(0, 6));
      req.setTrangThai(Contains.BAN_HANH);
    } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
      req.setDvql(dvql);
    }
    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
    Page<XhPhieuKnclHdr> search = xhPhieuKnclRepository.search(req, pageable);
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");

    Map<String, Map<String, Object>> mapVthh = getListDanhMucHangHoaObject();
    search.getContent().forEach(s -> {
      s.setMapVthh(mapVthh);
      s.setMapDmucDvi(mapDmucDvi);
    });
    return search;
  }

  @Transactional
  public XhPhieuKnclHdr create(CustomUserDetails currentUser, XhPhieuKnclReq objReq) throws Exception {
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    if (DataUtils.safeToString(objReq.getSoBbQd()).split("/").length != 1) {
      Optional<XhPhieuKnclHdr> optional = xhPhieuKnclRepository.findBySoBbQd(objReq.getSoBbQd());
      if (optional.isPresent()) {
        throw new Exception("Số quyết định đã tồn tại");
      }
    }
    XhPhieuKnclHdr data = new XhPhieuKnclHdr();
    BeanUtils.copyProperties(objReq, data);
    data.setMaDvi(currentUser.getUser().getDepartment());
    data.setTrangThai(Contains.DUTHAO);
    XhPhieuKnclHdr created = xhPhieuKnclRepository.save(data);
    String[] soBbQd = created.getSoBbQd().split("/");
    created.setSoBbQd(created.getId() + "/" + soBbQd[1]);
    XhPhieuKnclHdr save = xhPhieuKnclRepository.save(created);
    return save;
  }

  @Transactional
  public XhPhieuKnclHdr update(CustomUserDetails currentUser, XhPhieuKnclReq objReq) throws Exception {
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    Optional<XhPhieuKnclHdr> optional = xhPhieuKnclRepository.findById(objReq.getId());
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu cần sửa");
    }
    if (DataUtils.safeToString(objReq.getSoBbQd()).split("/").length != 1) {
      Optional<XhPhieuKnclHdr> soQd = xhPhieuKnclRepository.findBySoBbQd(objReq.getSoBbQd());
      if (soQd.isPresent()) {
        if (!soQd.get().getId().equals(objReq.getId())) {
          throw new Exception("Số quyết định đã tồn tại");
        }
      }
    }

    XhPhieuKnclHdr data = optional.get();
    BeanUtils.copyProperties(objReq, data, "id", "maDvi");
    XhPhieuKnclHdr updated = xhPhieuKnclRepository.save(data);
    return updated;
  }


  public List<XhPhieuKnclHdr> detail(List<Long> ids) throws Exception {
    if (DataUtils.isNullOrEmpty(ids)) throw new Exception("Tham số không hợp lệ.");
    List<XhPhieuKnclHdr> optional = xhPhieuKnclRepository.findByIdIn(ids);
    if (DataUtils.isNullOrEmpty(optional)) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    Map<String, Map<String, Object>> mapVthh = getListDanhMucHangHoaObject();
    List<XhPhieuKnclHdr> allById = xhPhieuKnclRepository.findAllById(ids);
    allById.forEach(data -> {
      data.setMapDmucDvi(mapDmucDvi);
      data.setMapVthh(mapVthh);
    });
    return allById;
  }

  @Transactional
  public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
    List<XhPhieuKnclHdr> list = xhPhieuKnclRepository.findAllByIdIn(idSearchReq.getIdList());
    if (list.isEmpty()) {
      throw new Exception("Bản ghi không tồn tại");
    }
    xhPhieuKnclRepository.deleteAll(list);

  }


  public XhPhieuKnclHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
    if (StringUtils.isEmpty(statusReq.getId())) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    Optional<XhPhieuKnclHdr> optional = xhPhieuKnclRepository.findById(Long.valueOf(statusReq.getId()));
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    } else {
      XhPhieuKnclHdr data = optional.get();
      String status = data.getTrangThai() + statusReq.getTrangThai();
      switch (status) {
        case Contains.DU_THAO + Contains.CHO_DUYET_TP:
        case Contains.TU_CHOI_TP + Contains.CHO_DUYET_TP:
        case Contains.TUCHOI_LDCC + Contains.CHO_DUYET_TP:
        case Contains.TUCHOI_LDC + Contains.CHO_DUYET_TP:
        case Contains.DU_THAO + Contains.CHODUYET_LDCC:
          data.setNguoiGduyetId(currentUser.getUser().getId());
          data.setNgayGduyet(LocalDate.now());
          data.setKtvBaoQuan(currentUser.getUser().getFullName());
          break;
        case Contains.CHO_DUYET_TP + Contains.CHODUYET_LDC:
          data.setNguoiPduyetTpId(currentUser.getUser().getId());
          data.setNgayPduyetTp(LocalDate.now());
          data.setTruongPhong(currentUser.getUser().getFullName());
          break;
        case Contains.CHO_DUYET_TP + Contains.TUCHOI_TP:
          data.setNguoiPduyetTpId(currentUser.getUser().getId());
          data.setNgayPduyetTp(LocalDate.now());
          data.setLyDoTuChoi(statusReq.getLyDoTuChoi());
          data.setTruongPhong(currentUser.getUser().getFullName());
          break;
        case Contains.CHODUYET_LDC + Contains.TUCHOI_LDC:
          data.setNguoiPduyetId(currentUser.getUser().getId());
          data.setNgayPduyet(LocalDate.now());
          data.setLyDoTuChoi(statusReq.getLyDoTuChoi());
          data.setLanhDaoCuc(currentUser.getUser().getFullName());
          break;
        case Contains.CHODUYET_LDC + Contains.DADUYET_LDC:
          data.setNguoiPduyetId(currentUser.getUser().getId());
          data.setIdLanhDao(currentUser.getUser().getId());
          data.setNgayPduyet(LocalDate.now());
          data.setLanhDaoCuc(currentUser.getUser().getFullName());
          break;
        case Contains.CHODUYET_LDCC + Contains.TUCHOI_LDCC:
          data.setNguoiPduyetId(currentUser.getUser().getId());
          data.setNgayPduyet(LocalDate.now());
          data.setLyDoTuChoi(statusReq.getLyDoTuChoi());
          break;
        case Contains.CHODUYET_LDCC + Contains.DADUYET_LDCC:
          data.setNguoiPduyetId(currentUser.getUser().getId());
          data.setNgayPduyet(LocalDate.now());
          break;
        default:
          throw new Exception("Phê duyệt không thành công");
      }
      data.setTrangThai(statusReq.getTrangThai());
      XhPhieuKnclHdr created = xhPhieuKnclRepository.save(data);
      return created;
    }
  }


  public void export(CustomUserDetails currentUser, SearchPhieuKnclReq objReq, HttpServletResponse response) throws Exception {
    PaggingReq paggingReq = new PaggingReq();
    paggingReq.setPage(0);
    paggingReq.setLimit(Integer.MAX_VALUE);
    objReq.setPaggingReq(paggingReq);
    Page<XhPhieuKnclHdr> page = this.searchPage(currentUser, objReq);
    List<XhPhieuKnclHdr> data = page.getContent();

    String title = "Danh sách quyết định thanh lý hàng DTQG ";
    String[] rowsName = new String[]{"STT", "Số quyết định", "Trích yếu", "Ngày ký", "Hồ sơ đề nghị thanh lý", "Trạng thái"};
    String fileName = "danh-sach-bien-ban-lay-mau.xlsx";
    List<Object[]> dataList = new ArrayList<Object[]>();
    Object[] objs = null;
    for (int i = 0; i < data.size(); i++) {
      XhPhieuKnclHdr qd = data.get(i);
      objs = new Object[rowsName.length];
      objs[0] = i;
      objs[1] = qd.getSoBbQd();
      /*objs[2] = qd.getTrichYeu();
      objs[3] = qd.getNgayKy();
      objs[4] = qd.getSoHoSo();
      objs[5] = qd.getTenTrangThai();*/
      dataList.add(objs);
    }
    ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
    ex.export();
  }

  public ReportTemplateResponse preview(XhPhieuKnclReq objReq) throws Exception {
    var xhPhieuKnclHdr = xhPhieuKnclRepository.findById(objReq.getId());
    if (!xhPhieuKnclHdr.isPresent()) throw new Exception("Không tồn tại bản ghi");
    var fileTemplate = "";
    if (StringUtils.isEmpty(xhPhieuKnclHdr.get().getLoaiVthh()))
      throw new Exception("Không tồn tại loại vật tư hàng hoá");
    var qlnvDmVattu = qlnvDmVattuRepository.findByMa(xhPhieuKnclHdr.get().getLoaiVthh());
    if (Objects.isNull(qlnvDmVattu)) throw new Exception("Không tồn tại bản ghi");
    if (!StringUtils.isEmpty(qlnvDmVattu.getLoaiHang())) {
      if (qlnvDmVattu.getLoaiHang().equals("VT")) {
        fileTemplate = "xuatcuutrovientro/" + "3.1.C84-HD_Phiếu kiểm nghiệm chất lượng_VT.docx";
      } else {
        fileTemplate = "xuatcuutrovientro/" + "3. C84-HD_Phiếu kiểm nghiệm chất lượng_LT.docx";
      }
    }
    Optional<UserInfo> userInfo = Optional.of(new UserInfo());
    if (xhPhieuKnclHdr.get().getIdLanhDao() != null) {
      userInfo = userInfoRepository.findById(xhPhieuKnclHdr.get().getIdLanhDao());
    }
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    List<XhPhieuKnclHdr> xhPhieuKnclHdrs = new ArrayList<>();
    xhPhieuKnclHdrs.add(xhPhieuKnclHdr.get());
    xhPhieuKnclHdrs.forEach(data -> {
      data.setMapDmucDvi(mapDmucDvi);
    });
    FileInputStream inputStream = new FileInputStream(baseReportFolder + fileTemplate);
    var xhPhieuKnclHdrPreview = setDataToPreview(xhPhieuKnclHdr, userInfo);
    return docxToPdfConverter.convertDocxToPdf(inputStream, xhPhieuKnclHdrPreview);
  }

  private XhPhieuKnclHdrPreview setDataToPreview(Optional<XhPhieuKnclHdr> xhPhieuKnclHdr, Optional<UserInfo> userInfo) throws Exception {
    return XhPhieuKnclHdrPreview.builder()
        .tenDvi(xhPhieuKnclHdr.get().getTenDvi())
        .maQhns(xhPhieuKnclHdr.get().getMaQhns())
        .loaiVthh(qlnvDmVattuRepository.findByMa(xhPhieuKnclHdr.get().getLoaiVthh()).getTen())
        .soBbQd(xhPhieuKnclHdr.get().getSoBbQd())
        .tenNganKho(xhPhieuKnclHdr.get().getTenNganKho())
        .tenLoKho(xhPhieuKnclHdr.get().getTenLoKho())
        .tenNhaKho(xhPhieuKnclHdr.get().getTenNhaKho())
        .tenDiemKho(xhPhieuKnclHdr.get().getTenDiemKho())
        .soLuongHangBaoQuan("")
        .hinhThucKeLotBaoQuan("")
        .tenThuKho("")
        .ngayNhapDayKho("")
        .ngayBbLayMau(xhPhieuKnclHdr.get().getSoBbLayMau())
        .ngayKiemNghiem(xhPhieuKnclHdr.get().getNgayKiemNghiem() != null ? Contains.convertDateToString(xhPhieuKnclHdr.get().getNgayKiemNghiem()) : "")
        .ketLuan(xhPhieuKnclHdr.get().getKetLuan())
        .ketQua(xhPhieuKnclHdr.get().getKetQua())
        .ngayNhap(xhPhieuKnclHdr.get().getNgayKiemNghiem() != null ? String.valueOf(xhPhieuKnclHdr.get().getNgayKiemNghiem().getDayOfMonth()) : "")
        .thangNhap(xhPhieuKnclHdr.get().getNgayKiemNghiem() != null ? String.valueOf(xhPhieuKnclHdr.get().getNgayKiemNghiem().getMonth().getValue()) : "")
        .namNhap(xhPhieuKnclHdr.get().getNgayKiemNghiem() != null ? String.valueOf(xhPhieuKnclHdr.get().getNgayKiemNghiem().getYear()) : "")
        .nguoiLapPhieu(xhPhieuKnclHdr.get().getNguoiTaoId() != null ? userInfoRepository.findById(xhPhieuKnclHdr.get().getNguoiTaoId()).get().getFullName() : "")
        .ktvBaoQuan(xhPhieuKnclHdr.get().getKtvBaoQuan())
        .lanhDaoCuc(userInfo.isPresent() ? userInfo.get().getFullName() : "")
        .xhPhieuKnclDtlDto(convertXhPhieuKnclDtlToDto(xhPhieuKnclHdr.get().getXhPhieuKnclDtl()))
        .build();
  }

  private List<XhPhieuKnclDtlDto> convertXhPhieuKnclDtlToDto(List<XhPhieuKnclDtl> xhPhieuKnclDtl) {
    List<XhPhieuKnclDtlDto> xhPhieuKnclDtlDtos = new ArrayList<>();
    int stt = 1;
    for (var res : xhPhieuKnclDtl) {
      var xhPhieuKnclDtlDto = XhPhieuKnclDtlDto.builder()
          .stt(stt++)
          .chiTieuCl(StringUtils.isEmpty(res.getChiTieuCl()) ?res.getTen():res.getChiTieuCl() )
          .chiSoCl(res.getChiSoCl())
          .ketQua(res.getKetQua())
          .phuongPhap(res.getPhuongPhap())
          .danhGia(res.getDanhGia())
          .build();
      xhPhieuKnclDtlDtos.add(xhPhieuKnclDtlDto);
    }
    return xhPhieuKnclDtlDtos;
  }
}
