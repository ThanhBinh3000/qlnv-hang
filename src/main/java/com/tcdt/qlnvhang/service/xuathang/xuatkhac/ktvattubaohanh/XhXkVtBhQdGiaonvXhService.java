package com.tcdt.qlnvhang.service.xuathang.xuatkhac.ktvattubaohanh;

import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.ktluongthuc.XhXkTongHopRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.vattubaohanh.XhXkVtBhQdGiaonvXhRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattubaohanh.XhXkVtBhQdGiaonvXhRequest;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.kthanghoa.XhXkTongHopHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu.XhXkKhXuatHang;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattubaohanh.XhXkVtBhQdGiaonvXhHdr;
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
import java.util.stream.Collectors;

@Service
public class XhXkVtBhQdGiaonvXhService extends BaseServiceImpl {
  @Autowired
  private XhXkVtBhQdGiaonvXhRepository xhXkVtBhQdGiaonvXhRepository;
  @Autowired
  private FileDinhKemService fileDinhKemService;
  @Autowired
  private XhXkTongHopRepository xhXkTongHopRepository;

  public Page<XhXkVtBhQdGiaonvXhHdr> searchPage(CustomUserDetails currentUser, XhXkVtBhQdGiaonvXhRequest req) throws Exception {
    String dvql = currentUser.getDvql();
    if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CHI_CUC)) {
      req.setDvql(dvql.substring(0, 6));
    } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
      req.setDvql(dvql);
    }
    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
    Page<XhXkVtBhQdGiaonvXhHdr> search = xhXkVtBhQdGiaonvXhRepository.searchPage(req, pageable);
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    search.getContent().forEach(s -> {
      s.setTenLoai(Contains.getLoaiHinhXuat(s.getLoai()));
      s.setTenTrangThai(TrangThaiAllEnum.getLabelById(s.getTrangThai()));
      s.setTenTrangThaiXh(TrangThaiAllEnum.getLabelById(s.getTrangThaiXh()));
      s.getQdGiaonvXhDtl().forEach(item -> {
        item.setMapVthh(mapVthh);
        item.setMapDmucDvi(mapDmucDvi);
      });
    });
    return search;
  }

  @Transactional
  public XhXkVtBhQdGiaonvXhHdr save(CustomUserDetails currentUser, XhXkVtBhQdGiaonvXhRequest objReq) throws Exception {
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    if (!DataUtils.isNullObject(objReq.getSoQuyetDinh())) {
      Optional<XhXkVtBhQdGiaonvXhHdr> optional = xhXkVtBhQdGiaonvXhRepository.findBySoQuyetDinh(objReq.getSoQuyetDinh());
      if (optional.isPresent()) {
        throw new Exception("Số quyết định đã tồn tại");
      }
    }
    XhXkVtBhQdGiaonvXhHdr data = new XhXkVtBhQdGiaonvXhHdr();
    BeanUtils.copyProperties(objReq, data);
    data.setTrangThai(Contains.DUTHAO);
    data.setTrangThaiXh(Contains.CHUA_THUC_HIEN);
    data.getQdGiaonvXhDtl().forEach(s -> {
      s.setQdGiaonvXhHdr(data);
      s.setId(null);
    });
    XhXkVtBhQdGiaonvXhHdr created = xhXkVtBhQdGiaonvXhRepository.save(data);
    //update số lần lấy mẫu
    this.updateTongHop(created, false);
    //save file đính kèm
    fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKemReq(), created.getId(), XhXkVtBhQdGiaonvXhHdr.TABLE_NAME);
    return detail(created.getId());
  }


  @Transactional()
  public XhXkVtBhQdGiaonvXhHdr update(CustomUserDetails currentUser, XhXkVtBhQdGiaonvXhRequest objReq) throws Exception {
    if (objReq.getId() == null) {
      throw new Exception("Bad request!");
    }
    Optional<XhXkVtBhQdGiaonvXhHdr> optional = xhXkVtBhQdGiaonvXhRepository.findById(objReq.getId());
    if (!optional.isPresent()) throw new Exception("Không tồn tại bản ghi!");

    if (objReq.getSoQuyetDinh().contains("/") && !ObjectUtils.isEmpty(objReq.getSoQuyetDinh().split("/")[0])) {
      Optional<XhXkVtBhQdGiaonvXhHdr> optionalBySoTt = xhXkVtBhQdGiaonvXhRepository.findBySoQuyetDinh(objReq.getSoQuyetDinh());
      if (optionalBySoTt.isPresent() && optionalBySoTt.get().getId() != objReq.getId()) {
        if (!optionalBySoTt.isPresent()) throw new Exception("Số quyết định đã tồn tại!");
      }
    }
    XhXkVtBhQdGiaonvXhHdr dx = optional.get();
    dx.getQdGiaonvXhDtl().forEach(e -> e.setQdGiaonvXhHdr(null));
    BeanUtils.copyProperties(objReq, dx);
    dx.getQdGiaonvXhDtl().forEach(e -> e.setQdGiaonvXhHdr(dx));
    dx.setQdGiaonvXhDtl(objReq.getQdGiaonvXhDtl());
    XhXkVtBhQdGiaonvXhHdr created = xhXkVtBhQdGiaonvXhRepository.save(dx);
    //update số lần lấy mẫu
    this.updateTongHop(created, false);
    fileDinhKemService.delete(dx.getId(), Collections.singleton(XhXkVtBhQdGiaonvXhHdr.TABLE_NAME));
    //save file đính kèm
    fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKemReq(), created.getId(), XhXkVtBhQdGiaonvXhHdr.TABLE_NAME);
    return detail(created.getId());
  }

  @Transactional()
  public XhXkVtBhQdGiaonvXhHdr detail(Long id) throws Exception {
    if (ObjectUtils.isEmpty(id)) throw new Exception("Tham số không hợp lệ.");
    Optional<XhXkVtBhQdGiaonvXhHdr> optional = xhXkVtBhQdGiaonvXhRepository.findById(id);
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    XhXkVtBhQdGiaonvXhHdr model = optional.get();
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    List<FileDinhKem> fileDinhKem = fileDinhKemService.search(model.getId(), Arrays.asList(XhXkKhXuatHang.TABLE_NAME));
    model.setFileDinhKems(fileDinhKem);
    model.getQdGiaonvXhDtl().forEach(s -> {
      s.setMapDmucDvi(mapDmucDvi);
      s.setMapVthh(mapVthh);
    });
    model.setTenDvi(mapDmucDvi.get(model.getMaDvi()));
    model.setTenTrangThai(TrangThaiAllEnum.getLabelById(model.getTrangThai()));
    return model;
  }


  @Transactional
  public void delete(IdSearchReq idSearchReq) throws Exception {
    Optional<XhXkVtBhQdGiaonvXhHdr> optional = xhXkVtBhQdGiaonvXhRepository.findById(idSearchReq.getId());
    if (!optional.isPresent()) {
      throw new Exception("Bản ghi không tồn tại");
    }
    XhXkVtBhQdGiaonvXhHdr data = optional.get();
    //update số lần lấy mẫu
    this.updateTongHop(data, true);
    fileDinhKemService.deleteMultiple(Collections.singleton(data.getId()), Collections.singleton(XhXkVtBhQdGiaonvXhHdr.TABLE_NAME));
    xhXkVtBhQdGiaonvXhRepository.delete(data);
  }

  @Transient
  public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
    List<XhXkVtBhQdGiaonvXhHdr> list = xhXkVtBhQdGiaonvXhRepository.findByIdIn(idSearchReq.getIdList());
    if (list.isEmpty()) {
      throw new Exception("Bản ghi không tồn tại");
    }
    //update số lần lấy mẫu
    List<Long> listId = list.stream().map(XhXkVtBhQdGiaonvXhHdr::getId).collect(Collectors.toList());
    List<XhXkTongHopHdr> listTongHop = xhXkTongHopRepository.findByIdIn(listId);
    if (!DataUtils.isNullObject(listTongHop)) {
      for (XhXkTongHopHdr tongHop : listTongHop) {
        tongHop.setSoLanLm(null);
        xhXkTongHopRepository.save(tongHop);
      }
    }
    fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Collections.singleton(XhXkVtBhQdGiaonvXhHdr.TABLE_NAME));
    xhXkVtBhQdGiaonvXhRepository.deleteAll(list);
  }


  public XhXkVtBhQdGiaonvXhHdr pheDuyet(CustomUserDetails currentUser, StatusReq req) throws Exception {
    Optional<XhXkVtBhQdGiaonvXhHdr> dx = xhXkVtBhQdGiaonvXhRepository.findById(req.getId());
    if (!dx.isPresent()) {
      throw new Exception("Không tồn tại bản ghi");
    }
    XhXkVtBhQdGiaonvXhHdr xhXkVtBhQdGiaonvXhHdr = dx.get();
    String status = xhXkVtBhQdGiaonvXhHdr.getTrangThai() + req.getTrangThai();
    switch (status) {
      case Contains.DU_THAO + Contains.CHO_DUYET_LDC:
      case Contains.TU_CHOI_LDC + Contains.CHO_DUYET_LDC:
        xhXkVtBhQdGiaonvXhHdr.setNguoiGduyetId(currentUser.getUser().getId());
        xhXkVtBhQdGiaonvXhHdr.setNgayGduyet(LocalDate.now());
        break;
      case Contains.CHO_DUYET_LDC + Contains.TU_CHOI_LDC:
        xhXkVtBhQdGiaonvXhHdr.setLyDoTuChoi(req.getLyDoTuChoi());
        break;
      case Contains.CHO_DUYET_LDC + Contains.DA_DUYET_LDC:
        xhXkVtBhQdGiaonvXhHdr.setNguoiPduyetId(currentUser.getUser().getId());
        xhXkVtBhQdGiaonvXhHdr.setNgayPduyet(LocalDate.now());
        break;
      default:
        throw new Exception("Phê duyệt không thành công.");
    }
    xhXkVtBhQdGiaonvXhHdr.setTrangThai(req.getTrangThai());
    XhXkVtBhQdGiaonvXhHdr model = xhXkVtBhQdGiaonvXhRepository.save(xhXkVtBhQdGiaonvXhHdr);
    return detail(model.getId());
  }


  public void export(CustomUserDetails currentUser, XhXkVtBhQdGiaonvXhRequest objReq, HttpServletResponse response) throws Exception {
    PaggingReq paggingReq = new PaggingReq();
    paggingReq.setPage(0);
    paggingReq.setLimit(Integer.MAX_VALUE);
    objReq.setPaggingReq(paggingReq);
    List<XhXkVtBhQdGiaonvXhHdr> data = this.searchPage(currentUser, objReq).getContent();
    String title, fileName = "";
    String[] rowsName;
    Object[] objs;
    List<Object[]> dataList = new ArrayList<>();
    title = "Danh sách quyết định giao nhiệm vụ xuất hàng";
    fileName = "danh-sach-quyet-dinh-giao-nhiem-vu-xuat-hang.xlsx";
    rowsName = new String[]{"STT", "Năm xuất", "Số quyết định", "Ngày quyết định", "Loại hình nhập xuất", "Mã DS tổng hợp", "Số QĐ xuất giảm TC", "Thời gian xuất hàng", "Trích yếu", "Trạng thái"};
    for (int i = 0; i < data.size(); i++) {
      XhXkVtBhQdGiaonvXhHdr dx = data.get(i);
      objs = new Object[rowsName.length];
      objs[0] = i;
      objs[1] = dx.getNam();
      objs[2] = dx.getSoQuyetDinh();
      objs[3] = dx.getNgayKy();
      objs[4] = dx.getTenLoai();
      objs[5] = dx.getSoCanCu();
      objs[6] = dx.getSoCanCu();
      objs[7] = dx.getThoiHanXuatHang();
      objs[8] = dx.getTrichYeu();
      objs[9] = dx.getTenTrangThai();
      dataList.add(objs);
    }
    ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
    ex.export();
  }

  public void updateTongHop(XhXkVtBhQdGiaonvXhHdr qdGiaonvXh, boolean xoa) {
    if (!DataUtils.isNullObject(qdGiaonvXh.getIdCanCu())) {
      Optional<XhXkTongHopHdr> tongHop = xhXkTongHopRepository.findById(qdGiaonvXh.getIdCanCu());
      if (tongHop.isPresent()) {
        XhXkTongHopHdr item = tongHop.get();
        if (xoa) {
          item.setSoLanLm(null);
        } else {
          item.setSoLanLm(qdGiaonvXh.getSoLanLm());
        }
        xhXkTongHopRepository.save(item);
      }
    }
  }

}
