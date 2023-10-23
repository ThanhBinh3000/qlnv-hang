package com.tcdt.qlnvhang.service.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtDeXuatHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtTongHopDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtTongHopHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.xuatcuutrovientro.XhCtvtTongHopHdrPreview;
import com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.SearchXhCtvtDeXuatHdrReq;
import com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.SearchXhCtvtTongHopHdr;
import com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtTongHopHdrReq;
import com.tcdt.qlnvhang.response.xuatcuutrovientro.XhCtvtTongHopDto;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtDeXuatHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtTongHopDtl;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtTongHopHdr;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import fr.opensagres.xdocreport.core.XDocReportException;
import lombok.var;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class XhCtvtTongHopHdrService extends BaseServiceImpl {


  @Autowired
  private XhCtvtTongHopHdrRepository xhCtvtTongHopHdrRepository;

  @Autowired
  private XhCtvtTongHopDtlRepository xhCtvtTongHopDtlRepository;

  @Autowired
  private XhCtvtDeXuatHdrRepository xhCtvtDeXuatHdrRepository;


  public Page<XhCtvtTongHopHdr> searchPage(CustomUserDetails currentUser, SearchXhCtvtTongHopHdr objReq) throws Exception {
    objReq.setDvql(currentUser.getDvql());
    Pageable pageable = PageRequest.of(objReq.getPaggingReq().getPage(), objReq.getPaggingReq().getLimit());
    Page<XhCtvtTongHopHdr> data = xhCtvtTongHopHdrRepository.search(objReq, pageable);
    return data;
  }

  public XhCtvtTongHopHdr summaryData(CustomUserDetails currentUser, SearchXhCtvtDeXuatHdrReq objReq) throws Exception {
    List<XhCtvtDeXuatHdr> dxuatList = xhCtvtDeXuatHdrRepository.listTongHop(objReq);
    if (dxuatList.isEmpty()) {
      throw new Exception("Không tìm thấy dữ liệu để tổng hợp");
    }
    XhCtvtTongHopHdr thopHdr = new XhCtvtTongHopHdr();
    thopHdr.setTrangThai(TrangThaiAllEnum.DU_THAO.getId());
    List<XhCtvtTongHopDtl> thopDtls = new ArrayList<>();
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();

    dxuatList.forEach(s -> {
      s.getDeXuatPhuongAn().forEach(s1 -> {
        XhCtvtTongHopDtl thopDtl = new XhCtvtTongHopDtl();
        thopDtl.setIdDx(s.getId());
        thopDtl.setSoDx(s.getSoDx());
        thopDtl.setNgayKyDx(s.getNgayPduyet());
        thopDtl.setTrichYeuDx(s.getTrichYeu());
        thopDtl.setSoLuongDx(s1.getSoLuong());
        thopDtl.setNoiDungDx(s1.getNoiDung());
        thopDtl.setLoaiNhapXuat(s.getLoaiNhapXuat());
        thopDtl.setKieuNhapXuat(s.getKieuNhapXuat());
        thopDtl.setMucDichXuat(s.getMucDichXuat());
        thopDtl.setLoaiVthh(s1.getLoaiVthh());
        thopDtl.setCloaiVthh(s1.getCloaiVthh());
        thopDtl.setMaDvi(s1.getMaDvi());
        thopDtl.setSoLuong(s1.getSoLuong());
        thopDtl.setTonKhoDvi(s1.getTonKhoDvi());
        thopDtl.setTonKhoLoaiVthh(s1.getTonKhoLoaiVthh());
        thopDtl.setTonKhoCloaiVthh(s1.getTonKhoCloaiVthh());
        thopDtl.setDonViTinh(s1.getDonViTinh());
        thopDtl.setMapDmucDvi(mapDmucDvi);
        thopDtl.setMapVthh(mapVthh);
        thopDtl.setSoLuongNhuCauXuat(s1.getSoLuongNhuCauXuat());
        thopDtl.setSoLuongChuyenCapThoc(s1.getSoLuongChuyenCapThoc());
        thopDtl.setSoLuongConThieu(s1.getSoLuongConThieu());
        thopDtl.setNgayKetThuc(s.getNgayKetThuc());
        thopDtls.add(thopDtl);
      });
    });
    thopHdr.setDeXuatCuuTro(thopDtls);
    return thopHdr;
  }

  @Transactional()
  public XhCtvtTongHopHdr save(CustomUserDetails currentUser, XhCtvtTongHopHdrReq objReq) throws Exception {
    XhCtvtTongHopHdr thopHdr = new XhCtvtTongHopHdr();
    DataUtils.copyProperties(objReq, thopHdr, "id");
    thopHdr.setTrangThai(TrangThaiAllEnum.DU_THAO.getId());
    thopHdr.setMaDvi(currentUser.getUser().getDepartment());

    XhCtvtTongHopHdr created = xhCtvtTongHopHdrRepository.save(thopHdr);

    if (created.getDeXuatCuuTro().size() > 0) {
      List<Long> collectDxId = created.getDeXuatCuuTro().stream().map(XhCtvtTongHopDtl::getIdDx).collect(Collectors.toList());
      List<XhCtvtDeXuatHdr> newDeXuatHdr = xhCtvtDeXuatHdrRepository.findAllByIdIn(collectDxId);
      newDeXuatHdr.forEach(s -> {
        s.setIdThop(created.getId());
        s.setMaTongHop(created.getMaTongHop());
      });
      xhCtvtDeXuatHdrRepository.saveAll(newDeXuatHdr);
    }
    return created;
  }


  @Transactional()
  public XhCtvtTongHopHdr update(CustomUserDetails currentUser, XhCtvtTongHopHdrReq objReq) throws Exception {
    if (StringUtils.isEmpty(objReq.getId())) throw new Exception(" Sửa thất bại, không tìm thấy dữ liệu");

    Optional<XhCtvtTongHopHdr> qOptional = xhCtvtTongHopHdrRepository.findById(Long.valueOf(objReq.getId()));
    if (!qOptional.isPresent()) throw new Exception("Không tìm thấy dữ liệu cần sửa");

    XhCtvtTongHopHdr data = qOptional.get();
    BeanUtils.copyProperties(objReq, data,"maDvi");
    XhCtvtTongHopHdr created = xhCtvtTongHopHdrRepository.save(data);

    //update dx
    //tim tat ca dx dang co ma th va update lại thanh null
    //update ma th cho dx xuat moi
    List<XhCtvtDeXuatHdr> oldDeXuatHdr = xhCtvtDeXuatHdrRepository.findAllByIdThop(created.getId());
    oldDeXuatHdr.forEach(s -> {
      s.setIdThop(null);
      s.setMaTongHop(null);
    });
    xhCtvtDeXuatHdrRepository.saveAll(oldDeXuatHdr);

    if (created.getDeXuatCuuTro().size() > 0) {
      List<Long> collectDxId = created.getDeXuatCuuTro().stream().map(XhCtvtTongHopDtl::getIdDx).collect(Collectors.toList());
      List<XhCtvtDeXuatHdr> newDeXuatHdr = xhCtvtDeXuatHdrRepository.findAllByIdIn(collectDxId);
      newDeXuatHdr.forEach(s -> {
        s.setIdThop(created.getId());
        s.setMaTongHop(created.getMaTongHop());
      });
      xhCtvtDeXuatHdrRepository.saveAll(newDeXuatHdr);
    }
    return created;
  }

  public List<XhCtvtTongHopHdr> detail(List<Long> ids) throws Exception {
    if (StringUtils.isEmpty(ids)) throw new Exception("Tham số không hợp lệ.");
    List<XhCtvtTongHopHdr> optional = xhCtvtTongHopHdrRepository.findByIdIn(ids);
    if (DataUtils.isNullOrEmpty(optional)) throw new Exception("Không tìm thấy dữ liệu");
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();

    List<XhCtvtTongHopHdr> allById = xhCtvtTongHopHdrRepository.findAllById(ids);
    allById.forEach(data -> {
      List<XhCtvtTongHopDtl> listTh = xhCtvtTongHopDtlRepository.findAllByXhCtvtTongHopHdrId(data.getId());
      listTh.forEach(s -> {
        s.setMapDmucDvi(mapDmucDvi);
        s.setMapVthh(mapVthh);
      });
    });
    return allById;
  }

  @Transactional
  public void delete(IdSearchReq idSearchReq) throws Exception {
    if (StringUtils.isEmpty(idSearchReq.getId())) throw new Exception("Xóa thất bại, không tìm thấy dữ liệu");

    Optional<XhCtvtTongHopHdr> optional = xhCtvtTongHopHdrRepository.findById(idSearchReq.getId());
    if (!optional.isPresent()) throw new Exception("Không tìm thấy dữ liệu cần xóa");

    XhCtvtTongHopHdr data = optional.get();
    List<XhCtvtTongHopDtl> listDls = xhCtvtTongHopDtlRepository.findAllByXhCtvtTongHopHdrId(data.getId());
    if (!CollectionUtils.isEmpty(listDls)) {
      List<Long> idDxList = listDls.stream().map(XhCtvtTongHopDtl::getIdDx).collect(Collectors.toList());
      List<XhCtvtDeXuatHdr> listDxHdr = xhCtvtDeXuatHdrRepository.findByIdIn(idDxList);
      if (!CollectionUtils.isEmpty(listDxHdr)) {
        listDxHdr.stream().map(item -> {
          item.setMaTongHop(null);
          item.setIdThop(null);
          return item;
        }).collect(Collectors.toList());
      }
      xhCtvtDeXuatHdrRepository.saveAll(listDxHdr);
    }
    xhCtvtTongHopDtlRepository.deleteAll(listDls);
    xhCtvtTongHopHdrRepository.delete(optional.get());

  }

  @Transactional
  public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
    if (StringUtils.isEmpty(idSearchReq.getIdList())) throw new Exception("Xóa thất bại, không tìm thấy dữ liệu");
    List<XhCtvtTongHopHdr> listThop = xhCtvtTongHopHdrRepository.findAllByIdIn(idSearchReq.getIdList());
    for (XhCtvtTongHopHdr thopHdr : listThop) {
      List<XhCtvtTongHopDtl> listDls = xhCtvtTongHopDtlRepository.findAllByXhCtvtTongHopHdrId(thopHdr.getId());
      if (!CollectionUtils.isEmpty(listDls)) {
        List<Long> idDxList = listDls.stream().map(XhCtvtTongHopDtl::getIdDx).collect(Collectors.toList());
        List<XhCtvtDeXuatHdr> listDxHdr = xhCtvtDeXuatHdrRepository.findByIdIn(idDxList);
        if (!CollectionUtils.isEmpty(listDxHdr)) {
          listDxHdr.stream().map(item -> {
            item.setMaTongHop(null);
            item.setIdThop(null);
            return item;
          }).collect(Collectors.toList());
        }
        xhCtvtDeXuatHdrRepository.saveAll(listDxHdr);
      }
    }
    xhCtvtTongHopHdrRepository.deleteAllByIdIn(idSearchReq.getIdList());
  }


  public void export(CustomUserDetails currentUser, SearchXhCtvtTongHopHdr searchReq, HttpServletResponse response) throws Exception {
    PaggingReq paggingReq = new PaggingReq();
    paggingReq.setPage(0);
    paggingReq.setLimit(Integer.MAX_VALUE);
    searchReq.setPaggingReq(paggingReq);
    Page<XhCtvtTongHopHdr> page = this.searchPage(currentUser, searchReq);
    List<XhCtvtTongHopHdr> data = page.getContent();

    String title = "Danh sách tổng hợp phương án xuất cứu trợ, viện trợ";
    String[] rowsName = new String[]{"STT", "Năm KH", "Mã Tổng hợp", "Ngày tổng hợp", "Số quyết định", "Ngày kí quyết định", "Loại hàng hóa", "Tổng SL xuất viện trợ, cứu trợ (kg)", "SL xuất CT,VT chuyển xuất cấp", "Nội dung tổng hợp", "Trạng thái"};
    String filename = "danh-sach-tong-hop-phuong-an-cuu-tro-vien-tro.xlsx";

    List<Object[]> dataList = new ArrayList<Object[]>();
    Object[] objs = null;
    for (int i = 0; i < data.size(); i++) {
      XhCtvtTongHopHdr dx = data.get(i);
      objs = new Object[rowsName.length];
      objs[0] = i;
      objs[1] = dx.getNam();
      objs[2] = dx.getMaTongHop();
      objs[3] = dx.getNgayThop();
      objs[4] = dx.getSoQdPd();
      objs[5] = dx.getNgayKyQd();
      objs[6] = dx.getTenLoaiVthh();

      objs[9] = dx.getNoiDungThop();
      objs[10] = dx.getTenTrangThai();
      dataList.add(objs);
    }

    ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
    ex.export();
  }


  public XhCtvtTongHopHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {

    if (StringUtils.isEmpty(statusReq.getId())) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    Optional<XhCtvtTongHopHdr> optional = xhCtvtTongHopHdrRepository.findById(Long.valueOf(statusReq.getId()));
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    }

    String status = statusReq.getTrangThai() + optional.get().getTrangThai();
    switch (status) {
      case Contains.CHODUYET_LDV + Contains.DUTHAO:
      case Contains.CHODUYET_LDV + Contains.TUCHOI_LDV:
        optional.get().setNguoiGduyetId(currentUser.getUser().getId());
        optional.get().setNgayGduyet(LocalDate.now());
        break;
      case Contains.TUCHOI_LDV + Contains.CHODUYET_LDV:
        optional.get().setNguoiPduyetId(currentUser.getUser().getId());
        optional.get().setNgayPduyet(LocalDate.now());
        optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
        break;
      case Contains.DADUYET_LDV + Contains.CHODUYET_LDV:
        optional.get().setNguoiPduyetId(currentUser.getUser().getId());
        optional.get().setNgayPduyet(LocalDate.now());
        break;
      default:
        throw new Exception("Phê duyệt không thành công");
    }
    optional.get().setTrangThai(statusReq.getTrangThai());
    XhCtvtTongHopHdr created = xhCtvtTongHopHdrRepository.save(optional.get());
    return created;
  }

  public List<XhCtvtTongHopHdr> searchList(CustomUserDetails currentUser, SearchXhCtvtTongHopHdr objReq) throws Exception {
    objReq.setDvql(currentUser.getDvql());
    List<XhCtvtTongHopHdr> data = xhCtvtTongHopHdrRepository.searchList(objReq);
    return data;
  }

  public ReportTemplateResponse preview(XhCtvtTongHopHdrReq obj) throws Exception {
      var xhCtvtTongHopHdr = xhCtvtTongHopHdrRepository.findById(obj.getId());
      if (!xhCtvtTongHopHdr.isPresent()) throw new Exception("Không tồn tại bản ghi");
      var fileTemplate = "";
      var checkTypeVT = false;
      if (StringUtils.isEmpty(xhCtvtTongHopHdr.get().getTenVthh())) throw new Exception("Không tồn tại loại hàng vật tư hàng hoá");
      if (!StringUtils.isEmpty(xhCtvtTongHopHdr.get().getTenVthh())) {
        if (xhCtvtTongHopHdr.get().getTenVthh().equals("Vật tư thiết bị")) {
          fileTemplate = "xuatcuutrovientro/" + "27.2.Tổng hợp PA Cứu trợ-Vật tư.docx";
          checkTypeVT = true;
        } else {
          fileTemplate = "xuatcuutrovientro/" + "27.1.Tổng hợp PA Cứu trợ-Lương thực.docx";
        }
      }
      if (Objects.isNull(xhCtvtTongHopHdr.get().getId())) throw new Exception("Không tồn tại bản ghi");
      var xhCtvtDeXuatHdr = xhCtvtDeXuatHdrRepository.findAllByIdThop(xhCtvtTongHopHdr.get().getId());
      Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
      Map<String, String> mapVthh = getListDanhMucHangHoa();
      xhCtvtTongHopHdr.get().getDeXuatCuuTro().forEach(data -> {
        data.setMapDmucDvi(mapDmucDvi);
        data.setMapVthh(mapVthh);
      });
      FileInputStream inputStream = new FileInputStream(baseReportFolder + fileTemplate);
      var xhCtvtTongHopHdrPreview = setDataToPreview(xhCtvtTongHopHdr, checkTypeVT, xhCtvtDeXuatHdr);
      return docxToPdfConverter.convertDocxToPdf(inputStream, xhCtvtTongHopHdrPreview);
  }

  private XhCtvtTongHopHdrPreview setDataToPreview(Optional<XhCtvtTongHopHdr> xhCtvtTongHopHdr, boolean checkTypeVT,
                                                   List<XhCtvtDeXuatHdr> xhCtvtDeXuatHdr) {
    var xhCtvtTongHopHdrPreview =  new XhCtvtTongHopHdrPreview();
    xhCtvtTongHopHdrPreview.setNamKeHoach(xhCtvtTongHopHdr.get().getNam());
    if (checkTypeVT) {
      xhCtvtTongHopHdrPreview.setXhCtvtTongHopDto(convertXhCtvtTongHopDtlToDtoVT(xhCtvtTongHopHdr, xhCtvtDeXuatHdr));
    } else {
      xhCtvtTongHopHdrPreview.setXhCtvtTongHopDto(convertXhCtvtTongHopDtlToDtoLT(xhCtvtTongHopHdr, xhCtvtDeXuatHdr));
      xhCtvtTongHopHdrPreview.setLoaiVthh(xhCtvtTongHopHdr.get().getTenLoaiVthh());
      xhCtvtTongHopHdrPreview.setTongSoLuong(getTongSoLuong(xhCtvtTongHopHdrPreview.getXhCtvtTongHopDto()));
    }
    return xhCtvtTongHopHdrPreview;
  }

  private String getTongSoLuong(List<XhCtvtTongHopDto> xhCtvtTongHopDto) {
    BigDecimal tongSoLuong = BigDecimal.ZERO;
    for (var res : xhCtvtTongHopDto) {
      if (!StringUtils.isEmpty(res.getTenDvi())) {
        tongSoLuong = tongSoLuong.add(BigDecimal.valueOf(Long.parseLong(res.getSoLuong())));
      }
    }
    return tongSoLuong.toString();
  }

  private List<XhCtvtTongHopDto> convertXhCtvtTongHopDtlToDtoLT(Optional<XhCtvtTongHopHdr> xhCtvtTongHopHdr,
                                                                List<XhCtvtDeXuatHdr> xhCtvtDeXuatHdr) {
    List<XhCtvtTongHopDto> xhCtvtTongHopDtos = new ArrayList<>();
    var xhCtvtTongHopDtl = xhCtvtTongHopHdr.get().getDeXuatCuuTro()
            .stream().collect(Collectors.groupingBy(XhCtvtTongHopDtl::getTenDvi));
    int stt = 1;
    for (var key : xhCtvtTongHopDtl.entrySet()) {
      var xhCtvtTongHopDtoLvOne = new XhCtvtTongHopDto();
      xhCtvtTongHopDtoLvOne.setStt(String.valueOf(stt++));
      xhCtvtTongHopDtoLvOne.setTenDvi(key.getKey());
      xhCtvtTongHopDtoLvOne.setMucDichXuat(xhCtvtTongHopHdr.get().getLoaiNhapXuat());
      xhCtvtTongHopDtoLvOne.setNgayKetThuc(Contains.convertDateToString(xhCtvtDeXuatHdr.stream().findAny().get().getNgayKetThuc()));
      xhCtvtTongHopDtos.add(xhCtvtTongHopDtoLvOne);
      for(var obj: key.getValue()){
        var xhCtvtTongHopDtoLv = new XhCtvtTongHopDto();
        xhCtvtTongHopDtoLv.setCloaiVthh(obj.getTenLoaiVthh());
        xhCtvtTongHopDtoLv.setSoLuong(String.valueOf(obj.getSoLuong()));
        xhCtvtTongHopDtos.add(xhCtvtTongHopDtoLv);
      }
      xhCtvtTongHopDtoLvOne.setSoLuong(String.valueOf(key.getValue()
              .stream().map(XhCtvtTongHopDtl::getSoLuong).reduce(BigDecimal.ZERO, BigDecimal::add)));
    }
    return xhCtvtTongHopDtos;
  }

  private void convertXhCtvtTongHopDtlToDtoLtThree(List<XhCtvtTongHopDto> xhCtvtTongHopDtos, Map.Entry<String, List<XhCtvtTongHopDtl>> res) {
    for (var value : res.getValue()) {
      var xhCtvtTongHopDtoLvThree = new XhCtvtTongHopDto();
      xhCtvtTongHopDtoLvThree.setCloaiVthh(value.getTenCloaiVthh());
      xhCtvtTongHopDtoLvThree.setSoLuong(String.valueOf(value.getSoLuong()));
      xhCtvtTongHopDtos.add(xhCtvtTongHopDtoLvThree);
    }
  }

  private List<XhCtvtTongHopDto> convertXhCtvtTongHopDtlToDtoVT(Optional<XhCtvtTongHopHdr> xhCtvtTongHopHdr,
                                                                List<XhCtvtDeXuatHdr> xhCtvtDeXuatHdr) {
    List<XhCtvtTongHopDto> xhCtvtTongHopDtos = new ArrayList<>();
    Map<String, List<XhCtvtTongHopDtl>> xhCtvtTongHopDtl = xhCtvtTongHopHdr.get().getDeXuatCuuTro()
            .stream().collect(Collectors.groupingBy(XhCtvtTongHopDtl::getNoiDungDx));
    int stt = 1;
    for (var key : xhCtvtTongHopDtl.entrySet()) {
      var xhCtvtTongHopDtoLvOne = new XhCtvtTongHopDto();
      xhCtvtTongHopDtoLvOne.setStt(String.valueOf(stt++));
      xhCtvtTongHopDtoLvOne.setDonViNhanCuuTro(key.getKey());
      xhCtvtTongHopDtoLvOne.setMucDichXuat(xhCtvtTongHopHdr.get().getLoaiNhapXuat());
      xhCtvtTongHopDtoLvOne.setNgayKetThuc(Contains.convertDateToString(xhCtvtDeXuatHdr.stream().findAny().get().getNgayKetThuc()));
      xhCtvtTongHopDtos.add(xhCtvtTongHopDtoLvOne);
    }
    return xhCtvtTongHopDtos;
  }

  private void convertXhCtvtTongHopDtlToDtoVtTwo(List<XhCtvtTongHopDto> xhCtvtTongHopDtos, Map.Entry<String, List<XhCtvtTongHopDtl>> key) {
    var xhCtvtTongHopDtoVt = key.getValue()
            .stream().collect(Collectors.groupingBy(XhCtvtTongHopDtl::getLoaiVthh));
    for (var res : xhCtvtTongHopDtoVt.entrySet()) {
      var xhCtvtTongHopDtoLvTwo = new  XhCtvtTongHopDto();
      xhCtvtTongHopDtoLvTwo.setLoaiVthh(res.getValue().stream().findAny().get().getTenLoaiVthh());
      xhCtvtTongHopDtoLvTwo.setSoLuong(String.valueOf(res.getValue()
              .stream().map(XhCtvtTongHopDtl::getSoLuong).reduce(BigDecimal.ZERO, BigDecimal::add)));
      xhCtvtTongHopDtoLvTwo.setDonViTinh(res.getValue().stream().findAny().get().getDonViTinh());
      xhCtvtTongHopDtos.add(xhCtvtTongHopDtoLvTwo);
      convertXhCtvtTongHopDtlToDtoVtThree(xhCtvtTongHopDtos, res);
    }
  }
  private void convertXhCtvtTongHopDtlToDtoVtThree(List<XhCtvtTongHopDto> xhCtvtTongHopDtos, Map.Entry<String, List<XhCtvtTongHopDtl>> res) {
    for (var value : res.getValue()) {
      var xhCtvtTongHopDtoLvThree = new XhCtvtTongHopDto();
      xhCtvtTongHopDtoLvThree.setCloaiVthh(value.getTenCloaiVthh());
      xhCtvtTongHopDtoLvThree.setTenDvi(value.getTenDvi());
      xhCtvtTongHopDtoLvThree.setSoLuong(String.valueOf(value.getSoLuong()));
      xhCtvtTongHopDtoLvThree.setDonViTinh(value.getDonViTinh());
      xhCtvtTongHopDtos.add(xhCtvtTongHopDtoLvThree);
    }
  }

}
