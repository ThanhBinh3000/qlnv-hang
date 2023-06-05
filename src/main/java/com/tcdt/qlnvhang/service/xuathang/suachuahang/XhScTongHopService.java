package com.tcdt.qlnvhang.service.xuathang.suachuahang;

import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.suachuahang.XhScDanhSachRepository;
import com.tcdt.qlnvhang.repository.xuathang.suachuahang.XhScTongHopRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlTongHopRequest;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.XhScDanhSachHdr;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.XhScTongHopDtl;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.XhScTongHopHdr;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.Transient;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class XhScTongHopService extends BaseServiceImpl {


  @Autowired
  private XhScTongHopRepository xhScTongHopRepository;
  @Autowired
  private XhScDanhSachRepository xhScDanhSachRepository;

  @Autowired
  private FileDinhKemService fileDinhKemService;

  public Page<XhScTongHopHdr> searchPage(CustomUserDetails currentUser, XhTlTongHopRequest req) throws Exception {
    req.setDvql(currentUser.getDvql());
    if (!DataUtils.isNullObject(req.getNgayTaoTu())) {
      req.setNgayTaoTu(req.getNgayTaoTu().toLocalDate().atTime(LocalTime.MAX));
    }
    if (!DataUtils.isNullObject(req.getNgayTaoDen())) {
      req.setNgayTaoDen(req.getNgayTaoTu().toLocalDate().atTime(LocalTime.MIN));
    }
    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
    Page<XhScTongHopHdr> search = xhScTongHopRepository.searchPage(req, pageable);

    //set label
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    search.getContent().forEach(s -> {
      s.getTongHopDtl().forEach(s1 -> {
        s1.setMapDmucDvi(mapDmucDvi);
        s1.setMapVthh(mapVthh);
      });
      s.setTenTrangThai(TrangThaiAllEnum.getLabelById(s.getTrangThai()));
      s.setTenDvi(mapDmucDvi.containsKey(s.getMaDvi()) ? mapDmucDvi.get(s.getMaDvi()) : null);
      String maDvql = DataUtils.isNullOrEmpty(s.getMaDvi()) ? s.getMaDvi() : s.getMaDvi().substring(0, s.getMaDvi().length() - 2);
      s.setMaDvql(maDvql);
      s.setTenDvql(mapDmucDvi.containsKey(maDvql) ? mapDmucDvi.get(maDvql) : null);
    });
    return search;
  }

  @Transactional
  public XhScTongHopHdr save(CustomUserDetails currentUser, XhTlTongHopRequest objReq) throws Exception {
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    if (!DataUtils.isNullObject(objReq.getMaDanhSach())) {
      Optional<XhScTongHopHdr> optional = xhScTongHopRepository.findByMaDanhSach(objReq.getMaDanhSach());
      if (optional.isPresent()) {
        throw new Exception("Mã danh sách tổng hợp đã tồn tại");
      }
    }
    XhScTongHopHdr data = new XhScTongHopHdr();
    BeanUtils.copyProperties(objReq, data);
    data.setMaDvi(currentUser.getUser().getDepartment());
    data.setTrangThai(Contains.DUTHAO);

    data.getTongHopDtl().forEach(s -> s.setTongHopHdr(data));

    XhScTongHopHdr created = xhScTongHopRepository.save(data);
    created.setMaDanhSach(created.getId() + created.getMaDanhSach());
    created = xhScTongHopRepository.save(created);
    Long id = created.getId();
    String ma = created.getMaDanhSach();
    //set ma tong hop cho danh sach
    List<Long> listIdDsHdr = created.getTongHopDtl().stream().map(XhScTongHopDtl::getIdDsHdr).collect(Collectors.toList());
    List<XhScDanhSachHdr> listDsHdr = xhScDanhSachRepository.findByIdIn(listIdDsHdr);
    listDsHdr.forEach(s -> {
      s.setIdTongHop(id);
      s.setMaTongHop(ma);
    });
    xhScDanhSachRepository.saveAll(listDsHdr);
    return detail(Arrays.asList(created.getId())).get(0);

  }

  @Transactional
  public XhScTongHopHdr update(CustomUserDetails currentUser, XhTlTongHopRequest objReq) throws Exception {
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    Optional<XhScTongHopHdr> optional = xhScTongHopRepository.findById(objReq.getId());
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu cần sửa");
    }
    Optional<XhScTongHopHdr> soDx = xhScTongHopRepository.findByMaDanhSach(objReq.getMaDanhSach());
    if (soDx.isPresent()) {
      if (!soDx.get().getId().equals(objReq.getId())) {
        throw new Exception("Mã danh sách tổng hợp đã tồn tại");
      }
    }

    XhScTongHopHdr data = optional.get();
    BeanUtils.copyProperties(objReq, data, "id");
    data.getTongHopDtl().forEach(s -> {
      s.setTongHopHdr(data);
    });
    XhScTongHopHdr created = xhScTongHopRepository.save(data);
    return created;
  }


  public List<XhScTongHopHdr> detail(List<Long> ids) throws Exception {
    if (DataUtils.isNullOrEmpty(ids)) throw new Exception("Tham số không hợp lệ.");
    List<XhScTongHopHdr> optional = xhScTongHopRepository.findByIdIn(ids);
    if (DataUtils.isNullOrEmpty(optional)) {
      throw new Exception("Không tìm thấy dữ liệu");
    }

    List<XhScTongHopHdr> allById = xhScTongHopRepository.findAllById(ids);
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
    });
    return allById;
  }

  @Transactional
  public void delete(IdSearchReq idSearchReq) throws Exception {
    Optional<XhScTongHopHdr> optional = xhScTongHopRepository.findById(idSearchReq.getId());
    if (!optional.isPresent()) {
      throw new Exception("Bản ghi không tồn tại");
    }
    XhScTongHopHdr data = optional.get();
    xhScTongHopRepository.delete(data);
  }

  @Transient
  public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
    List<XhScTongHopHdr> list = xhScTongHopRepository.findByIdIn(idSearchReq.getIdList());

    if (list.isEmpty()) {
      throw new Exception("Bản ghi không tồn tại");
    }
    xhScTongHopRepository.deleteAll(list);

  }


  public XhScTongHopHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {

    if (StringUtils.isEmpty(statusReq.getId())) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    Optional<XhScTongHopHdr> optional = xhScTongHopRepository.findById(Long.valueOf(statusReq.getId()));
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    }

    String status = optional.get().getTrangThai() + statusReq.getTrangThai();
    if (status.equals(TrangThaiAllEnum.DU_THAO.getId() + TrangThaiAllEnum.DA_TONG_HOP.getId())) {
      optional.get().setNguoiGduyetId(currentUser.getUser().getId());
      optional.get().setNgayGduyet(LocalDate.now());
    } else {
      throw new Exception("Phê duyệt không thành công");
    }
    optional.get().setTrangThai(statusReq.getTrangThai());
    XhScTongHopHdr created = xhScTongHopRepository.save(optional.get());
    return created;
  }


  /*public void export(CustomUserDetails currentUser, SearchXhTlQuyetDinhTl objReq, HttpServletResponse response) throws Exception {
    PaggingReq paggingReq = new PaggingReq();
    paggingReq.setPage(0);
    paggingReq.setLimit(Integer.MAX_VALUE);
    objReq.setPaggingReq(paggingReq);
    Page<XhScTongHopHdr> page = this.searchPage(currentUser, objReq);
    List<XhScTongHopHdr> data = page.getContent();

    String title = "Danh sách quyết định thanh lý hàng DTQG ";
    String[] rowsName = new String[]{"STT", "Số quyết định", "Trích yếu", "Ngày ký",
        "Hồ sơ đề nghị thanh lý", "Trạng thái"};
    String fileName = "danh-sach-phuong-quyet-dinh-thanh-ly-hang-dtqg.xlsx";
    List<Object[]> dataList = new ArrayList<Object[]>();
    Object[] objs = null;
    for (int i = 0; i < data.size(); i++) {
      XhScTongHopHdr qd = data.get(i);
      objs = new Object[rowsName.length];
      objs[0] = i;
      objs[1] = qd.getSoQd();
      objs[2] = qd.getTrichYeu();
      objs[3] = qd.getNgayKy();
      objs[4] = qd.getSoHoSo();
      objs[5] = qd.getTenTrangThai();
      dataList.add(objs);
    }
    ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
    ex.export();
  }*/
}
