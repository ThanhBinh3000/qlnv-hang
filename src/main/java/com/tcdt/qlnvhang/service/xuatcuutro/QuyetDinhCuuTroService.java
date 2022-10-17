package com.tcdt.qlnvhang.service.xuatcuutro;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.entities.FileDKemJoinDxuatCuuTro;
import com.tcdt.qlnvhang.entities.FileDKemJoinQdCuuTro;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.QlnvDmDonviRepository;
import com.tcdt.qlnvhang.repository.khotang.KtDiemKhoRepository;
import com.tcdt.qlnvhang.repository.khotang.KtNganKhoRepository;
import com.tcdt.qlnvhang.repository.khotang.KtNganLoRepository;
import com.tcdt.qlnvhang.repository.khotang.KtNhaKhoRepository;
import com.tcdt.qlnvhang.repository.xuatcuutro.*;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuatcuutro.XhDxCuuTroHdrSearchReq;
import com.tcdt.qlnvhang.request.xuatcuutro.XhQdCuuTroHdrSearchReq;
import com.tcdt.qlnvhang.request.xuatcuutro.XhThCuuTroHdrSearchReq;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.*;
import com.tcdt.qlnvhang.table.catalog.QlnvDmDonvi;
import com.tcdt.qlnvhang.table.khotang.KtDiemKho;
import com.tcdt.qlnvhang.table.khotang.KtNganKho;
import com.tcdt.qlnvhang.table.khotang.KtNganLo;
import com.tcdt.qlnvhang.table.khotang.KtNhaKho;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.ObjectMapperUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.text.MessageFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.tcdt.qlnvhang.util.Contains.CAP_TONG_CUC;

@Service
@Log4j2
public class QuyetDinhCuuTroService extends BaseServiceImpl {
  @Autowired
  private DeXuatCuuTroRepository deXuatCuuTroRepository;
  @Autowired
  private DeXuatCuuTroDtlRepository deXuatCuuTroDtlRepository;
  @Autowired
  private DeXuatCuuTroKhoRepository deXuatCuuTroKhoRepository;
  @Autowired
  private KtNganLoRepository ktNganLoRepository;
  @Autowired
  private KtDiemKhoRepository ktDiemKhoRepository;
  @Autowired
  private KtNhaKhoRepository ktNhaKhoRepository;
  @Autowired
  private KtNganKhoRepository ktNganKhoRepository;
  @Autowired
  private QlnvDmDonviRepository dmDonviRepository;
  @Autowired
  private FileDinhKemService fileDinhKemService;
  @Autowired
  private DeXuatCuuTroService deXuatCuuTroService;
  @Autowired
  private TongHopCuuTroRepository tongHopCuuTroRepository;
  @Autowired
  private TongHopCuuTroDtlRepository tongHopCuuTroDtlRepository;
  @Autowired
  private TongHopCuuTroKhoRepository tongHopCuuTroKhoRepository;
  @Autowired
  private QuyetDinhCuuTroRepository quyetDinhCuuTroRepository;
  @Autowired
  private QuyetDinhCuuTroDtlRepository quyetDinhCuuTroDtlRepository;

  public Page<XhQdCuuTroHdr> searchPage(CustomUserDetails currentUser, XhQdCuuTroHdrSearchReq req) throws Exception {
    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
    req.setDvql(currentUser.getDvql());
    Page<XhQdCuuTroHdr> search = quyetDinhCuuTroRepository.search(req, pageable);
//    Map<String, String> mapLoaiHinhNx = getListDanhMucChung("LOAI_HINH_NHAP_XUAT");
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    search.getContent().forEach(s -> {
      if (mapDmucDvi.get((s.getMaDvi())) != null) {
        s.setTenDvi(mapDmucDvi.get(s.getMaDvi()));
      }
      if (mapVthh.get((s.getLoaiVthh())) != null) {
        s.setTenLoaiVthh(mapVthh.get(s.getLoaiVthh()));
      }
      if (mapVthh.get((s.getCloaiVthh())) != null) {
        s.setTenCloaiVthh(mapVthh.get(s.getCloaiVthh()));
      }
    });
    return search;
  }

  public XhQdCuuTroHdr detail(CustomUserDetails currentUser, Long id) throws Exception {
    if (DataUtils.isNullObject(id))
      throw new Exception("Tham số không hợp lệ.");
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();

    //set thong tin de xuat
    Optional<XhQdCuuTroHdr> currentHdr = quyetDinhCuuTroRepository.findById(id);
    if (currentHdr.isPresent()) {
      XhQdCuuTroHdr data = currentHdr.get();
      //set thong tin de xuat
      List<XhDxCuuTroDtl> deXuatDtl = deXuatCuuTroDtlRepository.findByIdTongHop(data.getIdTongHop());
      data.setThongTinDeXuat(deXuatDtl);
      //set thong tin tong hop
      List<XhThCuuTroDtl> tongHopDtl = tongHopCuuTroDtlRepository.findByIdTongHop(data.getIdTongHop());
      if (!DataUtils.isNullOrEmpty(tongHopDtl)) {
        tongHopDtl.forEach(s -> {
          s.setTenDvi(mapDmucDvi.get(s.getMaDvi()));
        });
        data.setThongTinTongHop(tongHopDtl);
      }

      //set file dinh kem va can cu
      List<FileDinhKem> fileDinhKem = fileDinhKemService.search(id, Arrays.asList(XhQdCuuTroHdr.TABLE_NAME));
      if (!DataUtils.isNullOrEmpty(fileDinhKem)) {
        data.setFileDinhKem(fileDinhKem.get(0));
      }
      List<FileDinhKem> canCu = fileDinhKemService.search(id, Arrays.asList(XhQdCuuTroHdr.TABLE_NAME + "_CAN_CU"));
      data.setCanCu(canCu);

      //set thong tin quyet dinh
      List<XhQdCuuTroDtl> quyetDinhDtl = quyetDinhCuuTroDtlRepository.findByIdQd(id);
      data.setThongTinQuyetDinh(quyetDinhDtl);
      data.setTenDvi(mapDmucDvi.get(data.getMaDvi()));
      data.setTenLoaiVthh(mapVthh.get(data.getLoaiVthh()));
      data.setTenCloaiVthh(mapVthh.get(data.getLoaiVthh()));
      return data;
    }

    //Map<String, String> mapLoaiGia = qlnvDmService.getListDanhMucChung("LOAI_GIA");
    return currentHdr.get();
  }

  @Transactional(rollbackFor = Exception.class)
  public XhQdCuuTroHdr create(CustomUserDetails currentUser, XhQdCuuTroHdrSearchReq req) throws Exception {
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    /*if (DataUtils.isNullOrEmpty(req.getSoDxuat())) {
      throw new Exception("Loại hình nhập xuất thiếu hoặc không hợp lệ.");
    }*/
    if (DataUtils.isNullOrEmpty(req.getSoQd())) {
      throw new Exception("Số quyết định thiếu hoặc không hợp lệ.");
    }
    if (DataUtils.isNullOrEmpty(req.getLoaiVthh())) {
      throw new Exception("Loại hàng hóa thiếu hoặc không hợp lệ.");
    }
    if (DataUtils.isNullOrEmpty(req.getCloaiVthh())) {
      throw new Exception("Chủng loại hàng hóa thiếu hoặc không hợp lệ.");
    }
    Optional<XhQdCuuTroHdr> optional = quyetDinhCuuTroRepository.findFirstBySoQdAndNam(req.getSoQd(), req.getNam());
    if (optional.isPresent()) {
      throw new Exception(MessageFormat.format("Số quyết định {0} đã tồn tại", req.getSoQd()));
    }
    XhQdCuuTroHdr newRow = new XhQdCuuTroHdr();
    BeanUtils.copyProperties(req, newRow, "id");
    newRow.setTrangThai(TrangThaiAllEnum.DU_THAO.getId());
    newRow.setMaDvi(currentUser.getDvql());
//    newRow.setCapDvi(currentUser.getUser().getCapDvi());
    newRow = quyetDinhCuuTroRepository.save(newRow);

    //luu can cu dinh kem
    if (!DataUtils.isNullOrEmpty(req.getCanCu())) {
      fileDinhKemService.saveListFileDinhKem(req.getCanCu(), newRow.getId(), XhQdCuuTroHdr.TABLE_NAME + "_CAN_CU");
    }
    if (!DataUtils.isNullObject(req.getFileDinhKem())) {
      fileDinhKemService.saveListFileDinhKem(Arrays.asList(req.getFileDinhKem()), newRow.getId(), XhQdCuuTroHdr.TABLE_NAME);
    }
    //luu thong tin chi tiet
    List<XhQdCuuTroDtl> listDxHdr = new ArrayList();
    if (!DataUtils.isNullOrEmpty(req.getThongTinTongHop())) {
      listDxHdr = ObjectMapperUtils.mapAll(req.getThongTinTongHop(), XhQdCuuTroDtl.class);
      XhQdCuuTroHdr finalNewRow = newRow;
      listDxHdr.forEach(s -> {
        s.setIdQd(finalNewRow.getId());
        s.setMaDvi(finalNewRow.getMaDvi());
      });
      quyetDinhCuuTroDtlRepository.saveAll(listDxHdr);
      //luu nhap kho
        /*List<XhThCuuTroKho> phuongAnXuat = new ArrayList();
        if (!DataUtils.isNullOrEmpty(s.getPhuongAnXuat())) {
          phuongAnXuat = ObjectMapperUtils.mapAll(s.getPhuongAnXuat(), XhThCuuTroKho.class);
          phuongAnXuat.forEach(s1 -> {
            s1.setIdTongHop(finalNewRow.getId());
            s1.setIdTongHopDtl(newRowDtl.getId());
          });
          tongHopCuuTroKhoRepository.saveAll(phuongAnXuat);
        }*/

    }
    return newRow;
  }

  @Transactional(rollbackFor = Exception.class)
  public XhQdCuuTroHdr update(CustomUserDetails currentUser, XhQdCuuTroHdrSearchReq req) throws Exception {
    if (DataUtils.isNullObject(req.getId()))
      throw new Exception("Tham số không hợp lệ.");
    XhQdCuuTroHdr currentRow = quyetDinhCuuTroRepository.findById(req.getId()).orElse(null);
    if (DataUtils.isNullObject(currentRow))
      throw new Exception("Không tìm thấy dữ liệu.");
    XhQdCuuTroHdr validateRow = quyetDinhCuuTroRepository.findFirstBySoQdAndNam(currentRow.getSoQd(), currentRow.getNam()).get();
    if (!DataUtils.isNullObject(validateRow) && currentRow.getId() != req.getId()) {
      throw new Exception(MessageFormat.format("Số đề xuất {0} đã tồn tại", req.getSoDxuat()));
    }
    DataUtils.copyProperties(req, currentRow, "id", "trangThaiTh", "fileDinhKem", "thongTinChiTiet", "phuongAnXuat");

    if (DataUtils.isNullOrEmpty(currentRow.getLoaiVthh())) {
      throw new Exception("Loại hàng hóa thiếu hoặc không hợp lệ.");
    }
    if (DataUtils.isNullOrEmpty(currentRow.getCloaiVthh())) {
      throw new Exception("Chủng loại hàng hóa thiếu hoặc không hợp lệ.");
    }
    currentRow = quyetDinhCuuTroRepository.save(currentRow);

    //luu can cu dinh kem
    if (!DataUtils.isNullOrEmpty(req.getCanCu())) {
      fileDinhKemService.saveListFileDinhKem(req.getCanCu(), currentRow.getId(), XhQdCuuTroHdr.TABLE_NAME + "_CAN_CU");
    }
    if (!DataUtils.isNullObject(req.getFileDinhKem())) {
      fileDinhKemService.saveListFileDinhKem(Arrays.asList(req.getFileDinhKem()), currentRow.getId(), XhQdCuuTroHdr.TABLE_NAME);
    }
    //luu thong tin chi tiet
    List<XhQdCuuTroDtl> thongTinChiTiet = new ArrayList();
    if (!DataUtils.isNullOrEmpty(req.getThongTinQuyetDinh())) {
      List<Long> listDtlReq = req.getThongTinQuyetDinh().stream().map(XhQdCuuTroDtl::getId).collect(Collectors.toList());
      List<XhQdCuuTroDtl> listDtlCur = quyetDinhCuuTroDtlRepository.findByIdQd(currentRow.getId());
      List<Long> listRemoveId = listDtlCur.stream().map(XhQdCuuTroDtl::getId).collect(Collectors.toList());
      listRemoveId.removeAll(listDtlReq);
      quyetDinhCuuTroDtlRepository.deleteAllByIdIn(listRemoveId);
      //tongHopCuuTroKhoRepository.deleteAllByIdTongHopDtlIn(listRemoveId);
      //thongTinChiTiet = ObjectMapperUtils.mapAll(req.getThongTinQuyetDinh(), XhQdCuuTroDtl.class);
      quyetDinhCuuTroDtlRepository.saveAll(req.getThongTinQuyetDinh());
    }
    return currentRow;
  }

  @Transactional(rollbackFor = Exception.class)
  public boolean deleteMultiple(CustomUserDetails currentUser, List<Long> ids) throws Exception {
    if (CollectionUtils.isEmpty(ids)) throw new Exception("Bad request.");

    List<XhQdCuuTroHdr> listData = quyetDinhCuuTroRepository.findAllById(ids);
    List<XhQdCuuTroHdr> listDataValid = listData.stream()
        .filter(s -> s.getTrangThai().equals(TrangThaiAllEnum.DU_THAO.getId()))
        .collect(Collectors.toList());
    quyetDinhCuuTroRepository.deleteAll(listDataValid);
    return true;
  }

  public void export(CustomUserDetails currentUser, XhQdCuuTroHdrSearchReq req, HttpServletResponse response) throws Exception {
    PaggingReq paggingReq = new PaggingReq();
    paggingReq.setPage(0);
    paggingReq.setLimit(Integer.MAX_VALUE);
    req.setPaggingReq(paggingReq);
    Page<XhQdCuuTroHdr> page = this.searchPage(currentUser, req);
    List<XhQdCuuTroHdr> data = page.getContent();
    String title = "Danh sách Đề xuất phương án xuất cứu trợ, viện trợ";
    String[] rowsName = new String[]{"STT", "Loại hình nhập xuất", "Số công văn/đề xuất", "Đơn vị đề xuất", "Ngày đề xuất", "Loại hàng hoá", "Chủng loại hàng hóa", "Tổng SL xuất viện trợ, cứu trợ (kg)", "Trích yếu", "Trạng thái đề xuất", "Trạng thái tổng hợp"};
    String fileName = "danh-sach-de-xuat-phuong-an-xuat-cuu-tro-vien-tro.xlsx";
    List<Object[]> dataList = new ArrayList<Object[]>();
    Object[] objs = null;
    for (int i = 0; i < data.size(); i++) {
      XhQdCuuTroHdr dx = data.get(i);
      objs = new Object[rowsName.length];
      objs[0] = i;
    /*  objs[1] = dx.getTenLoaiHinhNhapXuat();
      objs[2] = dx.getSoDxuat();
      objs[3] = dx.getTenDvi();
      objs[4] = dx.getNgayDxuat();
      objs[5] = dx.getTenLoaiVthh();
      objs[6] = dx.getTenCloaiVthh();
      objs[7] = dx.getTongSoLuong();
      objs[8] = dx.getTrichYeu();
      objs[9] = dx.getTenTrangThai();
      objs[10] = dx.getTenTrangThaiTh();*/
      dataList.add(objs);
    }
    ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
    ex.export();
  }

  private void buildThongTinKho(List<XhDxCuuTroKho> responses) {
    if (!DataUtils.isNullOrEmpty(responses)) {
      Set<String> maChiCucList = responses.stream().map(XhDxCuuTroKho::getMaChiCuc).collect(Collectors.toSet());
      List<String> maLoKhoList = responses.stream().map(XhDxCuuTroKho::getMaLoKho).collect(Collectors.toList());
      List<String> maNhaKhoList = responses.stream().map(XhDxCuuTroKho::getMaNhaKho).collect(Collectors.toList());
      List<String> maDiemKhoList = responses.stream().map(XhDxCuuTroKho::getMaDiemKho).collect(Collectors.toList());
      Set<String> maNganKhoList = responses.stream().map(XhDxCuuTroKho::getMaNganKho).collect(Collectors.toSet());


      Map<String, KtNganLo> mapNganLo = ktNganLoRepository.findByMaNganloIn(maLoKhoList)
          .stream().collect(Collectors.toMap(KtNganLo::getMaNganlo, Function.identity()));

      Map<String, KtDiemKho> mapDiemKho = ktDiemKhoRepository.findByMaDiemkhoIn(maDiemKhoList)
          .stream().collect(Collectors.toMap(KtDiemKho::getMaDiemkho, Function.identity()));

      Map<String, KtNhaKho> mapNhaKho = ktNhaKhoRepository.findByMaNhakhoIn(maNhaKhoList)
          .stream().collect(Collectors.toMap(KtNhaKho::getMaNhakho, Function.identity()));

      Map<String, QlnvDmDonvi> mapChiCuc = dmDonviRepository.findByMaDviIn(maChiCucList)
          .stream().collect(Collectors.toMap(QlnvDmDonvi::getMaDvi, Function.identity()));

      Map<String, KtNganKho> mapNganKho = ktNganKhoRepository.findByMaNgankhoIn(maNganKhoList)
          .stream().collect(Collectors.toMap(KtNganKho::getMaNgankho, Function.identity()));

      for (XhDxCuuTroKho item : responses) {
        KtNganLo nganLo = mapNganLo.get(item.getMaLoKho());
        KtNhaKho nhaKho = mapNhaKho.get(item.getMaNhaKho());
        KtDiemKho diemKho = mapDiemKho.get(item.getMaDiemKho());
        QlnvDmDonvi chiCuc = mapChiCuc.get(item.getMaChiCuc());
        KtNganKho nganKho = mapNganKho.get(item.getMaNganKho());
        if (nganLo != null) item.setTenLoKho(nganLo.getTenNganlo());
        if (nhaKho != null) item.setTenNhaKho(nhaKho.getTenNhakho());
        if (diemKho != null) item.setTenDiemKho(diemKho.getTenDiemkho());
        if (chiCuc != null) item.setTenChiCuc(chiCuc.getTenDvi());
        if (nganKho != null) item.setTenNganKho(nganKho.getTenNgankho());
      }
    }
  }

  @Transactional(rollbackFor = Exception.class)
  public void deleteListId(List<Long> listId) {
    deXuatCuuTroDtlRepository.deleteAllByIdDxuatIn(listId);
    deXuatCuuTroKhoRepository.deleteAllByIdDxuatIn(listId);
    deXuatCuuTroRepository.deleteAllByIdIn(listId);
    fileDinhKemService.deleteMultiple(listId, Lists.newArrayList(XhDxCuuTroHdr.TABLE_NAME));
  }


  @Transactional(rollbackFor = Exception.class)
  public XhQdCuuTroHdr updateStatus(CustomUserDetails currentUser, StatusReq req) throws Exception {
    Optional<XhQdCuuTroHdr> currentRow = quyetDinhCuuTroRepository.findById(req.getId());
    if (!currentRow.isPresent())
      throw new Exception("Không tìm thấy dữ liệu.");
    /*if (currentUser.getUser().getCapDvi().equals(CAP_CUC) ||
        (currentUser.getUser().getCapDvi().equals(CAP_CHI_CUC))) {
      List<String> statusAllow = Arrays.asList(TrangThaiAllEnum.CHO_DUYET_TP.getId());
      if (!statusAllow.containsAll(statusAllow))
        throw new Exception("Tài khoản không có quyền thực hiện.");
    }*/
    String trangThai = "";
    String capDvi = currentUser.getUser().getCapDvi();
    String condition = currentRow.get().getTrangThai() + req.getTrangThai();
    if (capDvi.equals(CAP_TONG_CUC)) {

      //gui duyet
      if (condition.equals(TrangThaiAllEnum.DU_THAO.getId() + TrangThaiAllEnum.CHO_DUYET_LDTC.getId())) {
        trangThai = TrangThaiAllEnum.CHO_DUYET_LDTC.getId();
      } else if (condition.equals(TrangThaiAllEnum.TU_CHOI_LDTC.getId() + TrangThaiAllEnum.CHO_DUYET_LDTC.getId())) {
        trangThai = TrangThaiAllEnum.CHO_DUYET_LDTC.getId();
      }
      //duyet
      else if (condition.equals(TrangThaiAllEnum.CHO_DUYET_LDTC.getId() + TrangThaiAllEnum.DA_DUYET_LDTC.getId())) {
        trangThai = TrangThaiAllEnum.DA_DUYET_LDTC.getId();
      }
      //tu choi
      else if (condition.equals(TrangThaiAllEnum.CHO_DUYET_LDTC.getId() + TrangThaiAllEnum.TU_CHOI_LDTC.getId())) {
        trangThai = TrangThaiAllEnum.TU_CHOI_LDTC.getId();
      }
      //ban hanh
      if (condition.equals(TrangThaiAllEnum.DA_DUYET_LDTC.getId() + TrangThaiAllEnum.BAN_HANH.getId())) {
        trangThai = TrangThaiAllEnum.BAN_HANH.getId();
      }
    }
    /*else if (capDvi.equals(CAP_CUC)) {
      //gui duyet
      if (condition.equals(TrangThaiAllEnum.DU_THAO.getId() + TrangThaiAllEnum.CHO_DUYET_LDTC.getId())) {
        trangThai = TrangThaiAllEnum.CHO_DUYET_TP.getId();
      } else if (condition.equals(TrangThaiAllEnum.CHO_DUYET_TP.getId() + TrangThaiAllEnum.CHO_DUYET_LDTC.getId())) {
        trangThai = TrangThaiAllEnum.CHO_DUYET_LDC.getId();
      }
      //duyet
      else if (condition.equals(TrangThaiAllEnum.CHO_DUYET_TP.getId() + TrangThaiAllEnum.DA_DUYET_LDTC.getId())) {
        trangThai = TrangThaiAllEnum.DA_DUYET_LDC.getId();
      } else if (condition.equals(TrangThaiAllEnum.CHO_DUYET_LDC.getId() + TrangThaiAllEnum.DA_DUYET_LDTC.getId())) {
        trangThai = TrangThaiAllEnum.DA_DUYET_LDC.getId();
      }
      //tu choi
      else if (condition.equals(TrangThaiAllEnum.CHO_DUYET_TP.getId() + TrangThaiAllEnum.TU_CHOI_TP.getId())) {
        trangThai = TrangThaiAllEnum.TU_CHOI_TP.getId();
      }
      else if (condition.equals(TrangThaiAllEnum.CHO_DUYET_LDTC.getId() + TrangThaiAllEnum.TU_CHOI_LDTC.getId())) {
        trangThai = TrangThaiAllEnum.TU_CHOI_LDC.getId();
      }
    }*/
    if (DataUtils.isNullOrEmpty(trangThai)) {
      throw new Exception("Quy trình phê duyệt không hợp lệ.");
    }

    Optional<XhThCuuTroHdr> tongHopHdr = tongHopCuuTroRepository.findById(currentRow.get().getIdTongHop());
    if (tongHopHdr.isPresent()) {
      tongHopHdr.get().setTrangThai(TrangThaiAllEnum.DA_DU_THAO_QD.getId());
      // update trang thai cho bang tong hop
      if (trangThai.equals(TrangThaiAllEnum.BAN_HANH.getId())) {
        tongHopHdr.get().setIdQuyetDinh(currentRow.get().getId());
      }
      tongHopCuuTroRepository.save(tongHopHdr.get());
    }
    currentRow.get().setTrangThai(trangThai);
    if (trangThai.equals(TrangThaiAllEnum.TU_CHOI_LDTC.getId())) {
      currentRow.get().setLyDoTuChoi(DataUtils.safeToString(req.getLyDo()));
    }
    quyetDinhCuuTroRepository.save(currentRow.get());
    return currentRow.get();
  }
}