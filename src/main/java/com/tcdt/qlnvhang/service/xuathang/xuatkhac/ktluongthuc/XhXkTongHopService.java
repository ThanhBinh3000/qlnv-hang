package com.tcdt.qlnvhang.service.xuathang.xuatkhac.ktluongthuc;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.ktluongthuc.XhXkDanhSachRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.ktluongthuc.XhXkTongHopRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.xuatkhac.XhXkTongHopRequest;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.kthanghoa.XhXkDanhSachHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.kthanghoa.XhXkTongHopDtl;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.kthanghoa.XhXkTongHopHdr;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class XhXkTongHopService extends BaseServiceImpl {


  @Autowired
  private XhXkTongHopRepository xhXkTongHopRepository;
  @Autowired
  private XhXkDanhSachRepository xhXkDanhSachRepository;

  @Autowired
  private FileDinhKemService fileDinhKemService;

  public Page<XhXkTongHopHdr> searchPage(CustomUserDetails currentUser, XhXkTongHopRequest req) throws Exception {
    String dvql = currentUser.getDvql();
    if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CHI_CUC)) {
      req.setDvql(dvql.substring(0, 6));
    } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
      req.setDvql(dvql);
    }

    if (!DataUtils.isNullObject(req.getNgayTaoTu())) {
      req.setNgayTaoTu(req.getNgayTaoTu().toLocalDate().atTime(LocalTime.MAX));
    }
    if (!DataUtils.isNullObject(req.getNgayTaoDen())) {
      req.setNgayTaoDen(req.getNgayTaoTu().toLocalDate().atTime(LocalTime.MIN));
    }
    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
    Page<XhXkTongHopHdr> search = xhXkTongHopRepository.searchPage(req, pageable);
    //set label
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    search.getContent().forEach(s -> {
      s.getTongHopDtl().forEach(s1 -> {
        s1.setMapDmucDvi(mapDmucDvi);
        s1.setMapVthh(mapVthh);
        s1.setTenTrangThaiBienBan(TrangThaiAllEnum.getLabelById(s1.getTrangThaiBienBan()));
        s1.setTenTrangThaiKnCl(TrangThaiAllEnum.getLabelById(s1.getTrangThaiKnCl()));
      });
      s.setTenTrangThai(TrangThaiAllEnum.getLabelById(s.getTrangThai()));
      s.setTenDvi(mapDmucDvi.containsKey(s.getMaDvi()) ? mapDmucDvi.get(s.getMaDvi()) : null);
      List<FileDinhKem> fileDinhKems= fileDinhKemService.search(s.getId(),Arrays.asList(XhXkTongHopHdr.TABLE_NAME));
      s.setFileDinhKems(fileDinhKems);
      String maDvql = DataUtils.isNullOrEmpty(s.getMaDvi()) ? s.getMaDvi() : s.getMaDvi().substring(0, s.getMaDvi().length() - 2);
      s.setMaDvql(maDvql);
      s.setTenDvql(mapDmucDvi.containsKey(maDvql) ? mapDmucDvi.get(maDvql) : null);
    });
    return search;
  }

  @Transactional
  public XhXkTongHopHdr save(CustomUserDetails currentUser, XhXkTongHopRequest objReq) throws Exception {
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    if (!DataUtils.isNullObject(objReq.getMaDanhSach())) {
      Optional<XhXkTongHopHdr> optional = xhXkTongHopRepository.findByMaDanhSach(objReq.getMaDanhSach());
      if (optional.isPresent()) {
        throw new Exception("Mã danh sách tổng hợp đã tồn tại");
      }
    }
    XhXkTongHopHdr data = new XhXkTongHopHdr();
    BeanUtils.copyProperties(objReq, data);
    data.setMaDvi(currentUser.getUser().getDvql());
    data.setTrangThai(Contains.DUTHAO);
    data.getTongHopDtl().forEach(s -> {
      s.setTongHopHdr(data);
      s.setTrangThaiKnCl(Contains.CHUA_THUC_HIEN);
    });
    XhXkTongHopHdr created = xhXkTongHopRepository.save(data);
    created.setMaDanhSach(created.getMaDanhSach() +'-'+ created.getId());
    created = xhXkTongHopRepository.save(created);
    Long id = created.getId();
    String ma = created.getMaDanhSach();
    LocalDateTime ngay= created.getNgayTao();
    List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(),id,XhXkTongHopHdr.TABLE_NAME);
    created.setFileDinhKems(fileDinhKems);
    //set ma tong hop cho danh sach
    List<Long> listIdDsHdr = created.getTongHopDtl().stream().map(XhXkTongHopDtl::getIdDsHdr).collect(Collectors.toList());
    List<XhXkDanhSachHdr> listDsHdr = xhXkDanhSachRepository.findByIdIn(listIdDsHdr);
    listDsHdr.forEach(s -> {
      s.setIdTongHop(id);
      s.setMaTongHop(ma);
      s.setNgayTongHop(ngay);
    });
    xhXkDanhSachRepository.saveAll(listDsHdr);

    return created;
  }

  @Transactional
  public XhXkTongHopHdr update(CustomUserDetails currentUser, XhXkTongHopRequest objReq) throws Exception {
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    Optional<XhXkTongHopHdr> optional = xhXkTongHopRepository.findById(objReq.getId());
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu cần sửa");
    }
    Optional<XhXkTongHopHdr> soDx = xhXkTongHopRepository.findByMaDanhSach(objReq.getMaDanhSach());
    if (soDx.isPresent()) {
      if (!soDx.get().getId().equals(objReq.getId())) {
        throw new Exception("Mã danh sách tổng hợp đã tồn tại");
      }
    }

    XhXkTongHopHdr data = optional.get();
    BeanUtils.copyProperties(objReq, data, "id", "maDvi");
    data.getTongHopDtl().forEach(s -> {
      s.setTongHopHdr(data);
    });
    XhXkTongHopHdr created = xhXkTongHopRepository.save(data);
    fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(XhXkTongHopHdr.TABLE_NAME));
    List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(),created.getId(),XhXkTongHopHdr.TABLE_NAME);
    created.setFileDinhKems(fileDinhKems);
    return created;
  }


  public List<XhXkTongHopHdr> detail(List<Long> ids) throws Exception {
    if (DataUtils.isNullOrEmpty(ids)) throw new Exception("Tham số không hợp lệ.");
    List<XhXkTongHopHdr> optional = xhXkTongHopRepository.findByIdIn(ids);
    if (DataUtils.isNullOrEmpty(optional)) {
      throw new Exception("Không tìm thấy dữ liệu");
    }

    List<XhXkTongHopHdr> allById = xhXkTongHopRepository.findAllById(ids);
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    allById.forEach(data -> {
      data.getTongHopDtl().forEach(s -> {
        s.setMapDmucDvi(mapDmucDvi);
        s.setMapVthh(mapVthh);
      });
      data.setTenTrangThai(TrangThaiAllEnum.getLabelById(data.getTrangThai()));
      data.setTenDvi(mapDmucDvi.containsKey(data.getMaDvi()) ? mapDmucDvi.get(data.getMaDvi()) : null);
      String maDvql = DataUtils.isNullOrEmpty(data.getMaDvi()) ? data.getMaDvi() : data.getMaDvi().substring(0, data.getMaDvi().length() - 2);
      data.setMaDvql(maDvql);
      data.setTenDvql(mapDmucDvi.containsKey(maDvql) ? mapDmucDvi.get(maDvql) : null);
      List<FileDinhKem> fileDinhKems= fileDinhKemService.search(data.getId(),Arrays.asList(XhXkTongHopHdr.TABLE_NAME));
      data.setFileDinhKems(fileDinhKems);
    });
    return allById;
  }

  @Transactional
  public void delete(IdSearchReq idSearchReq) throws Exception {
    Optional<XhXkTongHopHdr> optional = xhXkTongHopRepository.findById(idSearchReq.getId());
    if (!optional.isPresent()) {
      throw new Exception("Bản ghi không tồn tại");
    }
    XhXkTongHopHdr data = optional.get();
    if (!DataUtils.isNullObject(data.getId())) {
      List<XhXkDanhSachHdr> items = xhXkDanhSachRepository.findAllByIdTongHop(data.getId());
      items.forEach(item -> {
        item.setIdTongHop(null);
        item.setMaTongHop(null);
        item.setNgayTongHop(null);
        xhXkDanhSachRepository.save(item);
      });
    }
    fileDinhKemService.delete(data.getId(),Lists.newArrayList(XhXkTongHopHdr.TABLE_NAME));
    xhXkTongHopRepository.delete(data);
  }

  @Transactional
  public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
    List<XhXkTongHopHdr> list = xhXkTongHopRepository.findByIdIn(idSearchReq.getIdList());

    if (list.isEmpty()) {
      throw new Exception("Bản ghi không tồn tại");
    }
    list.forEach(data -> {
      if (!DataUtils.isNullObject(data.getId())) {
        List<XhXkDanhSachHdr> items = xhXkDanhSachRepository.findAllByIdTongHop(data.getId());
        items.forEach(item -> {
          item.setIdTongHop(null);
          item.setMaTongHop(null);
          item.setNgayTongHop(null);
          xhXkDanhSachRepository.save(item);
        });
      }
    });
    fileDinhKemService.deleteMultiple(idSearchReq.getIdList(),Lists.newArrayList(XhXkTongHopHdr.TABLE_NAME));
    xhXkTongHopRepository.deleteAll(list);

  }


  public XhXkTongHopHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {

    if (StringUtils.isEmpty(statusReq.getId())) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    Optional<XhXkTongHopHdr> optional = xhXkTongHopRepository.findById(Long.valueOf(statusReq.getId()));
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    }

    String status = optional.get().getTrangThai() + statusReq.getTrangThai();
    if (status.equals(TrangThaiAllEnum.DU_THAO.getId() + TrangThaiAllEnum.GUI_DUYET.getId())) {
      optional.get().setNguoiGduyetId(currentUser.getUser().getId());
      optional.get().setNgayGduyet(LocalDate.now());
    } else {
      throw new Exception("Phê duyệt không thành công");
    }
    optional.get().setTrangThai(statusReq.getTrangThai());
    XhXkTongHopHdr created = xhXkTongHopRepository.save(optional.get());
    return created;
  }


  public void export(CustomUserDetails currentUser, XhXkTongHopRequest objReq, HttpServletResponse response) throws Exception {
    PaggingReq paggingReq = new PaggingReq();
    paggingReq.setPage(0);
    paggingReq.setLimit(Integer.MAX_VALUE);
    objReq.setPaggingReq(paggingReq);
    Page<XhXkTongHopHdr> page = this.searchPage(currentUser, objReq);
    List<XhXkTongHopHdr> data = page.getContent();

    String title = "Danh sách hàng DTQG còn 6 tháng hết hạn lưu kho nhưng chưa có kế hoạch xuất";
    String[] rowsName = new String[]{"STT", "Năm KH", "Mã danh sách", "Chi cục DTNN", "Loại hàng hóa", "Chủng loại",
        "Điểm kho", "Ngăn/lô kho", "Ngày nhập kho", "SL tồn", "DVT", "Ngày đề xuất", "Trạng thái", "Trạng thái kiểm tra chất lượng"};
    String fileName = "danh-sach-hang-dtqg-con-6-thang-het-han-luu-kho-nhung-chua-co-ke-hoach-xuat.xlsx";
    List<Object[]> dataList = new ArrayList<Object[]>();
    Object[] objs = null;
    for (int i = 0; i < data.size(); i++) {
      XhXkTongHopHdr qd = data.get(i);
      objs = new Object[rowsName.length];
      for (XhXkTongHopDtl dtl : qd.getTongHopDtl()) {
        objs[0] = i;
        objs[1] = qd.getNam();
        objs[2] = qd.getMaDanhSach();
        objs[3] = dtl.getTenChiCuc();
        objs[4] = dtl.getTenLoaiVthh();
        objs[5] = dtl.getTenCloaiVthh();
        objs[6] = dtl.getTenDiemKho();
        objs[7] = dtl.getTenLoKho();
        objs[8] = dtl.getNgayNhapKho();
        objs[9] = dtl.getSlTonKho();
        objs[10] = dtl.getDonViTinh();
        objs[11] = dtl.getNgayDeXuat();
        objs[12] = qd.getTenTrangThai();
        objs[13] = dtl.getTrangThaiKnCl();
      }
      dataList.add(objs);
    }
    ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
    ex.export();
  }
}

