package com.tcdt.qlnvhang.service.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly.XhTlDanhSachRepository;
import com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly.XhTlTongHopDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly.XhTlTongHopHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.suachua.ScTongHopReq;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlDanhSachRequest;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlTongHopRequest;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScTongHopDtl;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScTongHopHdr;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlDanhSachHdr;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlTongHopDtl;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlTongHopHdr;
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
public class XhTlTongHopService extends BaseServiceImpl {

  @Autowired
  private XhTlTongHopHdrRepository hdrRepository;

  @Autowired
  private XhTlTongHopDtlRepository dtlRepository;

  @Autowired
  private XhTlDanhSachRepository xhTlDanhSachRepository;

  @Autowired
  private XhTlDanhSachService xhTlDanhSachService;

  @Autowired
  private FileDinhKemService fileDinhKemService;

  public Page<XhTlTongHopHdr> searchPage(CustomUserDetails currentUser, XhTlTongHopRequest req) throws Exception {
    req.setDvql(currentUser.getDvql());
    if (!DataUtils.isNullObject(req.getNgayTaoTu())) {
      req.setNgayTaoTu(req.getNgayTaoTu().toLocalDate().atTime(LocalTime.MAX));
    }
    if (!DataUtils.isNullObject(req.getNgayTaoDen())) {
      req.setNgayTaoDen(req.getNgayTaoTu().toLocalDate().atTime(LocalTime.MIN));
    }
    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
    Page<XhTlTongHopHdr> search = hdrRepository.searchPage(req, pageable);

    List<Long> idsList = search.getContent().stream().map(XhTlTongHopHdr::getId).collect(Collectors.toList());
    HashMap<Long, List<XhTlTongHopDtl>> dataChilren = getDataChilren(idsList);
    //set label
    search.getContent().forEach(s -> {
      s.setChildren(dataChilren.get(s.getId()));
    });

    return search;
  }

  public List<XhTlTongHopHdr> dsTongHopTrinhVaThamDinh(ScTongHopReq req) throws Exception {
    UserInfo currentUser = SecurityContextService.getUser();
    if (currentUser == null){
      throw new Exception("Access denied.");
    }
    String dvql = currentUser.getDvql();
    if (currentUser.getCapDvi().equals(Contains.CAP_CUC)) {
      req.setMaDviSr(dvql);
    }
    req.setTrangThai(TrangThaiAllEnum.GUI_DUYET.getId());
    List<XhTlTongHopHdr> list = hdrRepository.listTongHopTrinhThamDinh(req);
    return list;
  }

  @Transactional
  public XhTlTongHopHdr save(XhTlTongHopRequest req) throws Exception {
    UserInfo userInfo = UserUtils.getUserInfo();
    if (!userInfo.getCapDvi().equals(Contains.CAP_CUC)) {
      throw new Exception("Tổng hợp danh sách hàng chỉ được thực hiện ở cấp cục");
    }
    XhTlDanhSachRequest reqTh = new XhTlDanhSachRequest();
    reqTh.setDvql(userInfo.getDvql());
    reqTh.setTrangThai(TrangThaiAllEnum.CHUA_CHOT.getId());
    reqTh.setThoiGianTlTu(req.getThoiGianTlTu());
    reqTh.setThoiGianTlDen(req.getThoiGianTlDen());
    List<XhTlDanhSachHdr> listTh = xhTlDanhSachRepository.listTongHop(reqTh);
    if(listTh == null || listTh.isEmpty()){
      throw new Exception("Không có dữ liệu để tổng hợp");
    }
    XhTlTongHopHdr hdr = new XhTlTongHopHdr();
    BeanUtils.copyProperties(req,hdr);
    hdr.setNam(LocalDate.now().getYear());
    hdr.setMaDvi(userInfo.getDvql());
    hdr.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
    hdr.setId(Long.parseLong(req.getMaDanhSach().split("-")[1]));
    XhTlTongHopHdr save = hdrRepository.save(hdr);
    saveFileDinhKem(req.getFileDinhKemReq(),save.getId(),XhTlTongHopHdr.TABLE_NAME);
    List<XhTlTongHopDtl> listDtl = new ArrayList<>();
    listTh.forEach(item -> {
      item.setTrangThai(TrangThaiAllEnum.DA_CHOT.getId());
      item.setIdTongHop(hdr.getId());
      item.setMaTongHop(hdr.getMaDanhSach());
      item.setNgayTongHop(LocalDate.now());
      XhTlTongHopDtl dtl = new XhTlTongHopDtl();
      dtl.setIdTongHop(hdr.getId());
      dtl.setIdDsHdr(item.getId());
      listDtl.add(dtl);
    });
    xhTlDanhSachRepository.saveAll(listTh);
    List<XhTlTongHopDtl> scTongHopDtls = dtlRepository.saveAll(listDtl);
    save.setChildren(scTongHopDtls);
    return save;

  }

  public XhTlTongHopHdr detail(Long id) throws Exception {
    Optional<XhTlTongHopHdr> optional = hdrRepository.findById(id);
    if(!optional.isPresent()){
      throw new Exception("Bản ghi không tồn tại");
    }
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    HashMap<Long, List<XhTlTongHopDtl>> dataChilren = getDataChilren(Collections.singletonList(optional.get().getId()));
    optional.get().setChildren(dataChilren.get(optional.get().getId()));
    optional.get().setTenDvi(mapDmucDvi.get(optional.get().getMaDvi()));
    optional.get().setFileDinhKem(fileDinhKemService.search(optional.get().getId(), XhTlTongHopHdr.TABLE_NAME));
    return optional.get();
  }

  private HashMap<Long,List<XhTlTongHopDtl>> getDataChilren(List<Long> idHdr){
    HashMap<Long,List<XhTlTongHopDtl>> hashMap = new HashMap<>();
    idHdr.forEach(item -> {
      List<XhTlTongHopDtl> dtl = dtlRepository.findAllByIdTongHop(item);
      dtl.forEach( dataChilren -> {
        try {
          dataChilren.setXhTlDanhSachHdr(xhTlDanhSachService.detail(dataChilren.getIdDsHdr()));
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      });
      hashMap.put(item,dtl);
    });
    return hashMap;
  }

  @Transactional
  public void delete(IdSearchReq idSearchReq) throws Exception {
    Optional<XhTlTongHopHdr> optional = hdrRepository.findById(idSearchReq.getId());
    if (!optional.isPresent()) {
      throw new Exception("Bản ghi không tồn tại");
    }
    XhTlTongHopHdr data = optional.get();
    List<XhTlDanhSachHdr> listDanhSach = xhTlDanhSachRepository.findAllByIdTongHop(data.getId());
    listDanhSach.forEach(s -> {
      s.setIdTongHop(null);
      s.setMaTongHop(null);
      s.setNgayTongHop(null);
      s.setTrangThai(TrangThaiAllEnum.CHUA_CHOT.getId());
    });
    xhTlDanhSachRepository.saveAll(listDanhSach);
    hdrRepository.delete(data);
    dtlRepository.deleteAllByIdTongHop(data.getId());
  }

  @Transactional
  public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
    List<XhTlTongHopHdr> list = hdrRepository.findByIdIn(idSearchReq.getIdList());

    if (list.isEmpty()) {
      throw new Exception("Bản ghi không tồn tại");
    }
    hdrRepository.deleteAll(list);
  }

  public XhTlTongHopHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {

    if (StringUtils.isEmpty(statusReq.getId())) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    Optional<XhTlTongHopHdr> optional = hdrRepository.findById(Long.valueOf(statusReq.getId()));
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
    XhTlTongHopHdr created = hdrRepository.save(optional.get());
    return created;
  }

}
