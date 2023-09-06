package com.tcdt.qlnvhang.service.xuathang.xuatcuutrovientroxuatcap.xuatcap;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcap.XhXuatCapPhieuKnClDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcap.XhXuatCapPhieuKnClHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcap.SearchXhXuatCapPhieuKnCl;
import com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcap.XhXuatCapPhieuKnClDtlReq;
import com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcap.XhXuatCapPhieuKnClHdrReq;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcap.XhXuatCapPhieuKnClDtl;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcap.XhXuatCapPhieuKnClHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtPhieuKnClHdr;
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
public class XhXuatCapPhieuKnClService extends BaseServiceImpl {


  @Autowired
  private XhXuatCapPhieuKnClHdrRepository xhCtvtPhieuKtClHdrRepository;

  @Autowired
  private XhXuatCapPhieuKnClDtlRepository xhCtvtPhieuKtClDtlRepository;
  @Autowired
  private FileDinhKemService fileDinhKemService;

  public Page<XhXuatCapPhieuKnClHdr> searchPage(CustomUserDetails currentUser, SearchXhXuatCapPhieuKnCl req) throws Exception {
    String dvql = currentUser.getDvql();
    if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CHI_CUC)) {
      req.setDvql(dvql.substring(0, 6));
      req.setTrangThai(TrangThaiAllEnum.DA_DUYET_LDC.getId());
    } else {
      req.setDvql(dvql);
    }
    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
    Page<XhXuatCapPhieuKnClHdr> search = xhCtvtPhieuKtClHdrRepository.search(req, pageable);
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
      if (mapVthh.get((s.getLoaiVthh())) != null) {
        s.setTenLoaiVthh(mapVthh.get(s.getLoaiVthh()));
      }
      if (mapVthh.get((s.getCloaiVthh())) != null) {
        s.setTenCloaiVthh(mapVthh.get(s.getCloaiVthh()));
      }
      s.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(s.getTrangThai()));
    });
    return search;
  }

  @Transactional
  public XhXuatCapPhieuKnClHdr save(CustomUserDetails currentUser, XhXuatCapPhieuKnClHdrReq objReq) throws Exception{
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    Optional<XhXuatCapPhieuKnClHdr> optional = xhCtvtPhieuKtClHdrRepository.findBySoPhieuKnCl(objReq.getSoPhieuKnCl());
    if(optional.isPresent()){
      throw new Exception("số phiếu kiểm nghiệm chất lượng đã tồn tại");
    }
    XhXuatCapPhieuKnClHdr data = new XhXuatCapPhieuKnClHdr();
    BeanUtils.copyProperties(objReq, data);
    data.setMaDvi(currentUser.getUser().getDepartment());
    data.setTrangThai(Contains.DUTHAO);
    XhXuatCapPhieuKnClHdr created=xhCtvtPhieuKtClHdrRepository.save(data);

    List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), created.getId(), XhXuatCapPhieuKnClHdr.TABLE_NAME );
    created.setFileDinhKems(fileDinhKems);

    this.saveCtiet(created.getId(),objReq);
    return created;
  }

  @Transactional()
  void saveCtiet(Long idHdr, XhXuatCapPhieuKnClHdrReq objReq) {
    for (XhXuatCapPhieuKnClDtlReq ketQuaPhanTichReq : objReq.getKetQuaPhanTich()) {
      XhXuatCapPhieuKnClDtl ketQuaPhanTich =new XhXuatCapPhieuKnClDtl();
      BeanUtils.copyProperties(ketQuaPhanTichReq,ketQuaPhanTich);
      ketQuaPhanTich.setId(null);
      ketQuaPhanTich.setIdHdr(idHdr);
      xhCtvtPhieuKtClDtlRepository.save(ketQuaPhanTich);
    }
  }

  @Transactional
  public XhXuatCapPhieuKnClHdr update(CustomUserDetails currentUser, XhXuatCapPhieuKnClHdrReq objReq) throws Exception{
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    Optional<XhXuatCapPhieuKnClHdr> optional = xhCtvtPhieuKtClHdrRepository.findById(objReq.getId());
    if (!optional.isPresent()){
      throw new Exception("Không tìm thấy dữ liệu cần sửa");
    }
    Optional<XhXuatCapPhieuKnClHdr> soDx = xhCtvtPhieuKtClHdrRepository.findBySoPhieuKnCl(objReq.getSoPhieuKnCl());
    if (soDx.isPresent()){
      if (!soDx.get().getId().equals(objReq.getId())){
        throw new Exception("số phiếu kiểm nghiệm chất lượng đã tồn tại");
      }
    }
    XhXuatCapPhieuKnClHdr data = optional.get();
    BeanUtils.copyProperties(objReq,data);
    XhXuatCapPhieuKnClHdr created=xhCtvtPhieuKtClHdrRepository.save(data);

    fileDinhKemService.delete(objReq.getId(), Lists.newArrayList( XhXuatCapPhieuKnClHdr.TABLE_NAME));
    List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), created.getId(), XhXuatCapPhieuKnClHdr.TABLE_NAME );
    created.setFileDinhKems(fileDinhKems);

    List<XhXuatCapPhieuKnClDtl> listketQuaPhanTich = xhCtvtPhieuKtClDtlRepository.findByIdHdr(objReq.getId());
    xhCtvtPhieuKtClDtlRepository.deleteAll(listketQuaPhanTich);
    this.saveCtiet(created.getId(),objReq);
    return created;
  }


  public List<XhXuatCapPhieuKnClHdr> detail(List<Long> ids) throws Exception {
    if (DataUtils.isNullOrEmpty(ids))
      throw new Exception("Tham số không hợp lệ.");
    List<XhXuatCapPhieuKnClHdr> optional = xhCtvtPhieuKtClHdrRepository.findByIdIn(ids);
    if (DataUtils.isNullOrEmpty(optional)){
      throw new Exception("Không tìm thấy dữ liệu");
    }
    Map<String, Map<String, Object>> mapDmucDvi = getListDanhMucDviObject(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    List<XhXuatCapPhieuKnClHdr> allById = xhCtvtPhieuKtClHdrRepository.findAllById(ids);
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
      data.setTenLoaiVthh(mapVthh.get(data.getLoaiVthh()));
      data.setTenCloaiVthh(mapVthh.get(data.getCloaiVthh()));
      data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
      List<FileDinhKem> fileDinhKems = fileDinhKemService.search(data.getId(), Arrays.asList(XhXuatCapPhieuKnClHdr.TABLE_NAME));
      data.setFileDinhKems(fileDinhKems);


      List<XhXuatCapPhieuKnClDtl> list = xhCtvtPhieuKtClDtlRepository.findByIdHdr(data.getId());
      data.setKetQuaPhanTich(list);
    });

    return allById;
  }

  @Transient
  public void delete(IdSearchReq idSearchReq) throws Exception{
    Optional<XhXuatCapPhieuKnClHdr> optional= xhCtvtPhieuKtClHdrRepository.findById(idSearchReq.getId());
    if (!optional.isPresent()){
      throw new Exception("Bản ghi không tồn tại");
    }
    XhXuatCapPhieuKnClHdr data = optional.get();
    List<XhXuatCapPhieuKnClDtl> list = xhCtvtPhieuKtClDtlRepository.findByIdHdr(data.getId());
    xhCtvtPhieuKtClDtlRepository.deleteAll(list);
    fileDinhKemService.delete(data.getId(), Lists.newArrayList(XhXuatCapPhieuKnClHdr.TABLE_NAME ));
    fileDinhKemService.delete(data.getId(), Lists.newArrayList(XhXuatCapPhieuKnClHdr.TABLE_NAME + "_CAN_CU"));
    fileDinhKemService.delete(data.getId(), Lists.newArrayList(XhXuatCapPhieuKnClHdr.TABLE_NAME + "_NIEM_PHONG"));
    xhCtvtPhieuKtClHdrRepository.delete(data);
  }

  @Transactional
  public void deleteMulti(IdSearchReq idSearchReq) throws Exception{
    List<XhXuatCapPhieuKnClHdr> list= xhCtvtPhieuKtClHdrRepository.findAllByIdIn(idSearchReq.getIdList());

    if (list.isEmpty()){
      throw new Exception("Bản ghi không tồn tại");
    }
    List<Long> listId = list.stream().map(XhXuatCapPhieuKnClHdr::getId).collect(Collectors.toList());
    List<XhXuatCapPhieuKnClDtl> listKetQua = xhCtvtPhieuKtClDtlRepository.findAllByIdHdrIn(listId);
    xhCtvtPhieuKtClDtlRepository.deleteAll(listKetQua);
    fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(XhXuatCapPhieuKnClHdr.TABLE_NAME ));

    xhCtvtPhieuKtClHdrRepository.deleteAll(list);
  }

  @Transient
  public XhXuatCapPhieuKnClHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {

    if (StringUtils.isEmpty(statusReq.getId())) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    Optional<XhXuatCapPhieuKnClHdr> optional = xhCtvtPhieuKtClHdrRepository.findById(Long.valueOf(statusReq.getId()));
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    }

    String status = statusReq.getTrangThai() + optional.get().getTrangThai();
    switch (status){
      case Contains.CHODUYET_TP + Contains.DUTHAO:
      case Contains.CHODUYET_LDC + Contains.CHODUYET_TP:
      case Contains.CHODUYET_TP + Contains.TUCHOI_TP:
      case Contains.CHODUYET_TP + Contains.TUCHOI_LDC:
        optional.get().setNguoiGduyetId(currentUser.getUser().getId());
        optional.get().setNgayGduyet(LocalDate.now());
        break;
      case Contains.TUCHOI_TP + Contains.CHODUYET_TP:
      case Contains.TUCHOI_LDC + Contains.CHODUYET_LDC:
        optional.get().setNguoiPduyetId(currentUser.getUser().getId());
        optional.get().setNgayPduyet(LocalDate.now());
        optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
        break;
      case Contains.DADUYET_LDC + Contains.CHODUYET_LDC:
        optional.get().setNguoiPduyetId(currentUser.getUser().getId());
        optional.get().setNgayPduyet(LocalDate.now());
        break;
      default:
        throw new Exception("Phê duyệt không thành công");
    }
    optional.get().setTrangThai(statusReq.getTrangThai());
    XhXuatCapPhieuKnClHdr created = xhCtvtPhieuKtClHdrRepository.save(optional.get());
    return created;
  }

  public  void export(CustomUserDetails currentUser , SearchXhXuatCapPhieuKnCl objReq, HttpServletResponse response) throws Exception{
    PaggingReq paggingReq = new PaggingReq();
    paggingReq.setPage(0);
    paggingReq.setLimit(Integer.MAX_VALUE);
    objReq.setPaggingReq(paggingReq);
    Page<XhXuatCapPhieuKnClHdr> page=this.searchPage(currentUser,objReq);
    List<XhXuatCapPhieuKnClHdr> data=page.getContent();

    String title="Danh sách phiếu kiểm nghiệm chất lượng ";
    String[] rowsName=new String[]{"STT","Số QĐ giao nhiệm vụ XH","Năm KH","Thời hạn xuất CT,VT","Số phiếu KNCL","Ngày kiểm nghiệm",
        "Điểm kho","Lô kho","Số BB tịnh kho","Trạng thái"};
    String fileName="danh-sach-phieu-kiem-nghiem-chat-luong.xlsx";
    List<Object[]> dataList = new ArrayList<Object[]>();
    Object[] objs=null;
    for (int i=0;i<data.size();i++){
      XhXuatCapPhieuKnClHdr dx=data.get(i);
      objs=new Object[rowsName.length];
      objs[0]=i;
      objs[1]=dx.getSoQdGiaoNvXh();
      objs[2]=dx.getNam();
      objs[3]=dx.getThoiHanXuatCtVt();
      objs[4]=dx.getSoPhieuKnCl();
      objs[5]=dx.getNgayLapPhieu();
      objs[6]=dx.getTenDiemKho();
      objs[7]=dx.getTenLoKho();
      objs[8]=dx.getSoBbTinhKho();
      objs[9]=dx.getTenTrangThai();
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
      List<XhXuatCapPhieuKnClHdr> detail = this.detail(Arrays.asList(DataUtils.safeToLong(body.get("id"))));
      return docxToPdfConverter.convertDocxToPdf(inputStream, detail.get(0));
    } catch (IOException e) {
      e.printStackTrace();
    } catch (XDocReportException e) {
      e.printStackTrace();
    }
    return null;
  }
}
