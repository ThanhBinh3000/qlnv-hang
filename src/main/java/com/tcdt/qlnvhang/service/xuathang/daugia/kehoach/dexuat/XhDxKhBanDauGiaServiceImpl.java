package com.tcdt.qlnvhang.service.xuathang.daugia.kehoach.dexuat;

import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.dexuat.XhDxKhBanDauGiaDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.dexuat.XhDxKhBanDauGiaPhanLoRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.dexuat.XhDxKhBanDauGiaRepository;
import com.tcdt.qlnvhang.request.CountKhlcntSlReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.kehoachbdg.dexuat.XhDxKhBanDauGiaDtlReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.kehoachbdg.dexuat.XhDxKhBanDauGiaPhanLoReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.kehoachbdg.dexuat.XhDxKhBanDauGiaReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.dexuat.XhDxKhBanDauGia;
import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.dexuat.XhDxKhBanDauGiaDtl;
import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.dexuat.XhDxKhBanDauGiaPhanLo;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class XhDxKhBanDauGiaServiceImpl extends BaseServiceImpl implements XhDxKhBanDauGiaService {
  @Autowired
  private XhDxKhBanDauGiaRepository xhDxKhBanDauGiaRepository;

  @Autowired
  private XhDxKhBanDauGiaDtlRepository xhDxKhBanDauGiaDtlRepository;

  @Autowired
  private XhDxKhBanDauGiaPhanLoRepository xhDxKhBanDauGiaPhanLoRepository;

  @Autowired
  private FileDinhKemService fileDinhKemService;

  @Override
  public Page<XhDxKhBanDauGia> searchPage(XhDxKhBanDauGiaReq req) throws Exception {
    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(),
        req.getPaggingReq().getLimit(), Sort.by("id").descending());
    Page<XhDxKhBanDauGia> data = xhDxKhBanDauGiaRepository.searchPage(
        req,
        pageable);
    Map<String, String> hashMapDmhh = getListDanhMucHangHoa();
    Map<String, String> listDanhMucDvi = getListDanhMucDvi(null, null, "01");

    data.getContent().forEach(f -> {
      f.setTenLoaiVthh(hashMapDmhh.get(f.getLoaiVthh()));
      f.setTenCloaiVthh(hashMapDmhh.get(f.getCloaiVthh()));
      f.setMaDvi(listDanhMucDvi.get(f.getMaDvi()));
    });
    return data;
  }

  @Override
  public XhDxKhBanDauGia create(XhDxKhBanDauGiaReq req) throws Exception {
    UserInfo userInfo = SecurityContextService.getUser();
    if (userInfo == null)
      throw new Exception("Bad request.");

    if (!StringUtils.isEmpty(req.getSoDxuat())) {
      Optional<XhDxKhBanDauGia> qOptional = xhDxKhBanDauGiaRepository.findBySoDxuat(req.getSoDxuat());
      if (qOptional.isPresent()) {
        throw new Exception("Số đề xuất " + req.getSoDxuat() + " đã tồn tại");
      }
    }

    XhDxKhBanDauGia dataMap = new XhDxKhBanDauGia();
    BeanUtils.copyProperties(req, dataMap, "id");
    dataMap.setNgayTao(getDateTimeNow());
    dataMap.setNguoiTaoId(userInfo.getId());
    dataMap.setTrangThai(Contains.DU_THAO);
    dataMap.setTrangThaiTh(Contains.CHUATONGHOP);

    int tongDviTsan = dataMap.getChildren().stream()
        .flatMap(child1 -> child1.getChildren().stream())
        .map(XhDxKhBanDauGiaPhanLo::getMaDviTsan).collect(Collectors.toSet()).size();
    System.out.println("tong"+tongDviTsan);
    dataMap.setSlDviTsan(DataUtils.safeToInt(tongDviTsan));

    XhDxKhBanDauGia created = xhDxKhBanDauGiaRepository.save(dataMap);

    if (!DataUtils.isNullOrEmpty(req.getFileDinhKems())) {
      List<FileDinhKem> fileDinhKemList = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), created.getId(), XhDxKhBanDauGia.TABLE_NAME);
      created.setFileDinhKems(fileDinhKemList);
    }

    this.saveDetail(req, dataMap.getId());
    return created;
  }

//    public void validateData(XhDxKhBanDauGia objHdr, String trangThai) throws Exception {
//        if (trangThai.equals(NhapXuatHangTrangThaiEnum.CHODUYET_TP.getId()) || trangThai.equals(NhapXuatHangTrangThaiEnum.DUTHAO.getId())) {
//            XhDxKhBanDauGia dXuat = xhDxKhBanDauGiaRepository.findAllByLoaiVthhAndCloaiVthhAndNamKhAndMaDviAndTrangThaiNot(objHdr.getLoaiVthh(), objHdr.getCloaiVthh(), objHdr.getNamKh(), objHdr.getMaDvi(), NhapXuatHangTrangThaiEnum.DUTHAO.getId());
//            if (!ObjectUtils.isEmpty(dXuat) && !dXuat.getId().equals(objHdr.getId())) {
//                throw new Exception("Chủng loại hàng hóa đã được tạo và gửi duyệt, xin vui lòng chọn lại chủng loại hàng hóa khác");
//            }
//        }
//       if (trangThai.equals(NhapXuatHangTrangThaiEnum.DADUYET_LDC.getId()) || trangThai.equals(NhapXuatHangTrangThaiEnum.CHODUYET_LDC.getId())){
//           for (XhDxKhBanDauGiaPhanLo chiCuc : objHdr.getChildren()){
//               BigDecimal aLong = xhDxKhBanDauGiaRepository.countSLDalenKh(objHdr.getNamKh(), objHdr.getLoaiVthh(), chiCuc.getMaDvi(),NhapXuatHangTrangThaiEnum.BAN_HANH.getId());
//               BigDecimal soLuongTotal = aLong.add(chiCuc.getSoLuong());
//               if (chiCuc.getSoLuongChiTieu() == null){
//                   throw new Exception("Hiện chưa có số lượng chỉ tiêu kế hoạch năm, vui lòng nhập lại");
//               }
//               if (soLuongTotal.compareTo(chiCuc.getSoLuongChiTieu()) > 0){
//                   throw new Exception(chiCuc.getTenDvi() + " đã nhập quá số lượng chi tiêu, vui lòng nhập lại");
//               }
//           }
//       }
//    }

  void saveDetail(XhDxKhBanDauGiaReq objReq, Long idHdr) {
    // Delete dtl in hdr
    xhDxKhBanDauGiaDtlRepository.deleteAllByIdHdr(idHdr);
    for (XhDxKhBanDauGiaDtl dtlReq : objReq.getChildren()) {
      XhDxKhBanDauGiaDtl dtl = new XhDxKhBanDauGiaDtl();
      BeanUtils.copyProperties(dtlReq, dtl, "id");
      dtl.setIdHdr(idHdr);
      xhDxKhBanDauGiaDtlRepository.save(dtl);
      // Delete phanLo in dtl
      xhDxKhBanDauGiaPhanLoRepository.deleteAllByIdDtl(dtlReq.getId());
      for (XhDxKhBanDauGiaPhanLo phanLoReq : dtlReq.getChildren()) {
        XhDxKhBanDauGiaPhanLo phanLo = new XhDxKhBanDauGiaPhanLo();
        BeanUtils.copyProperties(phanLoReq, phanLo, "id");
        phanLo.setIdDtl(dtl.getId());
        xhDxKhBanDauGiaPhanLoRepository.save(phanLo);
      }
    }
  }

  @Override
  public XhDxKhBanDauGia update(XhDxKhBanDauGiaReq req) throws Exception {
    UserInfo userInfo = SecurityContextService.getUser();
    if (userInfo == null)
      throw new Exception("Bad request.");

    if (StringUtils.isEmpty(req.getId()))
      throw new Exception("Sửa thất bại, không tìm thấy dữu liệu");

    Optional<XhDxKhBanDauGia> qOptional = xhDxKhBanDauGiaRepository.findById(req.getId());
    if (!qOptional.isPresent())
      throw new Exception("Không tìm thấy dữ liệu cần sửa");

    if (!StringUtils.isEmpty(req.getSoDxuat())) {
      Optional<XhDxKhBanDauGia> deXuat = xhDxKhBanDauGiaRepository.findBySoDxuat(req.getSoDxuat());
      if (deXuat.isPresent()) {
        if (!deXuat.get().getId().equals(req.getId())) {
          throw new Exception("Số đề xuất " + req.getSoDxuat() + " đã tồn tại");
        }
      }
    }

    XhDxKhBanDauGia dataDTB = qOptional.get();

    BeanUtils.copyProperties(req, dataDTB, "id", "trangThaiTh");
    dataDTB.setNgaySua(getDateTimeNow());
    dataDTB.setNguoiSuaId(userInfo.getId());

    Long tongDviTsan = dataDTB.getChildren().stream()
        .flatMap(child1 -> child1.getChildren().stream())
        .collect(Collectors.groupingBy(
            XhDxKhBanDauGiaPhanLo::getMaDviTsan,
            Collectors.counting()
        )).values().stream().reduce(0L, Long::sum);
    dataDTB.setSlDviTsan(DataUtils.safeToInt(tongDviTsan));

    XhDxKhBanDauGia created = xhDxKhBanDauGiaRepository.save(dataDTB);
    List<FileDinhKem> fileDinhKemList = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), created.getId(), XhDxKhBanDauGia.TABLE_NAME);
    dataDTB.setFileDinhKems(fileDinhKemList);

    this.saveDetail(req, dataDTB.getId());
    return dataDTB;
  }

  @Override
  public XhDxKhBanDauGia detail(Long id) throws Exception {
    Optional<XhDxKhBanDauGia> qOptional = xhDxKhBanDauGiaRepository.findById(id);

    if (!qOptional.isPresent()) {
      throw new UnsupportedOperationException("Không tồn tại bản ghi");
    }

    XhDxKhBanDauGia data = qOptional.get();

    Map<String, String> hasMapVthh = getListDanhMucHangHoa();
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");

    data.setTenLoaiVthh(hasMapVthh.get(data.getLoaiVthh()));
    data.setTenCloaiVthh(hasMapVthh.get(data.getCloaiVthh()));
    data.setTenDvi(mapDmucDvi.get(data.getMaDvi()));


    List<FileDinhKem> fileDinhKems = fileDinhKemService.search(data.getId(), Arrays.asList(XhDxKhBanDauGia.TABLE_NAME));
    data.setFileDinhKems(fileDinhKems);

    List<XhDxKhBanDauGiaDtl> dtlList = xhDxKhBanDauGiaDtlRepository.findAllByIdHdr(data.getId());
    for (XhDxKhBanDauGiaDtl dtl : dtlList) {
      dtl.setTenDvi(mapDmucDvi.get(dtl.getMaDvi()));
      List<XhDxKhBanDauGiaPhanLo> phanLoList = xhDxKhBanDauGiaPhanLoRepository.findByIdDtl(dtl.getId());
      phanLoList.forEach(f -> {
        f.setTenDiemKho(mapDmucDvi.get(f.getMaDiemKho()));
        f.setTenNhaKho(mapDmucDvi.get(f.getMaNhaKho()));
        f.setTenNganKho(mapDmucDvi.get(f.getMaNganKho()));
        f.setTenLoKho(mapDmucDvi.get(f.getMaLoKho()));
      });
      dtl.setChildren(phanLoList);
    }
    data.setChildren(dtlList);
    return data;
  }

  @Override
  public XhDxKhBanDauGia approve(XhDxKhBanDauGiaReq req) throws Exception {
    UserInfo userInfo = SecurityContextService.getUser();
    if (userInfo == null) {
      throw new Exception("Bad request.");
    }

    if (StringUtils.isEmpty(req.getId())) {
      throw new Exception("Không tìm thấy dữ liệu");
    }

    Optional<XhDxKhBanDauGia> optional = xhDxKhBanDauGiaRepository.findById(req.getId());
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    }

    XhDxKhBanDauGia data = optional.get();
    String status = req.getTrangThai() + data.getTrangThai();
    switch (status) {
      case Contains.CHODUYET_TP + Contains.DUTHAO:
      case Contains.CHODUYET_TP + Contains.TUCHOI_TP:
      case Contains.CHODUYET_TP + Contains.TUCHOI_LDC:
      case Contains.CHODUYET_TP + Contains.TU_CHOI_CBV:
        data.setNguoiGuiDuyetId(userInfo.getId());
        data.setNgayGuiDuyet(getDateTimeNow());
      case Contains.TUCHOI_TP + Contains.CHODUYET_TP:
      case Contains.TUCHOI_LDC + Contains.CHODUYET_LDC:
      case Contains.TU_CHOI_CBV + Contains.DADUYET_LDC:
        data.setNguoiPduyetId(userInfo.getId());
        data.setNgayPduyet(getDateTimeNow());
        data.setLyDoTuChoi(req.getLyDoTuChoi());
        break;
      case Contains.CHODUYET_LDC + Contains.CHODUYET_TP:
      case Contains.DADUYET_LDC + Contains.CHODUYET_LDC:
      case Contains.DA_DUYET_CBV + Contains.DADUYET_LDC:
        data.setNguoiPduyetId(userInfo.getId());
        data.setNgayPduyet(getDateTimeNow());
        break;
      default:
        throw new Exception("Phê duyệt không thành công");
    }
    data.setTrangThai(req.getTrangThai());
    return xhDxKhBanDauGiaRepository.save(data);
  }

  @Override
  public void delete(Long id) throws Exception {
    if (StringUtils.isEmpty(id)) {
      throw new Exception("Xóa thất bại không tìm thấy dữ liệu");
    }

    Optional<XhDxKhBanDauGia> optional = xhDxKhBanDauGiaRepository.findById(id);
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu cần xóa");
    }

    if (!optional.get().getTrangThai().equals(Contains.DUTHAO)
        && !optional.get().getTrangThai().equals(Contains.TU_CHOI_TP)
        && !optional.get().getTrangThai().equals(Contains.TUCHOI_LDC)) {
      throw new Exception("Chỉ thực hiện xóa với kế hoạch ở trạng thái bản nháp hoặc từ chối");
    }

    List<XhDxKhBanDauGiaDtl> allByIdHdr = xhDxKhBanDauGiaDtlRepository.findAllByIdHdr(id);
    for (XhDxKhBanDauGiaDtl dtl : allByIdHdr) {
      xhDxKhBanDauGiaPhanLoRepository.deleteAllByIdDtl(dtl.getId());
    }
    xhDxKhBanDauGiaDtlRepository.deleteAllByIdHdr(id);
    xhDxKhBanDauGiaRepository.delete(optional.get());
    fileDinhKemService.delete(optional.get().getId(), Collections.singleton(XhDxKhBanDauGia.TABLE_NAME));

  }

  @Override
  public void deleteMulti(List<Long> listMulti) throws Exception {
    if (StringUtils.isEmpty(listMulti)) {
      throw new Exception("Xóa thất bại, không tìm thấy dữ liệu");
    }
    List<XhDxKhBanDauGia> listDg = xhDxKhBanDauGiaRepository.findByIdIn(listMulti);
    if (listDg.isEmpty()) {
      throw new Exception("Không tìm thấy dữ liệu cần xóa");
    }

    for (XhDxKhBanDauGia dg : listDg) {
      this.delete(dg.getId());
    }
  }

  @Override
  public void export(XhDxKhBanDauGiaReq req, HttpServletResponse response) throws Exception {
    PaggingReq paggingReq = new PaggingReq();
    paggingReq.setPage(0);
    paggingReq.setLimit(Integer.MAX_VALUE);
    req.setPaggingReq(paggingReq);
    Page<XhDxKhBanDauGia> page = this.searchPage(req);
    List<XhDxKhBanDauGia> data = page.getContent();

    String title = "Danh sách đề xuất kế hoạch bán đấu giá";
    String[] rowsName = new String[]{"STT", "Năm KH", "Số KH/tờ trình", "Ngày lập KH", "Ngày duyệt KH", "Số QĐ duyệt KH bán ĐG", "Ngày ký QĐ", "Trích yếu", "Loại hàng hóa", "Chủng loại hàng hóa", "Số ĐV tài sản", "SL HĐ đã ký", "Số QĐ giao chỉ tiêu", "Trạng thái"};
    String filename = "danh-sach-dx-kh-ban-dau-gia.xlsx";
    List<Object[]> dataList = new ArrayList<Object[]>();
    Object[] objs = null;
    for (int i = 0; i < data.size(); i++) {
      XhDxKhBanDauGia hdr = data.get(i);
      objs = new Object[rowsName.length];
      objs[0] = i;
      objs[1] = hdr.getNamKh();
      objs[2] = hdr.getSoDxuat();
      objs[3] = hdr.getNgayTao();
      objs[4] = hdr.getNgayPduyet();
      objs[5] = hdr.getSoQdPd();
      objs[6] = hdr.getNgayKyQd();
      objs[7] = hdr.getTrichYeu();
      objs[8] = hdr.getTenLoaiVthh();
      objs[9] = hdr.getTenCloaiVthh();
      objs[10] = hdr.getSlDviTsan();
      objs[11] = null;
      objs[12] = hdr.getSoQdCtieu();
      objs[13] = hdr.getTenTrangThai();
      dataList.add(objs);
    }
    ExportExcel ex = new ExportExcel(title, filename, rowsName, dataList, response);
    ex.export();
  }


  public BigDecimal countSoLuongKeHoachNam(CountKhlcntSlReq req) {
    return xhDxKhBanDauGiaRepository.countSLDalenKh(req.getYear(), req.getLoaiVthh(), req.getMaDvi(), req.getLastest());
  }
}