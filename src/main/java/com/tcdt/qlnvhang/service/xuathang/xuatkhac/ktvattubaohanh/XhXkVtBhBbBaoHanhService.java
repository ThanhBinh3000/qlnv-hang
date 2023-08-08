package com.tcdt.qlnvhang.service.xuathang.xuatkhac.ktvattubaohanh;

import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.vattubaohanh.XhXkVtBhBbBaoHanhRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.vattubaohanh.XhXkVtBhPhieuKdclRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.vattubaohanh.XhXkVtBhPhieuXuatNhapKhoRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.vattubaohanh.XhXkVtBhQdGiaonvXnRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattubaohanh.XhXkVtBhBbBaoHanhRequest;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattubaohanh.*;
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
public class XhXkVtBhBbBaoHanhService extends BaseServiceImpl {


  @Autowired
  private XhXkVtBhBbBaoHanhRepository xhXkVtBhBbBaoHanhRepository;
  
  @Autowired
  private XhXkVtBhPhieuKdclRepository xhXkVtBhPhieuKdclRepository;

  
  @Autowired
  private FileDinhKemService fileDinhKemService;

  public Page<XhXkVtBhBbBaoHanh> searchPage(CustomUserDetails currentUser, XhXkVtBhBbBaoHanhRequest req) throws Exception {
    req.setDvql(currentUser.getDvql());
    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
    Page<XhXkVtBhBbBaoHanh> search = xhXkVtBhBbBaoHanhRepository.search(req, pageable);
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
//        Map<String, String> mapVthh = getListDanhMucHangHoa();
    search.getContent().forEach(s -> {
      s.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(s.getTrangThai()));
      s.setMapDmucDvi(mapDmucDvi);
    });
    return search;
  }

  @Transactional
  public XhXkVtBhBbBaoHanh save(CustomUserDetails currentUser, XhXkVtBhBbBaoHanhRequest objReq) throws Exception {
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    Optional<XhXkVtBhBbBaoHanh> optional = xhXkVtBhBbBaoHanhRepository.findBySoBienBan(objReq.getSoBienBan());
    if (optional.isPresent()) {
      throw new Exception("Số báo cáo đã tồn tại");
    }
    XhXkVtBhBbBaoHanh data = new XhXkVtBhBbBaoHanh();
    BeanUtils.copyProperties(objReq, data);
    data.setTrangThai(Contains.DUTHAO);
    XhXkVtBhBbBaoHanh created = xhXkVtBhBbBaoHanhRepository.save(data);
    List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), created.getId(), XhXkVtBhBbBaoHanh.TABLE_NAME);
    created.setFileDinhKems(fileDinhKems);

    //cập nhật số biên bản bảo hành vào phiếu kdcl
    this.updatePhieuData(created, false);
    
    return created;
  }

  @Transactional()
  public XhXkVtBhBbBaoHanh update(CustomUserDetails currentUser, XhXkVtBhBbBaoHanhRequest objReq) throws Exception {
    if (objReq.getId() == null) {
      throw new Exception("Bad request!");
    }
    Optional<XhXkVtBhBbBaoHanh> optional = xhXkVtBhBbBaoHanhRepository.findById(objReq.getId());
    if (!optional.isPresent()) throw new Exception("Không tồn tại bản ghi!");

    if (objReq.getSoBienBan().contains("/") && !ObjectUtils.isEmpty(objReq.getSoBienBan().split("/")[0])) {
      Optional<XhXkVtBhBbBaoHanh> optionalBySoBb = xhXkVtBhBbBaoHanhRepository.findBySoBienBan(objReq.getSoBienBan());
      if (optionalBySoBb.isPresent() && optionalBySoBb.get().getId() != objReq.getId()) {
        if (!optionalBySoBb.isPresent()) throw new Exception("Số báo cáo đã tồn tại!");
      }
    }
    XhXkVtBhBbBaoHanh data = optional.get();
    BeanUtils.copyProperties(objReq, data);
    XhXkVtBhBbBaoHanh created = xhXkVtBhBbBaoHanhRepository.save(data);
    fileDinhKemService.delete(data.getId(), Collections.singleton(XhXkVtBhBbBaoHanh.TABLE_NAME));
    //save file đính kèm
    fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKemReq(), created.getId(), XhXkVtBhBbBaoHanh.TABLE_NAME);
    //cập nhật số biên bản bảo hành vào phiếu kdcl
    this.updatePhieuData(created, false);
    return detail(created.getId());
  }


  @Transactional()
  public XhXkVtBhBbBaoHanh detail(Long id) throws Exception {
    if (ObjectUtils.isEmpty(id)) throw new Exception("Tham số không hợp lệ.");
    Optional<XhXkVtBhBbBaoHanh> optional = xhXkVtBhBbBaoHanhRepository.findById(id);
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    XhXkVtBhBbBaoHanh model = optional.get();
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    List<FileDinhKem> fileDinhKem = fileDinhKemService.search(model.getId(), Arrays.asList(XhXkVtBhBbBaoHanh.TABLE_NAME));
    model.setFileDinhKems(fileDinhKem);
    model.setTenDvi(mapDmucDvi.get(model.getMaDvi()));
    model.setMapDmucDvi(mapDmucDvi);
    model.setMapVthh(mapVthh);
    model.setTenTrangThai(TrangThaiAllEnum.getLabelById(model.getTrangThai()));
    
    return model;
  }

  @Transactional
  public void delete(IdSearchReq idSearchReq) throws Exception {
    Optional<XhXkVtBhBbBaoHanh> optional = xhXkVtBhBbBaoHanhRepository.findById(idSearchReq.getId());
    if (!optional.isPresent()) {
      throw new Exception("Bản ghi không tồn tại");
    }
    if (optional.get().getTrangThai().equals(TrangThaiAllEnum.DU_THAO.getId())) {
      XhXkVtBhBbBaoHanh data = optional.get();
      fileDinhKemService.deleteMultiple(Collections.singleton(data.getId()), Collections.singleton(XhXkVtBhBbBaoHanh.TABLE_NAME));
      //cập nhật số biên bản bảo hành vào phiếu kdcl
      this.updatePhieuData(data, true);
      xhXkVtBhBbBaoHanhRepository.delete(data);
    } else {
      throw new Exception("Bản ghi đang ở trạng thái " + TrangThaiAllEnum.getLabelById(optional.get().getTrangThai()) + " không thể xóa.");
    }
  }

  @Transient
  public XhXkVtBhBbBaoHanh approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {

    if (StringUtils.isEmpty(statusReq.getId())) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    Optional<XhXkVtBhBbBaoHanh> optional = xhXkVtBhBbBaoHanhRepository.findById(Long.valueOf(statusReq.getId()));
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    String status = statusReq.getTrangThai() + optional.get().getTrangThai();
    switch (status) {
      case Contains.DA_HOAN_THANH + Contains.DUTHAO:
        optional.get().setNguoiPduyetId(currentUser.getUser().getId());
        optional.get().setNgayPduyet(LocalDate.now());
        break;
      default:
        throw new Exception("Phê duyệt không thành công");
    }
    optional.get().setTrangThai(statusReq.getTrangThai());
    XhXkVtBhBbBaoHanh created = xhXkVtBhBbBaoHanhRepository.save(optional.get());
    return created;
  }

  public void export(CustomUserDetails currentUser, XhXkVtBhBbBaoHanhRequest objReq, HttpServletResponse response) throws Exception {
    PaggingReq paggingReq = new PaggingReq();
    paggingReq.setPage(0);
    paggingReq.setLimit(Integer.MAX_VALUE);
    objReq.setPaggingReq(paggingReq);
    Page<XhXkVtBhBbBaoHanh> page = this.searchPage(currentUser, objReq);
    List<XhXkVtBhBbBaoHanh> data = page.getContent();

    String title = "Danh sách biên bản yêu cầu nhà cung cấp bảo hành";
    String[] rowsName = new String[]{"STT", "Đơn vị tạo ","Số biên bản", "Ngày lập biên bản",  "Số QĐ giao NVXH để lấy mẫu",
        "Số QĐ giao NVXH để nhập hàng", "Số phiếu KTCL sau bảo hành","Trạng thái"};
    String fileName = "danh-sach-bien-ban-yeu-cau-bao-hanh.xlsx";
    List<Object[]> dataList = new ArrayList<Object[]>();
    Object[] objs = null;
    for (int i = 0; i < data.size(); i++) {
      XhXkVtBhBbBaoHanh dx = data.get(i);
      objs = new Object[rowsName.length];
      objs[0] = i;
      objs[1] = dx.getTenDvi();
      objs[2] = dx.getSoBienBan();
      objs[3] = dx.getNgayLapBb();
      objs[4] = dx.getSoCanCu();
      objs[5] = dx.getSoQdGnvNh();
      objs[6] = dx.getSoPhieuKdcl();
      objs[7] = dx.getTenTrangThai();
      dataList.add(objs);
    }
    ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
    ex.export();
  }
  public void updatePhieuData(XhXkVtBhBbBaoHanh bbBaoHanh, boolean xoa) {
    if (!DataUtils.isNullObject(bbBaoHanh.getIdCanCu())) {
      Optional<XhXkVtBhPhieuKdclHdr> baoCaoKdm = xhXkVtBhPhieuKdclRepository.findById(bbBaoHanh.getIdCanCu());
      if (baoCaoKdm.isPresent()) {
        XhXkVtBhPhieuKdclHdr item = baoCaoKdm.get();
        if (xoa) {
          item.setIdBbBaoHanh(null);
          item.setSoBbBaoHanh(null);
        } else {
          item.setIdBbBaoHanh(bbBaoHanh.getId());
          item.setSoBbBaoHanh(bbBaoHanh.getSoBienBan());
        }
        xhXkVtBhPhieuKdclRepository.save(item);
      }
    }
  }
}

