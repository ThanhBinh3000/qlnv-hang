package com.tcdt.qlnvhang.service.xuathang.thanhlytieuhuy.tieuhuy;

import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.tieuhuy.XhThDanhSachRepository;
import com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.tieuhuy.XhThTongHopDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.tieuhuy.XhThTongHopHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlDanhSachRequest;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.tieuhuy.XhThDanhSachReq;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.tieuhuy.XhThTongHopRequest;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.tieuhuy.XhThDanhSachHdr;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.tieuhuy.XhThTongHopDtl;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.tieuhuy.XhThTongHopHdr;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.UserUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class XhThTongHopService extends BaseServiceImpl {


  @Autowired
  private XhThTongHopHdrRepository hdrRepository;

  @Autowired
  private XhThTongHopDtlRepository dtlRepository;
  @Autowired
  private XhThDanhSachRepository xhThDanhSachRepository;
  @Autowired
  private XhThDanhSachService xhThDanhSachService;

  @Autowired
  private FileDinhKemService fileDinhKemService;

  public Page<XhThTongHopHdr> searchPage(CustomUserDetails currentUser, XhThTongHopRequest req) throws Exception {
    req.setMaDviSr(currentUser.getDvql());
    if (!DataUtils.isNullObject(req.getNgayTaoTu())) {
      req.setNgayTaoTu(req.getNgayTaoTu().toLocalDate().atTime(LocalTime.MAX));
    }
    if (!DataUtils.isNullObject(req.getNgayTaoDen())) {
      req.setNgayTaoDen(req.getNgayTaoTu().toLocalDate().atTime(LocalTime.MIN));
    }
    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
    Page<XhThTongHopHdr> search = hdrRepository.searchPage(req, pageable);

    List<Long> idsList = search.getContent().stream().map(XhThTongHopHdr::getId).collect(Collectors.toList());
    HashMap<Long, List<XhThTongHopDtl>> dataChilren = getDataChilren(idsList);
    //set label
    search.getContent().forEach(s -> {
      s.setChildren(dataChilren.get(s.getId()));
    });

    return search;
  }

  private HashMap<Long,List<XhThTongHopDtl>> getDataChilren(List<Long> idHdr){
    HashMap<Long,List<XhThTongHopDtl>> hashMap = new HashMap<>();
    idHdr.forEach(item -> {
      List<XhThTongHopDtl> dtl = dtlRepository.findAllByIdTongHop(item);
      dtl.forEach( dataChilren -> {
        try {
          dataChilren.setXhThDanhSachHdr(xhThDanhSachService.detail(dataChilren.getIdDsHdr()));
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      });
      hashMap.put(item,dtl);
    });
    return hashMap;
  }


  public List<XhThTongHopHdr> searchListTaoHsTtd(CustomUserDetails currentUser, XhThTongHopRequest req) throws Exception {
    req.setMaDviSr(currentUser.getDvql());
    List<XhThTongHopHdr> search = hdrRepository.listTongHongTaoHoSo(req);
    //set label
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    search.forEach(s -> {
//      s.getTongHopDtl().forEach(s1 -> {
//        s1.setMapDmucDvi(mapDmucDvi);
//        s1.setMapVthh(mapVthh);
//      });
      s.setTenTrangThai(TrangThaiAllEnum.getLabelById(s.getTrangThai()));
      s.setTenDvi(mapDmucDvi.containsKey(s.getMaDvi()) ? mapDmucDvi.get(s.getMaDvi()) : null);
      String maDvql = DataUtils.isNullOrEmpty(s.getMaDvi()) ? s.getMaDvi() : s.getMaDvi().substring(0, s.getMaDvi().length() - 2);
//      s.setMaDvql(maDvql);
//      s.setTenDvql(mapDmucDvi.containsKey(maDvql) ? mapDmucDvi.get(maDvql) : null);
    });
    return search;
  }

  @Transactional
  public XhThTongHopHdr save(XhThTongHopRequest req) throws Exception {
    UserInfo userInfo = UserUtils.getUserInfo();
    if (!userInfo.getCapDvi().equals(Contains.CAP_CUC)) {
      throw new Exception("Tổng hợp danh sách hàng chỉ được thực hiện ở cấp cục");
    }
    XhThDanhSachReq reqTh = new XhThDanhSachReq();
    reqTh.setMaDviSr(userInfo.getDvql());
    reqTh.setTrangThai(TrangThaiAllEnum.CHUA_CHOT.getId());
    reqTh.setThoiGianThTu(req.getThoiGianThTu());
    reqTh.setThoiGianThDen(req.getThoiGianThDen());
    List<XhThDanhSachHdr> listTh = xhThDanhSachRepository.listTongHop(reqTh);
    if(listTh == null || listTh.isEmpty()){
      throw new Exception("Không có dữ liệu để tổng hợp");
    }
    XhThTongHopHdr hdr = new XhThTongHopHdr();
    BeanUtils.copyProperties(req,hdr);
    hdr.setNam(LocalDate.now().getYear());
    hdr.setMaDvi(userInfo.getDvql());
    hdr.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
    hdr.setTrangThaiTh(NhapXuatHangTrangThaiEnum.CHUA_THUC_HIEN.getId());
    hdr.setId(Long.parseLong(req.getMaDanhSach().split("-")[1]));
    XhThTongHopHdr save = hdrRepository.save(hdr);
    saveFileDinhKem(req.getFileDinhKemReq(),save.getId(),XhThTongHopHdr.TABLE_NAME);
    List<XhThTongHopDtl> listDtl = new ArrayList<>();
    listTh.forEach(item -> {
      item.setTrangThai(TrangThaiAllEnum.DA_CHOT.getId());
      item.setIdTongHop(hdr.getId());
      item.setMaTongHop(hdr.getMaDanhSach());
      item.setNgayTongHop(LocalDate.now());
      XhThTongHopDtl dtl = new XhThTongHopDtl();
      dtl.setIdTongHop(hdr.getId());
      dtl.setIdDsHdr(item.getId());
      listDtl.add(dtl);
    });
    xhThDanhSachRepository.saveAll(listTh);
    List<XhThTongHopDtl> scTongHopDtls = dtlRepository.saveAll(listDtl);
    save.setChildren(scTongHopDtls);
    return save;

  }


  public XhThTongHopHdr detail(Long id) throws Exception {
    Optional<XhThTongHopHdr> optional = hdrRepository.findById(id);
    if(!optional.isPresent()){
      throw new Exception("Bản ghi không tồn tại");
    }
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    HashMap<Long, List<XhThTongHopDtl>> dataChilren = getDataChilren(Collections.singletonList(optional.get().getId()));
    optional.get().setChildren(dataChilren.get(optional.get().getId()));
    optional.get().setTenDvi(mapDmucDvi.get(optional.get().getMaDvi()));
    optional.get().setFileDinhKem(fileDinhKemService.search(optional.get().getId(), XhThTongHopHdr.TABLE_NAME));
    return optional.get();
  }

  @Transactional
  public void delete(IdSearchReq idSearchReq) throws Exception {
    Optional<XhThTongHopHdr> optional = hdrRepository.findById(idSearchReq.getId());
    if (!optional.isPresent()) {
      throw new Exception("Bản ghi không tồn tại");
    }
    XhThTongHopHdr data = optional.get();
    List<XhThDanhSachHdr> listDanhSach = xhThDanhSachRepository.findAllByIdTongHop(data.getId());
    listDanhSach.forEach(s -> {
      s.setIdTongHop(null);
      s.setMaTongHop(null);
      s.setNgayTongHop(null);
      s.setTrangThai(TrangThaiAllEnum.CHUA_CHOT.getId());
    });
    xhThDanhSachRepository.saveAll(listDanhSach);
    hdrRepository.delete(data);
    dtlRepository.deleteAllByIdTongHop(data.getId());
  }


  public XhThTongHopHdr approve(StatusReq statusReq) throws Exception {

    if (StringUtils.isEmpty(statusReq.getId())) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    Optional<XhThTongHopHdr> optional = hdrRepository.findById(Long.valueOf(statusReq.getId()));
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    }

    String status = optional.get().getTrangThai() + statusReq.getTrangThai();
    if (status.equals(TrangThaiAllEnum.DU_THAO.getId() + TrangThaiAllEnum.GUI_DUYET.getId())) {
//      optional.get().setNguoiGduyetId(currentUser.getUser().getId());
//      optional.get().setNgayGduyet(LocalDate.now());
    } else {
      throw new Exception("Phê duyệt không thành công");
    }
    optional.get().setTrangThai(statusReq.getTrangThai());
    XhThTongHopHdr created = hdrRepository.save(optional.get());
    return created;
  }



}
