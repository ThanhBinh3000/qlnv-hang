package com.tcdt.qlnvhang.service.suachuahang;

import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.suachuahang.ScDanhSachRepository;
import com.tcdt.qlnvhang.repository.xuathang.suachuahang.ScTongHopDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.suachuahang.ScTongHopHdrRepository;
import com.tcdt.qlnvhang.request.suachua.ScTongHopReq;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlDanhSachRequest;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScDanhSachHdr;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScTongHopDtl;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScTongHopHdr;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.UserUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.bag.CollectionBag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ScTongHopServiceImpl extends BaseServiceImpl implements ScTongHopService {

  @Autowired
  private ScTongHopHdrRepository hdrRepository;

  @Autowired
  private ScTongHopDtlRepository dtlRepository;
  private ScDanhSachRepository scDanhSachRepository;
  @Autowired
  private FileDinhKemService fileDinhKemService;

  @Override
  public Page<ScTongHopHdr> searchPage(ScTongHopReq req) throws Exception {
    UserInfo currentUser = SecurityContextService.getUser();
    if (currentUser == null){
      throw new Exception("Access denied.");
    }
    String dvql = currentUser.getDvql();
    if (!currentUser.getCapDvi().equals(Contains.CAP_TONG_CUC)) {
      req.setMaDvi(dvql.substring(0, 6));
    }
    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
    Page<ScTongHopHdr> search = hdrRepository.searchPage(req, pageable);

    //set label
    search.getContent().forEach(s -> {
      s.setTenTrangThai(TrangThaiAllEnum.getLabelById(s.getTrangThai()));
    });
    return search;
  }

  @Override
  public ScTongHopHdr create(ScTongHopReq req) throws Exception {
    UserInfo userInfo = UserUtils.getUserInfo();
    XhTlDanhSachRequest reqTh = new XhTlDanhSachRequest();
    reqTh.setDvql(userInfo.getDvql());
    List<ScDanhSachHdr> listTh = scDanhSachRepository.listTongHop(reqTh);

    ScTongHopHdr hdr = new ScTongHopHdr();
    BeanUtils.copyProperties(req,hdr);
    hdr.setNam(LocalDate.now().getYear());
    hdr.setMaDvi(userInfo.getDvql());
    hdr.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());

    ScTongHopHdr save = hdrRepository.save(hdr);

    List<ScTongHopDtl> listDtl = new ArrayList<>();
    listTh.forEach(item -> {
      ScTongHopDtl dtl = new ScTongHopDtl();
      dtl.setIdHdr(hdr.getId());
      dtl.setIdDsHdr(item.getId());
      listDtl.add(dtl);
    });
    List<ScTongHopDtl> scTongHopDtls = dtlRepository.saveAll(listDtl);
    save.setChildren(scTongHopDtls);
    return save;
  }

  @Override
  public ScTongHopHdr update(ScTongHopReq req) throws Exception {
    UserInfo currentUser = SecurityContextService.getUser();

    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    Optional<ScTongHopHdr> optional = hdrRepository.findById(req.getId());
    if (optional.isEmpty()) {
      throw new Exception("Không tìm thấy dữ liệu cần sửa");
    }

    ScTongHopHdr data = optional.get();
    BeanUtils.copyProperties(req, data, "id");
    ScTongHopHdr created = hdrRepository.save(data);
    return created;
  }

  @Override
  public ScTongHopHdr detail(Long id) throws Exception {
    Optional<ScTongHopHdr> optional = hdrRepository.findById(id);
    if(optional.isEmpty()){
      throw new Exception("Bản ghi không tồn tại");
    }
    return optional.get();
  }

  @Override
  public ScTongHopHdr approve(ScTongHopReq req) throws Exception {
    return null;
  }

  @Override
  public void delete(Long id) throws Exception {
    Optional<ScTongHopHdr> optional = hdrRepository.findById(id);
    if (optional.isEmpty()) {
      throw new Exception("Bản ghi không tồn tại");
    }
    hdrRepository.delete(optional.get());
  }

  @Override
  public void deleteMulti(List<Long> listMulti) throws Exception {
    if(CollectionUtils.isNotEmpty(listMulti)){
      listMulti.forEach( i -> {
        try {
          delete(i);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      });
    }else{
      throw new Exception("List id is null");
    }
  }

  @Override
  public void export(ScTongHopReq req, HttpServletResponse response) throws Exception {

  }

//  public Page<ScTongHopHdr> searchPage(CustomUserDetails currentUser, XhTlTongHopRequest req) throws Exception {
//    req.setDvql(currentUser.getDvql());
//    if (!DataUtils.isNullObject(req.getNgayTaoTu())) {
//      req.setNgayTaoTu(req.getNgayTaoTu().toLocalDate().atTime(LocalTime.MAX));
//    }
//    if (!DataUtils.isNullObject(req.getNgayTaoDen())) {
//      req.setNgayTaoDen(req.getNgayTaoTu().toLocalDate().atTime(LocalTime.MIN));
//    }
//    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
//    Page<ScTongHopHdr> search = scTongHopRepository.searchPage(req, pageable);
//
//    //set label
//    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
//    Map<String, String> mapVthh = getListDanhMucHangHoa();
//    search.getContent().forEach(s -> {
//      s.getTongHopDtl().forEach(s1 -> {
//        s1.setMapDmucDvi(mapDmucDvi);
//        s1.setMapVthh(mapVthh);
//      });
//      s.setTenTrangThai(TrangThaiAllEnum.getLabelById(s.getTrangThai()));
//      s.setTenDvi(mapDmucDvi.containsKey(s.getMaDvi()) ? mapDmucDvi.get(s.getMaDvi()) : null);
//      String maDvql = DataUtils.isNullOrEmpty(s.getMaDvi()) ? s.getMaDvi() : s.getMaDvi().substring(0, s.getMaDvi().length() - 2);
//      s.setMaDvql(maDvql);
//      s.setTenDvql(mapDmucDvi.containsKey(maDvql) ? mapDmucDvi.get(maDvql) : null);
//    });
//    return search;
//  }
//
//  @Transactional
//  public ScTongHopHdr save(CustomUserDetails currentUser, XhTlTongHopRequest objReq) throws Exception {
//    if (currentUser == null) {
//      throw new Exception("Bad request.");
//    }
//    if (!DataUtils.isNullObject(objReq.getMaDanhSach())) {
//      Optional<ScTongHopHdr> optional = scTongHopRepository.findByMaDanhSach(objReq.getMaDanhSach());
//      if (optional.isPresent()) {
//        throw new Exception("Mã danh sách tổng hợp đã tồn tại");
//      }
//    }
//    ScTongHopHdr data = new ScTongHopHdr();
//    BeanUtils.copyProperties(objReq, data);
//    data.setMaDvi(currentUser.getUser().getDepartment());
//    data.setTrangThai(Contains.DUTHAO);
//
//    data.getTongHopDtl().forEach(s -> s.setTongHopHdr(data));
//
//    ScTongHopHdr created = scTongHopRepository.save(data);
//    created.setMaDanhSach(created.getId() + created.getMaDanhSach());
//    created = scTongHopRepository.save(created);
//    Long id = created.getId();
//    String ma = created.getMaDanhSach();
//    //set ma tong hop cho danh sach
//    List<Long> listIdDsHdr = created.getTongHopDtl().stream().map(ScTongHopDtl::getIdDsHdr).collect(Collectors.toList());
//    List<ScDanhSachHdr> listDsHdr = scDanhSachRepository.findByIdIn(listIdDsHdr);
//    listDsHdr.forEach(s -> {
//      s.setIdTongHop(id);
//      s.setMaTongHop(ma);
//    });
//    scDanhSachRepository.saveAll(listDsHdr);
//    return detail(Arrays.asList(created.getId())).get(0);
//
//  }
//
//  @Transactional
//  public ScTongHopHdr update(CustomUserDetails currentUser, XhTlTongHopRequest objReq) throws Exception {
//    if (currentUser == null) {
//      throw new Exception("Bad request.");
//    }
//    Optional<ScTongHopHdr> optional = scTongHopRepository.findById(objReq.getId());
//    if (!optional.isPresent()) {
//      throw new Exception("Không tìm thấy dữ liệu cần sửa");
//    }
//    Optional<ScTongHopHdr> soDx = scTongHopRepository.findByMaDanhSach(objReq.getMaDanhSach());
//    if (soDx.isPresent()) {
//      if (!soDx.get().getId().equals(objReq.getId())) {
//        throw new Exception("Mã danh sách tổng hợp đã tồn tại");
//      }
//    }
//
//    ScTongHopHdr data = optional.get();
//    BeanUtils.copyProperties(objReq, data, "id");
//    data.getTongHopDtl().forEach(s -> {
//      s.setTongHopHdr(data);
//    });
//    ScTongHopHdr created = scTongHopRepository.save(data);
//    return created;
//  }
//
//
//  public List<ScTongHopHdr> detail(List<Long> ids) throws Exception {
//    if (DataUtils.isNullOrEmpty(ids)) throw new Exception("Tham số không hợp lệ.");
//    List<ScTongHopHdr> optional = scTongHopRepository.findByIdIn(ids);
//    if (DataUtils.isNullOrEmpty(optional)) {
//      throw new Exception("Không tìm thấy dữ liệu");
//    }
//
//    List<ScTongHopHdr> allById = scTongHopRepository.findAllById(ids);
//    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
//    Map<String, String> mapVthh = getListDanhMucHangHoa();
//    allById.forEach(data -> {
//      data.getTongHopDtl().forEach(s -> {
//        s.setMapDmucDvi(mapDmucDvi);
//        s.setMapVthh(mapVthh);
//      });
//      data.setTenTrangThai(TrangThaiAllEnum.getLabelById(data.getTrangThai()));
//      data.setTenDvi(mapDmucDvi.containsKey(data.getMaDvi()) ? mapDmucDvi.get(data.getMaDvi()) : null);
//      String maDvql = DataUtils.isNullOrEmpty(data.getMaDvi()) ? data.getMaDvi() : data.getMaDvi().substring(0, data.getMaDvi().length() - 2);
//      data.setMaDvql(maDvql);
//      data.setTenDvql(mapDmucDvi.containsKey(maDvql) ? mapDmucDvi.get(maDvql) : null);
//    });
//    return allById;
//  }
//
//  @Transactional
//  public void delete(IdSearchReq idSearchReq) throws Exception {
//    Optional<ScTongHopHdr> optional = scTongHopRepository.findById(idSearchReq.getId());
//    if (!optional.isPresent()) {
//      throw new Exception("Bản ghi không tồn tại");
//    }
//    ScTongHopHdr data = optional.get();
//    scTongHopRepository.delete(data);
//  }
//
//  @Transient
//  public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
//    List<ScTongHopHdr> list = scTongHopRepository.findByIdIn(idSearchReq.getIdList());
//
//    if (list.isEmpty()) {
//      throw new Exception("Bản ghi không tồn tại");
//    }
//    scTongHopRepository.deleteAll(list);
//
//  }
//
//
//  public ScTongHopHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
//
//    if (StringUtils.isEmpty(statusReq.getId())) {
//      throw new Exception("Không tìm thấy dữ liệu");
//    }
//    Optional<ScTongHopHdr> optional = scTongHopRepository.findById(Long.valueOf(statusReq.getId()));
//    if (!optional.isPresent()) {
//      throw new Exception("Không tìm thấy dữ liệu");
//    }
//
//    String status = optional.get().getTrangThai() + statusReq.getTrangThai();
//    if (status.equals(TrangThaiAllEnum.DU_THAO.getId() + TrangThaiAllEnum.DA_TONG_HOP.getId())) {
//      optional.get().setNguoiGduyetId(currentUser.getUser().getId());
//      optional.get().setNgayGduyet(LocalDate.now());
//    } else {
//      throw new Exception("Phê duyệt không thành công");
//    }
//    optional.get().setTrangThai(statusReq.getTrangThai());
//    ScTongHopHdr created = scTongHopRepository.save(optional.get());
//    return created;
//  }
//
//  /*public void export(CustomUserDetails currentUser, SearchXhTlQuyetDinhTl objReq, HttpServletResponse response) throws Exception {
//    PaggingReq paggingReq = new PaggingReq();
//    paggingReq.setPage(0);
//    paggingReq.setLimit(Integer.MAX_VALUE);
//    objReq.setPaggingReq(paggingReq);
//    Page<XhScTongHopHdr> page = this.searchPage(currentUser, objReq);
//    List<XhScTongHopHdr> data = page.getContent();
//
//    String title = "Danh sách quyết định thanh lý hàng DTQG ";
//    String[] rowsName = new String[]{"STT", "Số quyết định", "Trích yếu", "Ngày ký",
//        "Hồ sơ đề nghị thanh lý", "Trạng thái"};
//    String fileName = "danh-sach-phuong-quyet-dinh-thanh-ly-hang-dtqg.xlsx";
//    List<Object[]> dataList = new ArrayList<Object[]>();
//    Object[] objs = null;
//    for (int i = 0; i < data.size(); i++) {
//      XhScTongHopHdr qd = data.get(i);
//      objs = new Object[rowsName.length];
//      objs[0] = i;
//      objs[1] = qd.getSoQd();
//      objs[2] = qd.getTrichYeu();
//      objs[3] = qd.getNgayKy();
//      objs[4] = qd.getSoHoSo();
//      objs[5] = qd.getTenTrangThai();
//      dataList.add(objs);
//    }
//    ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
//    ex.export();
//  }*/
}
