package com.tcdt.qlnvhang.service.chotdulieu;

import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.chotdulieu.QthtChotGiaNhapXuatRepository;
import com.tcdt.qlnvhang.request.chotdulieu.QthtChotGiaNhapXuatReq;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.chotdulieu.QthtChotGiaNhapXuat;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.UserUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class QthtChotGiaNhapXuatServiceImpl extends BaseServiceImpl implements QthtChotGiaNhapXuatService {

  @Autowired
  private QthtChotGiaNhapXuatRepository hdrRepository;

  @Autowired
  private UserInfoRepository userInfoRepository;

  @Override
  public Page<QthtChotGiaNhapXuat> searchPage(QthtChotGiaNhapXuatReq req) throws Exception {
    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
    Page<QthtChotGiaNhapXuat> search = hdrRepository.searchPage(req, pageable);
    search.getContent().forEach( item -> {
      item.setTenNguoiTao(userInfoRepository.findById(item.getNguoiTaoId()).get().getFullName());
    });
    return search;
  }

  @Override
  public QthtChotGiaNhapXuat create(QthtChotGiaNhapXuatReq req) throws Exception {
    UserInfo userInfo = UserUtils.getUserInfo();
    QthtChotGiaNhapXuat hdr = new QthtChotGiaNhapXuat();
    BeanUtils.copyProperties(req,hdr);
    hdr.setNguoiTaoId(userInfo.getId());
    hdr.setNgayTao(LocalDateTime.now());
    hdr.setMaDonVi(String.  join(",", req.getListMaDvi()));
    QthtChotGiaNhapXuat save = hdrRepository.save(hdr);
    return save;
  }

  @Override
  public QthtChotGiaNhapXuat update(QthtChotGiaNhapXuatReq req) throws Exception {
    UserInfo userInfo = UserUtils.getUserInfo();

    Optional<QthtChotGiaNhapXuat> optional = hdrRepository.findById(req.getId());
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    if(optional.get().getNgayHuy() != null){
      throw new Exception("Dữ liệu đã bị hủy bỏ ");
    }
    QthtChotGiaNhapXuat data = optional.get();
    BeanUtils.copyProperties(req, data);
    return data;
  }

  @Override
  public QthtChotGiaNhapXuat detail(Long id) throws Exception {
    Optional<QthtChotGiaNhapXuat> optional = hdrRepository.findById(id);
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    return optional.get();
  }

  @Override
  public QthtChotGiaNhapXuat approve(QthtChotGiaNhapXuatReq req) throws Exception {
    return null;
  }

  @Override
  public void delete(Long id) throws Exception {
    Optional<QthtChotGiaNhapXuat> optional = hdrRepository.findById(id);
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    QthtChotGiaNhapXuat data = optional.get();
    if(data.getNgayHuy() != null){
      throw new Exception("Dữ liệu đã bị hủy bỏ ");
    }
    data.setNgayHuy(LocalDate.now());
    hdrRepository.save(data);
  }

  @Override
  public void deleteMulti(List<Long> listMulti) throws Exception {

  }

  @Override
  public void export(QthtChotGiaNhapXuatReq req, HttpServletResponse response) throws Exception {

  }
//
//  @Override
//  public Page<ScBaoCaoHdr> searchPage(ScBaoCaoReq req) throws Exception {
//    UserInfo userInfo = UserUtils.getUserInfo();
//    if (userInfo.getCapDvi().equals(Contains.CAP_CUC)) {
//      req.setMaDviSr(userInfo.getDvql());
//    }
//    if(userInfo.getCapDvi().equals(Contains.CAP_CHI_CUC)){
//      req.setMaDviSr(userInfo.getDvql().substring(0, 6));
//    }
//    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
//    Page<ScBaoCaoHdr> search = hdrRepository.searchPage(req, pageable);
//    return search;
//  }
//
//  @Override
//  public ScBaoCaoHdr create(ScBaoCaoReq req) throws Exception {
//    UserInfo userInfo = UserUtils.getUserInfo();
//    if (!userInfo.getCapDvi().equals(Contains.CAP_CUC)) {
//      throw new Exception("Chức năng chỉ dành cho cấp cục");
//    }
//    ScBaoCaoHdr hdr = new ScBaoCaoHdr();
//    BeanUtils.copyProperties(req,hdr);
//    hdr.setNam(LocalDate.now().getYear());
//    hdr.setMaDvi(userInfo.getDvql());
//    hdr.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
//    ScBaoCaoHdr save = hdrRepository.save(hdr);
//    saveFileDinhKem(req.getFileDinhKemReq(), save.getId(), ScBaoCaoHdr.TABLE_NAME);
//    List<ScBaoCaoDtl> dtlList = saveDtl(req, save.getId());
//    save.setChildren(dtlList);
//    return save;
//  }
//
//  public List<ScBaoCaoDtl> saveDtl(ScBaoCaoReq req, Long idHdr){
//    dtlRepository.deleteAllByIdHdr(idHdr);
//    req.getChildren().forEach(item -> {
//      item.setId(null);
//      item.setIdHdr(idHdr);
//      dtlRepository.save(item);
//    });
//    return req.getChildren();
//  }
//
//  @Override
//  public ScBaoCaoHdr update(ScBaoCaoReq req) throws Exception {
//    UserInfo userInfo = UserUtils.getUserInfo();
//    if (!userInfo.getCapDvi().equals(Contains.CAP_CUC)) {
//      throw new Exception("Chức năng chỉ dành cho cấp cục");
//    }
//    Optional<ScBaoCaoHdr> optional = hdrRepository.findById(req.getId());
//    if (!optional.isPresent()) {
//      throw new Exception("Không tìm thấy dữ liệu");
//    }
//    ScBaoCaoHdr data = optional.get();
//    BeanUtils.copyProperties(req, data);
//    fileDinhKemService.delete(data.getId(), Collections.singleton(ScBaoCaoHdr.TABLE_NAME));
//    saveFileDinhKem(req.getFileDinhKemReq(), data.getId(), ScBaoCaoHdr.TABLE_NAME);
//    List<ScBaoCaoDtl> dtlList = saveDtl(req, data.getId());
//    data.setChildren(dtlList);
//    return data;
//  }
//
//  @Override
//  public ScBaoCaoHdr detail(Long id) throws Exception {
//    Optional<ScBaoCaoHdr> optional = hdrRepository.findById(id);
//    if (!optional.isPresent()) {
//      throw new Exception("Không tìm thấy dữ liệu");
//    }
//    ScBaoCaoHdr data = optional.get();
//    data.setFileDinhKems(fileDinhKemService.search(id, Collections.singleton(ScBaoCaoHdr.TABLE_NAME)));
//    List<ScBaoCaoDtl> allByIdHdr = dtlRepository.findAllByIdHdr(id);
//    allByIdHdr.forEach(item -> {
//      try {
//        item.setScDanhSachHdr(scDanhSachServiceImpl.detail(item.getIdDanhSachHdr()));
//      } catch (Exception e) {
//        throw new RuntimeException(e);
//      }
//    });
//    data.setChildren(allByIdHdr);
//    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
//    Map<String, String> mapVthh = getListDanhMucHangHoa();
//    //set label
//    data.setTenDvi(mapDmucDvi.get(data.getMaDvi()));
//    data.setTenDviNhan(mapDmucDvi.get(data.getMaDviNhan()));
//    return data;
//  }
//
//  @Override
//  public ScBaoCaoHdr approve(ScBaoCaoReq req) throws Exception {
//    UserInfo userInfo = UserUtils.getUserInfo();
//    Optional<ScBaoCaoHdr> optional = hdrRepository.findById(req.getId());
//    if (!optional.isPresent()) {
//      throw new Exception("Bản ghi không tồn tại");
//    }
//    ScBaoCaoHdr hdr = optional.get();
//
//    String status = hdr.getTrangThai() + req.getTrangThai();
//    switch (status) {
//      // Re approve : gửi lại duyệt
//      case Contains.TUCHOI_TP + Contains.CHODUYET_TP:
//      case Contains.TUCHOI_LDC + Contains.CHODUYET_TP:
//        break;
//      // Arena các cấp duuyệt
//      case Contains.DUTHAO + Contains.CHODUYET_TP:
//      case Contains.CHODUYET_TP + Contains.CHODUYET_LDC:
//      case Contains.CHODUYET_LDC + Contains.DADUYET_LDC:
//        if(!userInfo.getCapDvi().equals(Contains.CAP_CUC)){
//          throw new Exception("Đơn vị gửi duyệt phải là cấp cục");
//        }
//        break;
//      // Arena từ chối
//      case Contains.CHODUYET_TP + Contains.TUCHOI_TP:
//      case Contains.CHODUYET_LDC + Contains.TUCHOI_LDC:
//        hdr.setLyDoTuChoi(req.getLyDoTuChoi());
//        break;
//      default:
//        throw new Exception("Phê duyệt không thành công");
//    }
//    hdr.setTrangThai(req.getTrangThai());
//    ScBaoCaoHdr save = hdrRepository.save(hdr);
//    return save;
//  }
//
//  @Override
//  public void delete(Long id) throws Exception {
//    UserInfo userInfo = UserUtils.getUserInfo();
//    if (!userInfo.getCapDvi().equals(Contains.CAP_CUC)) {
//      throw new Exception("Chức năng chỉ dành cho cấp chi cục");
//    }
//    Optional<ScBaoCaoHdr> optional = hdrRepository.findById(id);
//    if (!optional.isPresent()) {
//      throw new Exception("Không tìm thấy dữ liệu");
//    }
//    hdrRepository.delete(optional.get());
//    fileDinhKemService.delete(optional.get().getId(), Collections.singleton(ScPhieuXuatKhoHdr.TABLE_NAME));
//    dtlRepository.deleteAllByIdHdr(optional.get().getId());
//  }
//
//  @Override
//  public void deleteMulti(List<Long> listMulti) throws Exception {
//
//  }
//
//  @Override
//  public void export(ScBaoCaoReq req, HttpServletResponse response) throws Exception {
//
//  }
//
////  @Override
////  public Page<ScTongHopHdr> searchPage(ScTongHopReq req) throws Exception {
////    UserInfo currentUser = SecurityContextService.getUser();
////    if (currentUser == null){
////      throw new Exception("Access denied.");
////    }
////    String dvql = currentUser.getDvql();
////    if (currentUser.getCapDvi().equals(Contains.CAP_CUC)) {
////      req.setMaDviSr(dvql);
////    }
////    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
////    Page<ScTongHopHdr> search = hdrRepository.searchPage(req, pageable);
////
////    List<Long> idsList = search.getContent().stream().map(ScTongHopHdr::getId).collect(Collectors.toList());
////
////    HashMap<Long, List<ScTongHopDtl>> dataChilren = getDataChilren(idsList);
////    //set label
////    search.getContent().forEach(s -> {
////      s.setChildren(dataChilren.get(s.getId()));
////      s.setTenTrangThai(TrangThaiAllEnum.getLabelById(s.getTrangThai()));
////    });
////    return search;
////  }
////
////  private HashMap<Long,List<ScTongHopDtl>> getDataChilren(List<Long> idHdr){
////    HashMap<Long,List<ScTongHopDtl>> hashMap = new HashMap<>();
////    idHdr.forEach(item -> {
////        List<ScTongHopDtl> dtl = dtlRepository.findAllByIdHdr(item);
////        dtl.forEach( dataChilren -> {
////          try {
////            dataChilren.setScDanhSachHdr(scDanhSachServiceImpl.detail(dataChilren.getIdDsHdr()));
////          } catch (Exception e) {
////            throw new RuntimeException(e);
////          }
////        });
////        hashMap.put(item,dtl);
////    });
////    return hashMap;
////  }
////
////  @Override
////  public ScTongHopHdr create(ScTongHopReq req) throws Exception {
//
////  }
////
////  @Override
////  public ScTongHopHdr update(ScTongHopReq req) throws Exception {
////    return null;
////  }
////
////  @Override
////  public ScTongHopHdr detail(Long id) throws Exception {
////    Optional<ScTongHopHdr> optional = hdrRepository.findById(id);
////    if(!optional.isPresent()){
////      throw new Exception("Bản ghi không tồn tại");
////    }
////    HashMap<Long, List<ScTongHopDtl>> dataChilren = getDataChilren(Collections.singletonList(optional.get().getId()));
////    optional.get().setChildren(dataChilren.get(optional.get().getId()));
////
////    return optional.get();
////  }
////
////  @Override
////  public ScTongHopHdr approve(ScTongHopReq req) throws Exception {
////    Optional<ScTongHopHdr> optional = hdrRepository.findById(req.getId());
////    if(!optional.isPresent()){
////      throw new Exception("Thông tin tổng hợp không tồn tại");
////    }
////    String status = req.getTrangThai() + optional.get().getTrangThai();
////    if ((TrangThaiAllEnum.GUI_DUYET.getId() + TrangThaiAllEnum.DU_THAO.getId()).equals(status)) {
////      optional.get().setTrangThai(req.getTrangThai());
////    } else {
////      throw new Exception("Gửi duyệt không thành công");
////    }
////    hdrRepository.save(optional.get());
////
////
////    return optional.get();
////  }
////
////  @Override
////  public void delete(Long id) throws Exception {
////    Optional<ScTongHopHdr> optional = hdrRepository.findById(id);
////    if (!optional.isPresent()) {
////      throw new Exception("Bản ghi không tồn tại");
////    }
////    hdrRepository.delete(optional.get());
////    dtlRepository.deleteAllByIdHdr(optional.get().getId());
////  }
////
////  @Override
////  public void deleteMulti(List<Long> listMulti) throws Exception {
////
////  }
////
////  @Override
////  public void export(ScTongHopReq req, HttpServletResponse response) throws Exception {
////
////  }
////
////  @Override
////  public List<ScTongHopHdr> dsTongHopTrinhVaThamDinh(ScTongHopReq req) throws Exception {
////    UserInfo currentUser = SecurityContextService.getUser();
////    if (currentUser == null){
////      throw new Exception("Access denied.");
////    }
////    String dvql = currentUser.getDvql();
////    if (currentUser.getCapDvi().equals(Contains.CAP_CUC)) {
////      req.setMaDviSr(dvql);
////    }
////    req.setTrangThai(TrangThaiAllEnum.GUI_DUYET.getId());
////    List<ScTongHopHdr> list = hdrRepository.listTongHopTrinhThamDinh(req);
////    return list;
////  }
}
