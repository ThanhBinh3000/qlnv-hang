package com.tcdt.qlnvhang.service.xuathang.xuattheophuongthucdaugia.dexuatkehoach;

import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.repository.FileDinhKemRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuattheophuongthucdaugia.*;
import com.tcdt.qlnvhang.repository.xuathang.xuattheophuongthucdaugia.tochuctrienkhai.XhTcTtinBdgHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.xuattheophuongthucdaugia.*;
import com.tcdt.qlnvhang.service.feign.KeHoachService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.*;
import com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.tochuctrienkhai.XhTcTtinBdgHdr;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.ObjectMapperUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class XhQdPdKhBdgService extends BaseServiceImpl {


  @Autowired
  private XhQdPdKhBdgRepository xhQdPdKhBdgRepository;

  @Autowired
  private XhQdPdKhBdgDtlRepository xhQdPdKhBdgDtlRepository;

  @Autowired
  private XhQdPdKhBdgPlRepository xhQdPdKhBdgPlRepository;

  @Autowired
  private XhQdPdKhBdgPlDtlRepository xhQdPdKhBdgPlDtlRepository;

  @Autowired
  private XhThopDxKhBdgRepository xhThopDxKhBdgRepository;

  @Autowired
  private XhDxKhBanDauGiaRepository xhDxKhBanDauGiaRepository;

  @Autowired
  FileDinhKemService fileDinhKemService;

  @Autowired
  FileDinhKemRepository fileDinhKemRepository;

  @Autowired
  private KeHoachService keHoachService;

  @Autowired
  private XhTcTtinBdgHdrRepository xhTcTtinBdgHdrRepository;

  public Page<XhQdPdKhBdg> searchPage(XhQdPdKhBdgSearchReq objReq) throws Exception {
    Pageable pageable = PageRequest.of(objReq.getPaggingReq().getPage(),
        objReq.getPaggingReq().getLimit(), Sort.by("id").descending());
    Page<XhQdPdKhBdg> data = xhQdPdKhBdgRepository.searchPage(
        objReq.getNamKh(),
        objReq.getSoQdPd(),
        objReq.getTrichYeu(),
        Contains.convertDateToString(objReq.getNgayKyQdTu()),
        Contains.convertDateToString(objReq.getNgayKyQdDen()),
        objReq.getSoTrHdr(),
        objReq.getLoaiVthh(),
        objReq.getTrangThai(),
        objReq.getLastest(),
        objReq.getMaDvi(),
        pageable);
    Map<String, String> hashMapDmHh = getListDanhMucHangHoa();
    Map<String, String> listDanhMucDvi = getListDanhMucDvi(null, null, "01");

    data.getContent().forEach(f -> {
      f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
      f.setTenLoaiVthh(StringUtils.isEmpty(f.getLoaiVthh()) ? null : hashMapDmHh.get(f.getLoaiVthh()));
      f.setTenCloaiVthh(StringUtils.isEmpty(f.getCloaiVthh()) ? null : hashMapDmHh.get(f.getCloaiVthh()));
      List<XhQdPdKhBdgDtl> listDtl = xhQdPdKhBdgDtlRepository.findAllByIdQdHdr(f.getId());
      listDtl.forEach( x -> {
        x.setTenDvi(listDanhMucDvi.get(x.getMaDvi()));
      });
      f.setChildren(listDtl);
    });
    return data;
  }

  @Transactional
  public XhQdPdKhBdg create(XhQdPdKhBdgReq objReq) throws Exception {

    if (!StringUtils.isEmpty(objReq.getSoQdPd())) {
      List<XhQdPdKhBdg> checkSoQd = xhQdPdKhBdgRepository.findBySoQdPd(objReq.getSoQdPd());
      if (!checkSoQd.isEmpty()) {
        throw new Exception("Số quyết định " + objReq.getSoQdPd() + " đã tồn tại");
      }
    }

    if (objReq.getPhanLoai().equals("TH")) {
      Optional<XhThopDxKhBdg> qOptionalTh = xhThopDxKhBdgRepository.findById(objReq.getIdThHdr());
      if (!qOptionalTh.isPresent()) {
        throw new Exception("Không tìm thấy tổng hợp kế hoạch bán đấu giá");
      }
    } else {
      Optional<XhDxKhBanDauGia> byId = xhDxKhBanDauGiaRepository.findById(objReq.getIdTrHdr());
      if (!byId.isPresent()) {
        throw new Exception("Không tìm thấy đề xuất kế hoạch bán đấu giá");
      }
    }

    XhQdPdKhBdg dataMap = new XhQdPdKhBdg();
    BeanUtils.copyProperties(objReq,dataMap,"id");
    dataMap.setNgayTao(getDateTimeNow());
    dataMap.setNguoiTaoId(getUser().getId());
    dataMap.setTrangThai(Contains.DUTHAO);
    dataMap.setLastest(false);
    dataMap.setMaDvi(getUser().getDvql());
    xhQdPdKhBdgRepository.save(dataMap);
    List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), dataMap.getId(), "XH_DX_KH_BAN_DAU_GIA");
    dataMap.setFileDinhKems(fileDinhKems);

    // Update trạng thái tổng hợp dxkhclnt
    if (objReq.getPhanLoai().equals("TH")) {
      xhThopDxKhBdgRepository.updateTrangThai(dataMap.getIdThHdr(), Contains.DADUTHAO_QD);
    } else {
      xhDxKhBanDauGiaRepository.updateStatusInList(Arrays.asList(objReq.getSoTrHdr()), Contains.DADUTHAO_QD);
      xhDxKhBanDauGiaRepository.updateSoQdPd(Arrays.asList(objReq.getSoTrHdr()), dataMap.getSoQdPd());
      xhDxKhBanDauGiaRepository.updateNgayKyQd(Arrays.asList(objReq.getSoTrHdr()), dataMap.getNgayKyQd());
    }

    saveDetail(objReq, dataMap);
    return dataMap;

  }

  @Transactional
  public XhQdPdKhBdg createLuongThuc(XhQdPdKhBdgReq objReq) throws Exception {
    if (objReq.getLoaiVthh() == null || !Contains.mpLoaiVthh.containsKey(objReq.getLoaiVthh()))
      throw new Exception("Loại vật tư hàng hóa không phù hợp");

    if (!StringUtils.isEmpty(objReq.getSoQdPd())) {
      List<XhQdPdKhBdg> checkSoQd = xhQdPdKhBdgRepository.findBySoQdPd(objReq.getSoQdPd());
      if (!checkSoQd.isEmpty()) {
        throw new Exception("Số quyết định " + objReq.getSoQdPd() + " đã tồn tại");
      }
    }
    if (objReq.getPhanLoai().equals("TH")) {
      Optional<XhThopDxKhBdg> qOptionalTh = xhThopDxKhBdgRepository.findById(objReq.getIdThHdr());
      if (!qOptionalTh.isPresent()) {
        throw new Exception("Không tìm thấy tổng hợp kế hoạch bán đấu giá");
      }
    } else {
      Optional<XhDxKhBanDauGia> byId = xhDxKhBanDauGiaRepository.findById(objReq.getIdTrHdr());
      if (!byId.isPresent()) {
        throw new Exception("Không tìm thấy đề xuất kế hoạch bán đấu giá");
      }
    }
    XhQdPdKhBdg dataMap = ObjectMapperUtils.map(objReq, XhQdPdKhBdg.class);

    dataMap.setNgayTao(getDateTimeNow());
    dataMap.setTrangThai(Contains.DUTHAO);
    dataMap.setLastest(objReq.getLastest());
    dataMap.setMaDvi(getUser().getDvql());
    XhQdPdKhBdg created = xhQdPdKhBdgRepository.save(dataMap);
    List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), dataMap.getId(), "XH_DX_KH_BAN_DAU_GIA");
    created.setFileDinhKems(fileDinhKems);

    // Update trạng thái tổng hợp dxkhclnt
    if (objReq.getPhanLoai().equals("TH")) {
      xhThopDxKhBdgRepository.updateTrangThai(dataMap.getIdThHdr(), Contains.DADUTHAO_QD);
    } else {
      xhDxKhBanDauGiaRepository.updateStatusInList(Arrays.asList(objReq.getSoTrHdr()), Contains.DADUTHAO_QD);
      xhDxKhBanDauGiaRepository.updateSoQdPd(Arrays.asList(objReq.getSoTrHdr()), dataMap.getSoQdPd());
      xhDxKhBanDauGiaRepository.updateNgayKyQd(Arrays.asList(objReq.getSoTrHdr()), dataMap.getNgayKyQd());
    }
    saveDetail(objReq, dataMap);
    return dataMap;
  }

  @Transactional
  XhQdPdKhBdg createVatTu(XhQdPdKhBdgReq objReq) throws Exception {
    List<XhQdPdKhBdg> checkSoQd = xhQdPdKhBdgRepository.findBySoQdPd(objReq.getSoQdPd());
    if (!checkSoQd.isEmpty()) {
      throw new Exception("Số quyết định " + objReq.getSoQdPd() + " đã tồn tại");
    }

    XhQdPdKhBdg dataMap = ObjectMapperUtils.map(objReq, XhQdPdKhBdg.class);
    dataMap.setNgayTao(getDateTimeNow());
    dataMap.setTrangThai(Contains.DUTHAO);
    dataMap.setLastest(objReq.getLastest());
    dataMap.setMaDvi(getUser().getDvql());
    XhQdPdKhBdg created = xhQdPdKhBdgRepository.save(dataMap);
    List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), dataMap.getId(), "XH_DX_KH_BAN_DAU_GIA");
    created.setFileDinhKems(fileDinhKems);

    // Update trạng thái tờ trình
    xhDxKhBanDauGiaRepository.updateStatusInList(Arrays.asList(objReq.getSoTrHdr()), Contains.DADUTHAO_QD);

    saveDetail(objReq, dataMap);

    return created;
  }

  @Transactional
  void saveDetail(XhQdPdKhBdgReq objReq, XhQdPdKhBdg dataMap) {
    xhQdPdKhBdgDtlRepository.deleteAllByIdQdHdr(dataMap.getId());
    for (XhQdPdKhBdgDtlReq dtl : objReq.getChildren()) {
      XhQdPdKhBdgDtl qd = ObjectMapperUtils.map(dtl, XhQdPdKhBdgDtl.class);
      xhQdPdKhBdgPlRepository.deleteByIdQdDtl(qd.getId());
      qd.setId(null);
      qd.setIdQdHdr(dataMap.getId());
      qd.setTrangThai(Contains.CHUACAPNHAT);
      xhQdPdKhBdgDtlRepository.save(qd);
      for (XhQdPdKhBdgPlReq plReq : ObjectUtils.isEmpty(dtl.getDsPhanLoList()) ? dtl.getChildren() : dtl.getDsPhanLoList()) {
        XhQdPdKhBdgPl pl = new ModelMapper().map(plReq, XhQdPdKhBdgPl.class);
        pl.setId(null);
        pl.setIdQdDtl(qd.getId());
        pl.setTrangThai(Contains.CHUACAPNHAT);
        xhQdPdKhBdgPlRepository.save(pl);
        xhQdPdKhBdgPlDtlRepository.deleteAllByIdPhanLo(plReq.getId());
        for (XhQdPdKhBdgPlDtlReq plDtlReq : plReq.getChildren()) {
          XhQdPdKhBdgPlDtl plDtl = new ModelMapper().map(plDtlReq, XhQdPdKhBdgPlDtl.class);
          plDtl.setId(null);
          plDtl.setIdPhanLo(pl.getId());
          plDtl.setIdQdHdr(dataMap.getId());
          xhQdPdKhBdgPlDtlRepository.save(plDtl);
        }
      }
    }
  }


//    public void validateData(XhQdPdKhBdg objHdr) throws Exception{
//        for (XhQdPdKhBdgDtl dtl : objHdr.getChildren()){
//            for (XhQdPdKhBdgPl dsgthau : dtl.getChildren()){
//                BigDecimal aLong =xhDxKhBanDauGiaRepository.countSLDalenKh(objHdr.getNamKh(), objHdr.getLoaiVthh(), dsgthau.getMaDvi(),NhapXuatHangTrangThaiEnum.BAN_HANH.getId());
//                BigDecimal soLuongTotal = aLong.add(dsgthau.getSoLuong());
//                BigDecimal nhap = keHoachService.getChiTieuNhapXuat(objHdr.getNamKh(),objHdr.getLoaiVthh(), dsgthau.getMaDvi(), "NHAP" );
//                if (soLuongTotal.compareTo(nhap) >0){
//                    throw new Exception(dsgthau.getTenDvi()+ "Đã nhập quá số lượng chỉ tiêu vui lòng nhập lại");
//                }
//            }
//        }
//    }

  @Transactional
  public XhQdPdKhBdg update(XhQdPdKhBdgReq objReq) throws Exception {
    // Vật tư
    if (objReq.getLoaiVthh().startsWith("02")) {
      return updateVatTu(objReq);
    } else {
      // Lương thực
      return updateLuongThuc(objReq);
    }
  }

  @Transactional
  public XhQdPdKhBdg updateLuongThuc(XhQdPdKhBdgReq objReq) throws Exception {
    if (StringUtils.isEmpty(objReq.getId())) {
      throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");
    }

    Optional<XhQdPdKhBdg> qOptional = xhQdPdKhBdgRepository.findById(objReq.getId());
    if (!qOptional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu cần sửa");
    }

    if (!StringUtils.isEmpty(objReq.getSoQdPd())) {
      if (!objReq.getSoQdPd().equals(qOptional.get().getSoQdPd())) {
        List<XhQdPdKhBdg> checkSoQd = xhQdPdKhBdgRepository.findBySoQdPd(objReq.getSoQdPd());
        if (!checkSoQd.isEmpty()) {
          throw new Exception("Số quyết định " + objReq.getSoQdPd() + " đã tồn tại");
        }
      }
    }

    if (objReq.getPhanLoai().equals("TH")) {
      Optional<XhThopDxKhBdg> qOptionalTh = xhThopDxKhBdgRepository.findById(objReq.getIdThHdr());
      if (!qOptionalTh.isPresent()) {
        throw new Exception("Không tìm thấy tổng hợp kế hoạch bán đấu giá");
      }
    } else {
      Optional<XhDxKhBanDauGia> byId = xhDxKhBanDauGiaRepository.findById(objReq.getIdTrHdr());
      if (!byId.isPresent()) {
        throw new Exception("Không tìm thấy đề xuất kế hoạch bán đấu giá");
      }
    }

    XhQdPdKhBdg dataDB = qOptional.get();
    XhQdPdKhBdg dataMap = ObjectMapperUtils.map(objReq, XhQdPdKhBdg.class);

    updateObjectToObject(dataDB, dataMap);

    dataDB.setNgaySua(getDateTimeNow());

    xhQdPdKhBdgRepository.save(dataDB);
    this.saveDetail(objReq, dataDB);

    return dataDB;

  }

  @Transactional
  public XhQdPdKhBdg updateVatTu(XhQdPdKhBdgReq objReq) throws Exception {
    if (StringUtils.isEmpty(objReq.getId())) {
      throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");
    }

    Optional<XhQdPdKhBdg> qOptional = xhQdPdKhBdgRepository.findById(objReq.getId());
    if (!qOptional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu cần sửa");
    }

    if (!qOptional.get().getSoQdPd().equals(objReq.getSoQdPd())) {
      List<XhQdPdKhBdg> checkSoQd = xhQdPdKhBdgRepository.findBySoQdPd(objReq.getSoQdPd());
      if (!checkSoQd.isEmpty()) {
        throw new Exception("Số quết định" + objReq.getSoQdPd() + " đã tồn tại");
      }
    }
    XhQdPdKhBdg dataDB = qOptional.get();
    XhQdPdKhBdg dataMap = ObjectMapperUtils.map(objReq, XhQdPdKhBdg.class);


    updateObjectToObject(dataDB, dataMap);

    dataDB.setNgaySua(getDateTimeNow());

    xhQdPdKhBdgRepository.save(dataDB);

    xhQdPdKhBdgDtlRepository.deleteAllByIdQdHdr(dataMap.getId());
    XhQdPdKhBdgDtl qdDtl = new XhQdPdKhBdgDtl();
    qdDtl.setId(null);
    qdDtl.setIdQdHdr(dataDB.getId());
    qdDtl.setMaDvi(getUser().getDvql());
    qdDtl.setTrangThai(Contains.CHUACAPNHAT);
    xhQdPdKhBdgDtlRepository.save(qdDtl);

    this.saveDetail(objReq, dataDB);

    return dataDB;
  }

  public XhQdPdKhBdg detail(String ids) throws Exception {
    if (StringUtils.isEmpty(ids))
      throw new Exception("Không tồn tại bản ghi");

    Optional<XhQdPdKhBdg> qOptional = xhQdPdKhBdgRepository.findById(Long.parseLong(ids));


    if (!qOptional.isPresent())
      throw new UnsupportedOperationException("Không tồn tại bản ghi");

    Map<String, String> hashMapDmHh = getListDanhMucHangHoa();
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    Map<String, String> hashMapLoaiHdong = getListDanhMucChung("LOAI_HDONG");

    qOptional.get().setTenLoaiVthh(StringUtils.isEmpty(qOptional.get().getLoaiVthh()) ? null : hashMapDmHh.get(qOptional.get().getLoaiVthh()));
    qOptional.get().setTenCloaiVthh(StringUtils.isEmpty(qOptional.get().getCloaiVthh()) ? null : hashMapDmHh.get(qOptional.get().getCloaiVthh()));
    qOptional.get().setTenDvi(mapDmucDvi.get(qOptional.get().getMaDvi()));
    List<XhQdPdKhBdgDtl> xhQdPdKhBdgDtlList = new ArrayList<>();
    for (XhQdPdKhBdgDtl dtl : xhQdPdKhBdgDtlRepository.findAllByIdQdHdr(Long.parseLong(ids))) {
      List<XhQdPdKhBdgPl> xhQdPdKhBdgPlList = new ArrayList<>();
      for (XhQdPdKhBdgPl dsg : xhQdPdKhBdgPlRepository.findByIdQdDtl(dtl.getId())) {
        List<XhQdPdKhBdgPlDtl> xhQdPdKhBdgPlDtlList = xhQdPdKhBdgPlDtlRepository.findByIdPhanLo(dsg.getId());
        xhQdPdKhBdgPlDtlList.forEach(f -> {
          f.setTenDvi(mapDmucDvi.get(f.getMaDvi()));
          f.setTenDiemKho(mapDmucDvi.get(f.getMaDiemKho()));
          f.setTenNhakho(mapDmucDvi.get(f.getMaNhaKho()));
          f.setTenNganKho(mapDmucDvi.get(f.getMaNganKho()));
          f.setTenLoKho(mapDmucDvi.get(f.getMaLoKho()));
          f.setTenLoaiVthh(hashMapDmHh.get(f.getLoaiVthh()));
          f.setTenCloaiVthh(hashMapDmHh.get(f.getCloaiVthh()));
        });
        dsg.setTenDvi(mapDmucDvi.get(dsg.getMaDvi()));
        dsg.setTenDiemKho(mapDmucDvi.get(dsg.getMaDiemKho()));
        dsg.setTenNhakho(mapDmucDvi.get(dsg.getMaNhaKho()));
        dsg.setTenNganKho(mapDmucDvi.get(dsg.getMaNganKho()));
        dsg.setTenLoKho(mapDmucDvi.get(dsg.getMaLoKho()));
        dsg.setTenLoaiVthh(hashMapLoaiHdong.get(dsg.getTenLoaiVthh()));
        dsg.setTenCloaiVthh(hashMapDmHh.get(dsg.getCloaiVthh()));
        dsg.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(dsg.getTrangThai()));
        dsg.setChildren(xhQdPdKhBdgPlDtlList);
        xhQdPdKhBdgPlList.add(dsg);
      }
      ;
      dtl.setTenDvi(StringUtils.isEmpty(dtl.getMaDvi()) ? null : mapDmucDvi.get(dtl.getMaDvi()));
      dtl.setChildren(xhQdPdKhBdgPlList);
      dtl.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(dtl.getTrangThai()));
      xhQdPdKhBdgDtlList.add(dtl);
    }
    qOptional.get().setChildren(xhQdPdKhBdgDtlList);
    qOptional.get().setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(qOptional.get().getTrangThai()));
    return qOptional.get();
  }

  public XhQdPdKhBdgDtl detailDtl(String ids) throws Exception {
    if (StringUtils.isEmpty(ids)){
      throw new Exception("Không tồn tại bản ghi");
    }
    Optional<XhQdPdKhBdgDtl> qOptional = xhQdPdKhBdgDtlRepository.findById(Long.parseLong(ids));
    if (!qOptional.isPresent()){
      throw new UnsupportedOperationException("Không tồn tại bản ghi");
    }

    XhQdPdKhBdgDtl data = qOptional.get();
    return data;
  }

  @Transactional
  public void delete(IdSearchReq idSearchReq) throws Exception {
    if (StringUtils.isEmpty(idSearchReq.getId()))
      throw new Exception("Xóa thất bại, KHông tìm thấy dữ liệu");

    Optional<XhQdPdKhBdg> optional = xhQdPdKhBdgRepository.findById(idSearchReq.getId());
    if (!optional.isPresent())
      throw new Exception("Không tìm thấy dữ liệu cần xóa");

    if (!optional.get().getTrangThai().equals(Contains.DUTHAO) && !optional.get().getTrangThai().equals(Contains.TUCHOI_LDV)) {
      throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối");
    }

    List<XhQdPdKhBdgDtl> xhQdPdKhBdgDtl = xhQdPdKhBdgDtlRepository.findAllByIdQdHdr(optional.get().getId());
    if (!CollectionUtils.isEmpty(xhQdPdKhBdgDtl)) {
      for (XhQdPdKhBdgDtl dtl : xhQdPdKhBdgDtl) {
        List<XhQdPdKhBdgPl> byIdQdDtl = xhQdPdKhBdgPlRepository.findByIdQdDtl(dtl.getId());
        for (XhQdPdKhBdgPl phanLo : byIdQdDtl) {
          xhQdPdKhBdgPlDtlRepository.deleteAllByIdPhanLo(phanLo.getId());
        }
        xhQdPdKhBdgPlRepository.deleteByIdQdDtl(dtl.getId());
      }
      xhQdPdKhBdgDtlRepository.deleteAll(xhQdPdKhBdgDtl);
    }
    xhQdPdKhBdgRepository.delete(optional.get());

    if (optional.get().getPhanLoai().equals("TH")) {
      xhThopDxKhBdgRepository.updateTrangThai(optional.get().getIdThHdr(), NhapXuatHangTrangThaiEnum.CHUATAO_QD.getId());
    } else {
      xhDxKhBanDauGiaRepository.updateStatusInList(Arrays.asList(optional.get().getSoTrHdr()), NhapXuatHangTrangThaiEnum.CHUATONGHOP.getId());
    }
  }

  public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
    if (StringUtils.isEmpty(idSearchReq.getIdList()))
      throw new Exception("Xóa thất bại, không tìm thấy dữ liệu");
    List<XhQdPdKhBdg> listHdr = xhQdPdKhBdgRepository.findAllByIdIn(idSearchReq.getIdList());
    if (listHdr.isEmpty()) {
      throw new Exception("Không tìm thấy dữ liệu cần xóa");
    }
    for (XhQdPdKhBdg bdg : listHdr) {
      if (!bdg.getTrangThai().equals(Contains.DUTHAO))
        throw new Exception("Chỉ thực hiện xóa bản nghi ở trạng thái bản nháp hoặc từ chối");
    }
    List<XhQdPdKhBdg> list = xhQdPdKhBdgRepository.findAllByIdIn(idSearchReq.getIdList());
    List<Long> idList = listHdr.stream().map(XhQdPdKhBdg::getId).collect(Collectors.toList());
    List<XhQdPdKhBdgDtl> xhQdPdKhBdgDtls = xhQdPdKhBdgDtlRepository.findAllByIdQdHdrIn(idList);
    List<Long> listIdDx = xhQdPdKhBdgDtls.stream().map(XhQdPdKhBdgDtl::getId).collect(Collectors.toList());
    List<XhQdPdKhBdgPl> xhQdPdKhBdgPls = xhQdPdKhBdgPlRepository.findAllByIdQdDtlIn(listIdDx);
    for (XhQdPdKhBdgPl plDtl : xhQdPdKhBdgPls) {
      List<XhQdPdKhBdgPlDtl> phanLoDtl = xhQdPdKhBdgPlDtlRepository.findByIdPhanLo(plDtl.getId());
      xhQdPdKhBdgPlDtlRepository.deleteAll(phanLoDtl);
    }
    xhQdPdKhBdgPlRepository.deleteAll(xhQdPdKhBdgPls);
    xhQdPdKhBdgDtlRepository.deleteAll(xhQdPdKhBdgDtls);
    xhQdPdKhBdgRepository.deleteAll(list);

  }


  public XhQdPdKhBdg approve(StatusReq stReq) throws Exception {
    if (StringUtils.isEmpty(stReq.getId())) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    XhQdPdKhBdg dataDB = detail(String.valueOf(stReq.getId()));
    String status = stReq.getTrangThai() + dataDB.getTrangThai();
    switch (status) {
      case Contains.BAN_HANH + Contains.DUTHAO:
        dataDB.setNgayPduyet(getDateTimeNow());
        dataDB.setNguoiPduyetId(getUser().getId());
        break;
      default:
        throw new Exception("Phê duyệt không thành công");
    }
    dataDB.setTrangThai(stReq.getTrangThai());

    if (stReq.getTrangThai().equals(Contains.BAN_HANH)) {
      if (dataDB.getPhanLoai().equals("TH")) {
        Optional<XhThopDxKhBdg> qOptional = xhThopDxKhBdgRepository.findById(dataDB.getIdThHdr());
        if (qOptional.isPresent()) {
          if (qOptional.get().getTrangThai().equals(Contains.DABANHANH_QD)) {
            throw new Exception("Tổng hợp kế hoạch này đã được quyết định");
          }
          xhThopDxKhBdgRepository.updateTrangThai(dataDB.getIdThHdr(), Contains.DABANHANH_QD);
        } else {
          throw new Exception("Tổng hợp kế hoạch không được tìm thấy");
        }
      } else {
        Optional<XhDxKhBanDauGia> qOptional = xhDxKhBanDauGiaRepository.findById(dataDB.getIdTrHdr());
        if (qOptional.isPresent()) {
          if (qOptional.get().getTrangThai().equals(Contains.DABANHANH_QD)) {
            throw new Exception("Đề xuất này đã được quyết định");
          }
          // Update trạng thái tờ trình
          xhDxKhBanDauGiaRepository.updateStatusInList(Arrays.asList(dataDB.getSoTrHdr()), Contains.DABANHANH_QD);
        } else {
          throw new Exception("Số tờ trình kế hoạch không được tìm thấy");
        }
      }
      this.cloneProject(dataDB.getId());
      this.cloneForToChucBdg(dataDB);
    }
    XhQdPdKhBdg createCheck = xhQdPdKhBdgRepository.save(dataDB);
    return createCheck;

  }

  @Transactional(rollbackOn = Exception.class)
  XhQdPdKhBdg approveVatTu(StatusReq stReq, XhQdPdKhBdg dataDB) throws Exception {
    String status = stReq.getTrangThai() + dataDB.getTrangThai();
    switch (status) {
      case Contains.CHODUYET_LDV + Contains.DUTHAO:
      case Contains.CHODUYET_LDV + Contains.TUCHOI_LDV:
        dataDB.setNgayGuiDuyet(getDateTimeNow());
      case Contains.TUCHOI_LDV + Contains.CHODUYET_LDV:
        dataDB.setNgayPduyet(getDateTimeNow());
      case Contains.DADUYET_LDV + Contains.CHODUYET_LDV:
      case Contains.BAN_HANH + Contains.DADUYET_LDV:
        dataDB.setNgayPduyet(getDateTimeNow());
        break;
      default:
        throw new Exception("Phê duyệt không thành công");
    }
    dataDB.setTrangThai(stReq.getTrangThai());
    if (stReq.getTrangThai().equals(Contains.BAN_HANH)) {
      Optional<XhDxKhBanDauGia> qOptional = xhDxKhBanDauGiaRepository.findById(dataDB.getIdTrHdr());
      if (qOptional.isPresent()) {
        if (qOptional.get().getTrangThai().equals(Contains.DABANHANH_QD)) {
          throw new Exception("Đề xuất này đã được quyết định");
        }
        // Update trạng thái tờ trình
        xhDxKhBanDauGiaRepository.updateStatusInList(Arrays.asList(dataDB.getSoTrHdr()), Contains.DABANHANH_QD);
      } else {
        throw new Exception("Số tờ trình kế hoạch không được tìm thấy");
      }
      this.cloneProject(dataDB.getId());
      this.cloneForToChucBdg(dataDB);
    }
    XhQdPdKhBdg createCheck = xhQdPdKhBdgRepository.save(dataDB);
    return createCheck;
  }

  @Transactional(rollbackOn = Exception.class)
  XhQdPdKhBdg approveLT(StatusReq stReq, XhQdPdKhBdg dataDB) throws Exception {
    String status = stReq.getTrangThai() + dataDB.getTrangThai();
    switch (status) {
      case Contains.BAN_HANH + Contains.DUTHAO:
        dataDB.setNgayPduyet(getDateTimeNow());
        break;
      default:
        throw new Exception("Phê duyệt không thành công");
    }
    dataDB.setTrangThai(stReq.getTrangThai());
    if (stReq.getTrangThai().equals(Contains.BAN_HANH)) {
      if (dataDB.getPhanLoai().equals("TH")) {
        Optional<XhThopDxKhBdg> qOptional = xhThopDxKhBdgRepository.findById(dataDB.getIdThHdr());
        if (qOptional.isPresent()) {
          if (qOptional.get().getTrangThai().equals(Contains.DABANHANH_QD)) {
            throw new Exception("Tổng hợp kế hoạch này đã được quyết định");
          }
          xhThopDxKhBdgRepository.updateTrangThai(dataDB.getIdThHdr(), Contains.DABANHANH_QD);
        } else {
          throw new Exception("Tổng hợp kế hoạch không được tìm thấy");
        }
      } else {
        Optional<XhDxKhBanDauGia> qOptional = xhDxKhBanDauGiaRepository.findById(dataDB.getIdTrHdr());
        if (qOptional.isPresent()) {
          if (qOptional.get().getTrangThai().equals(Contains.DABANHANH_QD)) {
            throw new Exception("Đề xuất này đã được quyết định");
          }
          // Update trạng thái tờ trình
          xhDxKhBanDauGiaRepository.updateStatusInList(Arrays.asList(dataDB.getSoTrHdr()), Contains.DABANHANH_QD);
        } else {
          throw new Exception("Số tờ trình kế hoạch không được tìm thấy");
        }
      }
//            this.validateData(dataDB);
      this.cloneProject(dataDB.getId());
      this.cloneForToChucBdg(dataDB);
    }
    XhQdPdKhBdg createCheck = xhQdPdKhBdgRepository.save(dataDB);
    return createCheck;
  }

  private void cloneProject(Long idClone) throws Exception {
    XhQdPdKhBdg hdr = this.detail(idClone.toString());
    XhQdPdKhBdg hdrClone = new XhQdPdKhBdg();
    BeanUtils.copyProperties(hdr, hdrClone);
    hdrClone.setId(null);
    hdrClone.setLastest(true);
    hdrClone.setIdGoc(hdr.getId());
    xhQdPdKhBdgRepository.save(hdrClone);
    for (XhQdPdKhBdgDtl dx : hdr.getChildren()) {
      XhQdPdKhBdgDtl dxClone = new XhQdPdKhBdgDtl();
      BeanUtils.copyProperties(dx, dxClone);
      dxClone.setId(null);
      dxClone.setIdQdHdr(hdrClone.getId());
      xhQdPdKhBdgDtlRepository.save(dxClone);
      for (XhQdPdKhBdgPl pl : dxClone.getChildren()) {
        XhQdPdKhBdgPl plClone = new XhQdPdKhBdgPl();
        BeanUtils.copyProperties(pl, plClone);
        plClone.setId(null);
        plClone.setIdQdDtl(dxClone.getId());
        xhQdPdKhBdgPlRepository.save(plClone);
        for (XhQdPdKhBdgPlDtl plDtl : pl.getChildren()) {
          XhQdPdKhBdgPlDtl plDtlClone = new XhQdPdKhBdgPlDtl();
          BeanUtils.copyProperties(plDtl, plDtlClone);
          plDtlClone.setId(null);
          plDtlClone.setIdPhanLo(plClone.getId());
          xhQdPdKhBdgPlDtlRepository.save(plDtlClone);
        }
      }
    }
  }

  public void export(XhQdPdKhBdgSearchReq objReq, HttpServletResponse response) throws Exception {
    PaggingReq paggingReq = new PaggingReq();
    paggingReq.setPage(0);
    paggingReq.setLimit(Integer.MAX_VALUE);
    objReq.setPaggingReq(paggingReq);
    Page<XhQdPdKhBdg> page = this.searchPage(objReq);
    List<XhQdPdKhBdg> data = page.getContent();
    String title = " Danh sách quyết định phê duyệt kế hoạch mua trưc tiếp";
    String[] rowsName = new String[]{"STT", "Năm kế hoạch", "Số QĐ PD KH BĐG", "ngày ký QĐ", "Trích yếu", "Số KH/ Tờ trình", "Mã tổng hợp", "Loại hàng hóa", "Chủng loại hàng hóa", "Số ĐV tài sản", "SL HĐ đã ký", "Trạng Thái"};
    String fileName = "danh-sach-dx-pd-kh-mua-truc-tiep.xlsx";
    List<Object[]> dataList = new ArrayList<Object[]>();
    Object[] objs = null;
    for (int i = 0; i < data.size(); i++) {
      XhQdPdKhBdg pduyet = data.get(i);
      objs = new Object[rowsName.length];
      objs[0] = i;
      objs[2] = pduyet.getSoQdPd();
      objs[3] = pduyet.getNgayKyQd();
      objs[4] = pduyet.getTrichYeu();
      objs[5] = pduyet.getSoTrHdr();
      objs[6] = pduyet.getIdThHdr();
      objs[7] = pduyet.getTenLoaiVthh();
      objs[8] = pduyet.getTenCloaiVthh();
      objs[11] = pduyet.getTenTrangThai();
      dataList.add(objs);
    }
    ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
    ex.export();
  }

  private void cloneForToChucBdg(XhQdPdKhBdg data) throws Exception {
    List<XhTcTtinBdgHdr> listXhTcTtinBdgHdr = new ArrayList<>();
    data.getChildren().forEach(s -> {
      XhTcTtinBdgHdr hdr = new XhTcTtinBdgHdr();
      hdr.setMaDvi(data.getMaDvi());
      hdr.setLoaiVthh(data.getLoaiVthh());
      hdr.setCloaiVthh(data.getCloaiVthh());
      hdr.setIdQdPdKh(data.getId());
      hdr.setSoQdPdKh(data.getSoQdPd());
      hdr.setIdQdDcKh(null);
      hdr.setSoQdDcKh(null);
      hdr.setIdQdPdKq(null);
      hdr.setSoQdPdKq(null);
      hdr.setIdKhDx(data.getIdTrHdr());
      hdr.setSoKhDx(data.getSoTrHdr());
      hdr.setNgayQdPdKqBdg(null);
      hdr.setThoiHanGiaoNhan(s.getTgianGnhan());
      hdr.setThoiHanThanhToan(s.getTgianTtoan());
      hdr.setPhuongThucThanhToan(s.getPthucTtoan());
      hdr.setPhuongThucGiaoNhan(s.getPthucGnhan());
      hdr.setTrangThai(TrangThaiAllEnum.CHUA_CAP_NHAT.getId());
      hdr.setMaDviThucHien(s.getMaDvi());
      hdr.setTongTienGiaKhoiDiem(DataUtils.safeToLong(s.getTongTienKdienDonGia()));
      hdr.setTongTienDatTruoc(DataUtils.safeToLong(s.getKhoanTienDatTruoc()));
      hdr.setTongTienDatTruocDuocDuyet(DataUtils.safeToLong(s.getTongTienDatTruocDonGia()));
      hdr.setTongSoLuong(DataUtils.safeToLong(s.getTongSoLuong()));
      hdr.setPhanTramDatTruoc(DataUtils.safeToInt(s.getKhoanTienDatTruoc()));
      if (!DataUtils.isNullObject(s.getTgianDkienTu()) && !DataUtils.isNullObject(s.getTgianDkienDen())) {
        LocalDate localDateTu = Instant.ofEpochMilli(s.getTgianDkienTu().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localDateDen = Instant.ofEpochMilli(s.getTgianDkienDen().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
        hdr.setThoiGianToChucTu(localDateTu);
        hdr.setThoiGianToChucDen(localDateDen);
      }
      hdr.setSoDviTsan(DataUtils.safeToInt(s.getSoDviTsan()));
      hdr.setSoDviTsanThanhCong(0);
      hdr.setSoDviTsanKhongThanh(0);
      listXhTcTtinBdgHdr.add(hdr);
    });
    xhTcTtinBdgHdrRepository.saveAll(listXhTcTtinBdgHdr);
  }
}
