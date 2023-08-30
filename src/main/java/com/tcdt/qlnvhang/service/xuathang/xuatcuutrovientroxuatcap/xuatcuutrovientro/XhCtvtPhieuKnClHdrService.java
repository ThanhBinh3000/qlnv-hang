package com.tcdt.qlnvhang.service.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.entities.xuathang.daugia.tochuctrienkhai.ketqua.XhKqBdgHdr;
import com.tcdt.qlnvhang.entities.xuathang.daugia.tochuctrienkhai.thongtin.XhTcTtinBdgDtl;
import com.tcdt.qlnvhang.entities.xuathang.daugia.tochuctrienkhai.thongtin.XhTcTtinBdgHdr;
import com.tcdt.qlnvhang.entities.xuathang.daugia.tochuctrienkhai.thongtin.XhTcTtinBdgPlo;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtPhieuKnClDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtPhieuKnClHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.SearchXhCtvtPhieuKnCl;
import com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtPhieuKnClDtlReq;
import com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtPhieuKnClHdrReq;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.table.report.ReportTemplateRequest;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtPhieuKnClDtl;
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
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class XhCtvtPhieuKnClHdrService extends BaseServiceImpl {


  @Autowired
  private XhCtvtPhieuKnClHdrRepository xhCtvtPhieuKtClHdrRepository;

  @Autowired
  private XhCtvtPhieuKnClDtlRepository xhCtvtPhieuKtClDtlRepository;
  @Autowired
  private FileDinhKemService fileDinhKemService;

  public Page<XhCtvtPhieuKnClHdr> searchPage(CustomUserDetails currentUser, SearchXhCtvtPhieuKnCl req) throws Exception {
    String dvql = currentUser.getDvql();
    if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CHI_CUC)) {
      req.setDvql(dvql.substring(0, 6));
    } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
      req.setDvql(dvql);
    }
    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
    Page<XhCtvtPhieuKnClHdr> search = xhCtvtPhieuKtClHdrRepository.search(req, pageable);
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
  public XhCtvtPhieuKnClHdr save(CustomUserDetails currentUser, XhCtvtPhieuKnClHdrReq objReq) throws Exception{
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    Optional<XhCtvtPhieuKnClHdr> optional = xhCtvtPhieuKtClHdrRepository.findBySoPhieu(objReq.getSoPhieu());
    if(optional.isPresent()){
      throw new Exception("số phiếu kiểm nghiệm chất lượng đã tồn tại");
    }
    XhCtvtPhieuKnClHdr data = new XhCtvtPhieuKnClHdr();
    BeanUtils.copyProperties(objReq, data);
    data.setMaDvi(currentUser.getUser().getDepartment());
    data.setTrangThai(Contains.DUTHAO);
    XhCtvtPhieuKnClHdr created=xhCtvtPhieuKtClHdrRepository.save(data);

    List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), created.getId(), XhCtvtPhieuKnClHdr.TABLE_NAME );
    created.setFileDinhKems(fileDinhKems);

    this.saveCtiet(created.getId(),objReq);
    return created;
  }

  @Transactional()
  public void saveCtiet(Long idHdr, XhCtvtPhieuKnClHdrReq objReq) {
    for (XhCtvtPhieuKnClDtlReq ketQuaPhanTichReq : objReq.getKetQuaPhanTich()) {
      XhCtvtPhieuKnClDtl ketQuaPhanTich =new XhCtvtPhieuKnClDtl();
      BeanUtils.copyProperties(ketQuaPhanTichReq,ketQuaPhanTich);
      ketQuaPhanTich.setId(null);
      ketQuaPhanTich.setIdHdr(idHdr);
      xhCtvtPhieuKtClDtlRepository.save(ketQuaPhanTich);
    }
  }

  @Transactional
  public XhCtvtPhieuKnClHdr update(CustomUserDetails currentUser, XhCtvtPhieuKnClHdrReq objReq) throws Exception{
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    Optional<XhCtvtPhieuKnClHdr> optional = xhCtvtPhieuKtClHdrRepository.findById(objReq.getId());
    if (!optional.isPresent()){
      throw new Exception("Không tìm thấy dữ liệu cần sửa");
    }
    Optional<XhCtvtPhieuKnClHdr> soDx = xhCtvtPhieuKtClHdrRepository.findBySoPhieu(objReq.getSoPhieu());
    if (soDx.isPresent()){
      if (!soDx.get().getId().equals(objReq.getId())){
        throw new Exception("số phiếu kiểm nghiệm chất lượng đã tồn tại");
      }
    }
    XhCtvtPhieuKnClHdr data = optional.get();
    BeanUtils.copyProperties(objReq,data);
    XhCtvtPhieuKnClHdr created=xhCtvtPhieuKtClHdrRepository.save(data);

    fileDinhKemService.delete(objReq.getId(), Lists.newArrayList( XhCtvtPhieuKnClHdr.TABLE_NAME));
    List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), created.getId(), XhCtvtPhieuKnClHdr.TABLE_NAME );
    created.setFileDinhKems(fileDinhKems);

    List<XhCtvtPhieuKnClDtl> listketQuaPhanTich = xhCtvtPhieuKtClDtlRepository.findByIdHdr(objReq.getId());
    xhCtvtPhieuKtClDtlRepository.deleteAll(listketQuaPhanTich);
    this.saveCtiet(created.getId(),objReq);
    return created;
  }


  public List<XhCtvtPhieuKnClHdr> detail(List<Long> ids) throws Exception {
    if (DataUtils.isNullOrEmpty(ids))
      throw new Exception("Tham số không hợp lệ.");
    List<XhCtvtPhieuKnClHdr> optional = xhCtvtPhieuKtClHdrRepository.findByIdIn(ids);
    if (DataUtils.isNullOrEmpty(optional)){
      throw new Exception("Không tìm thấy dữ liệu");
    }
    Map<String, Map<String, Object>> mapDmucDvi = getListDanhMucDviObject(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    List<XhCtvtPhieuKnClHdr> allById = xhCtvtPhieuKtClHdrRepository.findAllById(ids);
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
      List<FileDinhKem> fileDinhKems = fileDinhKemService.search(data.getId(), Arrays.asList(XhCtvtPhieuKnClHdr.TABLE_NAME));
      data.setFileDinhKems(fileDinhKems);


      List<XhCtvtPhieuKnClDtl> list = xhCtvtPhieuKtClDtlRepository.findByIdHdr(data.getId());
      data.setKetQuaPhanTich(list);
    });

    return allById;
  }

  @Transient
  public void delete(IdSearchReq idSearchReq) throws Exception{
    Optional<XhCtvtPhieuKnClHdr> optional= xhCtvtPhieuKtClHdrRepository.findById(idSearchReq.getId());
    if (!optional.isPresent()){
      throw new Exception("Bản ghi không tồn tại");
    }
    XhCtvtPhieuKnClHdr data = optional.get();
    List<XhCtvtPhieuKnClDtl> list = xhCtvtPhieuKtClDtlRepository.findByIdHdr(data.getId());
    xhCtvtPhieuKtClDtlRepository.deleteAll(list);
    fileDinhKemService.delete(data.getId(), Lists.newArrayList(XhCtvtPhieuKnClHdr.TABLE_NAME ));
    fileDinhKemService.delete(data.getId(), Lists.newArrayList(XhCtvtPhieuKnClHdr.TABLE_NAME + "_CAN_CU"));
    fileDinhKemService.delete(data.getId(), Lists.newArrayList(XhCtvtPhieuKnClHdr.TABLE_NAME + "_NIEM_PHONG"));
    xhCtvtPhieuKtClHdrRepository.delete(data);
  }

  @Transactional
  public void deleteMulti(IdSearchReq idSearchReq) throws Exception{
    List<XhCtvtPhieuKnClHdr> list= xhCtvtPhieuKtClHdrRepository.findAllByIdIn(idSearchReq.getIdList());

    if (list.isEmpty()){
      throw new Exception("Bản ghi không tồn tại");
    }
    List<Long> listId = list.stream().map(XhCtvtPhieuKnClHdr::getId).collect(Collectors.toList());
    List<XhCtvtPhieuKnClDtl> listKetQua = xhCtvtPhieuKtClDtlRepository.findAllByIdHdrIn(listId);
    xhCtvtPhieuKtClDtlRepository.deleteAll(listKetQua);
    fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(XhCtvtPhieuKnClHdr.TABLE_NAME ));

    xhCtvtPhieuKtClHdrRepository.deleteAll(list);
  }

  @Transient
  public XhCtvtPhieuKnClHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {

    if (StringUtils.isEmpty(statusReq.getId())) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    Optional<XhCtvtPhieuKnClHdr> optional = xhCtvtPhieuKtClHdrRepository.findById(Long.valueOf(statusReq.getId()));
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
    XhCtvtPhieuKnClHdr created = xhCtvtPhieuKtClHdrRepository.save(optional.get());
    return created;
  }

  public  void export(CustomUserDetails currentUser , SearchXhCtvtPhieuKnCl objReq, HttpServletResponse response) throws Exception{
    PaggingReq paggingReq = new PaggingReq();
    paggingReq.setPage(0);
    paggingReq.setLimit(Integer.MAX_VALUE);
    objReq.setPaggingReq(paggingReq);
    Page<XhCtvtPhieuKnClHdr> page=this.searchPage(currentUser,objReq);
    List<XhCtvtPhieuKnClHdr> data=page.getContent();

    String title="Danh sách phiếu kiểm nghiệm chất lượng ";
    String[] rowsName=new String[]{"STT","Số QĐ giao nhiệm vụ XH","Năm KH","Thời hạn XH trước ngày","Số phiếu KNCL","Ngày kiểm nghiệm","Nhà kho","Ngăn Kho",
        "Lô kho","Số BB LM/BGM","Ngày lấy mẫu","Số BB tịnh kho","Ngày xuất dốc kho","Trạng thái"};
    String fileName="danh-sach-phieu-kiem-nghiem-chat-luong.xlsx";
    List<Object[]> dataList = new ArrayList<Object[]>();
    Object[] objs=null;
    for (int i=0;i<data.size();i++){
      XhCtvtPhieuKnClHdr dx=data.get(i);
      objs=new Object[rowsName.length];
      objs[0]=i;
      objs[1]=dx.getSoQdGiaoNvXh();
      objs[2]=dx.getNam();
      objs[3]=dx.getNgayQdGiaoNvXh();
      objs[4]=dx.getNgayKnMau();
      objs[5]=dx.getTenDiemKho();
      objs[6]=dx.getTenNhaKho();
      objs[7]=dx.getTenNganKho();
      objs[8]=dx.getTenLoKho();
      objs[9]=dx.getSoBienBan();
      objs[10]=dx.getNgayLayMau();
      objs[11]=dx.getSoBbTinhKho();
      objs[12]=dx.getNgayXuatDocKho();
      objs[13]=dx.getTenTrangThai();
      dataList.add(objs);
    }
    ExportExcel ex =new ExportExcel(title,fileName,rowsName,dataList,response);
    ex.export();
  }

  public ReportTemplateResponse preview(HashMap<String, Object> body) throws Exception {
    try {
//      ReportTemplateRequest reportTemplateRequest = new ReportTemplateRequest();
//      reportTemplateRequest.setFileName(DataUtils.safeToString(body.get("tenBaoCao")));
//      ReportTemplate model = findByTenFile(reportTemplateRequest);
//      byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
//      ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
      FileInputStream inputStream = new FileInputStream("src/main/resources/reports/xuatcuutrovientro/Phiếu kiểm nghiệm chất lượng.docx");
      List<XhCtvtPhieuKnClHdr> detail = this.detail(Arrays.asList(DataUtils.safeToLong(body.get("id"))));
      return docxToPdfConverter.convertDocxToPdf(inputStream, detail.get(0));
    } catch (IOException e) {
      e.printStackTrace();
    } catch (XDocReportException e) {
      e.printStackTrace();
    }
    return null;
  }
}
