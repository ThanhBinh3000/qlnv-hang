package com.tcdt.qlnvhang.service.xuatcuutro;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.QlnvDmDonviRepository;
import com.tcdt.qlnvhang.repository.khotang.KtDiemKhoRepository;
import com.tcdt.qlnvhang.repository.khotang.KtNganKhoRepository;
import com.tcdt.qlnvhang.repository.khotang.KtNganLoRepository;
import com.tcdt.qlnvhang.repository.khotang.KtNhaKhoRepository;
import com.tcdt.qlnvhang.repository.xuatcuutro.*;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuatcuutro.XhQdGnvCuuTroHdrSearchReq;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.*;
import com.tcdt.qlnvhang.table.catalog.QlnvDmDonvi;
import com.tcdt.qlnvhang.table.khotang.KtDiemKho;
import com.tcdt.qlnvhang.table.khotang.KtNganKho;
import com.tcdt.qlnvhang.table.khotang.KtNganLo;
import com.tcdt.qlnvhang.table.khotang.KtNhaKho;
import com.tcdt.qlnvhang.util.DataUtils;
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

import static com.tcdt.qlnvhang.util.Contains.CAP_CUC;
import static com.tcdt.qlnvhang.util.Contains.CAP_TONG_CUC;

@Service
@Log4j2
public class QuyetDinhGnvCuuTroService extends BaseServiceImpl {
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
  private XhQdGnvCuuTroHdrRepository xhQdGnvCuuTroHdrRepository;
  @Autowired
  private XhQdGnvCuuTroDtlRepository xhQdGnvCuuTroDtlRepository;
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

  public Page<XhQdGnvCuuTroHdr> searchPage(CustomUserDetails currentUser, XhQdGnvCuuTroHdrSearchReq req) throws Exception {
    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
//   / req.setDvql(currentUser.getDvql());
    if (!currentUser.getUser().getCapDvi().equals(CAP_TONG_CUC)) {
      req.setMaDviDxuat(currentUser.getDvql());
    }
    Page<XhQdGnvCuuTroHdr> search = xhQdGnvCuuTroHdrRepository.search(req, pageable);
//    Map<String, String> mapLoaiHinhNx = getListDanhMucChung("LOAI_HINH_NHAP_XUAT");
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    //set label
    search.getContent().forEach(s -> {
      s.setMapDmucDvi(mapDmucDvi);
      s.setMapVthh(mapVthh);
    });
    return search;
  }

  public XhQdGnvCuuTroHdr detail(CustomUserDetails currentUser, Long id) throws Exception {
    if (DataUtils.isNullObject(id))
      throw new Exception("Tham số không hợp lệ.");
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();

    //set thong tin de xuat
    Optional<XhQdGnvCuuTroHdr> currentHdr = xhQdGnvCuuTroHdrRepository.findById(id);
    if (currentHdr.isPresent()) {
      XhQdGnvCuuTroHdr data = currentHdr.get();
      data.setMapDmucDvi(mapDmucDvi);
      data.setMapVthh(mapVthh);
      //set thong tin de xuat
      List<XhQdGnvCuuTroDtl> dataDtl = xhQdGnvCuuTroDtlRepository.findByIdHdr(data.getId());
      dataDtl.forEach(s -> s.setMapDmucDvi(mapDmucDvi));
      data.setNoiDungCuuTro(dataDtl);

      //set file dinh kem va can cu
      List<FileDinhKem> canCu = fileDinhKemService.search(id, Arrays.asList(XhQdGnvCuuTroHdr.TABLE_NAME + "_CAN_CU"));
      data.setCanCu(canCu);
      List<FileDinhKem> fileDinhKem = fileDinhKemService.search(id, Arrays.asList(XhQdGnvCuuTroHdr.TABLE_NAME + "_DINH_KEM"));
      data.setCanCu(fileDinhKem);
      return data;
    }
    return currentHdr.get();
  }

  @Transactional(rollbackFor = Exception.class)
  public XhQdGnvCuuTroHdr create(CustomUserDetails currentUser, XhQdGnvCuuTroHdrSearchReq req) throws Exception {
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
    Optional<XhQdGnvCuuTroHdr> optional = xhQdGnvCuuTroHdrRepository.findFirstBySoQdAndNam(req.getSoQd(), req.getNam());
    if (optional.isPresent()) {
      throw new Exception(MessageFormat.format("Số quyết định {0} đã tồn tại", req.getSoQd()));
    }
    XhQdGnvCuuTroHdr newRow = new XhQdGnvCuuTroHdr();
    BeanUtils.copyProperties(req, newRow, "id");
    newRow.setTrangThai(TrangThaiAllEnum.DU_THAO.getId());
    newRow.setMaDvi(currentUser.getUser().getDepartment());
    newRow = xhQdGnvCuuTroHdrRepository.save(newRow);

    //luu can cu dinh kem
    if (!DataUtils.isNullOrEmpty(req.getCanCu())) {
      fileDinhKemService.saveListFileDinhKem(req.getCanCu(), newRow.getId(), XhQdGnvCuuTroHdr.TABLE_NAME + "_CAN_CU");
    }
    if (!DataUtils.isNullObject(req.getFileDinhKem())) {
      fileDinhKemService.saveListFileDinhKem(req.getFileDinhKem(), newRow.getId(), XhQdGnvCuuTroHdr.TABLE_NAME + "_DINH_KEM");
    }
    //luu thong tin chi tiet
    XhQdGnvCuuTroHdr finalNewRow = newRow;
    req.getNoiDungCuuTro().forEach(s -> {
      s.setIdHdr(finalNewRow.getId());
    });
    xhQdGnvCuuTroDtlRepository.saveAll(req.getNoiDungCuuTro());

    return null;
  }

  @Transactional(rollbackFor = Exception.class)
  public XhQdGnvCuuTroHdr update(CustomUserDetails currentUser, XhQdGnvCuuTroHdrSearchReq req) throws Exception {
    if (DataUtils.isNullObject(req.getId()))
      throw new Exception("Tham số không hợp lệ.");
    XhQdGnvCuuTroHdr currentRow = xhQdGnvCuuTroHdrRepository.findById(req.getId()).orElse(null);
    if (DataUtils.isNullObject(currentRow))
      throw new Exception("Không tìm thấy dữ liệu.");
    XhQdGnvCuuTroHdr validateRow = xhQdGnvCuuTroHdrRepository.findFirstBySoQdAndNam(currentRow.getSoQd(), currentRow.getNam()).get();
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
    currentRow = xhQdGnvCuuTroHdrRepository.save(currentRow);

    //luu can cu dinh kem
    if (!DataUtils.isNullOrEmpty(req.getCanCu())) {
      fileDinhKemService.saveListFileDinhKem(req.getCanCu(), currentRow.getId(), XhQdGnvCuuTroHdr.TABLE_NAME + "_CAN_CU");
    }
    if (!DataUtils.isNullObject(req.getFileDinhKem())) {
      fileDinhKemService.saveListFileDinhKem(req.getFileDinhKem(), currentRow.getId(), XhQdGnvCuuTroHdr.TABLE_NAME + "_DINH_KEM");
    }
    //luu thong tin chi tiet
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

  public void export(CustomUserDetails currentUser, XhQdGnvCuuTroHdrSearchReq req, HttpServletResponse response) throws Exception {
    /*PaggingReq paggingReq = new PaggingReq();
    paggingReq.setPage(0);
    paggingReq.setLimit(Integer.MAX_VALUE);
    req.setPaggingReq(paggingReq);
    Page<XhQdCuuTroHdr> page = this.searchPage(currentUser, req);
    List<XhQdCuuTroHdr> data = page.getContent();
    String title = "Danh sách Quyết định phê duyệt phương án xuất cứu trợ, viện trợ";
    String[] rowsName = new String[]{"STT", "Số quyết định", "Ngày ký quyết định", "Mã tổng hợp", "Ngày tổng hợp", "Loại hàng hoá", "Chủng loại hàng hóa", "Tổng SL xuất viện trợ, cứu trợ (kg)", "Trích yếu", "Trạng thái"};
    String fileName = "danh-sach-quyet-dinh-phe-duyet-phuong-an-xuat-cuu-tro-vien-tro.xlsx";
    List<Object[]> dataList = new ArrayList<Object[]>();
    Object[] objs = null;
    for (int i = 0; i < data.size(); i++) {
      XhQdCuuTroHdr dx = data.get(i);
      objs = new Object[rowsName.length];
      objs[0] = i;
      objs[1] = dx.getSoQd();
      objs[2] = dx.getNgayKy();
      objs[3] = dx.getMaTongHop();
      objs[4] = dx.getNgayTongHop();
      objs[5] = dx.getTenLoaiVthh();
      objs[6] = dx.getTenCloaiVthh();
      objs[7] = dx.getTongSoLuong();
      objs[8] = dx.getTrichYeu();
      objs[9] = dx.getTenTrangThai();
      dataList.add(objs);
    }
    ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
    ex.export();*/
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
    quyetDinhCuuTroDtlRepository.deleteAllByIdQdIn(listId);
    quyetDinhCuuTroRepository.deleteAllByIdIn(listId);
    fileDinhKemService.deleteMultiple(listId, Lists.newArrayList(XhQdCuuTroHdr.TABLE_NAME));
  }


  @Transactional(rollbackFor = Exception.class)
  public XhQdGnvCuuTroHdr updateStatus(CustomUserDetails currentUser, StatusReq req) throws Exception {
    Optional<XhQdGnvCuuTroHdr> currentRow = xhQdGnvCuuTroHdrRepository.findById(req.getId());
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
    /*if (capDvi.equals(CAP_TONG_CUC)) {

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
      if (condition.equals(TrangThaiAllEnum.DU_THAO.getId() + TrangThaiAllEnum.BAN_HANH.getId())) {
        trangThai = TrangThaiAllEnum.BAN_HANH.getId();
      }
    }*/
    if (capDvi.equals(CAP_CUC)) {
      //gui duyet
      if (condition.equals(TrangThaiAllEnum.DU_THAO.getId() + TrangThaiAllEnum.CHO_DUYET_LDC.getId())) {
        trangThai = TrangThaiAllEnum.CHO_DUYET_LDC.getId();
      } else if (condition.equals(TrangThaiAllEnum.TU_CHOI_LDC.getId() + TrangThaiAllEnum.CHO_DUYET_LDC.getId())) {
        trangThai = TrangThaiAllEnum.CHO_DUYET_LDC.getId();
      }
      //duyet
      else if (condition.equals(TrangThaiAllEnum.CHO_DUYET_LDC.getId() + TrangThaiAllEnum.DA_DUYET_LDC.getId())) {
        trangThai = TrangThaiAllEnum.DA_DUYET_LDC.getId();
      }
      //tu choi
      else if (condition.equals(TrangThaiAllEnum.CHO_DUYET_LDC.getId() + TrangThaiAllEnum.TU_CHOI_LDC.getId())) {
        trangThai = TrangThaiAllEnum.TU_CHOI_LDC.getId();
      }
    }
    if (DataUtils.isNullOrEmpty(trangThai)) {
      throw new Exception("Quy trình phê duyệt không hợp lệ.");
    }
    currentRow.get().setTrangThai(trangThai);
    if (trangThai.equals(TrangThaiAllEnum.TU_CHOI_LDTC.getId())) {
      currentRow.get().setLyDoTuChoi(DataUtils.safeToString(req.getLyDo()));
    }
    xhQdGnvCuuTroHdrRepository.save(currentRow.get());
    return currentRow.get();
  }
}