package com.tcdt.qlnvhang.service.xuathang.xuatkhac.ktvattubaohanh;

import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.vattubaohanh.XhXkVtBhBbLayMauRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.vattubaohanh.XhXkVtBhPhieuXuatNhapKhoRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.vattubaohanh.XhXkVtBhQdGiaonvXnRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattubaohanh.XhXkVtBhBbLayMauRequest;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtBbLayMauHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattubaohanh.XhXkVtBhBbLayMauHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattubaohanh.XhXkVtBhPhieuXuatNhapKho;
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

@Service
public class XhXkVtBhBbLayMauService extends BaseServiceImpl {


  @Autowired
  private XhXkVtBhBbLayMauRepository xhXkVtBhBbLayMauRepository;

  @Autowired
  private XhXkVtBhQdGiaonvXnRepository xhXkVtBhQdGiaonvXnRepository;


  @Autowired
  private XhXkVtBhPhieuXuatNhapKhoRepository xhXkVtBhPhieuXuatNhapKhoRepository;

  @Autowired
  private UserInfoRepository userInfoRepository;

  @Autowired
  private FileDinhKemService fileDinhKemService;

  public Page<XhXkVtBhBbLayMauHdr> searchPage(CustomUserDetails currentUser, XhXkVtBhBbLayMauRequest req) throws Exception {
    String dvql = currentUser.getDvql();
    if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CHI_CUC)) {
      req.setDvql(dvql.substring(0, 6));
    } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
      req.setDvql(dvql);
    }
    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
    Page<XhXkVtBhBbLayMauHdr> search = xhXkVtBhBbLayMauRepository.search(req, pageable);
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    search.getContent().forEach(s -> {
      s.setMapDmucDvi(mapDmucDvi);
      s.setMapVthh(mapVthh);
      if (s.getNguoiPduyetId() != null) {
        s.setTenThuKho(ObjectUtils.isEmpty(s.getNguoiPduyetId()) ? null : userInfoRepository.findById(s.getNguoiPduyetId()).get().getFullName());
      }
      s.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(s.getTrangThai()));
    });
    return search;
  }

  @Transactional
  public XhXkVtBhBbLayMauHdr save(CustomUserDetails currentUser, XhXkVtBhBbLayMauRequest objReq) throws Exception {
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    Optional<XhXkVtBhBbLayMauHdr> optional = xhXkVtBhBbLayMauRepository.findBySoBienBan(objReq.getSoBienBan());
    if (optional.isPresent()) {
      throw new Exception("Số biên bản đã tồn tại");
    }
    XhXkVtBhBbLayMauHdr data = new XhXkVtBhBbLayMauHdr();
    BeanUtils.copyProperties(objReq, data);
    data.setTrangThai(Contains.DUTHAO);
    data.getBbLayMauDtl().forEach(s -> {
      s.setBbLayMauHdr(data);
      s.setId(null);
    });
    XhXkVtBhBbLayMauHdr created = xhXkVtBhBbLayMauRepository.save(data);
    //update số lần lấy mẫu
    this.updatePhieuXk(created, false);
    List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), created.getId(), XhXkVtBhBbLayMauHdr.TABLE_NAME);
    created.setFileDinhKems(fileDinhKems);
    return created;
  }

  @Transactional()
  public XhXkVtBhBbLayMauHdr update(CustomUserDetails currentUser, XhXkVtBhBbLayMauRequest objReq) throws Exception {
    if (objReq.getId() == null) {
      throw new Exception("Bad request!");
    }
    Optional<XhXkVtBhBbLayMauHdr> optional = xhXkVtBhBbLayMauRepository.findById(objReq.getId());
    if (!optional.isPresent()) throw new Exception("Không tồn tại bản ghi!");

    if (objReq.getSoBienBan().contains("/") && !ObjectUtils.isEmpty(objReq.getSoBienBan().split("/")[0])) {
      Optional<XhXkVtBhBbLayMauHdr> optionalBySoBb = xhXkVtBhBbLayMauRepository.findBySoBienBan(objReq.getSoBienBan());
      if (optionalBySoBb.isPresent() && optionalBySoBb.get().getId() != objReq.getId()) {
        if (!optionalBySoBb.isPresent()) throw new Exception("Số biên bản đã tồn tại!");
      }
    }
    XhXkVtBhBbLayMauHdr dx = optional.get();
    dx.getBbLayMauDtl().forEach(e -> e.setBbLayMauHdr(null));
    BeanUtils.copyProperties(objReq, dx);
    dx.getBbLayMauDtl().forEach(e -> e.setBbLayMauHdr(dx));
    dx.setBbLayMauDtl(objReq.getBbLayMauDtl());
    XhXkVtBhBbLayMauHdr created = xhXkVtBhBbLayMauRepository.save(dx);
    this.updatePhieuXk(created, false);
    fileDinhKemService.delete(dx.getId(), Collections.singleton(XhXkVtBhBbLayMauHdr.TABLE_NAME));
    //save file đính kèm
    List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), created.getId(), XhXkVtBhBbLayMauHdr.TABLE_NAME);
    created.setFileDinhKems(fileDinhKems);
    return detail(created.getId());
  }


  @Transactional()
  public XhXkVtBhBbLayMauHdr detail(Long id) throws Exception {
    if (ObjectUtils.isEmpty(id)) throw new Exception("Tham số không hợp lệ.");
    Optional<XhXkVtBhBbLayMauHdr> optional = xhXkVtBhBbLayMauRepository.findById(id);
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    XhXkVtBhBbLayMauHdr model = optional.get();
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    List<FileDinhKem> fileDinhKem = fileDinhKemService.search(model.getId(), Arrays.asList(XhXkVtBhBbLayMauHdr.TABLE_NAME));
    model.setFileDinhKems(fileDinhKem);
    model.setMapDmucDvi(mapDmucDvi);
    model.setMapVthh(mapVthh);
    model.setTenDvi(mapDmucDvi.get(model.getMaDvi()));
    model.setTenTrangThai(TrangThaiAllEnum.getLabelById(model.getTrangThai()));
    return model;
  }

  @Transactional
  public void delete(IdSearchReq idSearchReq) throws Exception {
    Optional<XhXkVtBhBbLayMauHdr> optional = xhXkVtBhBbLayMauRepository.findById(idSearchReq.getId());
    if (!optional.isPresent()) {
      throw new Exception("Bản ghi không tồn tại");
    }
    if (!optional.get().getTrangThai().equals(TrangThaiAllEnum.DU_THAO.getId())) {
      throw new Exception("Bản ghi có trạng thái khác dự thảo, không thể xóa.");
    }
    XhXkVtBhBbLayMauHdr data = optional.get();
    this.updatePhieuXk(data, true);
    fileDinhKemService.deleteMultiple(Collections.singleton(data.getId()), Collections.singleton(XhXkVtBhBbLayMauHdr.TABLE_NAME));
    xhXkVtBhBbLayMauRepository.delete(data);
  }

  @Transient
  public XhXkVtBhBbLayMauHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {

    if (StringUtils.isEmpty(statusReq.getId())) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    Optional<XhXkVtBhBbLayMauHdr> optional = xhXkVtBhBbLayMauRepository.findById(Long.valueOf(statusReq.getId()));
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    String status = statusReq.getTrangThai() + optional.get().getTrangThai();
    switch (status) {
      case Contains.CHODUYET_LDC + Contains.DUTHAO:
      case Contains.CHODUYET_LDC + Contains.TUCHOI_LDC:
        optional.get().setNguoiGduyetId(currentUser.getUser().getId());
        optional.get().setNgayGduyet(LocalDate.now());
        break;
      case Contains.TUCHOI_LDC + Contains.CHODUYET_LDC:
        optional.get().setNguoiPduyetId(currentUser.getUser().getId());
        optional.get().setNgayPduyet(LocalDate.now());
        optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
        break;
      case Contains.DADUYET_LDC + Contains.CHODUYET_LDC:
        optional.get().setNguoiPduyetId(currentUser.getUser().getId());
        optional.get().setNgayPduyet(LocalDate.now());
        break;
      default:
        throw new Exception("Phê duyệt không thành công");
    }
    optional.get().setTrangThai(statusReq.getTrangThai());
    XhXkVtBhBbLayMauHdr created = xhXkVtBhBbLayMauRepository.save(optional.get());
    return created;
  }

  public void export(CustomUserDetails currentUser, XhXkVtBhBbLayMauRequest objReq, HttpServletResponse response) throws Exception {
    PaggingReq paggingReq = new PaggingReq();
    paggingReq.setPage(0);
    paggingReq.setLimit(Integer.MAX_VALUE);
    objReq.setPaggingReq(paggingReq);
    Page<XhXkVtBhBbLayMauHdr> page = this.searchPage(currentUser, objReq);
    List<XhXkVtBhBbLayMauHdr> data = page.getContent();

    String title = "Danh sách biên bản lấy mẫu bàn giao mẫu ";
    String[] rowsName = new String[]{"STT", "Số QĐ giao nhiệm vụ XH", "Năm", "Thời gian xuất lấy mẫu", "Điểm kho", "Ngăn/Lô kho",
        "Chủng loại hàng hóa", "Số BB LM/BGM", "Ngày lấy mẫu", "Lần lấy mẫu", "Trạng thái"};
    String fileName = "danh-sach-bien-ban-lay-mau-ban-giao-mau.xlsx";
    List<Object[]> dataList = new ArrayList<Object[]>();
    Object[] objs = null;
    for (int i = 0; i < data.size(); i++) {
      XhXkVtBhBbLayMauHdr dx = data.get(i);
      objs = new Object[rowsName.length];
      objs[0] = i;
      objs[1] = dx.getSoQdGiaoNvXh();
      objs[2] = dx.getNam();
      objs[3] = dx.getNgayXuatLayMau();
      objs[4] = dx.getTenDiemKho();
      objs[5] = (dx.getTenLoKho() != null && !dx.getTenLoKho().isEmpty()) ? (dx.getTenLoKho() + ' ' + dx.getTenNganKho()) : dx.getTenNganKho();
      objs[6] = dx.getTenCloaiVthh();
      objs[7] = dx.getSoBienBan();
      objs[8] = dx.getNgayLayMau();
      objs[9] = dx.getSoLanLm();
      objs[10] = dx.getTenTrangThai();
      dataList.add(objs);
    }
    ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
    ex.export();
  }

  public void updatePhieuXk(XhXkVtBhBbLayMauHdr bbLayMau, boolean xoa) {
    if (!DataUtils.isNullObject(bbLayMau.getIdPhieuXuatKho())) {
      Optional<XhXkVtBhPhieuXuatNhapKho> phieuXuatKho = xhXkVtBhPhieuXuatNhapKhoRepository.findById(bbLayMau.getIdPhieuXuatKho());
      if (phieuXuatKho.isPresent()) {
        XhXkVtBhPhieuXuatNhapKho item = phieuXuatKho.get();
        if (xoa) {
          item.setIdBbLayMau(null);
          item.setSoBbLayMau(null);
        } else {
          item.setIdBbLayMau(bbLayMau.getId());
          item.setSoBbLayMau(bbLayMau.getSoBienBan());
        }
        xhXkVtBhPhieuXuatNhapKhoRepository.save(item);
      }
    }
  }
  public ReportTemplateResponse preview(HashMap<String, Object> body) throws Exception {
    try {
      String fileName = DataUtils.safeToString(body.get("tenBaoCao"));
      String fileTemplate = "xuatcuutrovientro/" + fileName;
      FileInputStream inputStream = new FileInputStream(baseReportFolder + fileTemplate);
      XhXkVtBhBbLayMauHdr detail = this.detail(DataUtils.safeToLong(body.get("id")));
      return docxToPdfConverter.convertDocxToPdf(inputStream, detail);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (XDocReportException e) {
      e.printStackTrace();
    }
    return null;
  }
}

