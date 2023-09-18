package com.tcdt.qlnvhang.service.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtBbHaoDoiDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtBbHaoDoiHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.SearchXhCtvtBbHaoDoi;
import com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtBbHaoDoiDtlReq;
import com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtBbHaoDoiHdrReq;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtBbHaoDoiDtl;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtBbHaoDoiHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtBbTinhKhoHdr;
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
public class XhCtvtBbHaoDoiService extends BaseServiceImpl {



  @Autowired
  private XhCtvtBbHaoDoiHdrRepository xhCtvtBbHaoDoiHdrRepository;

  @Autowired
  private XhCtvtBbHaoDoiDtlRepository xhCtvtBbHaoDoiDtlRepository;

  @Autowired
  private UserInfoRepository userInfoRepository;

  @Autowired
  private FileDinhKemService fileDinhKemService;

  public Page<XhCtvtBbHaoDoiHdr> searchPage(CustomUserDetails currentUser, SearchXhCtvtBbHaoDoi req) throws Exception {
    req.setDvql(currentUser.getDvql());
    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
    Page<XhCtvtBbHaoDoiHdr> search = xhCtvtBbHaoDoiHdrRepository.search(req, pageable);
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
      s.setListPhieuXuatKho(xhCtvtBbHaoDoiDtlRepository.findByIdHdr(s.getId()));
    });
    return search;
  }

  @Transactional
  public XhCtvtBbHaoDoiHdr save(CustomUserDetails currentUser, XhCtvtBbHaoDoiHdrReq objReq) throws Exception{
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    Optional<XhCtvtBbHaoDoiHdr> optional = xhCtvtBbHaoDoiHdrRepository.findBySoBbHaoDoi(objReq.getSoBbHaoDoi());
    if(optional.isPresent()){
      throw new Exception("số biên bản đã tồn tại");
    }
    XhCtvtBbHaoDoiHdr data = new XhCtvtBbHaoDoiHdr();
    BeanUtils.copyProperties(objReq, data);
    data.setMaDvi(currentUser.getUser().getDepartment());
    data.setTrangThai(Contains.DUTHAO);
    XhCtvtBbHaoDoiHdr created=xhCtvtBbHaoDoiHdrRepository.save(data);
    List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), created.getId(), XhCtvtBbHaoDoiHdr.TABLE_NAME );
    created.setFileDinhKems(fileDinhKems);
    this.saveCtiet(created.getId(),objReq);
    return created;
  }

  @Transactional()
  public void saveCtiet(Long idHdr, XhCtvtBbHaoDoiHdrReq objReq) {
    for (XhCtvtBbHaoDoiDtlReq listPhieuXuatKhoReq : objReq.getListPhieuXuatKho()) {
      XhCtvtBbHaoDoiDtl listPhieuXuatKho =new XhCtvtBbHaoDoiDtl();
      BeanUtils.copyProperties(listPhieuXuatKhoReq,listPhieuXuatKho);
      listPhieuXuatKho.setId(null);
      listPhieuXuatKho.setIdHdr(idHdr);
      xhCtvtBbHaoDoiDtlRepository.save(listPhieuXuatKho);
    }
  }

  @Transactional
  public XhCtvtBbHaoDoiHdr update( CustomUserDetails currentUser,XhCtvtBbHaoDoiHdrReq objReq) throws Exception{
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    Optional<XhCtvtBbHaoDoiHdr> optional = xhCtvtBbHaoDoiHdrRepository.findById(objReq.getId());
    if (!optional.isPresent()){
      throw new Exception("Không tìm thấy dữ liệu cần sửa");
    }
    Optional<XhCtvtBbHaoDoiHdr> soDx = xhCtvtBbHaoDoiHdrRepository.findBySoBbHaoDoi(objReq.getSoBbHaoDoi());
    if (soDx.isPresent()){
      if (!soDx.get().getId().equals(objReq.getId())){
        throw new Exception("số biên bản đã tồn tại");
      }
    }
    XhCtvtBbHaoDoiHdr data = optional.get();
    BeanUtils.copyProperties(objReq,data,"maDvi");
    XhCtvtBbHaoDoiHdr created=xhCtvtBbHaoDoiHdrRepository.save(data);
    fileDinhKemService.delete(objReq.getId(), Lists.newArrayList( XhCtvtBbHaoDoiHdr.TABLE_NAME));
    List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), created.getId(), XhCtvtBbHaoDoiHdr.TABLE_NAME );
    created.setFileDinhKems(fileDinhKems);
    List<XhCtvtBbHaoDoiDtl> list = xhCtvtBbHaoDoiDtlRepository.findByIdHdr(data.getId());
    xhCtvtBbHaoDoiDtlRepository.deleteAll(list);
    this.saveCtiet(created.getId(),objReq);
    return created;
  }


  public List<XhCtvtBbHaoDoiHdr> detail(List<Long> ids) throws Exception {
    if (DataUtils.isNullOrEmpty(ids))
      throw new Exception("Tham số không hợp lệ.");
    List<XhCtvtBbHaoDoiHdr> optional = xhCtvtBbHaoDoiHdrRepository.findByIdIn(ids);
    if (DataUtils.isNullOrEmpty(optional)){
      throw new Exception("Không tìm thấy dữ liệu");
    }
    Map<String, Map<String, Object>> mapDmucDvi = getListDanhMucDviObject(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    List<XhCtvtBbHaoDoiHdr> allById = xhCtvtBbHaoDoiHdrRepository.findAllById(ids);
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
      List<FileDinhKem> fileDinhKems = fileDinhKemService.search(data.getId(), Arrays.asList(XhCtvtBbHaoDoiHdr.TABLE_NAME));
      data.setFileDinhKems(fileDinhKems);

      List<XhCtvtBbHaoDoiDtl> list = xhCtvtBbHaoDoiDtlRepository.findByIdHdr(data.getId());
      data.setListPhieuXuatKho(list);
    });

    return allById;
  }

  @Transient
  public void delete(IdSearchReq idSearchReq) throws Exception{
    Optional<XhCtvtBbHaoDoiHdr> optional= xhCtvtBbHaoDoiHdrRepository.findById(idSearchReq.getId());
    if (!optional.isPresent()){
      throw new Exception("Bản ghi không tồn tại");
    }
    XhCtvtBbHaoDoiHdr data = optional.get();
    fileDinhKemService.delete(data.getId(), Lists.newArrayList(XhCtvtBbHaoDoiHdr.TABLE_NAME ));
    List<XhCtvtBbHaoDoiDtl> list = xhCtvtBbHaoDoiDtlRepository.findByIdHdr(data.getId());
    xhCtvtBbHaoDoiDtlRepository.deleteAll(list);
    xhCtvtBbHaoDoiHdrRepository.delete(data);
  }

  @Transactional
  public void deleteMulti(IdSearchReq idSearchReq) throws Exception{
    List<XhCtvtBbHaoDoiHdr> list= xhCtvtBbHaoDoiHdrRepository.findAllByIdIn(idSearchReq.getIdList());

    if (list.isEmpty()){
      throw new Exception("Bản ghi không tồn tại");
    }
    List<Long> listId = list.stream().map(XhCtvtBbHaoDoiHdr::getId).collect(Collectors.toList());
    List<XhCtvtBbHaoDoiDtl> listKetQua = xhCtvtBbHaoDoiDtlRepository.findAllByIdHdrIn(listId);
    xhCtvtBbHaoDoiDtlRepository.deleteAll(listKetQua);
    fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(XhCtvtBbHaoDoiHdr.TABLE_NAME ));
    xhCtvtBbHaoDoiHdrRepository.deleteAll(list);
  }

  @Transient
  public XhCtvtBbHaoDoiHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {

    if (StringUtils.isEmpty(statusReq.getId())) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    Optional<XhCtvtBbHaoDoiHdr> optional = xhCtvtBbHaoDoiHdrRepository.findById(Long.valueOf(statusReq.getId()));
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
    XhCtvtBbHaoDoiHdr created = xhCtvtBbHaoDoiHdrRepository.save(optional.get());
    return created;
  }

  public  void export(CustomUserDetails currentUser ,SearchXhCtvtBbHaoDoi objReq, HttpServletResponse response) throws Exception{
    PaggingReq paggingReq = new PaggingReq();
    paggingReq.setPage(0);
    paggingReq.setLimit(Integer.MAX_VALUE);
    objReq.setPaggingReq(paggingReq);
    Page<XhCtvtBbHaoDoiHdr> page=this.searchPage(currentUser,objReq);
    List<XhCtvtBbHaoDoiHdr> data=page.getContent();

    String title="Danh sách biên bản hao dôi ";
    String[] rowsName=new String[]{"STT","Số QĐ giao nhiệm vụ XH","Năm KH","Thời hạn XH trước ngày","Số BB hao dôi","Sô BB tịnh kho",
        "Ngày bắt đầu","Ngày kết thúc xuất","Điểm kho", "Lô kho","Số bảng kê","Số phiếu XK","Ngày xuất kho","Trạng thái"};
    String fileName="danh-sach-bien-ban-hao-doi.xlsx";
    List<Object[]> dataList = new ArrayList<Object[]>();
    Object[] objs=null;
    for (int i=0;i<data.size();i++){
      XhCtvtBbHaoDoiHdr dx=data.get(i);
      objs=new Object[rowsName.length];
      objs[0]=i;
      objs[1]=dx.getSoQdGiaoNvXh();
      objs[2]=dx.getNam();
      objs[3]=dx.getNgayQdGiaoNvXh();
      objs[4]=dx.getSoBbHaoDoi();
      objs[5]=dx.getSoBbTinhKho();
      objs[6]=dx.getNgayBatDauXuat();
      objs[7]=dx.getNgayKetThucXuat();
      objs[8]=dx.getTenDiemKho();
      objs[9]=dx.getTenLoKho();
      Object[] finalObjs = objs;
      dx.getListPhieuXuatKho().forEach(s ->{
        finalObjs[10]=s.getSoBkCanHang();
        finalObjs[11]=s.getSoPhieuXuatKho();
        finalObjs[12]=s.getNgayXuatKho();
      });
      objs[13]=dx.getTenTrangThai();
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
      List<XhCtvtBbHaoDoiHdr> detail = this.detail(Arrays.asList(DataUtils.safeToLong(body.get("id"))));
      return docxToPdfConverter.convertDocxToPdf(inputStream, detail.get(0));
    } catch (IOException e) {
      e.printStackTrace();
    } catch (XDocReportException e) {
      e.printStackTrace();
    }
    return null;
  }
}