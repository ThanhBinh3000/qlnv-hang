package com.tcdt.qlnvhang.service.xuathang.thanhlytieuhuy.tieuhuy;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.common.DocxToPdfConverter;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.tieuhuy.XhThDanhSachRepository;
import com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.tieuhuy.XhThHoSoDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.tieuhuy.XhThHoSoHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.tieuhuy.XhThTongHopHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.tieuhuy.XhThHoSoRequest;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.tieuhuy.XhThDanhSachHdr;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.tieuhuy.XhThHoSoDtl;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.tieuhuy.XhThHoSoHdr;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.tieuhuy.XhThTongHopHdr;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.UserUtils;
import fr.opensagres.xdocreport.core.XDocReportException;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class XhThHoSoService extends BaseServiceImpl {

  @Autowired
  private XhThHoSoHdrRepository hdrRepository;

  @Autowired
  private XhThHoSoDtlRepository dtlRepository;

  @Autowired
  private XhThTongHopHdrRepository xhThTongHopHdrRepository;

  @Autowired
  private XhThDanhSachRepository xhThDanhSachRepository;

  @Autowired
  private XhThDanhSachService xhThDanhSachService;

  @Autowired
  private FileDinhKemService fileDinhKemService;

  public Page<XhThHoSoHdr> searchPage(CustomUserDetails currentUser, XhThHoSoRequest req) throws Exception {
    String dvql = currentUser.getDvql();
    req.setMaDviSr(dvql);
    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
    Page<XhThHoSoHdr> search = hdrRepository.searchPage(req, pageable);
    Map<String, Map<String, Object>> mapDmucDvi = getListDanhMucDviObject(null, null, "01");
    search.getContent().forEach(s -> {
      if (mapDmucDvi.containsKey((s.getMaDvi()))) {
        Map<String, Object> objDonVi = mapDmucDvi.get(s.getMaDvi());
        s.setTenDvi(objDonVi.get("tenDvi").toString());
      }
      s.setTenTrangThai(TrangThaiAllEnum.getLabelById(s.getTrangThai()));
      s.setTenTrangThaiTc(TrangThaiAllEnum.getLabelById(s.getTrangThaiTc()));
    });
    return search;
  }

  @Transactional
  public XhThHoSoHdr save(XhThHoSoRequest req) throws Exception {
    UserInfo userInfo = UserUtils.getUserInfo();
    if(!userInfo.getCapDvi().equals(Contains.CAP_CUC)){
      throw new Exception("Đơn vị lưu phải là cấp cục");
    }
    validateData(req);
    XhThHoSoHdr hdr = new XhThHoSoHdr();
    BeanUtils.copyProperties(req, hdr);
    hdr.setNam(LocalDate.now().getYear());
    hdr.setMaDvi(userInfo.getDvql());
    hdr.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
    XhThHoSoHdr created = hdrRepository.save(hdr);
    List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReq(), created.getId(), XhThHoSoHdr.FILE_DK);
    created.setFileDinhKem(fileDinhKem);
    List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(req.getFileCanCuReq(), created.getId(), XhThHoSoHdr.FILE_CC);
    created.setFileCanCu(canCu);
    List<XhThHoSoDtl> dtlList = this.saveDtl(req, created.getId());
    created.setChildren(dtlList);
    return created;
  }

  void validateData(XhThHoSoRequest req) throws Exception {
    Optional<XhThHoSoHdr> bySoQd = hdrRepository.findBySoHoSo(req.getSoHoSo());
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

  private List<XhThHoSoDtl> saveDtl(XhThHoSoRequest req, Long idHdr) throws Exception {
    List<XhThHoSoDtl> listDtl = new ArrayList<>();
    dtlRepository.deleteAllByIdHdr(idHdr);
    req.getChildren().forEach( item -> {
      XhThHoSoDtl dtl = new XhThHoSoDtl();
      BeanUtils.copyProperties(item, dtl,"id");
      dtl.setIdHdr(idHdr);
      dtlRepository.save(dtl);
      // Update lại data vào danh sách gốc
      Optional<XhThDanhSachHdr> dsHdr = xhThDanhSachRepository.findById(item.getIdDsHdr());
      if(dsHdr.isPresent()){
        dsHdr.get().setKetQua(item.getKetQua());
        dsHdr.get().setDonGiaDk(item.getDonGiaDk());
        dsHdr.get().setDonGiaPd(item.getDonGiaPd());
        dsHdr.get().setSlDaDuyet(item.getSlDaDuyet());
        XhThDanhSachHdr save = xhThDanhSachRepository.save(dsHdr.get());
        dtl.setXhThDanhSachHdr(save);
      }else{
        throw new RuntimeException("Không tìm thấy danh sách hàng cần sửa chữa");
      }
      listDtl.add(dtl);
    });
    return listDtl;
  }

  public List<XhThHoSoHdr> dsTaoQuyetDinhTieuHuy(XhThHoSoRequest req) throws Exception {
    UserInfo currentUser = SecurityContextService.getUser();
    if (currentUser == null){
      throw new Exception("Access denied.");
    }
    req.setTrangThai(TrangThaiAllEnum.DA_DUYET_BTC.getId());
    return hdrRepository.listTaoQuyetDinhTieuHuy(req);
  }

  public List<XhThHoSoHdr> dsTaoThongBaoTieuHuy(XhThHoSoRequest req) throws Exception {
    UserInfo currentUser = SecurityContextService.getUser();
    if (currentUser == null){
      throw new Exception("Access denied.");
    }
    req.setTrangThai(TrangThaiAllEnum.TU_CHOI_BTC.getId());
    return hdrRepository.listTaoThongBaoTieuHuy(req);
  }

  @Transactional
  public XhThHoSoHdr update(XhThHoSoRequest req) throws Exception {
    UserInfo userInfo = UserUtils.getUserInfo();
    Optional<XhThHoSoHdr> optional = hdrRepository.findById(req.getId());
    if (!optional.isPresent()){
      throw new Exception("Bản ghi không tồn tại");
    }
    validateData(req);
    XhThHoSoHdr hdr = optional.get();
    BeanUtils.copyProperties(req, hdr);
    hdr.setNam(LocalDate.now().getYear());
    if(hdr.getTrangThai().equals(TrangThaiAllEnum.DA_DUYET_LDC.getId())){
      hdr.setTrangThai(TrangThaiAllEnum.DANG_DUYET_CB_VU.getId());
    }
    XhThHoSoHdr created = hdrRepository.save(hdr);
    fileDinhKemService.delete(req.getId(), Lists.newArrayList(XhThHoSoHdr.FILE_DK,XhThHoSoHdr.FILE_CC));
    List<FileDinhKem> fileDinhKemList = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReq(), created.getId(), XhThHoSoHdr.FILE_DK);
    created.setFileDinhKem(fileDinhKemList);
    List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(req.getFileCanCuReq(), created.getId(), XhThHoSoHdr.FILE_CC);
    created.setFileCanCu(canCu);

    List<XhThHoSoDtl> dtlList = this.saveDtl(req, created.getId());
    created.setChildren(dtlList);
    return created;
  }


  public XhThHoSoHdr detail(Long id) throws Exception {
    Optional<XhThHoSoHdr> optional = hdrRepository.findById(id);
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    }

    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    XhThHoSoHdr data = optional.get();

    List<FileDinhKem> fileDinhKem = fileDinhKemService.search(data.getId(), XhThHoSoHdr.FILE_DK);
    data.setFileDinhKem(fileDinhKem);
    List<FileDinhKem> canCu = fileDinhKemService.search(data.getId(), XhThHoSoHdr.FILE_CC);
    data.setFileCanCu(canCu);
    HashMap<Long, List<XhThHoSoDtl>> dataChilren = getDataChilren(Collections.singletonList(data.getId()));
    data.setChildren(dataChilren.get(data.getId()));
    return data;
  }

  private HashMap<Long,List<XhThHoSoDtl>> getDataChilren(List<Long> idHdr){
    HashMap<Long,List<XhThHoSoDtl>> hashMap = new HashMap<>();
    idHdr.forEach(item -> {
      List<XhThHoSoDtl> dtl = dtlRepository.findAllByIdHdr(item);
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
    Optional<XhThHoSoHdr> optional = hdrRepository.findById(idSearchReq.getId());
    if (!optional.isPresent()) {
      throw new Exception("Bản ghi không tồn tại");
    }
    XhThHoSoHdr data = optional.get();
    fileDinhKemService.delete(data.getId(), Lists.newArrayList(XhThHoSoHdr.FILE_DK,XhThHoSoHdr.FILE_CC));
    hdrRepository.delete(data);
  }

  @Transactional
  public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
    List<XhThHoSoHdr> list = hdrRepository.findAllByIdIn(idSearchReq.getIdList());

    if (list.isEmpty()) {
      throw new Exception("Bản ghi không tồn tại");
    }

    fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(XhThHoSoHdr.FILE_DK,XhThHoSoHdr.FILE_CC));
    hdrRepository.deleteAll(list);

  }


  public XhThHoSoHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {

    if (StringUtils.isEmpty(statusReq.getId())) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    Optional<XhThHoSoHdr> optional = hdrRepository.findById(Long.valueOf(statusReq.getId()));
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    XhThHoSoHdr hdr = optional.get();
    if(currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)){
      String status = hdr.getTrangThai() + statusReq.getTrangThai();
      switch (status) {
        // Re approve : gửi lại duyệt
        case Contains.TUCHOI_TP + Contains.CHODUYET_TP:
        case Contains.TUCHOI_LDC + Contains.CHODUYET_TP:
        case Contains.TUCHOI_LDV + Contains.CHODUYET_TP:
        case Contains.TU_CHOI_CBV + Contains.CHODUYET_TP:
        case Contains.TUCHOI_LDTC + Contains.CHODUYET_TP:
          break;
        // Arena các cấp duuyệt
        case Contains.DUTHAO + Contains.CHODUYET_TP:
        case Contains.CHODUYET_TP + Contains.CHODUYET_LDC:
          break;
        case Contains.CHODUYET_LDC + Contains.DADUYET_LDC:
          hdr.setIdLdc(currentUser.getUser().getId());
          hdr.setNgayDuyetLdc(LocalDate.now());
          break;
        // Arena từ chối
        case Contains.CHODUYET_TP + Contains.TUCHOI_TP:
          hdr.setLyDoTuChoi(statusReq.getLyDoTuChoi());
          break;
        case Contains.CHODUYET_LDC + Contains.TUCHOI_LDC:
          hdr.setIdLdc(currentUser.getUser().getId());
          hdr.setNgayDuyetLdc(LocalDate.now());
          hdr.setLyDoTuChoi(statusReq.getLyDoTuChoi());
          break;
        default:
          throw new Exception("Phê duyệt không thành công");
      }
      hdr.setTrangThai(statusReq.getTrangThai());
    }else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_TONG_CUC)){
      String status = hdr.getTrangThai() + statusReq.getTrangThai();
      switch (status) {
        case Contains.DANG_DUYET_CB_VU + Contains.CHODUYET_LDV:
        case Contains.DADUYET_LDC + Contains.CHODUYET_LDV:
          break;
        case Contains.CHODUYET_LDV + Contains.CHODUYET_LDTC:
          hdr.setIdLdv(currentUser.getUser().getId());
          hdr.setNgayDuyetLdv(LocalDate.now());
          break;
        case Contains.CHODUYET_LDTC + Contains.DADUYET_LDTC:
        case Contains.CHODUYET_LDTC + Contains.TUCHOI_LDTC:
          hdr.setIdLdtc(currentUser.getUser().getId());
          hdr.setNgayDuyetLdtc(LocalDate.now());
          hdr.setLyDoTuChoi(statusReq.getLyDoTuChoi());
          break;
        case Contains.DADUYET_LDTC + Contains.CHO_DUYET_BTC:
        case Contains.CHO_DUYET_BTC + Contains.DA_DUYET_BTC:
          break;
        case Contains.CHODUYET_LDV + Contains.TUCHOI_LDV:
        case Contains.DA_DUYET_LDC + Contains.TU_CHOI_CBV:
        case Contains.DANG_DUYET_CB_VU + Contains.TU_CHOI_CBV:
        case Contains.CHO_DUYET_BTC + Contains.TU_CHOI_BTC:
          hdr.setLyDoTuChoi(statusReq.getLyDoTuChoi());
          break;
        default:
          throw new Exception("Phê duyệt không thành công");
      }
      hdr.setTrangThai(statusReq.getTrangThai());
    }

    XhThHoSoHdr created = hdrRepository.save(optional.get());
    return created;
  }


  public void export(CustomUserDetails currentUser, XhThHoSoRequest objReq, HttpServletResponse response) throws Exception {
    PaggingReq paggingReq = new PaggingReq();
    paggingReq.setPage(0);
    paggingReq.setLimit(Integer.MAX_VALUE);
    objReq.setPaggingReq(paggingReq);
    Page<XhThHoSoHdr> page = this.searchPage(currentUser, objReq);
    List<XhThHoSoHdr> data = page.getContent();

    String title = "Danh sách quyết định thanh lý hàng DTQG ";
    String[] rowsName = new String[]{"STT", "Số quyết định", "Trích yếu", "Ngày ký",
        "Hồ sơ đề nghị thanh lý", "Trạng thái"};
    String fileName = "danh-sach-quyet-dinh-thanh-ly-hang-dtqg.xlsx";
    List<Object[]> dataList = new ArrayList<Object[]>();
    Object[] objs = null;
    for (int i = 0; i < data.size(); i++) {
      XhThHoSoHdr qd = data.get(i);
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

  public ReportTemplateResponse preview(HashMap<String, Object> requestParams, CustomUserDetails currentUser) throws Exception {
    if (currentUser == null || requestParams == null) {
      throw new Exception("Bad request.");
    }
    try {
      String templateName = DataUtils.safeToString(requestParams.get("tenBaoCao"));
      String templatePath = "xuattieuhuy/" + templateName;
      FileInputStream templateInputStream = new FileInputStream(baseReportFolder + templatePath);
      XhThHoSoHdr data = this.detail(DataUtils.safeToLong(requestParams.get("id")));
      List<Map<String, Object>> detail = data.getChildren().stream().collect(Collectors.groupingBy(item -> item.getXhThDanhSachHdr().getTenChiCuc(), Collectors.collectingAndThen(Collectors.toList(), item1 -> {
        Map<String, Object> newData = new HashMap<>();
        newData.put("name", item1.get(0).getXhThDanhSachHdr().getTenChiCuc());
        newData.put("child", item1);
        return newData;
      }))).values().stream().collect(Collectors.toList());
      return docxToPdfConverter.convertDocxToPdf(templateInputStream, data, detail);
    } catch (IOException | XDocReportException exception) {
      exception.printStackTrace();
    }
    return null;
  }
}
