package com.tcdt.qlnvhang.service.suachuahang.impl;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntDtl;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntHdr;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.suachuahang.ScDanhSachRepository;
import com.tcdt.qlnvhang.repository.xuathang.suachuahang.ScTongHopDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.suachuahang.ScTongHopHdrRepository;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.suachua.ScTongHopReq;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlDanhSachRequest;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.service.suachuahang.ScTongHopService;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScDanhSachHdr;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScPhieuNhapKhoHdr;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScTongHopDtl;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScTongHopHdr;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.UserUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.bag.CollectionBag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ScTongHopServiceImpl extends BaseServiceImpl implements ScTongHopService {

  @Autowired
  private ScTongHopHdrRepository hdrRepository;

  @Autowired
  private ScTongHopDtlRepository dtlRepository;
  @Autowired
  private ScDanhSachRepository scDanhSachRepository;
  @Autowired
  private FileDinhKemService fileDinhKemService;
  @Autowired
  private ScDanhSachServiceImpl scDanhSachServiceImpl;

  @Override
  public Page<ScTongHopHdr> searchPage(ScTongHopReq req) throws Exception {
    UserInfo currentUser = SecurityContextService.getUser();
    if (currentUser == null){
      throw new Exception("Access denied.");
    }
    String dvql = currentUser.getDvql();
    if (currentUser.getCapDvi().equals(Contains.CAP_CUC)) {
      req.setDvql(dvql);
    }
    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
    Page<ScTongHopHdr> search = hdrRepository.searchPage(req, pageable);

    List<Long> idsList = search.getContent().stream().map(ScTongHopHdr::getId).collect(Collectors.toList());

    HashMap<Long, List<ScTongHopDtl>> dataChilren = getDataChilren(idsList);
    //set label
    search.getContent().forEach(s -> {
      s.setChildren(dataChilren.get(s.getId()));
    });
    return search;
  }

  private HashMap<Long,List<ScTongHopDtl>> getDataChilren(List<Long> idHdr){
    HashMap<Long,List<ScTongHopDtl>> hashMap = new HashMap<>();
    idHdr.forEach(item -> {
        List<ScTongHopDtl> dtl = dtlRepository.findAllByIdHdr(item);
        dtl.forEach( dataChilren -> {
          try {
            dataChilren.setScDanhSachHdr(scDanhSachServiceImpl.detail(dataChilren.getIdDsHdr()));
          } catch (Exception e) {
            throw new RuntimeException(e);
          }
        });
        hashMap.put(item,dtl);
    });
    return hashMap;
  }

  @Override
  public ScTongHopHdr create(ScTongHopReq req) throws Exception {
    UserInfo userInfo = UserUtils.getUserInfo();
    if (!userInfo.getCapDvi().equals(Contains.CAP_CUC)) {
      throw new Exception("Tổng hợp danh sách hàng chỉ được thực hiện ở cấp cục");
    }
    XhTlDanhSachRequest reqTh = new XhTlDanhSachRequest();
    reqTh.setDvql(userInfo.getDvql());
    List<ScDanhSachHdr> listTh = scDanhSachRepository.listTongHop(reqTh);
    if(listTh == null || listTh.isEmpty()){
      throw new Exception("Không có dữ liệu để tổng hợp");
    }
    ScTongHopHdr hdr = new ScTongHopHdr();
    BeanUtils.copyProperties(req,hdr);
    hdr.setNam(LocalDate.now().getYear());
    hdr.setMaDvi(userInfo.getDvql());
    hdr.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
    hdr.setId(Long.parseLong(req.getMaDanhSach().split("-")[1]));
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
    return null;
  }

  @Override
  public ScTongHopHdr detail(Long id) throws Exception {
    Optional<ScTongHopHdr> optional = hdrRepository.findById(id);
    if(!optional.isPresent()){
      throw new Exception("Bản ghi không tồn tại");
    }
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    HashMap<Long, List<ScTongHopDtl>> dataChilren = getDataChilren(Collections.singletonList(optional.get().getId()));
    optional.get().setChildren(dataChilren.get(optional.get().getId()));
    optional.get().setTenDvi(mapDmucDvi.get(optional.get().getMaDvi()));
    return optional.get();
  }

  @Override
  public ScTongHopHdr approve(ScTongHopReq req) throws Exception {
    Optional<ScTongHopHdr> optional = hdrRepository.findById(req.getId());
    if(!optional.isPresent()){
      throw new Exception("Thông tin tổng hợp không tồn tại");
    }
    String status = req.getTrangThai() + optional.get().getTrangThai();
    if ((TrangThaiAllEnum.GUI_DUYET.getId() + TrangThaiAllEnum.DU_THAO.getId()).equals(status)) {
      optional.get().setTrangThai(req.getTrangThai());
    } else {
      throw new Exception("Gửi duyệt không thành công");
    }
    hdrRepository.save(optional.get());


    return optional.get();
  }

  @Override
  public void delete(Long id) throws Exception {
    Optional<ScTongHopHdr> optional = hdrRepository.findById(id);
    if (!optional.isPresent()) {
      throw new Exception("Bản ghi không tồn tại");
    }
    hdrRepository.delete(optional.get());
    dtlRepository.deleteAllByIdHdr(optional.get().getId());
  }

  @Override
  public void deleteMulti(List<Long> listMulti) throws Exception {

  }

  @Override
  public void export(ScTongHopReq req, HttpServletResponse response) throws Exception {
//    PaggingReq paggingReq = new PaggingReq();
//    paggingReq.setPage(0);
//    paggingReq.setLimit(Integer.MAX_VALUE);
//    req.setPaggingReq(paggingReq);
//    Page<ScPhieuNhapKhoHdr> page = searchPage(req);
//    List<ScPhieuNhapKhoHdr> data = page.getContent();
//
//    String title = "Danh sách phiếu xuất kho";
//    String[] rowsName = new String[]{"STT", "Năm xuất", "Số QĐ giao NVXH", "Ngày ký QĐ giao NVXH", "Thời hạn xuất sửa chữa","Thời hạn nhập sửa chữa","Số QĐ SC hàng DTQG","Trích yếu", "Trạng thái QĐ","Trạng thái xuất để SC"};
//    String fileName = "danh-sach.xlsx";
//    List<Object[]> dataList = new ArrayList<Object[]>();
//    Object[] objs = null;
//    for (int i = 0; i < data.size(); i++) {
//      ScPhieuNhapKhoHdr dx = data.get(i);
//      objs = new Object[rowsName.length];
//      objs[0] = i + 1;
//      objs[1] = dx.getNam();
////            objs[2] = dx.getSoQdXh();
//      objs[3] = dx.getNam();
////            objs[4] = dx.getNgayXuatKho();
////            objs[5] = dx.getTenDiemKho()+"/"+dx.getTenNhaKho()+"/"+dx.getTenNganKho()+"/"+dx.getTenLoKho();
////            objs[6] = dx.getSoPhieuXuatKho();
////            objs[7] = dx.getNgayXuatKho();
//      objs[8] = null;
//      objs[9] = null;
//      objs[10] = dx.getTenTrangThai();
//      dataList.add(objs);
//    }
//    ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
//    ex.export();
  }

  @Override
  public List<ScTongHopHdr> dsTongHopTrinhVaThamDinh(ScTongHopReq req) throws Exception {
    UserInfo currentUser = SecurityContextService.getUser();
    if (currentUser == null){
      throw new Exception("Access denied.");
    }
    String dvql = currentUser.getDvql();
    if (currentUser.getCapDvi().equals(Contains.CAP_CUC)) {
      req.setMaDviSr(dvql);
    }
    req.setTrangThai(TrangThaiAllEnum.GUI_DUYET.getId());
    List<ScTongHopHdr> list = hdrRepository.listTongHopTrinhThamDinh(req);
    return list;
  }
}
