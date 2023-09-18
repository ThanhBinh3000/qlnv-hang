package com.tcdt.qlnvhang.service.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtBbTinhKhoDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtBbTinhKhoHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.SearchXhCtvtBbTinhKho;
import com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtBbTinhKhoHdrReq;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtBangKeHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtBbTinhKhoHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtBbTinhKhoDtl;
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

import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class XhCtvtBbTinhKhoService extends BaseServiceImpl {



  @Autowired
  private XhCtvtBbTinhKhoHdrRepository xhCtvtBbTinhKhoHdrRepository;

  @Autowired
  private XhCtvtBbTinhKhoDtlRepository xhCtvtBbTinhKhoDtlRepository;

  @Autowired
  private UserInfoRepository userInfoRepository;

  @Autowired
  private FileDinhKemService fileDinhKemService;

  public Page<XhCtvtBbTinhKhoHdr> searchPage(CustomUserDetails currentUser, SearchXhCtvtBbTinhKho req) throws Exception {
    req.setDvql(currentUser.getDvql());
    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
    Page<XhCtvtBbTinhKhoHdr> search = xhCtvtBbTinhKhoHdrRepository.search(req, pageable);
    Map<String, Map<String, Object>> mapDmucDvi = getListDanhMucDviObject(null, null, "01");

    Map<String, String> mapVthh = getListDanhMucHangHoa();
    search.getContent().forEach(s -> {
      if (mapDmucDvi.containsKey((s.getMaDvi()))) {
        Map<String, Object> objDonVi = mapDmucDvi.get(s.getMaDvi());
        s.setTenDvi(objDonVi.get("tenDvi").toString());
      }
      if (mapDmucDvi.containsKey(s.getMaDiemKho())) {
        s.setTenDiemKho(mapDmucDvi.get(s.getMaDiemKho()).get("tenDvi").toString());
      }
      if (mapDmucDvi.containsKey(s.getMaNhaKho())) {
        s.setTenNhaKho(mapDmucDvi.get(s.getMaNhaKho()).get("tenDvi").toString());
      }
      if (mapDmucDvi.containsKey(s.getMaNganKho())) {
        s.setTenNganKho(mapDmucDvi.get(s.getMaNganKho()).get("tenDvi").toString());
      }
      if (mapDmucDvi.containsKey(s.getMaLoKho())) {
        s.setTenLoKho(mapDmucDvi.get(s.getMaLoKho()).get("tenDvi").toString());
      }
      s.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(s.getTrangThai()));
      s.setListPhieuXuatKho(xhCtvtBbTinhKhoDtlRepository.findByIdHdr(s.getId()));
    });
    return search;
  }

  @Transactional
  public XhCtvtBbTinhKhoHdr save(CustomUserDetails currentUser, XhCtvtBbTinhKhoHdrReq objReq) throws Exception{
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    Optional<XhCtvtBbTinhKhoHdr> optional = xhCtvtBbTinhKhoHdrRepository.findBySoBbTinhKho(objReq.getSoBbTinhKho());
    if(optional.isPresent()){
      throw new Exception("số biên bản đã tồn tại");
    }
    XhCtvtBbTinhKhoHdr data = new XhCtvtBbTinhKhoHdr();
    BeanUtils.copyProperties(objReq, data);
    data.setMaDvi(currentUser.getUser().getDepartment());
    data.setTrangThai(Contains.DUTHAO);
    XhCtvtBbTinhKhoHdr created=xhCtvtBbTinhKhoHdrRepository.save(data);
    List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), created.getId(), XhCtvtBbTinhKhoHdr.TABLE_NAME );
    created.setFileDinhKems(fileDinhKems);
    this.saveCtiet(created.getId(),objReq);
    return created;
  }
  
  @Transactional()
  public void saveCtiet(Long idHdr, XhCtvtBbTinhKhoHdrReq objReq) {
    for (XhCtvtBbTinhKhoDtl listPhieuXuatKhoReq : objReq.getListPhieuXuatKho()) {
      XhCtvtBbTinhKhoDtl listPhieuXuatKho =new XhCtvtBbTinhKhoDtl();
      BeanUtils.copyProperties(listPhieuXuatKhoReq,listPhieuXuatKho);
//      listPhieuXuatKho.setId(null);
      listPhieuXuatKho.setIdHdr(idHdr);
      xhCtvtBbTinhKhoDtlRepository.save(listPhieuXuatKho);
    }
  }
  
  @Transactional
  public XhCtvtBbTinhKhoHdr update( CustomUserDetails currentUser,XhCtvtBbTinhKhoHdrReq objReq) throws Exception{
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    Optional<XhCtvtBbTinhKhoHdr> optional = xhCtvtBbTinhKhoHdrRepository.findById(objReq.getId());
    if (!optional.isPresent()){
      throw new Exception("Không tìm thấy dữ liệu cần sửa");
    }
    Optional<XhCtvtBbTinhKhoHdr> soDx = xhCtvtBbTinhKhoHdrRepository.findBySoBbTinhKho(objReq.getSoBbTinhKho());
    if (soDx.isPresent()){
      if (!soDx.get().getId().equals(objReq.getId())){
        throw new Exception("số biên bản đã tồn tại");
      }
    }
    XhCtvtBbTinhKhoHdr data = optional.get();
    BeanUtils.copyProperties(objReq,data,"listPhieuXuatKho","maDvi");
    XhCtvtBbTinhKhoHdr created=xhCtvtBbTinhKhoHdrRepository.save(data);
    fileDinhKemService.delete(objReq.getId(), Lists.newArrayList( XhCtvtBbTinhKhoHdr.TABLE_NAME));
    List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), created.getId(), XhCtvtBbTinhKhoHdr.TABLE_NAME );
    created.setFileDinhKems(fileDinhKems);
//    List<XhCtvtBbTinhKhoDtl> list = xhCtvtBbTinhKhoDtlRepository.findByIdHdr(data.getId());
//    xhCtvtBbTinhKhoDtlRepository.deleteAll(list);
    this.saveCtiet(created.getId(),objReq);
    return created;
  }


  public List<XhCtvtBbTinhKhoHdr> detail(List<Long> ids) throws Exception {
    if (DataUtils.isNullOrEmpty(ids))
      throw new Exception("Tham số không hợp lệ.");
    List<XhCtvtBbTinhKhoHdr> optional = xhCtvtBbTinhKhoHdrRepository.findByIdIn(ids);
    if (DataUtils.isNullOrEmpty(optional)){
      throw new Exception("Không tìm thấy dữ liệu");
    }
    Map<String, Map<String, Object>> mapDmucDvi = getListDanhMucDviObject(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    List<XhCtvtBbTinhKhoHdr> allById = xhCtvtBbTinhKhoHdrRepository.findAllById(ids);
    allById.forEach(data -> {
      if (mapDmucDvi.containsKey(data.getMaDvi())) {
        data.setTenDvi(mapDmucDvi.get(data.getMaDvi()).get("tenDvi").toString());
        data.setTenDviCha(mapDmucDvi.get(data.getMaDvi().substring(0,data.getMaDvi().length()-2)).get("tenDvi").toString());
        data.setDiaChiDvi(mapDmucDvi.get(data.getMaDvi()).get("diaChi").toString());
      }
      if (mapDmucDvi.containsKey(data.getMaDiemKho())) {
        data.setTenDiemKho(mapDmucDvi.get(data.getMaDiemKho()).get("tenDvi").toString());
      }
      if (mapDmucDvi.containsKey(data.getMaNhaKho())) {
        data.setTenNhaKho(mapDmucDvi.get(data.getMaNhaKho()).get("tenDvi").toString());
      }
      if (mapDmucDvi.containsKey(data.getMaNganKho())) {
        data.setTenNganKho(mapDmucDvi.get(data.getMaNganKho()).get("tenDvi").toString());
      }
      if (mapDmucDvi.containsKey(data.getMaLoKho())) {
        data.setTenLoKho(mapDmucDvi.get(data.getMaLoKho()).get("tenDvi").toString());
      }
      if (data.getNguoiPduyetId() != null) {
        data.setLdChiCuc(ObjectUtils.isEmpty(data.getNguoiPduyetId()) ? null : userInfoRepository.findById(data.getNguoiPduyetId()).get().getFullName());
      }
      if (mapVthh.get((data.getLoaiVthh())) != null) {
        data.setTenLoaiVthh(mapVthh.get(data.getLoaiVthh()));
      }
      if (mapVthh.get((data.getCloaiVthh())) != null) {
        data.setTenCloaiVthh(mapVthh.get(data.getCloaiVthh()));
      }
      data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
      List<FileDinhKem> fileDinhKems = fileDinhKemService.search(data.getId(), Arrays.asList(XhCtvtBbTinhKhoHdr.TABLE_NAME));
      data.setFileDinhKems(fileDinhKems);

      List<XhCtvtBbTinhKhoDtl> list = xhCtvtBbTinhKhoDtlRepository.findByIdHdr(data.getId());
      data.setListPhieuXuatKho(list);
    });

    return allById;
  }

  @Transient
  public void delete(IdSearchReq idSearchReq) throws Exception{
    Optional<XhCtvtBbTinhKhoHdr> optional= xhCtvtBbTinhKhoHdrRepository.findById(idSearchReq.getId());
    if (!optional.isPresent()){
      throw new Exception("Bản ghi không tồn tại");
    }
    XhCtvtBbTinhKhoHdr data = optional.get();
    fileDinhKemService.delete(data.getId(), Lists.newArrayList(XhCtvtBbTinhKhoHdr.TABLE_NAME ));
    List<XhCtvtBbTinhKhoDtl> list = xhCtvtBbTinhKhoDtlRepository.findByIdHdr(data.getId());
    xhCtvtBbTinhKhoDtlRepository.deleteAll(list);
    xhCtvtBbTinhKhoHdrRepository.delete(data);
  }

  @Transactional
  public void deleteMulti(IdSearchReq idSearchReq) throws Exception{
    List<XhCtvtBbTinhKhoHdr> list= xhCtvtBbTinhKhoHdrRepository.findAllByIdIn(idSearchReq.getIdList());

    if (list.isEmpty()){
      throw new Exception("Bản ghi không tồn tại");
    }
    List<Long> listId = list.stream().map(XhCtvtBbTinhKhoHdr::getId).collect(Collectors.toList());
    List<XhCtvtBbTinhKhoDtl> listKetQua = xhCtvtBbTinhKhoDtlRepository.findAllByIdHdrIn(listId);
    xhCtvtBbTinhKhoDtlRepository.deleteAll(listKetQua);
    fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(XhCtvtBbTinhKhoHdr.TABLE_NAME ));
    xhCtvtBbTinhKhoHdrRepository.deleteAll(list);
  }

  @Transient
  public XhCtvtBbTinhKhoHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {

    if (StringUtils.isEmpty(statusReq.getId())) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    Optional<XhCtvtBbTinhKhoHdr> optional = xhCtvtBbTinhKhoHdrRepository.findById(Long.valueOf(statusReq.getId()));
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    }

    String status = statusReq.getTrangThai() + optional.get().getTrangThai();
    switch (status){
      case Contains.CHODUYET_KTVBQ + Contains.DUTHAO:
      case Contains.CHODUYET_KT + Contains.CHODUYET_KTVBQ:
      case Contains.CHODUYET_LDCC + Contains.CHODUYET_KT:
      case Contains.CHODUYET_KTVBQ + Contains.TUCHOI_KTVBQ:
      case Contains.CHODUYET_KTVBQ + Contains.TUCHOI_KT:
      case Contains.CHODUYET_KTVBQ + Contains.TUCHOI_LDCC:
        optional.get().setNguoiGduyetId(currentUser.getUser().getId());
        optional.get().setNgayGduyet(LocalDate.now());
        break;
      case Contains.TUCHOI_KTVBQ + Contains.CHODUYET_KTVBQ:
      case Contains.TUCHOI_KT + Contains.CHODUYET_KT:
      case Contains.TUCHOI_LDCC + Contains.CHODUYET_LDCC:
        optional.get().setNguoiPduyetId(currentUser.getUser().getId());
        optional.get().setNgayPduyet(LocalDate.now());
        optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
        break;
      case Contains.DADUYET_LDCC + Contains.CHODUYET_LDCC:
        optional.get().setNguoiPduyetId(currentUser.getUser().getId());
        optional.get().setNgayPduyet(LocalDate.now());
        break;
      default:
        throw new Exception("Phê duyệt không thành công");
    }
    optional.get().setTrangThai(statusReq.getTrangThai());
    XhCtvtBbTinhKhoHdr created = xhCtvtBbTinhKhoHdrRepository.save(optional.get());
    return created;
  }

  public  void export(CustomUserDetails currentUser ,SearchXhCtvtBbTinhKho objReq, HttpServletResponse response) throws Exception{
    PaggingReq paggingReq = new PaggingReq();
    paggingReq.setPage(0);
    paggingReq.setLimit(Integer.MAX_VALUE);
    objReq.setPaggingReq(paggingReq);
    Page<XhCtvtBbTinhKhoHdr> page=this.searchPage(currentUser,objReq);
    List<XhCtvtBbTinhKhoHdr> data=page.getContent();

    String title="Danh sách biên bản tịnh kho ";
    String[] rowsName=new String[]{"STT","Số QĐ giao nhiệm vụ XH","Năm KH","Thời hạn XH trước ngày","Số BB tịnh kho",
        "Ngày bắt đầu","Ngày kết thúc xuất","Điểm kho", "Lô kho","Số phiếu XK","Ngày xuất kho","Số bảng kê","Trạng thái"};
    String fileName="danh-sach-bien-ban-tinh-kho.xlsx";
    List<Object[]> dataList = new ArrayList<Object[]>();
    Object[] objs=null;
    for (int i=0;i<data.size();i++){
      XhCtvtBbTinhKhoHdr dx=data.get(i);
      objs=new Object[rowsName.length];
      objs[0]=i;
      objs[1]=dx.getSoQdGiaoNvXh();
      objs[2]=dx.getNam();
      objs[3]=dx.getNgayQdGiaoNvXh();
      objs[4]=dx.getSoBbTinhKho();
      objs[5]=dx.getNgayBatDauXuat();
      objs[6]=dx.getNgayKetThucXuat();
      objs[7]=dx.getTenDiemKho();
      objs[8]=dx.getTenLoKho();
      Object[] finalObjs = objs;
      dx.getListPhieuXuatKho().forEach(s ->{
        finalObjs[9]=s.getSoPhieuXuatKho();
        finalObjs[10]=s.getNgayXuatKho();
        finalObjs[11]=s.getSoBkCanHang();
      });
      objs[12]=dx.getTenTrangThai();
      dataList.add(objs);
    }
    ExportExcel ex =new ExportExcel(title,fileName,rowsName,dataList,response);
    ex.export();
  }

  public ReportTemplateResponse preview(HashMap<String, Object> body) throws Exception {
    try {
      String fileName = DataUtils.safeToString(body.get("tenBaoCao"));
      String fileTemplate = "xuatcuutrovientro/" + fileName;
      FileInputStream inputStream = new FileInputStream(baseReportFolder + fileTemplate);
      List<XhCtvtBbTinhKhoHdr> detail = this.detail(Arrays.asList(DataUtils.safeToLong(body.get("id"))));
      return docxToPdfConverter.convertDocxToPdf(inputStream, detail.get(0));
    } catch (IOException e) {
      e.printStackTrace();
    } catch (XDocReportException e) {
      e.printStackTrace();
    }
    return null;
  }
}
