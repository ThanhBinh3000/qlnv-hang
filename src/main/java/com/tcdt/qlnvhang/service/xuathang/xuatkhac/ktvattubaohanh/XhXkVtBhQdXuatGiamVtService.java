package com.tcdt.qlnvhang.service.xuathang.xuatkhac.ktvattubaohanh;

import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.vattubaohanh.XhXkVtBhBaoCaoKdmRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.vattubaohanh.XhXkVtBhQdGiaonvXnRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.vattubaohanh.XhXkVtBhQdXuatGiamVtRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattubaohanh.XhXkVtBhQdXuatGiamVtRequest;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattubaohanh.XhXkVtBhBaoCaoKdm;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattubaohanh.XhXkVtBhQdXuatGiamVt;
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

import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;

@Service
public class XhXkVtBhQdXuatGiamVtService extends BaseServiceImpl {
  @Autowired
  private XhXkVtBhQdXuatGiamVtRepository xhXkVtBhQdXuatGiamVattuRepository;
  @Autowired
  private XhXkVtBhBaoCaoKdmRepository xhXkVtBhBaoCaoKdmRepository;
  @Autowired
  private XhXkVtBhQdGiaonvXnRepository xhXkVtBhQdGiaonvXnRepository;
  @Autowired
  private FileDinhKemService fileDinhKemService;

  public Page<XhXkVtBhQdXuatGiamVt> searchPage(CustomUserDetails currentUser, XhXkVtBhQdXuatGiamVtRequest req) throws Exception {
    String dvql = currentUser.getDvql();
    if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
      req.setDvql(dvql.substring(0, 4));
    } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_TONG_CUC)) {
      req.setDvql(dvql);
    }
    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
    Page<XhXkVtBhQdXuatGiamVt> search = xhXkVtBhQdXuatGiamVattuRepository.searchPage(req, pageable);
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    search.getContent().forEach(s -> {
      s.setTenLoai(Contains.getLoaiHinhXuat(s.getLoai()));
      s.setTenTrangThai(TrangThaiAllEnum.getLabelById(s.getTrangThai()));
      s.setTenDvi(mapDmucDvi.get(s.getMaDvi()));
      s.setTenDviNhan(mapDmucDvi.get(s.getMaDviNhan()));
    });
    return search;
  }

  @Transactional
  public XhXkVtBhQdXuatGiamVt save(CustomUserDetails currentUser, XhXkVtBhQdXuatGiamVtRequest objReq) throws Exception {
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    if (!DataUtils.isNullObject(objReq.getSoQuyetDinh())) {
      Optional<XhXkVtBhQdXuatGiamVt> optional = xhXkVtBhQdXuatGiamVattuRepository.findBySoQuyetDinh(objReq.getSoQuyetDinh());
      if (optional.isPresent()) {
        throw new Exception("Số quyết định đã tồn tại");
      }
    }
    if (objReq.getQdXuatGiamVtDtl().isEmpty()) {
      throw new Exception("Danh sách hàng hóa xuất giảm trống, không thể tạo quyết định.");
    }
    XhXkVtBhQdXuatGiamVt data = new XhXkVtBhQdXuatGiamVt();
    BeanUtils.copyProperties(objReq, data);
    data.setTrangThai(Contains.DUTHAO);
    data.getQdXuatGiamVtDtl().forEach(s -> {
      s.setQdXuatGiamVt(data);
      s.setId(null);
    });
    XhXkVtBhQdXuatGiamVt created = xhXkVtBhQdXuatGiamVattuRepository.save(data);
    //save file đính kèm
    fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), created.getId(), XhXkVtBhQdXuatGiamVt.TABLE_NAME);
    //cập nhật số qd vào báo cáo kq kdm
    this.updatePhieuData(created, false);
    return detail(created.getId());
  }


  @Transactional()
  public XhXkVtBhQdXuatGiamVt update(CustomUserDetails currentUser, XhXkVtBhQdXuatGiamVtRequest objReq) throws Exception {
    if (objReq.getId() == null) {
      throw new Exception("Bad request!");
    }
    Optional<XhXkVtBhQdXuatGiamVt> optional = xhXkVtBhQdXuatGiamVattuRepository.findById(objReq.getId());
    if (!optional.isPresent()) throw new Exception("Không tồn tại bản ghi!");

    if (objReq.getSoQuyetDinh().contains("/") && !ObjectUtils.isEmpty(objReq.getSoQuyetDinh().split("/")[0])) {
      Optional<XhXkVtBhQdXuatGiamVt> optionalBySoTt = xhXkVtBhQdXuatGiamVattuRepository.findBySoQuyetDinh(objReq.getSoQuyetDinh());
      if (optionalBySoTt.isPresent() && optionalBySoTt.get().getId() != objReq.getId()) {
        if (!optionalBySoTt.isPresent()) throw new Exception("Số quyết định đã tồn tại!");
      }
    }
    if (objReq.getQdXuatGiamVtDtl().isEmpty()) {
      throw new Exception("Danh sách hàng hóa xuất giảm trống, không thể tạo quyết định.");
    }
    XhXkVtBhQdXuatGiamVt dx = optional.get();
    dx.getQdXuatGiamVtDtl().forEach(e -> e.setQdXuatGiamVt(null));

    BeanUtils.copyProperties(objReq, dx);
    dx.getQdXuatGiamVtDtl().forEach(e -> e.setQdXuatGiamVt(dx));
    dx.setQdXuatGiamVtDtl(objReq.getQdXuatGiamVtDtl());
    XhXkVtBhQdXuatGiamVt created = xhXkVtBhQdXuatGiamVattuRepository.save(dx);
    fileDinhKemService.delete(dx.getId(), Collections.singleton(XhXkVtBhQdXuatGiamVt.TABLE_NAME));
    //save file đính kèm
    fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), created.getId(), XhXkVtBhQdXuatGiamVt.TABLE_NAME);
    //cập nhật số qd vào báo cáo kq kdm
    this.updatePhieuData(created, false);
    return detail(created.getId());
  }

  @Transactional()
  public XhXkVtBhQdXuatGiamVt detail(Long id) throws Exception {
    if (ObjectUtils.isEmpty(id)) throw new Exception("Tham số không hợp lệ.");
    Optional<XhXkVtBhQdXuatGiamVt> optional = xhXkVtBhQdXuatGiamVattuRepository.findById(id);
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    XhXkVtBhQdXuatGiamVt model = optional.get();
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    List<FileDinhKem> fileDinhKem = fileDinhKemService.search(model.getId(), Arrays.asList(XhXkVtBhQdXuatGiamVt.TABLE_NAME));
    model.setFileDinhKems(fileDinhKem);
    model.setTenDvi(mapDmucDvi.get(model.getMaDvi()));
    model.setTenDviNhan(mapDmucDvi.get(model.getMaDviNhan()));
    model.setTenTrangThai(TrangThaiAllEnum.getLabelById(model.getTrangThai()));
    model.getQdXuatGiamVtDtl().forEach(item -> {
      item.setMapVthh(mapVthh);
      item.setMapDmucDvi(mapDmucDvi);
    });
    return model;
  }


  @Transactional
  public void delete(IdSearchReq idSearchReq) throws Exception {
    Optional<XhXkVtBhQdXuatGiamVt> optional = xhXkVtBhQdXuatGiamVattuRepository.findById(idSearchReq.getId());
    if (!optional.isPresent()) {
      throw new Exception("Bản ghi không tồn tại");
    }
    XhXkVtBhQdXuatGiamVt data = optional.get();
    fileDinhKemService.deleteMultiple(Collections.singleton(data.getId()), Collections.singleton(XhXkVtBhQdXuatGiamVt.TABLE_NAME));
    //cập nhật số qd vào báo cáo kq kdm
    this.updatePhieuData(data, true);
    xhXkVtBhQdXuatGiamVattuRepository.delete(data);
  }

  @Transient
  public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
    List<XhXkVtBhQdXuatGiamVt> list = xhXkVtBhQdXuatGiamVattuRepository.findByIdIn(idSearchReq.getIdList());
    if (list.isEmpty()) {
      throw new Exception("Bản ghi không tồn tại");
    }
    for (XhXkVtBhQdXuatGiamVt qdXuatGiamVattu : list) {
      //cập nhật số qd vào báo cáo kq kdm
      this.updatePhieuData(qdXuatGiamVattu, true);
    }
    fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Collections.singleton(XhXkVtBhQdXuatGiamVt.TABLE_NAME));
    xhXkVtBhQdXuatGiamVattuRepository.deleteAll(list);
  }


  public XhXkVtBhQdXuatGiamVt pheDuyet(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
    Optional<XhXkVtBhQdXuatGiamVt> optional = xhXkVtBhQdXuatGiamVattuRepository.findById(statusReq.getId());
    if (!optional.isPresent()) {
      throw new Exception("Không tồn tại bản ghi");
    }
    String status = statusReq.getTrangThai() + optional.get().getTrangThai();
    switch (status) {
      case Contains.CHODUYET_LDV + Contains.DUTHAO:
      case Contains.CHODUYET_LDTC + Contains.CHODUYET_LDV:
      case Contains.CHODUYET_LDV + Contains.TUCHOI_LDV:
      case Contains.CHODUYET_LDV + Contains.TUCHOI_LDTC:
        optional.get().setNguoiGduyetId(currentUser.getUser().getId());
        optional.get().setNgayGduyet(LocalDate.now());
        break;
      case Contains.TUCHOI_LDV + Contains.CHODUYET_LDV:
      case Contains.TUCHOI_LDTC + Contains.CHODUYET_LDTC:
        optional.get().setNguoiPduyetId(currentUser.getUser().getId());
        optional.get().setNgayPduyet(LocalDate.now());
        optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
        break;
      case Contains.BAN_HANH + Contains.CHODUYET_LDTC:
        optional.get().setNguoiPduyetId(currentUser.getUser().getId());
        optional.get().setNgayPduyet(LocalDate.now());
        break;
      default:
        throw new Exception("Phê duyệt không thành công");
    }
    optional.get().setTrangThai(statusReq.getTrangThai());
    XhXkVtBhQdXuatGiamVt created = xhXkVtBhQdXuatGiamVattuRepository.save(optional.get());
    return created;
  }

  public void export(CustomUserDetails currentUser, XhXkVtBhQdXuatGiamVtRequest objReq, HttpServletResponse response) throws Exception {
    PaggingReq paggingReq = new PaggingReq();
    paggingReq.setPage(0);
    paggingReq.setLimit(Integer.MAX_VALUE);
    objReq.setPaggingReq(paggingReq);
    List<XhXkVtBhQdXuatGiamVt> data = this.searchPage(currentUser, objReq).getContent();
    String title, fileName = "";
    String[] rowsName;
    Object[] objs;
    List<Object[]> dataList = new ArrayList<>();
    title = "Danh sách quyết định xuất giảm vật tư";
    fileName = "ds-ke-qd-xuat-giam-vat-tu.xlsx";
    rowsName = new String[]{"STT", "Năm xuất", "Số quyết định", "Ngày quyết định", "Số báo cáo KQKĐ mẫu", "Thời hạn xuất giảm VT", "Trích yếu quyết định", "Đơn vị nhận quyết định", "Số quyết định giao NVXH của Cục", "Trạng thái"};
    for (int i = 0; i < data.size(); i++) {
      XhXkVtBhQdXuatGiamVt dx = data.get(i);
      objs = new Object[rowsName.length];
      objs[0] = i;
      objs[1] = dx.getNamKeHoach();
      objs[2] = dx.getSoQuyetDinh();
      objs[3] = dx.getNgayKy();
      objs[4] = dx.getSoCanCu();
      objs[5] = dx.getThoiHanXuatGiam();
      objs[6] = dx.getTrichYeu();
      objs[7] = dx.getTenDviNhan();
      objs[8] = dx.getListSoQdGiaoNvXh();
      objs[9] = dx.getTenTrangThai();
      dataList.add(objs);
    }
    ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
    ex.export();
  }

  public void updatePhieuData(XhXkVtBhQdXuatGiamVt qdXuatGiamVattu, boolean xoa) {
    if (!DataUtils.isNullObject(qdXuatGiamVattu.getIdCanCu())) {
      Optional<XhXkVtBhBaoCaoKdm> baoCaoKdm = xhXkVtBhBaoCaoKdmRepository.findById(qdXuatGiamVattu.getIdCanCu());
      if (baoCaoKdm.isPresent()) {
        XhXkVtBhBaoCaoKdm item = baoCaoKdm.get();
        if (xoa) {
          item.setIdQdXuatGiamVt(null);
          item.setSoQdXuatGiamVt(null);
        } else {
          item.setIdQdXuatGiamVt(qdXuatGiamVattu.getId());
          item.setSoQdXuatGiamVt(qdXuatGiamVattu.getSoQuyetDinh());
        }
        xhXkVtBhBaoCaoKdmRepository.save(item);
      }
    }
  }

}
