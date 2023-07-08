package com.tcdt.qlnvhang.service.xuathang.daugia.xuatkho;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.entities.xuathang.daugia.xuatkho.XhDgBbHaoDoiDtl;
import com.tcdt.qlnvhang.entities.xuathang.daugia.xuatkho.XhDgBbHaoDoiHdr;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.xuatkho.XhDgBbHaoDoiDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.xuatkho.XhDgBbHaoDoiHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.xuatkho.SearchXhDgBbHaoDoi;
import com.tcdt.qlnvhang.request.xuathang.daugia.xuatkho.XhDgBbHaoDoiDtlReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.xuatkho.XhDgBbHaoDoiHdrReq;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
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
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class XhDgBbHaoDoiService extends BaseServiceImpl {



  @Autowired
  private XhDgBbHaoDoiHdrRepository xhDgBbHaoDoiHdrRepository;

  @Autowired
  private XhDgBbHaoDoiDtlRepository xhDgBbHaoDoiDtlRepository;

  @Autowired
  private UserInfoRepository userInfoRepository;

  @Autowired
  private FileDinhKemService fileDinhKemService;

  public Page<XhDgBbHaoDoiHdr> searchPage(CustomUserDetails currentUser, SearchXhDgBbHaoDoi req) throws Exception {
    req.setDvql(currentUser.getDvql());
    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
    Page<XhDgBbHaoDoiHdr> search = xhDgBbHaoDoiHdrRepository.search(req, pageable);
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
      s.setListPhieuXuatKho(xhDgBbHaoDoiDtlRepository.findByIdHdr(s.getId()));
    });
    return search;
  }

  @Transactional
  public XhDgBbHaoDoiHdr save(CustomUserDetails currentUser, XhDgBbHaoDoiHdrReq objReq) throws Exception{
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    Optional<XhDgBbHaoDoiHdr> optional = xhDgBbHaoDoiHdrRepository.findBySoBbHaoDoi(objReq.getSoBbHaoDoi());
    if(optional.isPresent()){
      throw new Exception("số biên bản đã tồn tại");
    }
    XhDgBbHaoDoiHdr data = new XhDgBbHaoDoiHdr();
    BeanUtils.copyProperties(objReq, data);
    data.setIdThuKho(currentUser.getUser().getId());
    data.setMaDvi(currentUser.getUser().getDepartment());
    data.setTrangThai(Contains.DUTHAO);
    XhDgBbHaoDoiHdr created=xhDgBbHaoDoiHdrRepository.save(data);
    List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), created.getId(), XhDgBbHaoDoiHdr.TABLE_NAME );
    created.setFileDinhKems(fileDinhKems);
    this.saveCtiet(created.getId(),objReq);
    return created;
  }

  @Transactional()
  void saveCtiet(Long idHdr, XhDgBbHaoDoiHdrReq objReq) {
    for (XhDgBbHaoDoiDtlReq listPhieuXuatKhoReq : objReq.getListPhieuXuatKho()) {
      XhDgBbHaoDoiDtl listPhieuXuatKho =new XhDgBbHaoDoiDtl();
      BeanUtils.copyProperties(listPhieuXuatKhoReq,listPhieuXuatKho);
      listPhieuXuatKho.setId(null);
      listPhieuXuatKho.setIdHdr(idHdr);
      xhDgBbHaoDoiDtlRepository.save(listPhieuXuatKho);
    }
  }

  @Transactional
  public XhDgBbHaoDoiHdr update( CustomUserDetails currentUser,XhDgBbHaoDoiHdrReq objReq) throws Exception{
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    Optional<XhDgBbHaoDoiHdr> optional = xhDgBbHaoDoiHdrRepository.findById(objReq.getId());
    if (!optional.isPresent()){
      throw new Exception("Không tìm thấy dữ liệu cần sửa");
    }
    Optional<XhDgBbHaoDoiHdr> soDx = xhDgBbHaoDoiHdrRepository.findBySoBbHaoDoi(objReq.getSoBbHaoDoi());
    if (soDx.isPresent()){
      if (!soDx.get().getId().equals(objReq.getId())){
        throw new Exception("số biên bản đã tồn tại");
      }
    }
    XhDgBbHaoDoiHdr data = optional.get();
    BeanUtils.copyProperties(objReq,data);
    data.setIdThuKho(currentUser.getUser().getId());
    XhDgBbHaoDoiHdr created=xhDgBbHaoDoiHdrRepository.save(data);
    fileDinhKemService.delete(objReq.getId(), Lists.newArrayList( XhDgBbHaoDoiHdr.TABLE_NAME));
    List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), created.getId(), XhDgBbHaoDoiHdr.TABLE_NAME );
    created.setFileDinhKems(fileDinhKems);
    List<XhDgBbHaoDoiDtl> list = xhDgBbHaoDoiDtlRepository.findByIdHdr(data.getId());
    xhDgBbHaoDoiDtlRepository.deleteAll(list);
    this.saveCtiet(created.getId(),objReq);
    return created;
  }


  public List<XhDgBbHaoDoiHdr> detail(List<Long> ids) throws Exception {
    if (DataUtils.isNullOrEmpty(ids))
      throw new Exception("Tham số không hợp lệ.");
    List<XhDgBbHaoDoiHdr> optional = xhDgBbHaoDoiHdrRepository.findByIdIn(ids);
    if (DataUtils.isNullOrEmpty(optional)){
      throw new Exception("Không tìm thấy dữ liệu");
    }
    Map<String, Map<String, Object>> mapDmucDvi = getListDanhMucDviObject(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    List<XhDgBbHaoDoiHdr> allById = xhDgBbHaoDoiHdrRepository.findAllById(ids);
    allById.forEach(data -> {
      if (mapDmucDvi.containsKey(data.getMaDvi())) {
        data.setTenDvi(mapDmucDvi.get(data.getMaDvi()).get("tenDvi").toString());
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
      if(data.getIdThuKho() != null){
        data.setTenThuKho(ObjectUtils.isEmpty(data.getIdThuKho())? null : userInfoRepository.findById(data.getIdThuKho()).get().getFullName());
      }
      if (data.getIdKtvBaoQuan() != null){
        data.setTenKtvBaoQuan(ObjectUtils.isEmpty(data.getIdKtvBaoQuan()) ? null : userInfoRepository.findById(data.getIdKtvBaoQuan()).get().getFullName());
      }
      if (data.getIdKeToan() != null){
        data.setTenKeToan(ObjectUtils.isEmpty(data.getIdKeToan()) ? null : userInfoRepository.findById(data.getIdKeToan()).get().getFullName());
      }
      if (data.getNguoiPduyetId() != null) {
        data.setTenLanhDaoChiCuc(ObjectUtils.isEmpty(data.getNguoiPduyetId()) ? null : userInfoRepository.findById(data.getNguoiPduyetId()).get().getFullName());
      }
      data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
      List<FileDinhKem> fileDinhKems = fileDinhKemService.search(data.getId(), Arrays.asList(XhDgBbHaoDoiHdr.TABLE_NAME));
      data.setFileDinhKems(fileDinhKems);

      List<XhDgBbHaoDoiDtl> list = xhDgBbHaoDoiDtlRepository.findByIdHdr(data.getId());
      data.setListPhieuXuatKho(list);
    });

    return allById;
  }

  @Transient
  public void delete(IdSearchReq idSearchReq) throws Exception{
    Optional<XhDgBbHaoDoiHdr> optional= xhDgBbHaoDoiHdrRepository.findById(idSearchReq.getId());
    if (!optional.isPresent()){
      throw new Exception("Bản ghi không tồn tại");
    }
    XhDgBbHaoDoiHdr data = optional.get();
    fileDinhKemService.delete(data.getId(), Lists.newArrayList(XhDgBbHaoDoiHdr.TABLE_NAME ));
    List<XhDgBbHaoDoiDtl> list = xhDgBbHaoDoiDtlRepository.findByIdHdr(data.getId());
    xhDgBbHaoDoiDtlRepository.deleteAll(list);
    xhDgBbHaoDoiHdrRepository.delete(data);
  }

  @Transient
  public void deleteMulti(IdSearchReq idSearchReq) throws Exception{
    List<XhDgBbHaoDoiHdr> list= xhDgBbHaoDoiHdrRepository.findAllByIdIn(idSearchReq.getIdList());

    if (list.isEmpty()){
      throw new Exception("Bản ghi không tồn tại");
    }
    List<Long> listId = list.stream().map(XhDgBbHaoDoiHdr::getId).collect(Collectors.toList());
    List<XhDgBbHaoDoiDtl> listKetQua = xhDgBbHaoDoiDtlRepository.findAllByIdHdrIn(listId);
    xhDgBbHaoDoiDtlRepository.deleteAll(listKetQua);
    fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(XhDgBbHaoDoiHdr.TABLE_NAME ));
    xhDgBbHaoDoiHdrRepository.deleteAll(list);
  }

  @Transient
  public XhDgBbHaoDoiHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {

    if (StringUtils.isEmpty(statusReq.getId())) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    Optional<XhDgBbHaoDoiHdr> optional = xhDgBbHaoDoiHdrRepository.findById(Long.valueOf(statusReq.getId()));
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    }

    String status = statusReq.getTrangThai() + optional.get().getTrangThai();
    switch (status){
      case Contains.CHODUYET_KTVBQ + Contains.DUTHAO:
      case Contains.CHODUYET_KTVBQ + Contains.TUCHOI_KTVBQ:
      case Contains.CHODUYET_KTVBQ + Contains.TUCHOI_KT:
      case Contains.CHODUYET_KTVBQ + Contains.TUCHOI_LDCC:
        optional.get().setNguoiGduyetId(currentUser.getUser().getId());
        optional.get().setNgayGduyet(LocalDate.now());
        break;
      case Contains.CHODUYET_KT + Contains.CHODUYET_KTVBQ:
        optional.get().setIdKtvBaoQuan(currentUser.getUser().getId());
        optional.get().setNgayPduyetKtvBq(LocalDate.now());
        break;
      case Contains.CHODUYET_LDCC + Contains.CHODUYET_KT:
        optional.get().setIdKeToan(currentUser.getUser().getId());
        optional.get().setNgayPduyetKt(LocalDate.now());
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
    XhDgBbHaoDoiHdr created = xhDgBbHaoDoiHdrRepository.save(optional.get());
    return created;
  }

  public  void export(CustomUserDetails currentUser ,SearchXhDgBbHaoDoi objReq, HttpServletResponse response) throws Exception{
    PaggingReq paggingReq = new PaggingReq();
    paggingReq.setPage(0);
    paggingReq.setLimit(Integer.MAX_VALUE);
    objReq.setPaggingReq(paggingReq);
    Page<XhDgBbHaoDoiHdr> page=this.searchPage(currentUser,objReq);
    List<XhDgBbHaoDoiHdr> data=page.getContent();

    String title="Danh sách biên bản hao dôi ";
    String[] rowsName=new String[]{"STT","Số QĐ giao nhiệm vụ XH","Năm KH","Thời hạn XH trước ngày","Số BB hao dôi","Sô BB tịnh kho",
        "Ngày bắt đầu","Ngày kết thúc xuất","Điểm kho", "Lô kho","Số bảng kê","Số phiếu XK","Ngày xuất kho","Trạng thái"};
    String fileName="danh-sach-bien-ban-hao-doi.xlsx";
    List<Object[]> dataList = new ArrayList<Object[]>();
    Object[] objs=null;
    for (int i=0;i<data.size();i++){
      XhDgBbHaoDoiHdr dx=data.get(i);
      objs=new Object[rowsName.length];
      objs[0]=i;
      objs[1]=dx.getSoQdGiaoNvXh();
      objs[2]=dx.getNam();
      objs[3]=dx.getNgayQdGiaoNvXh();
      objs[4]=dx.getSoBbHaoDoi();
      objs[5]=dx.getSoBbTinhKho();
//      objs[6]=dx.getNgayBatDauXuat();
//      objs[7]=dx.getNgayKetThucXuat();
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
}