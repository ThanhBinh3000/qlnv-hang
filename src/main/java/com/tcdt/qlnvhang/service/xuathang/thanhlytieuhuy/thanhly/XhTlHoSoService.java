package com.tcdt.qlnvhang.service.xuathang.thanhlytieuhuy.thanhly;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly.*;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.suachua.ScTrinhThamDinhHdrReq;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlHoSoReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScTrinhThamDinhDtl;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScTrinhThamDinhHdr;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.*;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.tieuhuy.XhThHoSoHdr;
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
public class XhTlHoSoService extends BaseServiceImpl {


  @Autowired
  private XhTlHoSoHdrRepository xhTlHoSoHdrRepository;

  @Autowired
  private XhTlHoSoHdrRepository hdrRepository;

  @Autowired
  private XhTlHoSoDtlRepository dtlRepository;

  @Autowired
  private XhTlDanhSachRepository xhTlDanhSachRepository;

  @Autowired
  private XhTlDanhSachService xhTlDanhSachService;

  @Autowired
  private XhTlQuyetDinhHdrRepository xhTlQuyetDinhHdrRepository;

  @Autowired
  private XhTlThongBaoKqRepository xhTlThongBaoKqRepository;

  @Autowired
  private FileDinhKemService fileDinhKemService;

  public Page<XhTlHoSoHdr> searchPage(CustomUserDetails currentUser, XhTlHoSoReq req) throws Exception {
    String dvql = currentUser.getDvql();
    if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
      req.setDvql(dvql.substring(0, 4));
    } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_TONG_CUC)) {
      req.setDvql(dvql);
    }
    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
    Page<XhTlHoSoHdr> search = xhTlHoSoHdrRepository.searchPage(req, pageable);
    Map<String, Map<String, Object>> mapDmucDvi = getListDanhMucDviObject(null, null, "01");
    search.getContent().forEach(s -> {
      if (mapDmucDvi.containsKey((s.getMaDvi()))) {
        Map<String, Object> objDonVi = mapDmucDvi.get(s.getMaDvi());
        s.setTenDvi(objDonVi.get("tenDvi").toString());
      }
      setThongTinQdKq(s);
      s.setTenTrangThai(TrangThaiAllEnum.getLabelById(s.getTrangThai()));
    });
    return search;
  }

  void setThongTinQdKq(XhTlHoSoHdr data){
    Optional<XhTlQuyetDinhHdr> qdOpt = xhTlQuyetDinhHdrRepository.findByIdHoSo(data.getId());
    if(qdOpt.isPresent()){
      data.setIdQdTl(qdOpt.get().getId());
      data.setSoQdTl(qdOpt.get().getSoQd());
    }
    Optional<XhTlThongBaoKq> tbOpt = xhTlThongBaoKqRepository.findByIdHoSo(data.getId());
    if(tbOpt.isPresent()){
      data.setIdTb(tbOpt.get().getId());
      data.setSoThongBaoKq(tbOpt.get().getSoThongBao());
    }
  }

  public List<XhTlHoSoHdr> dsTaoQuyetDinhThanhLy(XhTlHoSoReq req) throws Exception {
    UserInfo currentUser = SecurityContextService.getUser();
    if (currentUser == null){
      throw new Exception("Access denied.");
    }
    req.setTrangThai(TrangThaiAllEnum.DA_DUYET_BTC.getId());
    List<XhTlHoSoHdr> list = hdrRepository.listTaoQuyetDinhThanhLy(req);
    return list;
  }

  public List<XhTlHoSoHdr> dsTaoThongBaoThanhLy(XhTlHoSoReq req) throws Exception {
    UserInfo currentUser = SecurityContextService.getUser();
    if (currentUser == null){
      throw new Exception("Access denied.");
    }
    req.setTrangThai(TrangThaiAllEnum.TU_CHOI_BTC.getId());
    List<XhTlHoSoHdr> list = hdrRepository.listTaoThongBaoThanhLy(req);
    return list;
  }

  @Transactional
  public XhTlHoSoHdr save(XhTlHoSoReq req) throws Exception {
    UserInfo userInfo = UserUtils.getUserInfo();
    if(!userInfo.getCapDvi().equals(Contains.CAP_CUC)){
      throw new Exception("Đơn vị lưu phải là cấp cục");
    }
    validateData(req);
    XhTlHoSoHdr hdr = new XhTlHoSoHdr();
    BeanUtils.copyProperties(req, hdr);
    hdr.setNam(LocalDate.now().getYear());
    hdr.setMaDvi(userInfo.getDvql());
    hdr.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
    XhTlHoSoHdr created = hdrRepository.save(hdr);
    List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(req.getFileCanCuReq(), created.getId(), XhTlHoSoHdr.TABLE_NAME + "_CAN_CU");
    created.setFileCanCu(canCu);
    List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReq(), created.getId(), XhTlHoSoHdr.TABLE_NAME + "_DINH_KEM");
    created.setFileDinhKem(fileDinhKem);
    List<XhTlHoSoDtl> dtlList = this.saveDtl(req, created.getId());
    created.setChildren(dtlList);
    return created;
  }

  void validateData(XhTlHoSoReq req) throws Exception {
    Optional<XhTlHoSoHdr> bySoQd = hdrRepository.findBySoHoSo(req.getSoHoSo());
    if(bySoQd.isPresent()){
      if(ObjectUtils.isEmpty(req.getId())){
        throw new Exception("Số hồ sơ " + bySoQd.get().getSoHoSo() +" đã tồn tại");
      }else{
        if(!req.getId().equals(bySoQd.get().getId())){
          throw new Exception("Số hồ sơ " + bySoQd.get().getSoHoSo() +" đã tồn tại");
        }
      }
    }
  }

  private List<XhTlHoSoDtl> saveDtl(XhTlHoSoReq req, Long idHdr) throws Exception {
    List<XhTlHoSoDtl> listDtl = new ArrayList<>();
    dtlRepository.deleteAllByIdHdr(idHdr);
    req.getChildren().forEach( item -> {
      XhTlHoSoDtl dtl = new XhTlHoSoDtl();
      dtl.setIdHdr(idHdr);
      dtl.setIdDsHdr(item.getId());
      dtlRepository.save(dtl);
      // Update lại data vào danh sách gốc
      Optional<XhTlDanhSachHdr> dsHdr = xhTlDanhSachRepository.findById(item.getId());
      if(dsHdr.isPresent()){
        dsHdr.get().setKetQua(item.getKetQua());
        dsHdr.get().setDonGiaDk(item.getDonGiaDk());
        dsHdr.get().setDonGiaPd(item.getDonGiaPd());
        dsHdr.get().setSlDaDuyet(item.getSlDaDuyet());
        XhTlDanhSachHdr save = xhTlDanhSachRepository.save(dsHdr.get());
        dtl.setXhTlDanhSachHdr(save);
      }else{
        throw new RuntimeException("Không tìm thấy danh sách hàng cần sửa chữa");
      }
      listDtl.add(dtl);
    });
    return listDtl;
  }

  @Transactional
  public XhTlHoSoHdr update(XhTlHoSoReq req) throws Exception {
    UserInfo userInfo = UserUtils.getUserInfo();
    Optional<XhTlHoSoHdr> optional = hdrRepository.findById(req.getId());
    if (!optional.isPresent()){
      throw new Exception("Bản ghi không tồn tại");
    }
    validateData(req);
    XhTlHoSoHdr hdr = optional.get();
    BeanUtils.copyProperties(req, hdr);

    hdr.setNam(LocalDate.now().getYear());
    hdr.setMaDvi(userInfo.getDvql());
    if(hdr.getTrangThai().equals(TrangThaiAllEnum.DA_DUYET_LDC.getId())){
      hdr.setTrangThai(TrangThaiAllEnum.DANG_DUYET_CB_VU.getId());
    }
    XhTlHoSoHdr created = hdrRepository.save(hdr);
    fileDinhKemService.delete(req.getId(), Lists.newArrayList(XhTlHoSoHdr.TABLE_NAME + "_CAN_CU"));
    List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(req.getFileCanCuReq(), created.getId(), XhTlHoSoHdr.TABLE_NAME + "_CAN_CU");
    created.setFileCanCu(canCu);
    fileDinhKemService.delete(req.getId(), Lists.newArrayList(XhTlHoSoHdr.TABLE_NAME + "_DINH_KEM"));
    List<FileDinhKem> fileDinhKemList = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReq(), created.getId(), XhTlHoSoHdr.TABLE_NAME + "_DINH_KEM");
    created.setFileDinhKem(fileDinhKemList);
    List<XhTlHoSoDtl> dtlList = this.saveDtl(req, created.getId());
    created.setChildren(dtlList);
    return created;
  }


  public XhTlHoSoHdr detail(Long id) throws Exception {
    Optional<XhTlHoSoHdr> optional = xhTlHoSoHdrRepository.findById(id);
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    }

    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    XhTlHoSoHdr data = optional.get();

    List<FileDinhKem> fileDinhKem = fileDinhKemService.search(data.getId(), Arrays.asList(XhTlHoSoHdr.TABLE_NAME+"_DINH_KEM"));
    data.setFileDinhKem(fileDinhKem);
    List<FileDinhKem> canCu = fileDinhKemService.search(data.getId(), Arrays.asList(XhTlHoSoHdr.TABLE_NAME + "_CAN_CU"));
    data.setFileCanCu(canCu);
    HashMap<Long, List<XhTlHoSoDtl>> dataChilren = getDataChilren(Collections.singletonList(data.getId()));
    data.setChildren(dataChilren.get(data.getId()));
    return data;
  }

  private HashMap<Long,List<XhTlHoSoDtl>> getDataChilren(List<Long> idHdr){
    HashMap<Long,List<XhTlHoSoDtl>> hashMap = new HashMap<>();
    idHdr.forEach(item -> {
      List<XhTlHoSoDtl> dtl = dtlRepository.findAllByIdHdr(item);
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
    Optional<XhTlHoSoHdr> optional = xhTlHoSoHdrRepository.findById(idSearchReq.getId());
    if (!optional.isPresent()) {
      throw new Exception("Bản ghi không tồn tại");
    }
    XhTlHoSoHdr data = optional.get();

    fileDinhKemService.delete(data.getId(), Lists.newArrayList(XhTlHoSoHdr.TABLE_NAME + "_CAN_CU"));
    fileDinhKemService.delete(data.getId(), Lists.newArrayList(XhTlHoSoHdr.TABLE_NAME));
    xhTlHoSoHdrRepository.delete(data);
    dtlRepository.deleteAllByIdHdr(data.getId());
  }

  @Transactional
  public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
    List<XhTlHoSoHdr> list = xhTlHoSoHdrRepository.findAllByIdIn(idSearchReq.getIdList());

    if (list.isEmpty()) {
      throw new Exception("Bản ghi không tồn tại");
    }

    fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(XhTlHoSoHdr.TABLE_NAME));
    fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(XhTlHoSoHdr.TABLE_NAME + "_CAN_CU"));
    xhTlHoSoHdrRepository.deleteAll(list);

  }


  public XhTlHoSoHdr approve(StatusReq req) throws Exception {

    if (StringUtils.isEmpty(req.getId())) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    Optional<XhTlHoSoHdr> optional = hdrRepository.findById(Long.valueOf(req.getId()));
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    XhTlHoSoHdr hdr = optional.get();
    if(getUser().getCapDvi().equals(Contains.CAP_CUC)){
      String status = hdr.getTrangThai() + req.getTrangThai();
      switch (status) {
        // Re approve : gửi lại duyệt
        case Contains.TUCHOI_TP + Contains.CHODUYET_TP:
        case Contains.TUCHOI_LDC + Contains.CHODUYET_TP:
        case Contains.TUCHOI_LDV + Contains.CHODUYET_TP:
        case Contains.TU_CHOI_CBV + Contains.CHODUYET_TP:
        case Contains.TUCHOI_LDTC + Contains.CHODUYET_TP:
          optional.get().setNguoiGduyetId(getUser().getId());
          optional.get().setNgayGduyet(LocalDate.now());
          break;
        // Arena các cấp duuyệt
        case Contains.DUTHAO + Contains.CHODUYET_TP:
        case Contains.CHODUYET_TP + Contains.CHODUYET_LDC:
          optional.get().setNguoiPduyetId(getUser().getId());
          optional.get().setNgayPduyet(LocalDate.now());
          break;
        case Contains.CHODUYET_LDC + Contains.DADUYET_LDC:
          optional.get().setIdLdc(getUser().getId());
          optional.get().setNgayDuyetLdc(LocalDate.now());
          break;
        // Arena từ chối
        case Contains.CHODUYET_TP + Contains.TUCHOI_TP:
          hdr.setLyDoTuChoi(req.getLyDoTuChoi());
          break;
        case Contains.CHODUYET_LDC + Contains.TUCHOI_LDC:
          optional.get().setIdLdc(getUser().getId());
          optional.get().setNgayDuyetLdc(LocalDate.now());
          hdr.setLyDoTuChoi(req.getLyDoTuChoi());
          break;
        default:
          throw new Exception("Phê duyệt không thành công");
      }
      optional.get().setTrangThai(req.getTrangThai());
    }else if (getUser().getCapDvi().equals(Contains.CAP_TONG_CUC)){
      String status = hdr.getTrangThai() + req.getTrangThai();
      switch (status) {
        case Contains.DANG_DUYET_CB_VU + Contains.CHODUYET_LDV:
        case Contains.DADUYET_LDC + Contains.CHODUYET_LDV:
          optional.get().setNguoiPduyetId(getUser().getId());
          optional.get().setNgayDuyetLan2(LocalDate.now());
          break;
        case Contains.CHODUYET_LDV + Contains.CHODUYET_LDTC:
          optional.get().setIdLdv(getUser().getId());
          optional.get().setNgayDuyetLdv(LocalDate.now());
          break;
        case Contains.CHODUYET_LDTC + Contains.DADUYET_LDTC:
          optional.get().setIdLdtc(getUser().getId());
          optional.get().setNgayDuyetLdtc(LocalDate.now());
          break;
        case Contains.DADUYET_LDTC + Contains.CHO_DUYET_BTC:
        case Contains.CHO_DUYET_BTC + Contains.DA_DUYET_BTC:
          optional.get().setNguoiPduyetId(getUser().getId());
          optional.get().setNgayDuyetLan3(LocalDate.now());
          break;
        case Contains.DA_DUYET_LDC + Contains.TU_CHOI_CBV:
        case Contains.CHODUYET_LDV + Contains.TUCHOI_LDV:
        case Contains.DANG_DUYET_CB_VU + Contains.TU_CHOI_CBV:
        case Contains.CHO_DUYET_BTC + Contains.TU_CHOI_BTC:
          hdr.setLyDoTuChoi(req.getLyDoTuChoi());
          break;
        default:
          throw new Exception("Phê duyệt không thành công");
      }
      optional.get().setTrangThai(req.getTrangThai());
    }

    XhTlHoSoHdr created = hdrRepository.save(optional.get());
    return created;
  }


  public void export(CustomUserDetails currentUser, XhTlHoSoReq objReq, HttpServletResponse response) throws Exception {
    PaggingReq paggingReq = new PaggingReq();
    paggingReq.setPage(0);
    paggingReq.setLimit(Integer.MAX_VALUE);
    objReq.setPaggingReq(paggingReq);
    Page<XhTlHoSoHdr> page = this.searchPage(currentUser, objReq);
    List<XhTlHoSoHdr> data = page.getContent();

    String title = "Danh sách quyết định thanh lý hàng DTQG ";
    String[] rowsName = new String[]{"STT", "Số quyết định", "Trích yếu", "Ngày ký",
        "Hồ sơ đề nghị thanh lý", "Trạng thái"};
    String fileName = "danh-sach-quyet-dinh-thanh-ly-hang-dtqg.xlsx";
    List<Object[]> dataList = new ArrayList<Object[]>();
    Object[] objs = null;
    for (int i = 0; i < data.size(); i++) {
      XhTlHoSoHdr qd = data.get(i);
      objs = new Object[rowsName.length];
      objs[0] = i;
      objs[1] = qd.getSoQd();
      objs[2] = qd.getTrichYeu();
      objs[4] = qd.getSoHoSo();
      objs[5] = qd.getTenTrangThai();
      dataList.add(objs);
    }
    ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
    ex.export();
  }
}
