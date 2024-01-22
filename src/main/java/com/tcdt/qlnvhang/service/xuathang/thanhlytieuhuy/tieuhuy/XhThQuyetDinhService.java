package com.tcdt.qlnvhang.service.xuathang.thanhlytieuhuy.tieuhuy;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.tieuhuy.XhThHoSoHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.tieuhuy.XhThQuyetDinhRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlHoSoReq;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlQuyetDinhHdrReq;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.tieuhuy.SearchXhThQuyetDinh;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.tieuhuy.XhThQuyetDinhHdrReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.service.xuathang.thanhlytieuhuy.thanhly.XhTlHoSoService;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScQuyetDinhSc;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlQuyetDinhHdr;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.tieuhuy.XhThHoSoHdr;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.tieuhuy.XhThQuyetDinhHdr;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
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
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class XhThQuyetDinhService extends BaseServiceImpl {


  @Autowired
  private XhThQuyetDinhRepository hdrRepository;

  @Autowired
  private XhThHoSoHdrRepository xhThHoSoHdrRepository;

  @Autowired
  private XhThHoSoService xhThHoSoService;

  @Autowired
  private FileDinhKemService fileDinhKemService;

  public Page<XhThQuyetDinhHdr> searchPage(CustomUserDetails currentUser, SearchXhThQuyetDinh req) throws Exception {
    String dvql = currentUser.getDvql();
    req.setDvql(dvql);
    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
    Page<XhThQuyetDinhHdr> search = hdrRepository.search(req, pageable);
    Map<String, Map<String, Object>> mapDmucDvi = getListDanhMucDviObject(null, null, "01");

    Map<String, String> mapVthh = getListDanhMucHangHoa();
    search.getContent().forEach(s -> {
//      if (mapDmucDvi.containsKey((s.getMaDvi()))) {
//        Map<String, Object> objDonVi = mapDmucDvi.get(s.getMaDvi());
//        s.setTenDvi(objDonVi.get("tenDvi").toString());
//      }
//      s.setTenTrangThai(TrangThaiAllEnum.getLabelById(s.getTrangThai()));
    });
    return search;
  }

  public List<XhThQuyetDinhHdr> dsTaoBaoCaoTieuHuy(XhThQuyetDinhHdrReq req) throws Exception {
    UserInfo currentUser = SecurityContextService.getUser();
    if (currentUser == null){
      throw new Exception("Access denied.");
    }
    req.setTrangThai(TrangThaiAllEnum.BAN_HANH.getId());
    List<XhThQuyetDinhHdr> list = hdrRepository.listTaoBaoCaoTieuHuy(req);
    return list;
  }


  @Transactional
  public XhThQuyetDinhHdr save( XhThQuyetDinhHdrReq req) throws Exception {
    UserInfo currentUser = SecurityContextService.getUser();
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    XhThQuyetDinhHdr hdr = new XhThQuyetDinhHdr();
    validateData(req);
    BeanUtils.copyProperties(req, hdr);
    hdr.setMaDvi(currentUser.getDvql());
    hdr.setTrangThai(NhapXuatHangTrangThaiEnum.DUTHAO.getId());
    XhThQuyetDinhHdr created = hdrRepository.save(hdr);
    List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(req.getFileCanCuReq(), created.getId(), XhThQuyetDinhHdr.TABLE_NAME + "_CAN_CU");
    created.setFileCanCu(canCu);
    List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReq(), created.getId(), XhThQuyetDinhHdr.TABLE_NAME + "_DINH_KEM");
    created.setFileDinhKem(fileDinhKem);

    if (!DataUtils.isNullObject(req.getIdHoSo())) {
      Optional<XhThHoSoHdr> hoSo = xhThHoSoHdrRepository.findById(req.getIdHoSo());
      if (hoSo.isPresent()) {
        hoSo.get().setIdQd(created.getId());
        hoSo.get().setSoQd(created.getSoQd());
        xhThHoSoHdrRepository.save(hoSo.get());
      }
    }

    return created;
  }

  void validateData(XhThQuyetDinhHdrReq req) throws Exception {
    Optional<XhThQuyetDinhHdr> bySoQd = hdrRepository.findBySoQd(req.getSoQd());
    if(bySoQd.isPresent()){
      if(ObjectUtils.isEmpty(req.getId())){
        throw new Exception("Số quyết định " + bySoQd.get().getSoQd() +" đã tồn tại");
      }else{
        if(!req.getId().equals(bySoQd.get().getId())){
          throw new Exception("Số quyết định " + bySoQd.get().getSoQd() +" đã tồn tại");
        }
      }
    }
  }

  @Transactional
  public XhThQuyetDinhHdr update( XhThQuyetDinhHdrReq req) throws Exception {
    UserInfo currentUser = SecurityContextService.getUser();
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    Optional<XhThQuyetDinhHdr> optional = hdrRepository.findById(req.getId());
    if (!optional.isPresent()){
      throw new Exception("Bản ghi không tồn tại");
    }
    validateData(req);
    XhThQuyetDinhHdr hdr = optional.get();
    BeanUtils.copyProperties(req, hdr);
    XhThQuyetDinhHdr created = hdrRepository.save(hdr);
    fileDinhKemService.delete(req.getId(), Lists.newArrayList(XhThQuyetDinhHdr.TABLE_NAME + "_CAN_CU"));
    List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(req.getFileCanCuReq(), created.getId(), XhThQuyetDinhHdr.TABLE_NAME + "_CAN_CU");
    created.setFileCanCu(canCu);
    fileDinhKemService.delete(req.getId(), Lists.newArrayList(XhThQuyetDinhHdr.TABLE_NAME + "_TAI_LIEU_DINH_KEM"));
    List<FileDinhKem> fileDinhKemList = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKemReq(), created.getId(), XhThQuyetDinhHdr.TABLE_NAME + "_DINH_KEM");
    created.setFileDinhKem(fileDinhKemList);
    if (!DataUtils.isNullObject(req.getIdHoSo())) {
      Optional<XhThHoSoHdr> hoSo = xhThHoSoHdrRepository.findById(req.getIdHoSo());
      if (hoSo.isPresent()) {
        hoSo.get().setIdQd(created.getId());
        hoSo.get().setSoQd(created.getSoQd());
        xhThHoSoHdrRepository.save(hoSo.get());
      }
    }
    return created;
  }


  public XhThQuyetDinhHdr detail(Long id) throws Exception {
    Optional<XhThQuyetDinhHdr> optional = hdrRepository.findById(id);
    if (!optional.isPresent()){
      throw new Exception("Không tìm thấy dữ liệu");
    }
    XhThQuyetDinhHdr data = optional.get();
    List<FileDinhKem> canCu = fileDinhKemService.search(data.getId(), Collections.singleton(XhThQuyetDinhHdr.TABLE_NAME + "_CAN_CU"));
    data.setFileCanCu(canCu);

    List<FileDinhKem> fileDinhKemList = fileDinhKemService.search(data.getId(), Collections.singleton(XhThQuyetDinhHdr.TABLE_NAME + "_DINH_KEM"));
    data.setFileDinhKem(fileDinhKemList);

    data.setXhThHoSoHdr(xhThHoSoService.detail(data.getIdHoSo()));
    return data;
  }

  @Transactional
  public void delete(IdSearchReq idSearchReq) throws Exception {
    Optional<XhThQuyetDinhHdr> optional = hdrRepository.findById(idSearchReq.getId());
    if (!optional.isPresent()) {
      throw new Exception("Bản ghi không tồn tại");
    }
    XhThQuyetDinhHdr data = optional.get();

    if (!DataUtils.isNullObject(data.getIdHoSo())) {
      Optional<XhThHoSoHdr> hoSo = xhThHoSoHdrRepository.findById(data.getIdHoSo());
      if (hoSo.isPresent()) {
        hoSo.get().setIdQd(null);
        hoSo.get().setSoQd(null);
        xhThHoSoHdrRepository.save(hoSo.get());
      }
    }

    fileDinhKemService.delete(data.getId(), Lists.newArrayList(XhThQuyetDinhHdr.TABLE_NAME + "_CAN_CU"));
    fileDinhKemService.delete(data.getId(), Lists.newArrayList(XhThQuyetDinhHdr.TABLE_NAME));
    hdrRepository.delete(data);

  }

  @Transactional
  public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
    List<XhThQuyetDinhHdr> list = hdrRepository.findAllByIdIn(idSearchReq.getIdList());

    if (list.isEmpty()) {
      throw new Exception("Bản ghi không tồn tại");
    }

    List<Long> listHoSo = list.stream().map(XhThQuyetDinhHdr::getIdHoSo).collect(Collectors.toList());
    List<XhThHoSoHdr> listObjQdPd = xhThHoSoHdrRepository.findByIdIn(listHoSo);
    listObjQdPd.forEach(s -> {
      s.setIdQd(null);
      s.setSoQd(null);
    });
    xhThHoSoHdrRepository.saveAll(listObjQdPd);

    fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(XhThQuyetDinhHdr.TABLE_NAME));
    fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(XhThQuyetDinhHdr.TABLE_NAME + "_CAN_CU"));
    hdrRepository.deleteAll(list);

  }


  public XhThQuyetDinhHdr approve(StatusReq statusReq) throws Exception {

    if (StringUtils.isEmpty(statusReq.getId())) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    Optional<XhThQuyetDinhHdr> optional = hdrRepository.findById(Long.valueOf(statusReq.getId()));
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    }

    String status = statusReq.getTrangThai() + optional.get().getTrangThai();
    switch (status) {
      case Contains.CHODUYET_LDV + Contains.DUTHAO:
      case Contains.CHODUYET_LDTC + Contains.CHODUYET_LDV:
      case Contains.CHODUYET_LDV + Contains.TUCHOI_LDV:
      case Contains.CHODUYET_LDV + Contains.TUCHOI_LDTC:
//        optional.get().setNguoiGduyetId(currentUser.getUser().getId());
//        optional.get().setNgayGduyet(LocalDate.now());
        break;
      case Contains.TUCHOI_LDV + Contains.CHODUYET_LDV:
      case Contains.TUCHOI_LDTC + Contains.CHODUYET_LDTC:
//        optional.get().setNguoiPduyetId(currentUser.getUser().getId());
//        optional.get().setNgayPduyet(LocalDate.now());
        optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
        break;
      case Contains.BAN_HANH + Contains.CHODUYET_LDTC:
//        optional.get().setNguoiPduyetId(currentUser.getUser().getId());
//        optional.get().setNgayPduyet(LocalDate.now());
        break;
      default:
        throw new Exception("Phê duyệt không thành công");
    }
    optional.get().setTrangThai(statusReq.getTrangThai());
    XhThQuyetDinhHdr created = hdrRepository.save(optional.get());
    return created;
  }


  public void export(CustomUserDetails currentUser, SearchXhThQuyetDinh objReq, HttpServletResponse response) throws Exception {
    PaggingReq paggingReq = new PaggingReq();
    paggingReq.setPage(0);
    paggingReq.setLimit(Integer.MAX_VALUE);
    objReq.setPaggingReq(paggingReq);
    Page<XhThQuyetDinhHdr> page = this.searchPage(currentUser, objReq);
    List<XhThQuyetDinhHdr> data = page.getContent();

    String title = "Danh sách quyết định tiêu hủy hàng DTQG ";
    String[] rowsName = new String[]{"STT", "Số quyết định", "Trích yếu", "Ngày ký",
        "Hồ sơ đề nghị thanh lý", "Trạng thái"};
    String fileName = "danh-sach-quyet-dinh-tieu-huy-hang-dtqg.xlsx";
    List<Object[]> dataList = new ArrayList<Object[]>();
    Object[] objs = null;
    for (int i = 0; i < data.size(); i++) {
      XhThQuyetDinhHdr qd = data.get(i);
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
  }
  public ReportTemplateResponse preview(HashMap<String, Object> requestParams, CustomUserDetails currentUser) throws Exception {
    if (currentUser == null || requestParams == null) {
      throw new Exception("Bad request.");
    }
    try {
      String templateName = DataUtils.safeToString(requestParams.get("tenBaoCao"));
      String templatePath = "xuattieuhuy/" + templateName;
      FileInputStream templateInputStream = new FileInputStream(baseReportFolder + templatePath);
      XhThQuyetDinhHdr data = this.detail(DataUtils.safeToLong(requestParams.get("id")));
      List<Map<String, Object>> detail = data.getXhThHoSoHdr().getChildren().stream().collect(Collectors.groupingBy(item -> item.getXhThDanhSachHdr().getTenChiCuc(), Collectors.collectingAndThen(Collectors.toList(), item1 -> {
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
