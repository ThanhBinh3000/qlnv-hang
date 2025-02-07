package com.tcdt.qlnvhang.service.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.tcdt.qlnvhang.entities.FileDinhKemJoinTable;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.QlnvDmDonviRepository;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtQdPdDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtQdPdHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtQuyetDinhGnvHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.xuatcuutrovientro.XhCtvtQuyetDinhGnvHdrPreview;
import com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.SearchXhCtvtQuyetDinhGnv;
import com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtQuyetDinhGnvHdrReq;
import com.tcdt.qlnvhang.response.xuatcuutrovientro.XhCtvtQuyetDinhGnvDtlDto;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.*;
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

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.tcdt.qlnvhang.util.Contains.CAP_CHI_CUC;

@Service
public class XhCtvtQuyetDinhGnvService extends BaseServiceImpl {


  @Autowired
  private XhCtvtQuyetDinhGnvHdrRepository xhCtvtQuyetDinhGnvHdrRepository;

  @Autowired
  private XhCtvtQdPdHdrRepository xhCtvtQdPdHdrRepository;

  @Autowired
  private XhCtvtQdPdDtlRepository xhCtvtQdPdDtlRepository;
  @Autowired
  private UserInfoRepository userInfoRepository;
  @Autowired
  private QlnvDmDonviRepository qlnvDmDonviRepository;


  public Page<XhCtvtQuyetDinhGnvHdr> searchPage(CustomUserDetails currentUser, SearchXhCtvtQuyetDinhGnv objReq) throws Exception {
    if (!currentUser.getUser().getCapDvi().equals(CAP_CHI_CUC)) {
      objReq.setDvql(currentUser.getDvql());
    } else {
      objReq.setMaDviGiao(currentUser.getDvql());
    }
    if(objReq.getType() != null){
      objReq.getTypes().add(objReq.getType());
    }

    Pageable pageable = PageRequest.of(objReq.getPaggingReq().getPage(), objReq.getPaggingReq().getLimit());
    Page<XhCtvtQuyetDinhGnvHdr> data = xhCtvtQuyetDinhGnvHdrRepository.search(objReq, pageable);
    if (currentUser.getUser().getCapDvi().equals(CAP_CHI_CUC)) {
      data.getContent().stream().forEach(s->{
        List<XhCtvtQuyetDinhGnvDtl> quyetDinhGnvDtls = s.getDataDtl().stream().filter(s1 -> s1.getMaDvi().contains(currentUser.getDvql())).collect(Collectors.toList());
        if(!quyetDinhGnvDtls.isEmpty()){
          s.setTrangThaiXh(quyetDinhGnvDtls.get(0).getTrangThai());
          s.setTenTrangThaiXh(TrangThaiAllEnum.getLabelById(quyetDinhGnvDtls.get(0).getTenTrangThai()));
        }
      });
    }

    return data;
  }

  @Transactional()
  public XhCtvtQuyetDinhGnvHdr save(CustomUserDetails currentUser, XhCtvtQuyetDinhGnvHdrReq objReq) throws Exception {
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    if (DataUtils.safeToString(objReq.getSoBbQd()).split("/").length != 1) {
      Optional<XhCtvtQuyetDinhGnvHdr> optional = xhCtvtQuyetDinhGnvHdrRepository.findBySoBbQd(objReq.getSoBbQd());
      if (optional.isPresent()) {
        throw new Exception("Số quyết định đã tồn tại");
      }
    }

    XhCtvtQuyetDinhGnvHdr saveData = new XhCtvtQuyetDinhGnvHdr();
    DataUtils.copyProperties(objReq, saveData, "id");
    saveData.setMaDvi(currentUser.getUser().getDepartment());
    saveData.setTrangThai(TrangThaiAllEnum.DU_THAO.getId());
    saveData.setTrangThaiXh(TrangThaiAllEnum.CHUA_THUC_HIEN.getId());
    XhCtvtQuyetDinhGnvHdr created = xhCtvtQuyetDinhGnvHdrRepository.save(saveData);

    List<Long> listIdQdPdDtl = objReq.getDataDtl().stream().map(XhCtvtQuyetDinhGnvDtl::getIdQdPdDtl).collect(Collectors.toList());
    if (!DataUtils.isNullObject(created)) {
      Optional<XhCtvtQuyetDinhPdHdr> quyetDinhPd = xhCtvtQdPdHdrRepository.findById(created.getIdQdPd());
      quyetDinhPd.get().getQuyetDinhPdDtl().forEach(s -> {
        if (listIdQdPdDtl.contains(s.getId())) {
          s.setIdQdGnv(created.getId());
          s.setSoQdGnv(created.getSoBbQd());
        }
      });
      xhCtvtQdPdHdrRepository.save(quyetDinhPd.get());
    }
    return created;
  }


  @Transactional()
  public XhCtvtQuyetDinhGnvHdr update(CustomUserDetails currentUser, XhCtvtQuyetDinhGnvHdrReq objReq) throws Exception {
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    Optional<XhCtvtQuyetDinhGnvHdr> optional = xhCtvtQuyetDinhGnvHdrRepository.findById(objReq.getId());
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu cần sửa");
    }
    if (DataUtils.safeToString(objReq.getSoBbQd()).split("/").length != 1) {
      Optional<XhCtvtQuyetDinhGnvHdr> soBbQd = xhCtvtQuyetDinhGnvHdrRepository.findBySoBbQd(objReq.getSoBbQd());
      if (soBbQd.isPresent()) {
        if (!soBbQd.get().getId().equals(objReq.getId())) {
          throw new Exception("số quyết định đã tồn tại");
        }
      }
    }

    XhCtvtQuyetDinhGnvHdr data = optional.get();
    BeanUtils.copyProperties(objReq, data, "id", "maDvi");
    if(3 == Integer.parseInt(currentUser.getUser().getCapDvi())){
      List<XhCtvtQuyetDinhGnvDtl> quyetDinhGnvDtls = data.getDataDtl().stream().filter(s -> s.getMaDvi().contains(currentUser.getDvql())).collect(Collectors.toList());
      List<XhCtvtQuyetDinhGnvDtl> dinhGnvDtls = quyetDinhGnvDtls.stream().filter(s -> DataUtils.safeToString(s.getTrangThai()).equals(TrangThaiAllEnum.DA_HOAN_THANH.getId())).collect(Collectors.toList());
      if(!dinhGnvDtls.isEmpty()){
        data.setTrangThaiXh(TrangThaiAllEnum.DANG_THUC_HIEN.getId());
      }
    }

    Boolean alledMatch = data.getDataDtl().stream().allMatch(s -> DataUtils.safeToString(s.getTrangThai()).equals(TrangThaiAllEnum.DA_HOAN_THANH.getId()));
    if (alledMatch != null && alledMatch) {
      data.setTrangThaiXh(TrangThaiAllEnum.DA_HOAN_THANH.getId());
    }
    XhCtvtQuyetDinhGnvHdr created = xhCtvtQuyetDinhGnvHdrRepository.save(data);
    return created;
  }

  public List<XhCtvtQuyetDinhGnvHdr> detail(List<Long> ids) throws Exception {
    if (StringUtils.isEmpty(ids)) throw new Exception("Tham số không hợp lệ.");
    List<XhCtvtQuyetDinhGnvHdr> optional = xhCtvtQuyetDinhGnvHdrRepository.findByIdIn(ids);
    if (DataUtils.isNullOrEmpty(optional)) throw new Exception("Không tìm thấy dữ liệu");
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    Map<String, Map<String, Object>> mapVthh = getListDanhMucHangHoaObject();
    List<XhCtvtQuyetDinhGnvHdr> allById = xhCtvtQuyetDinhGnvHdrRepository.findAllById(ids);
    allById.forEach(data -> {
      String tenDvi = mapDmucDvi.containsKey(data.getMaDvi()) ? mapDmucDvi.get(data.getMaDvi()) : null;
      String tenDviCha = mapDmucDvi.containsKey(org.apache.velocity.util.StringUtils.chop(data.getMaDvi(),2)) ? mapDmucDvi.get(org.apache.velocity.util.StringUtils.chop(data.getMaDvi(),2)) : null;
      data.setTenDvi(tenDvi);
      data.setTenDviCha(tenDviCha);
      data.getDataDtl().forEach(s -> {
        s.setMapDmucDvi(mapDmucDvi);
        s.setMapVthh(mapVthh);
      });
    });
    UserInfo user = getUser();
    if (user.getCapDvi().equals(CAP_CHI_CUC)) {
      allById.stream().forEach(s->{
        List<XhCtvtQuyetDinhGnvDtl> quyetDinhGnvDtls = s.getDataDtl().stream().filter(s1 -> s1.getMaDvi().contains(user.getDvql())).collect(Collectors.toList());
        if(!quyetDinhGnvDtls.isEmpty()){
          s.setTrangThaiXh(quyetDinhGnvDtls.get(0).getTrangThai());
          s.setTenTrangThaiXh(TrangThaiAllEnum.getLabelById(quyetDinhGnvDtls.get(0).getTenTrangThai()));
        }
      });
    }
    return allById;
  }


  @Transactional
  public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
    if (StringUtils.isEmpty(idSearchReq.getIdList())) throw new Exception("Xóa thất bại, không tìm thấy dữ liệu");
    List<XhCtvtQuyetDinhPdDtl> listQdPdDtl = xhCtvtQdPdDtlRepository.findByIdQdGnvIn(idSearchReq.getIdList());
    listQdPdDtl.forEach(s -> {
      s.setIdQdGnv(null);
      s.setSoQdGnv(null);
    });

    xhCtvtQdPdDtlRepository.saveAll(listQdPdDtl);
    xhCtvtQuyetDinhGnvHdrRepository.deleteAllByIdIn(idSearchReq.getIdList());
  }

  public void export(CustomUserDetails currentUser, SearchXhCtvtQuyetDinhGnv searchReq, HttpServletResponse response) throws Exception {
    PaggingReq paggingReq = new PaggingReq();
    paggingReq.setPage(0);
    paggingReq.setLimit(Integer.MAX_VALUE);
    searchReq.setPaggingReq(paggingReq);
    Page<XhCtvtQuyetDinhGnvHdr> page = this.searchPage(currentUser, searchReq);
    List<XhCtvtQuyetDinhGnvHdr> data = page.getContent();

    String title = "Danh sách tổng hợp phương án xuất cứu trợ, viện trợ";
    String[] rowsName = new String[]{"STT", "Năm KH", "Mã Tổng hợp", "Ngày tổng hợp", "Số quyết định", "Ngày kí quyết định", "Loại hàng hóa", "Tổng SL xuất viện trợ, cứu trợ (kg)", "SL xuất CT,VT chuyển xuất cấp", "Nội dung tổng hợp", "Trạng thái"};
    String filename = "danh-sach-tong-hop-phuong-an-cuu-tro-vien-tro.xlsx";

    List<Object[]> dataList = new ArrayList<Object[]>();
    Object[] objs = null;
    for (int i = 0; i < data.size(); i++) {
      XhCtvtQuyetDinhGnvHdr dx = data.get(i);
      objs = new Object[rowsName.length];
      objs[0] = i;
     /* objs[1] = dx.getNam();
      objs[2] = dx.getMaTongHop();
      objs[3] = dx.getNgayThop();
      objs[4] = dx.getSoQdPd();
      objs[5] = dx.getNgayKyQd();
      objs[6] = dx.getTenLoaiVthh();

      objs[9] = dx.getNoiDungThop();
      objs[10] = dx.getTenTrangThai();*/
      dataList.add(objs);
    }

    ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
    ex.export();
  }


  public XhCtvtQuyetDinhGnvHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
    if (StringUtils.isEmpty(statusReq.getId())) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    Optional<XhCtvtQuyetDinhGnvHdr> optional = xhCtvtQuyetDinhGnvHdrRepository.findById(Long.valueOf(statusReq.getId()));
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    } else {
      XhCtvtQuyetDinhGnvHdr data = optional.get();
      String status = data.getTrangThai() + statusReq.getTrangThai();
      if (status.equals(TrangThaiAllEnum.DU_THAO.getId() + TrangThaiAllEnum.CHO_DUYET_TP.getId()) || status.equals(TrangThaiAllEnum.TU_CHOI_TP.getId() + TrangThaiAllEnum.CHO_DUYET_TP.getId()) || status.equals(TrangThaiAllEnum.TU_CHOI_LDC.getId() + TrangThaiAllEnum.CHO_DUYET_TP.getId())) {
        data.setNguoiGduyetId(currentUser.getUser().getId());
        data.setNgayGduyet(LocalDate.now());
      } else if (status.equals(TrangThaiAllEnum.CHO_DUYET_TP.getId() + TrangThaiAllEnum.TU_CHOI_TP.getId()) || status.equals(TrangThaiAllEnum.CHO_DUYET_LDC.getId() + TrangThaiAllEnum.TU_CHOI_LDC.getId())) {
        data.setNguoiPduyetId(currentUser.getUser().getId());
        data.setNgayPduyet(LocalDate.now());
        data.setLyDoTuChoi(statusReq.getLyDoTuChoi());
      } else if (status.equals(TrangThaiAllEnum.CHO_DUYET_TP.getId() + TrangThaiAllEnum.CHO_DUYET_LDC.getId()) || status.equals(TrangThaiAllEnum.CHO_DUYET_LDC.getId() + TrangThaiAllEnum.BAN_HANH.getId())) {
        data.setNguoiPduyetId(currentUser.getUser().getId());
        data.setIdLanhDao(currentUser.getUser().getId());
        data.setNgayPduyet(LocalDate.now());
      } else {
        throw new Exception("Phê duyệt không thành công");
      }
      data.setTrangThai(statusReq.getTrangThai());
      XhCtvtQuyetDinhGnvHdr created = xhCtvtQuyetDinhGnvHdrRepository.save(data);
      return created;
    }
  }

    public ReportTemplateResponse preview(XhCtvtQuyetDinhGnvHdrReq objReq) throws Exception {
      var xhCtvtQuyetDinhGnvHdr = xhCtvtQuyetDinhGnvHdrRepository.findById(objReq.getId());
      if (!xhCtvtQuyetDinhGnvHdr.isPresent()) throw new Exception("Không tồn tại bản ghi");
      var fileTemplate = "";
      var checkTypeVT = false;
      Optional<UserInfo> userInfo = Optional.of(new UserInfo());
      if (xhCtvtQuyetDinhGnvHdr.get().getIdLanhDao() != null) {
        userInfo = userInfoRepository.findById(xhCtvtQuyetDinhGnvHdr.get().getIdLanhDao());
      }
      if (StringUtils.isEmpty(xhCtvtQuyetDinhGnvHdr.get().getTenVthh())) throw new Exception("Không tồn tại loại hàng vật tư hàng hoá");
      if (!StringUtils.isEmpty(xhCtvtQuyetDinhGnvHdr.get().getTenVthh())) {
        if (xhCtvtQuyetDinhGnvHdr.get().getTenVthh().equals("Vật tư thiết bị")) {
          fileTemplate = "xuatcuutrovientro/" + "QĐ giao nhiệm vụ xuất hàng_Xuất cứu trợ, viện trợ-VT.docx";
          checkTypeVT = true;
        } else {
          fileTemplate = "xuatcuutrovientro/" + "QĐ giao nhiệm vụ xuất hàng_Xuất cứu trợ, viện trợ-LT.docx";
        }
      }
      Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
      Map<String, Map<String, Object>> mapVthh = getListDanhMucHangHoaObject();
      xhCtvtQuyetDinhGnvHdr.get().getDataDtl().forEach(data -> {
        data.setMapDmucDvi(mapDmucDvi);
        data.setMapVthh(mapVthh);
        });
      FileInputStream inputStream = new FileInputStream(baseReportFolder + fileTemplate);
      var xhCtvtQuyetDinhGnvHdrPreview = setDataToPreview(xhCtvtQuyetDinhGnvHdr, checkTypeVT, userInfo);
      return docxToPdfConverter.convertDocxToPdf(inputStream, xhCtvtQuyetDinhGnvHdrPreview);
    }

  private XhCtvtQuyetDinhGnvHdrPreview setDataToPreview(Optional<XhCtvtQuyetDinhGnvHdr> xhCtvtQuyetDinhGnvHdr,
                                                        Boolean checkTypeVT, Optional<UserInfo> userInfo) {
    return XhCtvtQuyetDinhGnvHdrPreview.builder()
            .tenDonvi(qlnvDmDonviRepository.findByMaDvi(xhCtvtQuyetDinhGnvHdr.get().getMaDvi()).getTenDvi())
            .soBbQd(xhCtvtQuyetDinhGnvHdr.get().getSoBbQd())
            .ngayKy(xhCtvtQuyetDinhGnvHdr.get().getNgayKy().getDayOfMonth())
            .thangKy(xhCtvtQuyetDinhGnvHdr.get().getNgayKy().getMonth().getValue())
            .namKy(xhCtvtQuyetDinhGnvHdr.get().getNgayKy().getYear())
            .namKeHoach(xhCtvtQuyetDinhGnvHdr.get().getNam())
            .canCuPhapLy(xhCtvtQuyetDinhGnvHdr.get().getCanCu()
                    .stream().map(FileDinhKemJoinTable::getNoiDung).collect(Collectors.joining(" ,")))
            .tongSoLuong(xhCtvtQuyetDinhGnvHdr.get().getTongSoLuong() == null? new BigDecimal(0l) : xhCtvtQuyetDinhGnvHdr.get().getTongSoLuong())
            .donViTinh(checkTypeVT.equals(Boolean.FALSE) ? "kg" : "")
            .loaiVthh(xhCtvtQuyetDinhGnvHdr.get().getTenLoaiVthh() == null?xhCtvtQuyetDinhGnvHdr.get().getTenVthh(): xhCtvtQuyetDinhGnvHdr.get().getTenLoaiVthh())
            .thoiGianGiaoNhan(xhCtvtQuyetDinhGnvHdr.get().getThoiGianGiaoNhan().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
            .lanhDaoCuc(userInfo.isPresent() ? userInfo.get().getFullName() : "")
            .xhCtvtQuyetDinhGnvDtlDto(convertXhCtvtQuyetDinhGnvDtlDtoToDto(xhCtvtQuyetDinhGnvHdr.get().getDataDtl()))
            .build();
  }

  private List<XhCtvtQuyetDinhGnvDtlDto> convertXhCtvtQuyetDinhGnvDtlDtoToDto(List<XhCtvtQuyetDinhGnvDtl> dataDtl) {
    List<XhCtvtQuyetDinhGnvDtlDto> xhCtvtQuyetDinhGnvDtlDtos = new ArrayList<>();
    UserInfo userInfo = getUser();
    List<XhCtvtQuyetDinhGnvDtl> quyetDinhGnvDtls = dataDtl.stream().filter(item -> !StringUtils.isEmpty(item.getMaDvi()) && item.getMaDvi().contains(userInfo.getDvql())).collect(Collectors.toList());
    int stt = 1;
    for (var res : quyetDinhGnvDtls) {
      var xhCtvtQuyetDinhGnvDtlDto = XhCtvtQuyetDinhGnvDtlDto.builder()
              .stt(stt++)
              .tenChiCuc(res.getTenChiCuc())
              .soLuong(res.getSoLuongGiao() == null ? new BigDecimal(0l): res.getSoLuongGiao())
              .loaiVthh(res.getTenLoaiVthh())
              .cloaiVthh(res.getTenCloaiVthh())
              .build();
      xhCtvtQuyetDinhGnvDtlDtos.add(xhCtvtQuyetDinhGnvDtlDto);
    }
    return xhCtvtQuyetDinhGnvDtlDtos;
  }

  public List<XhCtvtQuyetDinhGnvHdr> searchList(CustomUserDetails currentUser, SearchXhCtvtQuyetDinhGnv objReq) {
    if (!currentUser.getUser().getCapDvi().equals(CAP_CHI_CUC)) {
      objReq.setDvql(currentUser.getDvql());
    } else {
      objReq.setMaDviGiao(currentUser.getDvql());
    }
    if(objReq.getType() != null){
      objReq.getTypes().add(objReq.getType());
    }

    List<XhCtvtQuyetDinhGnvHdr> data = xhCtvtQuyetDinhGnvHdrRepository.searchList(objReq);
    if (currentUser.getUser().getCapDvi().equals(CAP_CHI_CUC)) {
      data.stream().forEach(s->{
        List<XhCtvtQuyetDinhGnvDtl> quyetDinhGnvDtls = s.getDataDtl().stream().filter(s1 -> s1.getMaDvi().contains(currentUser.getDvql())).collect(Collectors.toList());
        if(!quyetDinhGnvDtls.isEmpty()){
          s.setTrangThaiXh(quyetDinhGnvDtls.get(0).getTrangThai());
          s.setTenTrangThaiXh(TrangThaiAllEnum.getLabelById(quyetDinhGnvDtls.get(0).getTenTrangThai()));
        }
      });
    }

    return data;
  }
}
