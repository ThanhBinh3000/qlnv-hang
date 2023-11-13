package com.tcdt.qlnvhang.service.xuathang.thanhlytieuhuy.tieuhuy;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.tieuhuy.XhThBaoCaoKqDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.tieuhuy.XhThBaoCaoKqHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.tieuhuy.XhThDanhSachRepository;
import com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.tieuhuy.XhThQuyetDinhRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlHoSoReq;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.tieuhuy.SearchXhThQuyetDinh;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.tieuhuy.XhThBaoCaoKqHdrReq;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.tieuhuy.XhThHoSoRequest;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScTrinhThamDinhHdr;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlQuyetDinhHdr;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.tieuhuy.*;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.UserUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;

@Service
public class XhThBaoCaoKqService extends BaseServiceImpl {
  @Autowired
  private XhThBaoCaoKqHdrRepository hdrRepository;

  @Autowired
  private XhThBaoCaoKqDtlRepository dtlRepository;

  @Autowired
  private XhThBaoCaoKqHdrRepository xhThBaoCaoKqHdrRepository;

  @Autowired
  private XhThDanhSachRepository xhThDanhSachRepository;

  @Autowired
  private XhThDanhSachService xhThDanhSachService;

  @Autowired
  private FileDinhKemService fileDinhKemService;

  public Page<XhThBaoCaoKqHdr> searchPage(CustomUserDetails currentUser, SearchXhThQuyetDinh req) throws Exception {
    String dvql = currentUser.getDvql();
    if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
      req.setDvql(dvql.substring(0, 4));
    } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_TONG_CUC)) {
      req.setDvql(dvql);
    }
    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
    Page<XhThBaoCaoKqHdr> search = xhThBaoCaoKqHdrRepository.search(req, pageable);
    Map<String, Map<String, Object>> mapDmucDvi = getListDanhMucDviObject(null, null, "01");

    Map<String, String> mapVthh = getListDanhMucHangHoa();
    search.getContent().forEach(s -> {
      if (mapDmucDvi.containsKey((s.getMaDvi()))) {
        Map<String, Object> objDonVi = mapDmucDvi.get(s.getMaDvi());
        s.setTenDvi(objDonVi.get("tenDvi").toString());
      }
      s.setTenTrangThai(TrangThaiAllEnum.getLabelById(s.getTrangThai()));
    });
    return search;
  }

  @Transactional
  public XhThBaoCaoKqHdr save(XhThBaoCaoKqHdrReq req) throws Exception {
    UserInfo userInfo = UserUtils.getUserInfo();
    if(!userInfo.getCapDvi().equals(Contains.CAP_CUC)){
      throw new Exception("Đơn vị lưu phải là cấp cục");
    }
    validateData(req);
    XhThBaoCaoKqHdr hdr = new XhThBaoCaoKqHdr();
    BeanUtils.copyProperties(req, hdr);
    hdr.setNam(LocalDate.now().getYear());
    hdr.setMaDvi(userInfo.getDvql());
    hdr.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
    XhThBaoCaoKqHdr created = hdrRepository.save(hdr);
    List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReq(), created.getId(), XhThBaoCaoKqHdr.TABLE_NAME + "_DINH_KEM");
    created.setFileDinhKem(fileDinhKem);
    List<XhThBaoCaoKqDtl> dtlList = this.saveDtl(req, created.getId());
    created.setChildren(dtlList);

    return created;
  }

  void validateData(XhThBaoCaoKqHdrReq req) throws Exception {
    Optional<XhThBaoCaoKqHdr> bySoQd = hdrRepository.findBySoBaoCao(req.getSoBaoCao());
    if(bySoQd.isPresent()){
      if(ObjectUtils.isEmpty(req.getId())){
        throw new Exception("Số báo cáo " + bySoQd.get().getSoBaoCao() +" đã tồn tại");
      }else{
        if(!req.getId().equals(bySoQd.get().getId())){
          throw new Exception("Số báo cáo " + bySoQd.get().getSoBaoCao() +" đã tồn tại");
        }
      }
    }
  }

  private List<XhThBaoCaoKqDtl> saveDtl(XhThBaoCaoKqHdrReq req, Long idHdr) throws Exception {
    List<XhThBaoCaoKqDtl> listDtl = new ArrayList<>();
    dtlRepository.deleteAllByIdHdr(idHdr);
    req.getChildren().forEach( item -> {
      XhThBaoCaoKqDtl dtl = new XhThBaoCaoKqDtl();
      BeanUtils.copyProperties(item, dtl,"id");
      dtl.setIdHdr(idHdr);
      dtlRepository.save(dtl);
      // Update lại data vào danh sách gốc
      Optional<XhThDanhSachHdr> dsHdr = xhThDanhSachRepository.findById(item.getIdDsHdr());
      if(dsHdr.isPresent()){
        dsHdr.get().setSlThucTe(item.getSlThucTe());
        dsHdr.get().setDonGiaThucTe(item.getDonGiaThucTe());
        dsHdr.get().setDviToChuc(item.getDviToChuc());
        dsHdr.get().setLyDoTieuHuy(item.getLyDoTieuHuy());
        dsHdr.get().setKetQuaTieuHuy(item.getKetQuaTieuHuy());
        XhThDanhSachHdr save = xhThDanhSachRepository.save(dsHdr.get());
        dtl.setXhThDanhSachHdr(save);
      }else{
        throw new RuntimeException("Không tìm thấy danh sách hàng cần sửa chữa");
      }
      listDtl.add(dtl);
    });
    return listDtl;
  }

  @Transactional
  public XhThBaoCaoKqHdr update(XhThBaoCaoKqHdrReq req) throws Exception {
    UserInfo userInfo = UserUtils.getUserInfo();
    Optional<XhThBaoCaoKqHdr> optional = hdrRepository.findById(req.getId());
    if (!optional.isPresent()){
      throw new Exception("Bản ghi không tồn tại");
    }
    validateData(req);
    XhThBaoCaoKqHdr hdr = optional.get();
    BeanUtils.copyProperties(req, hdr);

    hdr.setNam(LocalDate.now().getYear());
    hdr.setMaDvi(userInfo.getDvql());
    if(hdr.getTrangThai().equals(TrangThaiAllEnum.DA_DUYET_LDC.getId())){
      hdr.setTrangThai(TrangThaiAllEnum.DANG_DUYET_CB_VU.getId());
    }
    XhThBaoCaoKqHdr created = hdrRepository.save(hdr);
    fileDinhKemService.delete(req.getId(), Lists.newArrayList(XhThBaoCaoKqHdr.TABLE_NAME + "_DINH_KEM"));
    List<FileDinhKem> fileDinhKemList = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReq(), created.getId(), XhThBaoCaoKqHdr.TABLE_NAME + "_DINH_KEM");
    created.setFileDinhKem(fileDinhKemList);
    List<XhThBaoCaoKqDtl> dtlList = this.saveDtl(req, created.getId());
    created.setChildren(dtlList);
    return created;
  }


  public XhThBaoCaoKqHdr detail(Long id) throws Exception {
    Optional<XhThBaoCaoKqHdr> optional = hdrRepository.findById(id);
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    Map<String, String> mapDmucDvi = getListDanhMucDvi("2", null, "01");
    XhThBaoCaoKqHdr data = optional.get();
    data.setTenDvi(mapDmucDvi.get(data.getMaDvi()));
    List<FileDinhKem> fileDinhKem = fileDinhKemService.search(data.getId(), Arrays.asList(XhThBaoCaoKqHdr.TABLE_NAME + "_DINH_KEM"));
    data.setFileDinhKem(fileDinhKem);
    HashMap<Long, List<XhThBaoCaoKqDtl>> dataChilren = getDataChilren(Collections.singletonList(data.getId()));
    data.setChildren(dataChilren.get(data.getId()));
    return data;
  }


  private HashMap<Long,List<XhThBaoCaoKqDtl>> getDataChilren(List<Long> idHdr){
    HashMap<Long,List<XhThBaoCaoKqDtl>> hashMap = new HashMap<>();
    idHdr.forEach(item -> {
      List<XhThBaoCaoKqDtl> dtl = dtlRepository.findAllByIdHdr(item);
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

  @Transactional
  public void delete(IdSearchReq idSearchReq) throws Exception {
    Optional<XhThBaoCaoKqHdr> optional = xhThBaoCaoKqHdrRepository.findById(idSearchReq.getId());
    if (!optional.isPresent()) {
      throw new Exception("Bản ghi không tồn tại");
    }
    XhThBaoCaoKqHdr data = optional.get();

//    if (!DataUtils.isNullObject(data.getIdQd())) {
//      xhThQuyetDinhRepository.findById(data.getIdQd())
//          .ifPresent(item -> {
//            item.setIdKq(null);
//            item.setSoKq(null);
//            xhThQuyetDinhRepository.save(item);
//          });
//    }

    fileDinhKemService.delete(data.getId(), Lists.newArrayList(XhThBaoCaoKqHdr.TABLE_NAME+"_DINH_KEM"));
    xhThBaoCaoKqHdrRepository.delete(data);

  }

  @Transactional
  public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
    List<XhThBaoCaoKqHdr> list = xhThBaoCaoKqHdrRepository.findAllByIdIn(idSearchReq.getIdList());

    if (list.isEmpty()) {
      throw new Exception("Bản ghi không tồn tại");
    }

//    list.forEach(f -> {
//      if (!DataUtils.isNullObject(f.getIdQd())) {
//        xhThQuyetDinhRepository.findById(f.getIdQd())
//            .ifPresent(item -> {
//              item.setIdKq(null);
//              item.setSoKq(null);
//              xhThQuyetDinhRepository.save(item);
//            });
//      }
//    });

    fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(XhThBaoCaoKqHdr.TABLE_NAME+"_DINH_KEM"));
    xhThBaoCaoKqHdrRepository.deleteAll(list);

  }

  public XhThBaoCaoKqHdr approve(StatusReq statusReq) throws Exception {
    UserInfo userInfo = UserUtils.getUserInfo();
    if (StringUtils.isEmpty(statusReq.getId())) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    Optional<XhThBaoCaoKqHdr> optional = xhThBaoCaoKqHdrRepository.findById(Long.valueOf(statusReq.getId()));
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    }

    String status = statusReq.getTrangThai() + optional.get().getTrangThai();
    switch (status) {
      case Contains.CHODUYET_TP + Contains.DUTHAO:
      case Contains.CHODUYET_TP + Contains.TUCHOI_TP:
      case Contains.CHODUYET_TP + Contains.TUCHOI_LDC:
        break;
      case Contains.TUCHOI_TP + Contains.CHODUYET_TP:
      case Contains.CHODUYET_LDC + Contains.CHODUYET_TP:
        optional.get().setNgayDuyetTp(LocalDate.now());
        optional.get().setIdTp(userInfo.getId());
        optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
        break;
      case Contains.DADUYET_LDC + Contains.CHODUYET_LDC:
      case Contains.TUCHOI_LDC + Contains.CHODUYET_LDC:
        optional.get().setNgayDuyetLdc(LocalDate.now());
        optional.get().setIdLdc(userInfo.getId());
        optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
        break;
      default:
        throw new Exception("Phê duyệt không thành công");
    }
    optional.get().setTrangThai(statusReq.getTrangThai());
    XhThBaoCaoKqHdr created = xhThBaoCaoKqHdrRepository.save(optional.get());
    return created;
  }


  public void export(CustomUserDetails currentUser, SearchXhThQuyetDinh objReq, HttpServletResponse response) throws Exception {
    PaggingReq paggingReq = new PaggingReq();
    paggingReq.setPage(0);
    paggingReq.setLimit(Integer.MAX_VALUE);
    objReq.setPaggingReq(paggingReq);
    Page<XhThBaoCaoKqHdr> page = this.searchPage(currentUser, objReq);
    List<XhThBaoCaoKqHdr> data = page.getContent();

    String title = "Danh sách báo cáo kết quả bán thanh lý hàng DTQG ";
    String[] rowsName = new String[]{"STT", "Số báo cáo", "Nội dung báo cáo", "Ngày báo cáo",
        "Số quyết định thanh lý", "Trạng thái"};
    String fileName = "danh-sach-bao-cao-ket-qua-ban-thanh-ly-hang-dtqg.xlsx";
    List<Object[]> dataList = new ArrayList<Object[]>();
    Object[] objs = null;
    for (int i = 0; i < data.size(); i++) {
      XhThBaoCaoKqHdr qd = data.get(i);
      objs = new Object[rowsName.length];
      objs[0] = i;
      objs[1] = qd.getSoBaoCao();
      objs[2] = qd.getNoiDung();
      objs[3] = qd.getNgayBaoCao();
      objs[4] = qd.getSoQd();
      objs[5] = qd.getTenTrangThai();
      dataList.add(objs);
    }
    ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
    ex.export();
  }
}
