package com.tcdt.qlnvhang.service.xuathang.xuatkhac.ktvattubaohanh;

import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.vattubaohanh.XhXkVtBhBbBaoHanhRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.vattubaohanh.XhXkVtBhPhieuKtclRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.vattubaohanh.XhXkVtBhPhieuXuatNhapKhoRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattubaohanh.XhXkVtBhPhieuKtclRequest;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattubaohanh.XhXkVtBhBbBaoHanh;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattubaohanh.XhXkVtBhPhieuKtclHdr;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
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
import java.time.LocalDate;
import java.util.*;

@Service
public class XhXkVtBhPhieuKtclService extends BaseServiceImpl {


  @Autowired
  private XhXkVtBhPhieuKtclRepository xhXkVtBhPhieuKtclRepository;

  @Autowired
  private XhXkVtBhBbBaoHanhRepository xhXkVtBhBbBaoHanhRepository;


  @Autowired
  private XhXkVtBhPhieuXuatNhapKhoRepository xhXkVtBhPhieuXuatNhapKhoRepository;

  @Autowired
  private UserInfoRepository userInfoRepository;

  @Autowired
  private FileDinhKemService fileDinhKemService;

  public Page<XhXkVtBhPhieuKtclHdr> searchPage(CustomUserDetails currentUser, XhXkVtBhPhieuKtclRequest req) throws Exception {
    String dvql = currentUser.getDvql();
    if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CHI_CUC)) {
      req.setDvql(dvql.substring(0, 6));
    } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
      req.setDvql(dvql);
    }
    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
    Page<XhXkVtBhPhieuKtclHdr> search = xhXkVtBhPhieuKtclRepository.search(req, pageable);
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    search.getContent().forEach(s -> {
      s.setMapDmucDvi(mapDmucDvi);
      s.setMapVthh(mapVthh);
      s.setTenDvi(mapDmucDvi.get(s.getMaDvi()));
      if (s.getNguoiPduyetId() != null) {
        s.setTenThuKho(ObjectUtils.isEmpty(s.getNguoiPduyetId()) ? null : userInfoRepository.findById(s.getNguoiPduyetId()).get().getFullName());
      }
      s.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(s.getTrangThai()));
    });
    return search;
  }

  @Transactional
  public XhXkVtBhPhieuKtclHdr save(CustomUserDetails currentUser, XhXkVtBhPhieuKtclRequest objReq) throws Exception {
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    Optional<XhXkVtBhPhieuKtclHdr> optional = xhXkVtBhPhieuKtclRepository.findBySoPhieu(objReq.getSoPhieu());
    if (optional.isPresent()) {
      throw new Exception("Số phiếu đã tồn tại");
    }
    XhXkVtBhPhieuKtclHdr data = new XhXkVtBhPhieuKtclHdr();
    BeanUtils.copyProperties(objReq, data);
    data.setTrangThai(Contains.DUTHAO);
    data.getPhieuKtclDtl().forEach(s -> {
      s.setPhieuKtclHdr(data);
      s.setId(null);
    });
    XhXkVtBhPhieuKtclHdr created = xhXkVtBhPhieuKtclRepository.save(data);
    this.updateQdBbBaoHanh(created, false);
    List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), created.getId(), XhXkVtBhPhieuKtclHdr.TABLE_NAME);
    created.setFileDinhKems(fileDinhKems);
    return created;
  }

  @Transactional()
  public XhXkVtBhPhieuKtclHdr update(CustomUserDetails currentUser, XhXkVtBhPhieuKtclRequest objReq) throws Exception {
    if (objReq.getId() == null) {
      throw new Exception("Bad request!");
    }
    Optional<XhXkVtBhPhieuKtclHdr> optional = xhXkVtBhPhieuKtclRepository.findById(objReq.getId());
    if (!optional.isPresent()) throw new Exception("Không tồn tại bản ghi!");

    if (objReq.getSoPhieu().contains("/") && !ObjectUtils.isEmpty(objReq.getSoPhieu().split("/")[0])) {
      Optional<XhXkVtBhPhieuKtclHdr> optionalBySoPhieu = xhXkVtBhPhieuKtclRepository.findBySoPhieu(objReq.getSoPhieu());
      if (optionalBySoPhieu.isPresent() && optionalBySoPhieu.get().getId() != objReq.getId()) {
        if (!optionalBySoPhieu.isPresent()) throw new Exception("Số phiếu đã tồn tại!");
      }
    }
    XhXkVtBhPhieuKtclHdr dx = optional.get();
    dx.getPhieuKtclDtl().forEach(e -> e.setPhieuKtclHdr(null));
    BeanUtils.copyProperties(objReq, dx);
    dx.getPhieuKtclDtl().forEach(e -> e.setPhieuKtclHdr(dx));
    dx.setPhieuKtclDtl(objReq.getPhieuKtclDtl());
    XhXkVtBhPhieuKtclHdr created = xhXkVtBhPhieuKtclRepository.save(dx);
    this.updateQdBbBaoHanh(created, false);
    fileDinhKemService.delete(dx.getId(), Collections.singleton(XhXkVtBhPhieuKtclHdr.TABLE_NAME));
    //save file đính kèm
    fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKemReq(), created.getId(), XhXkVtBhPhieuKtclHdr.TABLE_NAME);
    return detail(created.getId());
  }


  @Transactional()
  public XhXkVtBhPhieuKtclHdr detail(Long id) throws Exception {
    if (ObjectUtils.isEmpty(id)) throw new Exception("Tham số không hợp lệ.");
    Optional<XhXkVtBhPhieuKtclHdr> optional = xhXkVtBhPhieuKtclRepository.findById(id);
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    XhXkVtBhPhieuKtclHdr model = optional.get();
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    List<FileDinhKem> fileDinhKem = fileDinhKemService.search(model.getId(), Arrays.asList(XhXkVtBhPhieuKtclHdr.TABLE_NAME));
    model.setFileDinhKems(fileDinhKem);
    model.setMapDmucDvi(mapDmucDvi);
    model.setMapVthh(mapVthh);
    model.setTenDvi(mapDmucDvi.get(model.getMaDvi()));
    model.setTenTrangThai(TrangThaiAllEnum.getLabelById(model.getTrangThai()));
    return model;
  }

  @Transactional
  public void delete(IdSearchReq idSearchReq) throws Exception {
    Optional<XhXkVtBhPhieuKtclHdr> optional = xhXkVtBhPhieuKtclRepository.findById(idSearchReq.getId());
    if (!optional.isPresent()) {
      throw new Exception("Bản ghi không tồn tại");
    }
    if (!optional.get().getTrangThai().equals(TrangThaiAllEnum.DU_THAO.getId())) {
      throw new Exception("Bản ghi có trạng thái khác dự thảo, không thể xóa.");
    }
    XhXkVtBhPhieuKtclHdr data = optional.get();
    this.updateQdBbBaoHanh(data, true);
    fileDinhKemService.deleteMultiple(Collections.singleton(data.getId()), Collections.singleton(XhXkVtBhPhieuKtclHdr.TABLE_NAME));
    xhXkVtBhPhieuKtclRepository.delete(data);
  }

  @Transient
  public XhXkVtBhPhieuKtclHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {

    if (StringUtils.isEmpty(statusReq.getId())) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    Optional<XhXkVtBhPhieuKtclHdr> optional = xhXkVtBhPhieuKtclRepository.findById(Long.valueOf(statusReq.getId()));
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    String status = statusReq.getTrangThai() + optional.get().getTrangThai();
    switch (status) {
      case Contains.CHO_DUYET_TP + Contains.DUTHAO:
      case Contains.CHODUYET_LDC + Contains.CHODUYET_TP:
      case Contains.CHO_DUYET_TP + Contains.TUCHOI_TP:
      case Contains.CHO_DUYET_TP + Contains.TUCHOI_LDC:
        optional.get().setNguoiGduyetId(currentUser.getUser().getId());
        optional.get().setNgayGduyet(LocalDate.now());
        break;
      case Contains.TUCHOI_TP + Contains.CHO_DUYET_TP:
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
    XhXkVtBhPhieuKtclHdr created = xhXkVtBhPhieuKtclRepository.save(optional.get());
    return created;
  }

  public void export(CustomUserDetails currentUser, XhXkVtBhPhieuKtclRequest objReq, HttpServletResponse response) throws Exception {
    PaggingReq paggingReq = new PaggingReq();
    paggingReq.setPage(0);
    paggingReq.setLimit(Integer.MAX_VALUE);
    objReq.setPaggingReq(paggingReq);
    Page<XhXkVtBhPhieuKtclHdr> page = this.searchPage(currentUser, objReq);
    List<XhXkVtBhPhieuKtclHdr> data = page.getContent();

    String title = "Danh sách phiếu kiểm định chất lượng";
    String[] rowsName = new String[]{"STT", "Năm KH", "Đơn vị tạo", "Số phiếu KTCL sau BH", "Ngày lập phiếu",
        "Số BB yêu cầu BH", "Ngày yêu cầu", "Số QĐ giao NVXH để lấy mẫu", "Số QĐ giao NV nhập hàng", "Trạng thái", "Trạng thái NH"};
    String fileName = "danh-sach-phieu-kiem-dinh-chat-luong.xlsx";
    List<Object[]> dataList = new ArrayList<Object[]>();
    Object[] objs = null;
    for (int i = 0; i < data.size(); i++) {
      XhXkVtBhPhieuKtclHdr dx = data.get(i);
      objs = new Object[rowsName.length];
      objs[0] = i;
      objs[1] = dx.getNam();
      objs[2] = dx.getTenDvi();
      objs[3] = dx.getSoPhieu();
      objs[4] = dx.getNgayLapPhieu();
      objs[5] = dx.getSoBbBaoHanh();
      objs[6] = dx.getThoiGianBh();
      objs[7] = dx.getSoQdGiaoNvXh();
      objs[8] = dx.getSoQdGiaoNvNh();
      objs[9] = dx.getTenTrangThai();
      objs[10] = dx.getTenTrangThaiNh();
      dataList.add(objs);
    }
    ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
    ex.export();
  }

  public void updateQdBbBaoHanh(XhXkVtBhPhieuKtclHdr phieuKtcl, boolean xoa) {
    if (!DataUtils.isNullObject(phieuKtcl.getIdBbBaoHanh())) {
      Optional<XhXkVtBhBbBaoHanh> bbBaoHanh = xhXkVtBhBbBaoHanhRepository.findById(phieuKtcl.getIdBbBaoHanh());
      XhXkVtBhBbBaoHanh f = bbBaoHanh.get();
      if (xoa) {
        f.setIdPhieuKtcl(null);
        f.setSoPhieuKtcl(null);
      } else {
        f.setIdPhieuKtcl(phieuKtcl.getId());
        f.setSoPhieuKtcl(phieuKtcl.getSoPhieu());
      }
      xhXkVtBhBbBaoHanhRepository.save(f);
    }
  }
}

