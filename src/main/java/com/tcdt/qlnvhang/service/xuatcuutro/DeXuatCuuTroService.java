package com.tcdt.qlnvhang.service.xuatcuutro;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcdt.qlnvhang.entities.FileDKemJoinDxuatCuuTro;
import com.tcdt.qlnvhang.entities.FileDKemJoinHopDong;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.QlnvDmDonviRepository;
import com.tcdt.qlnvhang.repository.khotang.KtDiemKhoRepository;
import com.tcdt.qlnvhang.repository.khotang.KtNganKhoRepository;
import com.tcdt.qlnvhang.repository.khotang.KtNganLoRepository;
import com.tcdt.qlnvhang.repository.khotang.KtNhaKhoRepository;
import com.tcdt.qlnvhang.repository.xuatcuutro.DeXuatCuuTroDtlRepository;
import com.tcdt.qlnvhang.repository.xuatcuutro.DeXuatCuuTroKhoRepository;
import com.tcdt.qlnvhang.repository.xuatcuutro.DeXuatCuuTroRepository;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuatcuutro.XhDxCuuTroHdrSearchReq;
import com.tcdt.qlnvhang.response.banhangdaugia.quyetdinhpheduyetketquabandaugia.BhQdPheDuyetKqbdgCtRes;
import com.tcdt.qlnvhang.response.banhangdaugia.tonghopdexuatkhbdg.BhQdPheDuyetKhBdgThongTinTaiSanResponse;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.*;
import com.tcdt.qlnvhang.table.catalog.QlnvDmDonvi;
import com.tcdt.qlnvhang.table.khotang.KtDiemKho;
import com.tcdt.qlnvhang.table.khotang.KtNganKho;
import com.tcdt.qlnvhang.table.khotang.KtNganLo;
import com.tcdt.qlnvhang.table.khotang.KtNhaKho;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import com.tcdt.qlnvhang.util.ObjectMapperUtils;
import com.tcdt.qlnvhang.util.PaginationSet;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.tcdt.qlnvhang.util.Contains.*;

@Service
@Log4j2
public class DeXuatCuuTroService extends BaseServiceImpl {
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


  public Page<XhDxCuuTroHdr> searchPage(CustomUserDetails currentUser, XhDxCuuTroHdrSearchReq req) throws Exception {
    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
    Page<XhDxCuuTroHdr> search = deXuatCuuTroRepository.search(req, pageable);
    //Map<String, String> mapLoaiGia = qlnvDmService.getListDanhMucChung("LOAI_GIA");
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

  public XhDxCuuTroHdr detail(CustomUserDetails currentUser, Long id) throws Exception {
    if (DataUtils.isNullObject(id))
      throw new Exception("Tham số không hợp lệ.");
    Optional<XhDxCuuTroHdr> currentHdr = deXuatCuuTroRepository.findById(id);
    if (currentHdr.isPresent()) {
      XhDxCuuTroHdr data = currentHdr.get();
      List<XhDxCuuTroDtl> dataDtl = deXuatCuuTroDtlRepository.findByIdDxuat(data.getId());
      List<XhDxCuuTroKho> dataKho = deXuatCuuTroKhoRepository.findByIdDxuat(data.getId());
      buildThongTinKho(dataKho);
      data.setThongTinChiTiet(DataUtils.isNullOrEmpty(dataDtl) ? null : dataDtl);
      data.setPhuongAnXuat(DataUtils.isNullOrEmpty(dataKho) ? null : dataKho);
      Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
      Map<String, String> mapVthh = getListDanhMucHangHoa();
      data.setTenDvi(mapDmucDvi.get(data.getMaDvi()));
      data.setTenLoaiVthh(mapVthh.get(data.getLoaiVthh()));
      data.setTenCloaiVthh(mapVthh.get(data.getCloaiVthh()));
      return data;
    }

    //Map<String, String> mapLoaiGia = qlnvDmService.getListDanhMucChung("LOAI_GIA");
    return currentHdr.get();
  }

  @Transactional(rollbackFor = Exception.class)
  public XhDxCuuTroHdr create(CustomUserDetails currentUser, XhDxCuuTroHdrSearchReq req) throws Exception {
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    if (DataUtils.isNullOrEmpty(req.getLoaiHinhNhapXuat())) {
      throw new Exception("Loại hình nhập xuất thiếu hoặc không hợp lệ.");
    }
    if (DataUtils.isNullOrEmpty(req.getKieuNhapXuat())) {
      throw new Exception("Kiểu nhập xuất thiếu hoặc không hợp lệ.");
    }
    if (DataUtils.isNullOrEmpty(req.getSoDxuat())) {
      throw new Exception("Số công văn/đề xuất thiếu hoặc không hợp lệ.");
    }
    if (DataUtils.isNullOrEmpty(req.getLoaiVthh())) {
      throw new Exception("Loại hàng hóa thiếu hoặc không hợp lệ.");
    }
    if (DataUtils.isNullOrEmpty(req.getCloaiVthh())) {
      throw new Exception("Chủng loại hàng hóa thiếu hoặc không hợp lệ.");
    }
    Optional<XhDxCuuTroHdr> optional = deXuatCuuTroRepository.findFirstBySoDxuatAndNam(req.getSoDxuat(), req.getNam());
    if (optional.isPresent()) {
      throw new Exception(MessageFormat.format("Số đề xuất {0} đã tồn tại", req.getSoDxuat()));
    }
    XhDxCuuTroHdr newRow = new XhDxCuuTroHdr();
    BeanUtils.copyProperties(req, newRow, "id", "fileDinhKem");
    newRow.setTrangThai(TrangThaiAllEnum.DU_THAO.getId());
    newRow.setTrangThaiTh(TrangThaiAllEnum.CHUA_TONG_HOP.getId());
    newRow.setMaDvi(currentUser.getDvql());
//    newRow.setCapDvi(currentUser.getUser().getCapDvi());
    newRow = deXuatCuuTroRepository.save(newRow);

    //luu file dinh kem
    List<FileDKemJoinDxuatCuuTro> fileDinhKem = new ArrayList();

    if (!DataUtils.isNullOrEmpty(req.getFileDinhKem())) {
      fileDinhKemService.saveListFileDinhKem(req.getFileDinhKem(), newRow.getId(), XhDxCuuTroHdr.TABLE_NAME);
      /*fileDinhKem = ObjectMapperUtils.mapAll(req.getFileDinhKem(), FileDKemJoinDxuatCuuTro.class);
      fileDinhKem.forEach(f -> {
        f.setDataType(XhDxCuuTroHdr.TABLE_NAME);
        f.setCreateDate(new Date());
      });*/
    }

    //luu thong tin chi tiet
    List<XhDxCuuTroDtl> thongTinChiTiet = new ArrayList();
    if (!DataUtils.isNullOrEmpty(req.getThongTinChiTiet())) {
      thongTinChiTiet = ObjectMapperUtils.mapAll(req.getThongTinChiTiet(), XhDxCuuTroDtl.class);
      XhDxCuuTroHdr finalNewRow = newRow;
      thongTinChiTiet.forEach(s -> {
        s.setIdDxuat(finalNewRow.getId());
      });
      deXuatCuuTroDtlRepository.saveAll(thongTinChiTiet);
    }

    //luu nhap kho
    List<XhDxCuuTroKho> phuongAnXuat = new ArrayList();
    if (!DataUtils.isNullOrEmpty(req.getPhuongAnXuat())) {
      phuongAnXuat = ObjectMapperUtils.mapAll(req.getPhuongAnXuat(), XhDxCuuTroKho.class);
      XhDxCuuTroHdr finalNewRow = newRow;
      phuongAnXuat.forEach(s -> {
        s.setIdDxuat(finalNewRow.getId());
      });
      deXuatCuuTroKhoRepository.saveAll(phuongAnXuat);
    }
    return newRow;
  }

  @Transactional(rollbackFor = Exception.class)
  public XhDxCuuTroHdr update(CustomUserDetails currentUser, XhDxCuuTroHdrSearchReq req) throws Exception {
    if (DataUtils.isNullObject(req.getId()))
      throw new Exception("Tham số không hợp lệ.");
    XhDxCuuTroHdr currentRow = deXuatCuuTroRepository.findById(req.getId()).orElse(null);
    if (DataUtils.isNullObject(currentRow))
      throw new Exception("Không tìm thấy dữ liệu.");
    XhDxCuuTroHdr validateRow = deXuatCuuTroRepository.findFirstBySoDxuatAndNam(currentRow.getSoDxuat(), currentRow.getNam()).get();
    if (!DataUtils.isNullObject(validateRow) && currentRow.getId() != req.getId()) {
      throw new Exception(MessageFormat.format("Số đề xuất {0} đã tồn tại", req.getSoDxuat()));
    }
    DataUtils.copyProperties(req, currentRow, "id", "trangThai", "trangThaiTh", "fileDinhKem", "thongTinChiTiet", "phuongAnXuat");
    if (DataUtils.isNullOrEmpty(currentRow.getLoaiHinhNhapXuat())) {
      throw new Exception("Loại hình nhập xuất thiếu hoặc không hợp lệ.");
    }
    if (DataUtils.isNullOrEmpty(currentRow.getKieuNhapXuat())) {
      throw new Exception("Kiểu nhập xuất thiếu hoặc không hợp lệ.");
    }
    if (DataUtils.isNullOrEmpty(currentRow.getSoDxuat())) {
      throw new Exception("Số công văn/đề xuất thiếu hoặc không hợp lệ.");
    }
    if (DataUtils.isNullOrEmpty(currentRow.getLoaiVthh())) {
      throw new Exception("Loại hàng hóa thiếu hoặc không hợp lệ.");
    }
    if (DataUtils.isNullOrEmpty(currentRow.getCloaiVthh())) {
      throw new Exception("Chủng loại hàng hóa thiếu hoặc không hợp lệ.");
    }
    currentRow = deXuatCuuTroRepository.save(currentRow);

    //luu file dinh kem
    List<FileDKemJoinDxuatCuuTro> fileDinhKem = new ArrayList();
    if (!DataUtils.isNullOrEmpty(req.getFileDinhKem())) {
      fileDinhKemService.saveListFileDinhKem(req.getFileDinhKem(), currentRow.getId(), XhDxCuuTroHdr.TABLE_NAME);
    /*  fileDinhKem = ObjectMapperUtils.mapAll(req.getFileDinhKem(), FileDKemJoinDxuatCuuTro.class);
      fileDinhKem.forEach(f -> {
        f.setDataType(XhDxCuuTroHdr.TABLE_NAME);
        f.setCreateDate(new Date());
      });*/
    }

    //luu thong tin chi tiet
    List<XhDxCuuTroDtl> thongTinChiTiet = new ArrayList();
    if (!DataUtils.isNullOrEmpty(req.getThongTinChiTiet())) {
      thongTinChiTiet = ObjectMapperUtils.mapAll(req.getThongTinChiTiet(), XhDxCuuTroDtl.class);
      XhDxCuuTroHdr finalNewRow = currentRow;
      thongTinChiTiet.forEach(s -> {
        s.setIdDxuat(finalNewRow.getId());
      });
      deXuatCuuTroDtlRepository.saveAll(thongTinChiTiet);
    }

    //luu nhap kho
    List<XhDxCuuTroKho> phuongAnXuat = new ArrayList();
    if (!DataUtils.isNullOrEmpty(req.getThongTinChiTiet())) {
      phuongAnXuat = ObjectMapperUtils.mapAll(req.getThongTinChiTiet(), XhDxCuuTroKho.class);
      XhDxCuuTroHdr finalNewRow = currentRow;
      phuongAnXuat.forEach(s -> {
        s.setIdDxuat(finalNewRow.getId());
      });
      deXuatCuuTroKhoRepository.saveAll(phuongAnXuat);
    }

    return currentRow;
  }

  @Transactional(rollbackFor = Exception.class)
  public boolean deleteMultiple(CustomUserDetails currentUser, List<Long> ids) throws Exception {
    if (CollectionUtils.isEmpty(ids)) throw new Exception("Bad request.");

    List<XhDxCuuTroHdr> listData = deXuatCuuTroRepository.findAllById(ids);
    List<XhDxCuuTroHdr> listDataValid = listData.stream()
        .filter(s -> s.getTrangThai().equals(TrangThaiAllEnum.DU_THAO.getId()))
        .collect(Collectors.toList());
    deXuatCuuTroRepository.deleteAll(listDataValid);
    return true;
  }

  public void export(CustomUserDetails currentUser, XhDxCuuTroHdrSearchReq req, HttpServletResponse response) throws Exception {
    PaggingReq paggingReq = new PaggingReq();
    paggingReq.setPage(0);
    paggingReq.setLimit(Integer.MAX_VALUE);
    req.setPaggingReq(paggingReq);
    Page<XhDxCuuTroHdr> page = this.searchPage(currentUser, req);
    List<XhDxCuuTroHdr> data = page.getContent();

    String title = "Danh sách Quyết định giá mua tối đa, giá bán tối thiểu của BTC";
    String[] rowsName = new String[]{"STT", "Số quyết định", "Ngày ký", "Trích yếu", "Năm kế hoạch", "Loại giá", "Loại hàng hóa", "Trạng thái"};
    String fileName = "danh-sach-qd-gia-mua-toi-da-ban-toi-thieu-cua-btc.xlsx";
    List<Object[]> dataList = new ArrayList<Object[]>();
    Object[] objs = null;
    for (int i = 0; i < data.size(); i++) {
      XhDxCuuTroHdr dx = data.get(i);
      objs = new Object[rowsName.length];
      objs[0] = i;
      /*objs[1] = dx.getSoQd();
      objs[2] = dx.getNgayKy();
      objs[3] = dx.getTrichYeu();
      objs[4] = dx.getNamKeHoach();
      objs[5] = dx.getTenLoaiGia();*/
      objs[6] = dx.getTenLoaiVthh();
      objs[7] = TrangThaiAllEnum.getLabelById(dx.getTrangThai());
      objs[8] = TrangThaiAllEnum.getLabelById(dx.getTrangThaiTh());
      dataList.add(objs);
    }
    ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
    ex.export();
  }

  @Transactional(rollbackFor = Exception.class)
  public XhDxCuuTroHdr updateStatus(CustomUserDetails currentUser, StatusReq req) throws Exception {
    Optional<XhDxCuuTroHdr> currentRow = deXuatCuuTroRepository.findById(req.getId());
    if (!currentRow.isPresent())
      throw new Exception("Không tìm thấy dữ liệu.");
    if (currentUser.getUser().getCapDvi().equals(CAP_CUC) ||
        (currentUser.getUser().getCapDvi().equals(CAP_CHI_CUC))) {
      List<String> statusAllow = Arrays.asList(TrangThaiAllEnum.CHO_DUYET_TP.getId());
      if (!statusAllow.containsAll(statusAllow))
        throw new Exception("Tài khoản không có quyền thực hiện.");
      currentRow.get().setTrangThai(req.getTrangThai());
      deXuatCuuTroRepository.save(currentRow.get());
    }
    return currentRow.get();
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
}