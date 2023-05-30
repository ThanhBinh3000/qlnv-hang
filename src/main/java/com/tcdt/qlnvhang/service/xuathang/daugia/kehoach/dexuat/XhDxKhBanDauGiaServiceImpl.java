package com.tcdt.qlnvhang.service.xuathang.daugia.kehoach.dexuat;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.dexuat.XhDxKhBanDauGiaDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.dexuat.XhDxKhBanDauGiaPhanLoRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.dexuat.XhDxKhBanDauGiaRepository;
import com.tcdt.qlnvhang.request.CountKhlcntSlReq;
import com.tcdt.qlnvhang.request.PaggingReq;
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
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
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
    Page<XhDxKhBanDauGia> dataKeHoachDauGia = xhDxKhBanDauGiaRepository.searchPage(
        req,
        pageable);

    Map<String, String> mapDmucHangHoa = getListDanhMucHangHoa();
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null,null,"01");

    dataKeHoachDauGia.getContent().forEach(keHoachDauGia -> {
      if (mapDmucDvi.containsKey((keHoachDauGia.getMaDvi()))) {
        keHoachDauGia.setTenDvi(mapDmucDvi.get(keHoachDauGia.getMaDvi()));
      }
      if (mapDmucHangHoa.get((keHoachDauGia.getLoaiVthh())) != null) {
        keHoachDauGia.setTenLoaiVthh(mapDmucHangHoa.get(keHoachDauGia.getLoaiVthh()));
      }
      if (mapDmucHangHoa.get((keHoachDauGia.getCloaiVthh())) != null) {
        keHoachDauGia.setTenCloaiVthh(mapDmucHangHoa.get(keHoachDauGia.getCloaiVthh()));
      }
      keHoachDauGia.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(keHoachDauGia.getTrangThai()));
      if (DataUtils.isNullObject(keHoachDauGia.getIdThop())) {
        keHoachDauGia.setTenTrangThaiTh("Chưa tổng hợp");
      } else {
        keHoachDauGia.setTenTrangThaiTh(NhapXuatHangTrangThaiEnum.getTenById(keHoachDauGia.getTrangThaiTh()));
      }
    });
    return dataKeHoachDauGia;
  }

  @Override
  public XhDxKhBanDauGia create(XhDxKhBanDauGiaReq req) throws Exception {
    if(req == null) return null;

    UserInfo userInfo = SecurityContextService.getUser();
    if (userInfo == null) throw new Exception("Bad request.");

    if (!StringUtils.isEmpty(req.getSoDxuat())) {
      Optional<XhDxKhBanDauGia> optional = xhDxKhBanDauGiaRepository.findBySoDxuat(req.getSoDxuat());
      if (optional.isPresent()) throw new Exception("Số đề xuất " + req.getSoDxuat() + " đã tồn tại");
    }

    log.info("Save ke hoach ban dau gia");
    XhDxKhBanDauGia keHoachDauGia = new XhDxKhBanDauGia();
    BeanUtils.copyProperties(req, keHoachDauGia, "id");

    keHoachDauGia.setMaDvi(userInfo.getDvql());
    keHoachDauGia.setNguoiTaoId(userInfo.getId());
    keHoachDauGia.setTrangThai(Contains.DU_THAO);
    keHoachDauGia.setTrangThaiTh(Contains.CHUATONGHOP);

    log.info("Save so luong don vi tai san");
    int slDviTsan = keHoachDauGia.getChildren().stream()
        .flatMap(item -> item.getChildren().stream())
        .map(XhDxKhBanDauGiaPhanLo::getMaDviTsan).collect(Collectors.toSet()).size();
    keHoachDauGia.setSlDviTsan(DataUtils.safeToInt(slDviTsan));

    XhDxKhBanDauGia save = xhDxKhBanDauGiaRepository.save(keHoachDauGia);

    log.info("Save file dinh kem");
    if (!DataUtils.isNullOrEmpty(req.getFileDinhKems())) {
      List<FileDinhKem> fileDinhKemList = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), save.getId(), XhDxKhBanDauGia.TABLE_NAME);
      keHoachDauGia.setFileDinhKems(fileDinhKemList);
    }

    this.saveDetail(req, save.getId());
    return save;
  }


  void saveDetail(XhDxKhBanDauGiaReq req, Long idHdr) {

    xhDxKhBanDauGiaDtlRepository.deleteAllByIdHdr(idHdr);
    log.info("Save ke hoach ban dau gia chi tiet");
    for (XhDxKhBanDauGiaDtl keHoachDauGiaDtlReq : req.getChildren()) {
      XhDxKhBanDauGiaDtl keHoachDauGiaDtl = new XhDxKhBanDauGiaDtl();
      BeanUtils.copyProperties(keHoachDauGiaDtlReq, keHoachDauGiaDtl, "id");
      keHoachDauGiaDtl.setIdHdr(idHdr);
      xhDxKhBanDauGiaDtlRepository.save(keHoachDauGiaDtl);

      xhDxKhBanDauGiaPhanLoRepository.deleteAllByIdDtl(keHoachDauGiaDtlReq.getId());
      log.info("Save phan lo tai san");
      for (XhDxKhBanDauGiaPhanLo keHoachDauGiaPhanLoReq : keHoachDauGiaDtlReq.getChildren()) {
        XhDxKhBanDauGiaPhanLo keHoachDauGiaPhanLo = new XhDxKhBanDauGiaPhanLo();
        BeanUtils.copyProperties(keHoachDauGiaPhanLoReq, keHoachDauGiaPhanLo, "id");
        keHoachDauGiaPhanLo.setIdDtl(keHoachDauGiaDtl.getId());
        xhDxKhBanDauGiaPhanLoRepository.save(keHoachDauGiaPhanLo);
      }
    }
  }

  @Override
  public XhDxKhBanDauGia update(XhDxKhBanDauGiaReq req) throws Exception {
    if (req == null) return null;

    UserInfo userInfo = SecurityContextService.getUser();
    if (userInfo == null) throw new Exception("Bad request.");

    Optional<XhDxKhBanDauGia> optional = xhDxKhBanDauGiaRepository.findById(req.getId());
    if (!optional.isPresent()) throw new Exception("Kế hoạch bán đấu giá không tồn tại");

    if (!StringUtils.isEmpty(req.getSoDxuat())) {
      Optional<XhDxKhBanDauGia> qOptional = xhDxKhBanDauGiaRepository.findBySoDxuat(req.getSoDxuat());
      if (qOptional.isPresent()) {
        if (!qOptional.get().getId().equals(req.getId())) {
          throw new Exception("Số đề xuất " + req.getSoDxuat() + " đã tồn tại");
        }
      }
    }

    XhDxKhBanDauGia keHoachDauGia = optional.get();

    log.info("Update ke hoach ban dau gia");
    BeanUtils.copyProperties(req, keHoachDauGia, "id", "trangThaiTh");
    keHoachDauGia.setNgaySua(LocalDate.now());
    keHoachDauGia.setNguoiSuaId(userInfo.getId());

    log.info("Update so luong don vi tai san");
    int slDviTsan = keHoachDauGia.getChildren().stream()
        .flatMap(item -> item.getChildren().stream())
        .map(XhDxKhBanDauGiaPhanLo::getMaDviTsan).collect(Collectors.toSet()).size();
    keHoachDauGia.setSlDviTsan(DataUtils.safeToInt(slDviTsan));

    XhDxKhBanDauGia saveUpdate = xhDxKhBanDauGiaRepository.save(keHoachDauGia);

    log.info("Update file dinh kem");
    fileDinhKemService.delete(saveUpdate.getId(), Collections.singleton(XhDxKhBanDauGia.TABLE_NAME));
    List<FileDinhKem> fileDinhKemList = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), saveUpdate.getId(), XhDxKhBanDauGia.TABLE_NAME);
    keHoachDauGia.setFileDinhKems(fileDinhKemList);

    this.saveDetail(req, saveUpdate.getId());
    return saveUpdate;
  }

  @Override
  public XhDxKhBanDauGia detail(Long id) throws Exception {
    UserInfo userInfo = SecurityContextService.getUser();
    if (userInfo == null) throw new Exception("Bad request.");

    Optional<XhDxKhBanDauGia> optional = xhDxKhBanDauGiaRepository.findById(id);
    if (!optional.isPresent()) throw new UnsupportedOperationException("Kế hoạch bán đấu giá không tồn tại");

    XhDxKhBanDauGia keHoachDauGia = optional.get();

    Map<String, String> mapDmucHangHoa = getListDanhMucHangHoa();
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null,null,"01");

    List<XhDxKhBanDauGiaDtl> keHoachDauGiaDtlList = xhDxKhBanDauGiaDtlRepository.findAllByIdHdr(keHoachDauGia.getId());
    if(!CollectionUtils.isEmpty(keHoachDauGiaDtlList)){
      for (XhDxKhBanDauGiaDtl keHoachDauGiaDtl : keHoachDauGiaDtlList) {
        if (mapDmucDvi.containsKey(keHoachDauGiaDtl.getMaDvi())) {
          keHoachDauGiaDtl.setTenDvi(mapDmucDvi.get(keHoachDauGiaDtl.getMaDvi()));
        }
        List<XhDxKhBanDauGiaPhanLo> keHoachDauGiaPhanLoList = xhDxKhBanDauGiaPhanLoRepository.findByIdDtl(keHoachDauGiaDtl.getId());
        if(!CollectionUtils.isEmpty(keHoachDauGiaPhanLoList)){
          keHoachDauGiaPhanLoList.forEach(phanLo -> {
            if (mapDmucDvi.containsKey((phanLo.getMaDiemKho()))) {
              phanLo.setTenDiemKho(mapDmucDvi.get(phanLo.getMaDiemKho()));
            }
            if (mapDmucDvi.containsKey((phanLo.getMaNhaKho()))) {
              phanLo.setTenNhaKho(mapDmucDvi.get(phanLo.getMaNhaKho()));
            }
            if (mapDmucDvi.containsKey((phanLo.getMaNganKho()))) {
              phanLo.setTenNganKho(mapDmucDvi.get(phanLo.getMaNganKho()));
            }
            if (mapDmucDvi.containsKey((phanLo.getMaLoKho()))) {
              phanLo.setTenLoKho(mapDmucDvi.get(phanLo.getMaLoKho()));
            }
            if (mapDmucHangHoa.get((phanLo.getLoaiVthh())) != null) {
              phanLo.setTenLoaiVthh(mapDmucHangHoa.get(phanLo.getLoaiVthh()));
            }
            if (mapDmucHangHoa.get((phanLo.getCloaiVthh())) != null) {
              phanLo.setTenCloaiVthh(mapDmucHangHoa.get(phanLo.getCloaiVthh()));
            }

            BigDecimal donGiaDuocDuyet = BigDecimal.ZERO;
            if(keHoachDauGia.getLoaiVthh().startsWith("02")){
              donGiaDuocDuyet = xhDxKhBanDauGiaPhanLoRepository.getDonGiaVatVt(keHoachDauGia.getCloaiVthh(), keHoachDauGia.getNamKh());
              if (!DataUtils.isNullObject(donGiaDuocDuyet)){
                phanLo.setDonGiaDuocDuyet(donGiaDuocDuyet);
                BigDecimal tongKhoanTienDtTheoDgiaDd = keHoachDauGia.getTongSoLuong().multiply(donGiaDuocDuyet).multiply(keHoachDauGia.getKhoanTienDatTruoc()).divide(BigDecimal.valueOf(100));
                keHoachDauGia.setTongKhoanTienDtTheoDgiaDd(tongKhoanTienDtTheoDgiaDd);
              }
            }else {
              donGiaDuocDuyet = xhDxKhBanDauGiaPhanLoRepository.getDonGiaVatLt(keHoachDauGia.getCloaiVthh(), keHoachDauGia.getMaDvi(), keHoachDauGia.getNamKh());
              if (!DataUtils.isNullObject(donGiaDuocDuyet)){
                phanLo.setDonGiaDuocDuyet(donGiaDuocDuyet);
                BigDecimal tongKhoanTienDtTheoDgiaDd = keHoachDauGia.getTongSoLuong().multiply(donGiaDuocDuyet).multiply(keHoachDauGia.getKhoanTienDatTruoc()).divide(BigDecimal.valueOf(100));
                keHoachDauGia.setTongKhoanTienDtTheoDgiaDd(tongKhoanTienDtTheoDgiaDd);
              }
            }
          });
          keHoachDauGiaDtl.setChildren(keHoachDauGiaPhanLoList);
        }
      }
      keHoachDauGia.setChildren(keHoachDauGiaDtlList);
    }
    if (mapDmucDvi.containsKey((keHoachDauGia.getMaDvi()))) {
      keHoachDauGia.setTenDvi(mapDmucDvi.get(keHoachDauGia.getMaDvi()));
    }
    if (mapDmucHangHoa.get((keHoachDauGia.getLoaiVthh())) != null) {
      keHoachDauGia.setTenLoaiVthh(mapDmucHangHoa.get(keHoachDauGia.getLoaiVthh()));
    }
    if (mapDmucHangHoa.get((keHoachDauGia.getCloaiVthh())) != null) {
      keHoachDauGia.setTenCloaiVthh(mapDmucHangHoa.get(keHoachDauGia.getCloaiVthh()));
    }
    keHoachDauGia.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(keHoachDauGia.getTrangThai()));

    List<FileDinhKem> fileDinhKems = fileDinhKemService.search(keHoachDauGia.getId(), Arrays.asList(XhDxKhBanDauGia.TABLE_NAME));
    if (!CollectionUtils.isEmpty(fileDinhKems)) keHoachDauGia.setFileDinhKems(fileDinhKems);
    return keHoachDauGia;
  }

  @Override
  public XhDxKhBanDauGia approve(XhDxKhBanDauGiaReq req) throws Exception {
    UserInfo userInfo = SecurityContextService.getUser();
    if (userInfo == null) throw new Exception("Bad request.");

    Optional<XhDxKhBanDauGia> optional = xhDxKhBanDauGiaRepository.findById(req.getId());
    if (!optional.isPresent()) throw new Exception("Kế hoạch bán đấu giá không tồn tại");

    XhDxKhBanDauGia keHoachDauGia = optional.get();
    String status = req.getTrangThai() + keHoachDauGia.getTrangThai();
    switch (status) {
      case Contains.CHODUYET_TP + Contains.DUTHAO:
      case Contains.CHODUYET_TP + Contains.TUCHOI_TP:
      case Contains.CHODUYET_TP + Contains.TUCHOI_LDC:
      case Contains.CHODUYET_TP + Contains.TU_CHOI_CBV:
        keHoachDauGia.setNguoiGuiDuyetId(userInfo.getId());
        keHoachDauGia.setNgayGuiDuyet(LocalDate.now());
      case Contains.TUCHOI_TP + Contains.CHODUYET_TP:
      case Contains.TUCHOI_LDC + Contains.CHODUYET_LDC:
      case Contains.TU_CHOI_CBV + Contains.DADUYET_LDC:
        keHoachDauGia.setNguoiPduyetId(userInfo.getId());
        keHoachDauGia.setNgayPduyet(LocalDate.now());
        keHoachDauGia.setLyDoTuChoi(req.getLyDoTuChoi());
        break;
      case Contains.CHODUYET_LDC + Contains.CHODUYET_TP:
      case Contains.DADUYET_LDC + Contains.CHODUYET_LDC:
      case Contains.DA_DUYET_CBV + Contains.DADUYET_LDC:
        keHoachDauGia.setNguoiPduyetId(userInfo.getId());
        keHoachDauGia.setNgayPduyet(LocalDate.now());
        break;
      default:
        throw new Exception("Phê duyệt không thành công");
    }
    keHoachDauGia.setTrangThai(req.getTrangThai());
    return xhDxKhBanDauGiaRepository.save(keHoachDauGia);
  }

  @Override
  public void delete(Long id) throws Exception {
    UserInfo userInfo = SecurityContextService.getUser();
    if (userInfo == null) throw new Exception("Bad request.");

    if (StringUtils.isEmpty(id)) throw new Exception("Xóa thất bại không tìm thấy dữ liệu");

    Optional<XhDxKhBanDauGia> optional = xhDxKhBanDauGiaRepository.findById(id);
    if (!optional.isPresent()) throw new Exception("Không tìm thấy dữ liệu cần xóa");

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
    UserInfo userInfo = SecurityContextService.getUser();
    if (userInfo == null) throw new Exception("Bad request.");

    if (StringUtils.isEmpty(listMulti)) throw new Exception("Xóa thất bại, không tìm thấy dữ liệu");

    List<XhDxKhBanDauGia> listDg = xhDxKhBanDauGiaRepository.findByIdIn(listMulti);
    if (listDg.isEmpty()) throw new Exception("Không tìm thấy dữ liệu cần xóa");

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

  @Override
  public BigDecimal getGiaBanToiThieu(String cloaiVthh, String maDvi, Integer namKh) {
    if (cloaiVthh.startsWith("02")) {
      return xhDxKhBanDauGiaRepository.getGiaBanToiThieuVt(cloaiVthh, namKh);
    }else {
      return xhDxKhBanDauGiaRepository.getGiaBanToiThieuLt(cloaiVthh, maDvi, namKh);
    }
  }
}