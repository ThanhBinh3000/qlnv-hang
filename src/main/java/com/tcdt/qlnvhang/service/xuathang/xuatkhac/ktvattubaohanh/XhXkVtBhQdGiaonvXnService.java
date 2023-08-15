package com.tcdt.qlnvhang.service.xuathang.xuatkhac.ktvattubaohanh;

import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.ktluongthuc.XhXkTongHopRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.ktvattu.XhXkVtQdXuatGiamVattuRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.vattubaohanh.XhXkVtBhBbBaoHanhRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.vattubaohanh.XhXkVtBhPhieuKdclRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.vattubaohanh.XhXkVtBhQdGiaonvXnRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattubaohanh.XhXkVtBhQdGiaonvXnRequest;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.kthanghoa.XhXkTongHopHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu.XhXkKhXuatHang;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu.XhXkVtQdXuatGiamVattu;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattubaohanh.XhXkVtBhBbBaoHanh;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattubaohanh.XhXkVtBhPhieuKdclHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattubaohanh.XhXkVtBhQdGiaonvXnHdr;
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

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class XhXkVtBhQdGiaonvXnService extends BaseServiceImpl {
  @Autowired
  private XhXkVtBhQdGiaonvXnRepository xhXkVtBhQdGiaonvXnRepository;
  @Autowired
  private FileDinhKemService fileDinhKemService;
  @Autowired
  private XhXkTongHopRepository xhXkTongHopRepository;
  @Autowired
  private XhXkVtQdXuatGiamVattuRepository xhXkVtQdXuatGiamVattuRepository;
  @Autowired
  private XhXkVtBhBbBaoHanhRepository xhXkVtBhBbBaoHanhRepository;
  @Autowired
  private XhXkVtBhPhieuKdclRepository xhXkVtBhPhieuKdclRepository;


  public Page<XhXkVtBhQdGiaonvXnHdr> searchPage(CustomUserDetails currentUser, XhXkVtBhQdGiaonvXnRequest req) throws Exception {
    String dvql = currentUser.getDvql();
    if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CHI_CUC)) {
      req.setDvql(dvql.substring(0, 6));
    } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
      req.setDvql(dvql);
    }
    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
    Page<XhXkVtBhQdGiaonvXnHdr> search = xhXkVtBhQdGiaonvXnRepository.searchPage(req, pageable);
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
  public XhXkVtBhQdGiaonvXnHdr save(CustomUserDetails currentUser, XhXkVtBhQdGiaonvXnRequest objReq) throws Exception {
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    if (!DataUtils.isNullObject(objReq.getSoQuyetDinh())) {
      Optional<XhXkVtBhQdGiaonvXnHdr> optional = xhXkVtBhQdGiaonvXnRepository.findBySoQuyetDinh(objReq.getSoQuyetDinh());
      if (optional.isPresent()) {
        throw new Exception("Số quyết định đã tồn tại");
      }
    }
    XhXkVtBhQdGiaonvXnHdr data = new XhXkVtBhQdGiaonvXnHdr();
    BeanUtils.copyProperties(objReq, data);
    data.setTrangThai(Contains.DUTHAO);
    data.setTrangThaiXh(Contains.CHUA_THUC_HIEN);
    data.getQdGiaonvXhDtl().forEach(s -> {
      s.setQdGiaonvXhHdr(data);
      s.setId(null);
    });
    XhXkVtBhQdGiaonvXnHdr created = xhXkVtBhQdGiaonvXnRepository.save(data);
    //update số lần lấy mẫu
    this.updateQdXuat(created, false);
    this.updateQdGiaonvNhap(created, false);
    //save file đính kèm
    fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKemReq(), created.getId(), XhXkVtBhQdGiaonvXnHdr.TABLE_NAME);
    return detail(created.getId());
  }


  @Transactional()
  public XhXkVtBhQdGiaonvXnHdr update(CustomUserDetails currentUser, XhXkVtBhQdGiaonvXnRequest objReq) throws Exception {
    if (objReq.getId() == null) {
      throw new Exception("Bad request!");
    }
    Optional<XhXkVtBhQdGiaonvXnHdr> optional = xhXkVtBhQdGiaonvXnRepository.findById(objReq.getId());
    if (!optional.isPresent()) throw new Exception("Không tồn tại bản ghi!");

    if (objReq.getSoQuyetDinh().contains("/") && !ObjectUtils.isEmpty(objReq.getSoQuyetDinh().split("/")[0])) {
      Optional<XhXkVtBhQdGiaonvXnHdr> optionalBySoTt = xhXkVtBhQdGiaonvXnRepository.findBySoQuyetDinh(objReq.getSoQuyetDinh());
      if (optionalBySoTt.isPresent() && optionalBySoTt.get().getId() != objReq.getId()) {
        if (!optionalBySoTt.isPresent()) throw new Exception("Số quyết định đã tồn tại!");
      }
    }
    XhXkVtBhQdGiaonvXnHdr dx = optional.get();
    dx.getQdGiaonvXhDtl().forEach(e -> e.setQdGiaonvXhHdr(null));
    BeanUtils.copyProperties(objReq, dx);
    dx.getQdGiaonvXhDtl().forEach(e -> e.setQdGiaonvXhHdr(dx));
    dx.setQdGiaonvXhDtl(objReq.getQdGiaonvXhDtl());
    XhXkVtBhQdGiaonvXnHdr created = xhXkVtBhQdGiaonvXnRepository.save(dx);
    //update số lần lấy mẫu
    this.updateQdXuat(created, false);
    this.updateQdGiaonvNhap(created, false);
    fileDinhKemService.delete(dx.getId(), Collections.singleton(XhXkVtBhQdGiaonvXnHdr.TABLE_NAME));
    //save file đính kèm
    fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKemReq(), created.getId(), XhXkVtBhQdGiaonvXnHdr.TABLE_NAME);
    return detail(created.getId());
  }

  @Transactional()
  public XhXkVtBhQdGiaonvXnHdr detail(Long id) throws Exception {
    if (ObjectUtils.isEmpty(id)) throw new Exception("Tham số không hợp lệ.");
    Optional<XhXkVtBhQdGiaonvXnHdr> optional = xhXkVtBhQdGiaonvXnRepository.findById(id);
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    XhXkVtBhQdGiaonvXnHdr model = optional.get();
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
    Optional<XhXkVtBhQdGiaonvXnHdr> optional = xhXkVtBhQdGiaonvXnRepository.findById(idSearchReq.getId());
    if (!optional.isPresent()) {
      throw new Exception("Bản ghi không tồn tại");
    }
    XhXkVtBhQdGiaonvXnHdr data = optional.get();
    //update số lần lấy mẫu
    this.updateQdXuat(data, true);
    this.updateQdGiaonvNhap(data, true);
    fileDinhKemService.deleteMultiple(Collections.singleton(data.getId()), Collections.singleton(XhXkVtBhQdGiaonvXnHdr.TABLE_NAME));
    xhXkVtBhQdGiaonvXnRepository.delete(data);
  }

  @Transactional
  public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
    List<XhXkVtBhQdGiaonvXnHdr> list = xhXkVtBhQdGiaonvXnRepository.findByIdIn(idSearchReq.getIdList());
    if (list.isEmpty()) {
      throw new Exception("Bản ghi không tồn tại");
    }
    //update số lần lấy mẫu
    List<Long> listId = list.stream().map(XhXkVtBhQdGiaonvXnHdr::getId).collect(Collectors.toList());
    List<XhXkTongHopHdr> listTongHop = xhXkTongHopRepository.findByIdIn(listId);
    if (!DataUtils.isNullObject(listTongHop)) {
      for (XhXkTongHopHdr tongHop : listTongHop) {
        tongHop.setSoLanLm(null);
        xhXkTongHopRepository.save(tongHop);
      }
    }
    fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Collections.singleton(XhXkVtBhQdGiaonvXnHdr.TABLE_NAME));
    xhXkVtBhQdGiaonvXnRepository.deleteAll(list);
  }


  public XhXkVtBhQdGiaonvXnHdr pheDuyet(CustomUserDetails currentUser, StatusReq req) throws Exception {
    Optional<XhXkVtBhQdGiaonvXnHdr> dx = xhXkVtBhQdGiaonvXnRepository.findById(req.getId());
    if (!dx.isPresent()) {
      throw new Exception("Không tồn tại bản ghi");
    }
    XhXkVtBhQdGiaonvXnHdr xhXkVtBhQdGiaonvXnHdr = dx.get();
    String status = xhXkVtBhQdGiaonvXnHdr.getTrangThai() + req.getTrangThai();
    switch (status) {
      case Contains.DU_THAO + Contains.CHO_DUYET_LDC:
      case Contains.TU_CHOI_LDC + Contains.CHO_DUYET_LDC:
        xhXkVtBhQdGiaonvXnHdr.setNguoiGduyetId(currentUser.getUser().getId());
        xhXkVtBhQdGiaonvXnHdr.setNgayGduyet(LocalDate.now());
        break;
      case Contains.CHO_DUYET_LDC + Contains.TU_CHOI_LDC:
        xhXkVtBhQdGiaonvXnHdr.setLyDoTuChoi(req.getLyDoTuChoi());
        break;
      case Contains.CHO_DUYET_LDC + Contains.DA_DUYET_LDC:
        xhXkVtBhQdGiaonvXnHdr.setNguoiPduyetId(currentUser.getUser().getId());
        xhXkVtBhQdGiaonvXnHdr.setNgayPduyet(LocalDate.now());
        break;
      default:
        throw new Exception("Phê duyệt không thành công.");
    }
    xhXkVtBhQdGiaonvXnHdr.setTrangThai(req.getTrangThai());
    XhXkVtBhQdGiaonvXnHdr model = xhXkVtBhQdGiaonvXnRepository.save(xhXkVtBhQdGiaonvXnHdr);
    return detail(model.getId());
  }


  public void export(CustomUserDetails currentUser, XhXkVtBhQdGiaonvXnRequest objReq, HttpServletResponse response) throws Exception {
    PaggingReq paggingReq = new PaggingReq();
    paggingReq.setPage(0);
    paggingReq.setLimit(Integer.MAX_VALUE);
    objReq.setPaggingReq(paggingReq);
    List<XhXkVtBhQdGiaonvXnHdr> data = this.searchPage(currentUser, objReq).getContent();
    String title, fileName = "";
    String[] rowsName;
    Object[] objs;
    List<Object[]> dataList = new ArrayList<>();
    title = "Danh sách quyết định giao nhiệm vụ xuất hàng";
    fileName = "danh-sach-quyet-dinh-giao-nhiem-vu-xuat-hang.xlsx";
    rowsName = new String[]{"STT", "Năm xuất", "Số quyết định", "Ngày quyết định", "Loại hình nhập xuất", "Mã DS tổng hợp", "Số QĐ xuất giảm TC", "Thời gian xuất hàng", "Trích yếu", "Trạng thái"};
    for (int i = 0; i < data.size(); i++) {
      XhXkVtBhQdGiaonvXnHdr dx = data.get(i);
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

  public void updateQdXuat(XhXkVtBhQdGiaonvXnHdr qdGiaonvXh, boolean xoa) {
    if (!DataUtils.isNullObject(qdGiaonvXh.getIdCanCu())) {
      Long[] idsQdGiaoNvXh = Arrays.stream(qdGiaonvXh.getIdCanCu().split(","))
          .map(String::trim)
          .map(Long::valueOf)
          .toArray(Long[]::new);
      if (qdGiaonvXh.getLoai().equals(Contains.XUAT_MAU)) {
        if (qdGiaonvXh.getLoaiCanCu().equals("TONG_HOP")) {
          Optional<XhXkTongHopHdr> tongHop = xhXkTongHopRepository.findById(Long.valueOf(qdGiaonvXh.getIdCanCu()));
          if (tongHop.isPresent()) {
            XhXkTongHopHdr item = tongHop.get();
            if (xoa) {
              item.setSoLanLm(null);
              item.setIdQdGiaoNvXh(null);
              item.setSoQdGiaoNvXh(null);
            } else {
              item.setSoLanLm(qdGiaonvXh.getSoLanLm());
              item.setIdQdGiaoNvXh(qdGiaonvXh.getId());
              item.setSoQdGiaoNvXh(qdGiaonvXh.getSoQuyetDinh());
            }
            xhXkTongHopRepository.save(item);
          } else {
            List<XhXkVtBhPhieuKdclHdr> allByIdCanCuIn = xhXkVtBhPhieuKdclRepository.findByIdIn(Arrays.asList(idsQdGiaoNvXh));
            if (!allByIdCanCuIn.isEmpty()) {
              allByIdCanCuIn.forEach(item -> {
                if (xoa) {
                  item.setIdQdGiaoNvXh(null);
                  item.setSoQdGiaoNvXh(null);
                } else {
                  item.setIdQdGiaoNvXh(qdGiaonvXh.getId());
                  item.setSoQdGiaoNvXh(qdGiaonvXh.getSoQuyetDinh());
                }
              });
              xhXkVtBhPhieuKdclRepository.saveAll(allByIdCanCuIn);
            }
          }
        }
      } else if (qdGiaonvXh.getLoai().equals(Contains.XUAT_HUY)) {
        Optional<XhXkVtQdXuatGiamVattu> xuatGiamVattu = xhXkVtQdXuatGiamVattuRepository.findById(Long.valueOf(qdGiaonvXh.getIdCanCu()));
        if (xuatGiamVattu.isPresent()) {
          XhXkVtQdXuatGiamVattu item = xuatGiamVattu.get();
          if (xoa) {
            item.setIdQdGiaoNvXh(null);
            item.setSoQdGiaoNvXh(null);
          } else {
            item.setIdQdGiaoNvXh(qdGiaonvXh.getId());
            item.setSoQdGiaoNvXh(qdGiaonvXh.getSoQuyetDinh());
          }
          xhXkVtQdXuatGiamVattuRepository.save(item);
        }
      } else {
        List<XhXkVtBhBbBaoHanh> allByIdCanCuIn = xhXkVtBhBbBaoHanhRepository.findByIdIn(Arrays.asList(idsQdGiaoNvXh));
        if (!allByIdCanCuIn.isEmpty()) {
          allByIdCanCuIn.forEach(item -> {
            if (xoa) {
              item.setIdQdGiaoNvXh(null);
              item.setSoQdGiaoNvXh(null);
            } else {
              item.setIdQdGiaoNvXh(qdGiaonvXh.getId());
              item.setSoQdGiaoNvXh(qdGiaonvXh.getSoQuyetDinh());
            }
          });
          xhXkVtBhBbBaoHanhRepository.saveAll(allByIdCanCuIn);
        }
      }

    }
  }

  public void updateQdGiaonvNhap(XhXkVtBhQdGiaonvXnHdr qdGiaonvNh, boolean xoa) {
    if (!DataUtils.isNullObject(qdGiaonvNh.getIdCanCu())) {
      if (qdGiaonvNh.getLoai().equals(Contains.NHAP_MAU)) {
        Optional<XhXkVtBhPhieuKdclHdr> phieuKdcl = xhXkVtBhPhieuKdclRepository.findById(Long.valueOf(qdGiaonvNh.getIdCanCu()));
        if (phieuKdcl.isPresent()) {
          XhXkVtBhPhieuKdclHdr item = phieuKdcl.get();
          if (xoa) {
            item.setSoQdGiaoNvNh(null);
            item.setIdQdGiaoNvNh(null);
          } else {
            item.setSoQdGiaoNvNh(qdGiaonvNh.getSoQuyetDinh());
            item.setIdQdGiaoNvNh(qdGiaonvNh.getId());
          }
          xhXkVtBhPhieuKdclRepository.save(item);
        }
      }
    }
  }

}
