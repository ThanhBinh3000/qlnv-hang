package com.tcdt.qlnvhang.service.xuathang.xuatcuutrovientroxuatcap.xuatcap;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.QlnvDmVattuRepository;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcap.XhXuatCapPhieuKtClDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcap.XhXuatCapPhieuKtClHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.xuatcuutrovientro.XhPhieuKnclHdrPreview;
import com.tcdt.qlnvhang.request.xuathang.kiemtrachatluong.XhPhieuKnclReq;
import com.tcdt.qlnvhang.request.xuathang.kiemtrachatluong.XhPhieuKtclReq;
import com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcap.SearchXhXuatCapPhieuKtCl;
import com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcap.XhXuatCapPhieuKtClDtlReq;
import com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcap.XhXuatCapPhieuKtClHdrReq;
import com.tcdt.qlnvhang.response.xuatcuutrovientro.XhPhieuKnclDtlDto;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.xuathang.kiemtrachatluong.phieukncl.XhPhieuKnclDtl;
import com.tcdt.qlnvhang.table.xuathang.kiemtrachatluong.phieukncl.XhPhieuKnclHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcap.XhXuatCapPhieuKtClDtl;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcap.XhXuatCapPhieuKtClHdr;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import lombok.var;
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
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class XhXuatCapPhieuKtClService extends BaseServiceImpl {


  @Autowired
  private XhXuatCapPhieuKtClHdrRepository xhCtvtPhieuKtClHdrRepository;

  @Autowired
  private XhXuatCapPhieuKtClDtlRepository xhCtvtPhieuKtClDtlRepository;
  @Autowired
  private FileDinhKemService fileDinhKemService;
  @Autowired
  private QlnvDmVattuRepository qlnvDmVattuRepository;
  @Autowired
  private UserInfoRepository userInfoRepository;
  public Page<XhXuatCapPhieuKtClHdr> searchPage(CustomUserDetails currentUser, SearchXhXuatCapPhieuKtCl req) throws Exception {
    req.setDvql(currentUser.getDvql());
    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
    Page<XhXuatCapPhieuKtClHdr> search = xhCtvtPhieuKtClHdrRepository.search(req, pageable);
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
  public XhXuatCapPhieuKtClHdr save(CustomUserDetails currentUser, XhXuatCapPhieuKtClHdrReq objReq) throws Exception{
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    Optional<XhXuatCapPhieuKtClHdr> optional = xhCtvtPhieuKtClHdrRepository.findBySoPhieuKtCl(objReq.getSoPhieuKtCl());
    if(optional.isPresent()){
      throw new Exception("số phiếu kiểm nghiệm chất lượng đã tồn tại");
    }
    XhXuatCapPhieuKtClHdr data = new XhXuatCapPhieuKtClHdr();
    BeanUtils.copyProperties(objReq, data);
    data.setMaDvi(currentUser.getUser().getDepartment());
    data.setTrangThai(Contains.DUTHAO);
    XhXuatCapPhieuKtClHdr created=xhCtvtPhieuKtClHdrRepository.save(data);

    List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), created.getId(), XhXuatCapPhieuKtClHdr.TABLE_NAME );
    created.setFileDinhKems(fileDinhKems);

    this.saveCtiet(created.getId(),objReq);
    return created;
  }

  @Transactional()
  public void saveCtiet(Long idHdr, XhXuatCapPhieuKtClHdrReq objReq) {
    for (XhXuatCapPhieuKtClDtlReq ketQuaPhanTichReq : objReq.getKetQuaPhanTich()) {
      XhXuatCapPhieuKtClDtl ketQuaPhanTich =new XhXuatCapPhieuKtClDtl();
      BeanUtils.copyProperties(ketQuaPhanTichReq,ketQuaPhanTich);
      ketQuaPhanTich.setId(null);
      ketQuaPhanTich.setIdHdr(idHdr);
      xhCtvtPhieuKtClDtlRepository.save(ketQuaPhanTich);
    }
  }

  @Transactional
  public XhXuatCapPhieuKtClHdr update(CustomUserDetails currentUser, XhXuatCapPhieuKtClHdrReq objReq) throws Exception{
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    Optional<XhXuatCapPhieuKtClHdr> optional = xhCtvtPhieuKtClHdrRepository.findById(objReq.getId());
    if (!optional.isPresent()){
      throw new Exception("Không tìm thấy dữ liệu cần sửa");
    }
    Optional<XhXuatCapPhieuKtClHdr> soDx = xhCtvtPhieuKtClHdrRepository.findBySoPhieuKtCl(objReq.getSoPhieuKtCl());
    if (soDx.isPresent()){
      if (!soDx.get().getId().equals(objReq.getId())){
        throw new Exception("số phiếu kiểm nghiệm chất lượng đã tồn tại");
      }
    }
    XhXuatCapPhieuKtClHdr data = optional.get();
    BeanUtils.copyProperties(objReq,data);
    XhXuatCapPhieuKtClHdr created=xhCtvtPhieuKtClHdrRepository.save(data);

    fileDinhKemService.delete(objReq.getId(), Lists.newArrayList( XhXuatCapPhieuKtClHdr.TABLE_NAME));
    List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), created.getId(), XhXuatCapPhieuKtClHdr.TABLE_NAME );
    created.setFileDinhKems(fileDinhKems);

    List<XhXuatCapPhieuKtClDtl> listketQuaPhanTich = xhCtvtPhieuKtClDtlRepository.findByIdHdr(objReq.getId());
    xhCtvtPhieuKtClDtlRepository.deleteAll(listketQuaPhanTich);
    this.saveCtiet(created.getId(),objReq);
    return created;
  }


  public List<XhXuatCapPhieuKtClHdr> detail(List<Long> ids) throws Exception {
    if (DataUtils.isNullOrEmpty(ids))
      throw new Exception("Tham số không hợp lệ.");
    List<XhXuatCapPhieuKtClHdr> optional = xhCtvtPhieuKtClHdrRepository.findByIdIn(ids);
    if (DataUtils.isNullOrEmpty(optional)){
      throw new Exception("Không tìm thấy dữ liệu");
    }
    Map<String, Map<String, Object>> mapDmucDvi = getListDanhMucDviObject(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    List<XhXuatCapPhieuKtClHdr> allById = xhCtvtPhieuKtClHdrRepository.findAllById(ids);
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
      List<FileDinhKem> fileDinhKems = fileDinhKemService.search(data.getId(), Arrays.asList(XhXuatCapPhieuKtClHdr.TABLE_NAME));
      data.setFileDinhKems(fileDinhKems);


      List<XhXuatCapPhieuKtClDtl> list = xhCtvtPhieuKtClDtlRepository.findByIdHdr(data.getId());
      data.setKetQuaPhanTich(list);
    });

    return allById;
  }

  @Transient
  public void delete(IdSearchReq idSearchReq) throws Exception{
    Optional<XhXuatCapPhieuKtClHdr> optional= xhCtvtPhieuKtClHdrRepository.findById(idSearchReq.getId());
    if (!optional.isPresent()){
      throw new Exception("Bản ghi không tồn tại");
    }
    XhXuatCapPhieuKtClHdr data = optional.get();
    List<XhXuatCapPhieuKtClDtl> list = xhCtvtPhieuKtClDtlRepository.findByIdHdr(data.getId());
    xhCtvtPhieuKtClDtlRepository.deleteAll(list);
    fileDinhKemService.delete(data.getId(), Lists.newArrayList(XhXuatCapPhieuKtClHdr.TABLE_NAME ));
    fileDinhKemService.delete(data.getId(), Lists.newArrayList(XhXuatCapPhieuKtClHdr.TABLE_NAME + "_CAN_CU"));
    fileDinhKemService.delete(data.getId(), Lists.newArrayList(XhXuatCapPhieuKtClHdr.TABLE_NAME + "_NIEM_PHONG"));
    xhCtvtPhieuKtClHdrRepository.delete(data);
  }

  @Transactional
  public void deleteMulti(IdSearchReq idSearchReq) throws Exception{
    List<XhXuatCapPhieuKtClHdr> list= xhCtvtPhieuKtClHdrRepository.findAllByIdIn(idSearchReq.getIdList());

    if (list.isEmpty()){
      throw new Exception("Bản ghi không tồn tại");
    }
    List<Long> listId = list.stream().map(XhXuatCapPhieuKtClHdr::getId).collect(Collectors.toList());
    List<XhXuatCapPhieuKtClDtl> listKetQua = xhCtvtPhieuKtClDtlRepository.findAllByIdHdrIn(listId);
    xhCtvtPhieuKtClDtlRepository.deleteAll(listKetQua);
    fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(XhXuatCapPhieuKtClHdr.TABLE_NAME ));

    xhCtvtPhieuKtClHdrRepository.deleteAll(list);
  }

  @Transient
  public XhXuatCapPhieuKtClHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {

    if (StringUtils.isEmpty(statusReq.getId())) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    Optional<XhXuatCapPhieuKtClHdr> optional = xhCtvtPhieuKtClHdrRepository.findById(Long.valueOf(statusReq.getId()));
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    }

    String status = statusReq.getTrangThai() + optional.get().getTrangThai();
    switch (status){
      case Contains.CHODUYET_LDCC + Contains.DUTHAO:
      case Contains.CHODUYET_LDCC + Contains.TUCHOI_LDCC:
        optional.get().setNguoiGduyetId(currentUser.getUser().getId());
        optional.get().setNgayGduyet(LocalDate.now());
        break;
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
    XhXuatCapPhieuKtClHdr created = xhCtvtPhieuKtClHdrRepository.save(optional.get());
    return created;
  }

  public  void export(CustomUserDetails currentUser , SearchXhXuatCapPhieuKtCl objReq, HttpServletResponse response) throws Exception{
    PaggingReq paggingReq = new PaggingReq();
    paggingReq.setPage(0);
    paggingReq.setLimit(Integer.MAX_VALUE);
    objReq.setPaggingReq(paggingReq);
    Page<XhXuatCapPhieuKtClHdr> page=this.searchPage(currentUser,objReq);
    List<XhXuatCapPhieuKtClHdr> data=page.getContent();

    String title="Danh sách phiếu kiểm nghiệm chất lượng ";
    String[] rowsName=new String[]{"STT","Số QĐ giao nhiệm vụ XH","Năm KH","Thời hạn xuất CT,VT","Số phiếu KTCL","Ngày kiểm tra",
        "Điểm kho","Lô kho","Số BB tịnh kho","Trạng thái"};
    String fileName="danh-sach-phieu-kiem-tra-chat-luong.xlsx";
    List<Object[]> dataList = new ArrayList<Object[]>();
    Object[] objs=null;
    for (int i=0;i<data.size();i++){
      XhXuatCapPhieuKtClHdr dx=data.get(i);
      objs=new Object[rowsName.length];
      objs[0]=i;
      objs[1]=dx.getSoQdGiaoNvXh();
      objs[2]=dx.getNam();
      objs[3]=dx.getThoiHanXuatCtVt();
      objs[4]=dx.getSoPhieuKtCl();
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

  public ReportTemplateResponse preview(XhPhieuKtclReq objReq) throws Exception {
    var xhPhieuKtclHdr = xhCtvtPhieuKtClHdrRepository.findById(objReq.getId());
    List<XhXuatCapPhieuKtClDtl> list = xhCtvtPhieuKtClDtlRepository.findByIdHdr(objReq.getId());
    xhPhieuKtclHdr.get().setKetQuaPhanTich(list);
    if (!xhPhieuKtclHdr.isPresent()) throw new Exception("Không tồn tại bản ghi");
    var fileTemplate = "xuatcuutrovientro/" + "2.1.C77-HD_PhieuKiemTraChatLuong_LT.docx";
    if (StringUtils.isEmpty(xhPhieuKtclHdr.get().getLoaiVthh()))
      throw new Exception("Không tồn tại loại vật tư hàng hoá");
    var qlnvDmVattu = qlnvDmVattuRepository.findByMa(xhPhieuKtclHdr.get().getLoaiVthh());
    if (Objects.isNull(qlnvDmVattu)) throw new Exception("Không tồn tại bản ghi");
    Optional<UserInfo> userInfo = Optional.of(new UserInfo());
    if (xhPhieuKtclHdr.get().getNguoiPduyetId() != null) {
      userInfo = userInfoRepository.findById(xhPhieuKtclHdr.get().getNguoiPduyetId());
    }
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    List<XhXuatCapPhieuKtClHdr> xhPhieuKtclHdrs = new ArrayList<>();
    xhPhieuKtclHdrs.add(xhPhieuKtclHdr.get());
    xhPhieuKtclHdrs.forEach(data -> {
      data.setMapDmucDvi(mapDmucDvi);
    });
    FileInputStream inputStream = new FileInputStream(baseReportFolder + fileTemplate);
    var xhPhieuKnclHdrPreview = setDataToPreview(xhPhieuKtclHdr, userInfo);
    return docxToPdfConverter.convertDocxToPdf(inputStream, xhPhieuKnclHdrPreview);
  }

  private XhPhieuKnclHdrPreview setDataToPreview(Optional<XhXuatCapPhieuKtClHdr> xhPhieuKtclHdr, Optional<UserInfo> userInfo) throws Exception {
    return XhPhieuKnclHdrPreview.builder()
            .tenDvi(xhPhieuKtclHdr.get().getTenDvi())
            .maQhns(xhPhieuKtclHdr.get().getMaQhNs())
            .loaiVthh(qlnvDmVattuRepository.findByMa(xhPhieuKtclHdr.get().getLoaiVthh()).getTen().toUpperCase())
            .soBbQd(xhPhieuKtclHdr.get().getSoPhieuKtCl())
            .tenNganKho(xhPhieuKtclHdr.get().getTenNganKho())
            .tenLoKho(xhPhieuKtclHdr.get().getTenLoKho())
            .tenNhaKho(xhPhieuKtclHdr.get().getTenNhaKho())
            .tenDiemKho(xhPhieuKtclHdr.get().getTenDiemKho())
            .soLuongHangBaoQuan(xhPhieuKtclHdr.get().getSoLuongXuat() ==null ? "": xhPhieuKtclHdr.get().getSoLuongXuat().toString())
            .hinhThucKeLotBaoQuan("")
            .tenThuKho("")
            .ngayNhapDayKho("")
            .ngayBbLayMau(null)
            .ngayKiemNghiem(xhPhieuKtclHdr.get().getNgayLapPhieu() != null ? Contains.convertDateToString(xhPhieuKtclHdr.get().getNgayLapPhieu()) : "")
            .ketLuan(xhPhieuKtclHdr.get().getKetLuan())
            .ketQua(xhPhieuKtclHdr.get().getKetQua())
            .ngayNhap(xhPhieuKtclHdr.get().getNgayLapPhieu() != null ? String.valueOf(xhPhieuKtclHdr.get().getNgayLapPhieu().getDayOfMonth()) : "")
            .thangNhap(xhPhieuKtclHdr.get().getNgayLapPhieu() != null ? String.valueOf(xhPhieuKtclHdr.get().getNgayLapPhieu().getMonth().getValue()) : "")
            .namNhap(xhPhieuKtclHdr.get().getNgayLapPhieu() != null ? String.valueOf(xhPhieuKtclHdr.get().getNgayLapPhieu().getYear()) : "")
            .nguoiLapPhieu(xhPhieuKtclHdr.get().getNguoiTaoId() != null ? userInfoRepository.findById(xhPhieuKtclHdr.get().getNguoiTaoId()).get().getFullName() : "")
            .ktvBaoQuan("")
            .lanhDaoCuc(userInfo.isPresent() ? userInfo.get().getFullName() : "")
            .xhPhieuKnclDtlDto(convertXhPhieuKnclDtlToDto(xhPhieuKtclHdr.get().getKetQuaPhanTich()))
            .build();
  }

  private List<XhPhieuKnclDtlDto> convertXhPhieuKnclDtlToDto(List<XhXuatCapPhieuKtClDtl> xhPhieuKtclDtl) {
    List<XhPhieuKnclDtlDto> xhPhieuKnclDtlDtos = new ArrayList<>();
    int stt = 1;
    for (var res : xhPhieuKtclDtl) {
      var xhPhieuKnclDtlDto = XhPhieuKnclDtlDto.builder()
              .stt(stt++)
              .chiTieuCl(res.getTenTchuan() )
              .chiSoCl(StringUtils.isEmpty(res.getChiSoXuat()) ?res.getChiSoNhap():res.getChiSoXuat())
              .ketQua(res.getKetQuaPt())
              .phuongPhap(res.getPhuongPhap())
              .danhGia("")
              .build();
      xhPhieuKnclDtlDtos.add(xhPhieuKnclDtlDto);
    }
    return xhPhieuKnclDtlDtos;
  }
}
