package com.tcdt.qlnvhang.service.xuatcuutro;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.entities.FileDKemJoinDxuatCuuTro;
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
import java.util.stream.Stream;

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
    req.setDvql(currentUser.getDvql());
    Page<XhDxCuuTroHdr> search = deXuatCuuTroRepository.search(req, pageable);
    Map<String, String> mapLoaiHinhNx = getListDanhMucChung("LOAI_HINH_NHAP_XUAT");
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
      if (mapLoaiHinhNx.get((s.getLoaiHinhNhapXuat())) != null) {
        s.setTenLoaiHinhNhapXuat(mapLoaiHinhNx.get(s.getLoaiHinhNhapXuat()));
      }
    });
    return search;
  }

  public List<XhDxCuuTroHdr> detail(CustomUserDetails currentUser, List<Long> ids) throws Exception {
    if (DataUtils.isNullOrEmpty(ids))
      throw new Exception("Tham số không hợp lệ.");
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    List<XhDxCuuTroHdr> allById = deXuatCuuTroRepository.findAllById(ids);
    allById.forEach(data -> {
      List<XhDxCuuTroDtl> dataDtl = deXuatCuuTroDtlRepository.findByIdDxuat(data.getId());
      dataDtl.forEach(s -> {
        List<XhDxCuuTroKho> dataKho = deXuatCuuTroKhoRepository.findByIdDxuatDtl(s.getId());
        buildThongTinKho(dataKho);
        s.setPhuongAnXuat(DataUtils.isNullOrEmpty(dataKho) ? new ArrayList<>() : dataKho);
      });
      data.setThongTinChiTiet(DataUtils.isNullOrEmpty(dataDtl) ? new ArrayList<>() : dataDtl);
      data.setTenDvi(mapDmucDvi.get(data.getMaDvi()));
      data.setTenLoaiVthh(mapVthh.get(data.getLoaiVthh()));
      data.setTenCloaiVthh(mapVthh.get(data.getCloaiVthh()));
    });
    //Map<String, String> mapLoaiGia = qlnvDmService.getListDanhMucChung("LOAI_GIA");
    return allById;
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
    if (req.getTrangThai().equals(TrangThaiAllEnum.CHO_DUYET_TP.getId())) {
      newRow.setTrangThai(TrangThaiAllEnum.CHO_DUYET_TP.getId());
    } else {
      newRow.setTrangThai(TrangThaiAllEnum.DU_THAO.getId());
    }
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
        s.setSoDxuat(finalNewRow.getSoDxuat());
        XhDxCuuTroDtl newRowDtl = deXuatCuuTroDtlRepository.save(s);
        //luu nhap kho
        List<XhDxCuuTroKho> phuongAnXuat = new ArrayList();
        if (!DataUtils.isNullOrEmpty(s.getPhuongAnXuat())) {
          phuongAnXuat = ObjectMapperUtils.mapAll(s.getPhuongAnXuat(), XhDxCuuTroKho.class);
          phuongAnXuat.forEach(s1 -> {
            s1.setIdDxuat(finalNewRow.getId());
            s1.setIdDxuatDtl(newRowDtl.getId());
          });
          deXuatCuuTroKhoRepository.saveAll(phuongAnXuat);
        }
      });
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
    DataUtils.copyProperties(req, currentRow, "id", "trangThaiTh", "fileDinhKem", "thongTinChiTiet", "phuongAnXuat");
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

    List<XhDxCuuTroDtl> thongTinChiTiet = new ArrayList();
    if (!DataUtils.isNullOrEmpty(req.getThongTinChiTiet())) {
      List<Long> listDtlReq = req.getThongTinChiTiet().stream().map(XhDxCuuTroDtl::getId).collect(Collectors.toList());
      List<XhDxCuuTroDtl> listDtlCur = deXuatCuuTroDtlRepository.findByIdDxuat(currentRow.getId());
      List<Long> listRemoveId = listDtlCur.stream().map(XhDxCuuTroDtl::getId).collect(Collectors.toList());
      listRemoveId.removeAll(listDtlReq);
      deXuatCuuTroDtlRepository.deleteAllByIdIn(listRemoveId);
      deXuatCuuTroKhoRepository.deleteAllByIdDxuatDtlIn(listRemoveId);

      thongTinChiTiet = ObjectMapperUtils.mapAll(req.getThongTinChiTiet(), XhDxCuuTroDtl.class);
      XhDxCuuTroHdr finalNewRow = currentRow;
      thongTinChiTiet.forEach(s -> {
        List<XhDxCuuTroKho> listRemoveKho = deXuatCuuTroKhoRepository.findByIdDxuatDtl(s.getId());
//        List<Long> listRemoveKhoId = listRemoveKho.stream().map(XhDxCuuTroKho::getId).collect(Collectors.toList());
        s.setIdDxuat(finalNewRow.getId());
        s.setSoDxuat(finalNewRow.getSoDxuat());
        XhDxCuuTroDtl newRowDtl = deXuatCuuTroDtlRepository.save(s);
        //luu nhap kho
        List<XhDxCuuTroKho> phuongAnXuat = new ArrayList();
        if (!DataUtils.isNullOrEmpty(s.getPhuongAnXuat())) {
          phuongAnXuat = ObjectMapperUtils.mapAll(s.getPhuongAnXuat(), XhDxCuuTroKho.class);
          phuongAnXuat.forEach(s1 -> {
            listRemoveKho.remove(s1);
            s1.setIdDxuat(finalNewRow.getId());
            s1.setIdDxuatDtl(newRowDtl.getId());
          });
          deXuatCuuTroKhoRepository.saveAll(phuongAnXuat);
        }
      });
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
    String title = "Danh sách Đề xuất phương án xuất cứu trợ, viện trợ";
    String[] rowsName = new String[]{"STT", "Loại hình nhập xuất", "Số công văn/đề xuất", "Đơn vị đề xuất", "Ngày đề xuất", "Loại hàng hoá", "Chủng loại hàng hóa", "Tổng SL xuất viện trợ, cứu trợ (kg)", "Trích yếu", "Trạng thái đề xuất", "Trạng thái tổng hợp"};
    String fileName = "danh-sach-de-xuat-phuong-an-xuat-cuu-tro-vien-tro.xlsx";
    List<Object[]> dataList = new ArrayList<Object[]>();
    Object[] objs = null;
    for (int i = 0; i < data.size(); i++) {
      XhDxCuuTroHdr dx = data.get(i);
      objs = new Object[rowsName.length];
      objs[0] = i;
      objs[1] = dx.getTenLoaiHinhNhapXuat();
      objs[2] = dx.getSoDxuat();
      objs[3] = dx.getTenDvi();
      objs[4] = dx.getNgayDxuat();
      objs[5] = dx.getTenLoaiVthh();
      objs[6] = dx.getTenCloaiVthh();
      objs[7] = dx.getTongSoLuong();
      objs[8] = dx.getTrichYeu();
      objs[9] = dx.getTenTrangThai();
      objs[10] = dx.getTenTrangThaiTh();
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
    /*if (currentUser.getUser().getCapDvi().equals(CAP_CUC) ||
        (currentUser.getUser().getCapDvi().equals(CAP_CHI_CUC))) {
      List<String> statusAllow = Arrays.asList(TrangThaiAllEnum.CHO_DUYET_TP.getId());
      if (!statusAllow.containsAll(statusAllow))
        throw new Exception("Tài khoản không có quyền thực hiện.");
    }*/
    String trangThai = TrangThaiAllEnum.DU_THAO.getId();
    String capDvi = currentUser.getUser().getCapDvi();
    String condition = currentRow.get().getTrangThai() + req.getTrangThai();
    if (capDvi.equals(CAP_TONG_CUC)) {
      //gui duyet
      if (condition.equals(TrangThaiAllEnum.DU_THAO.getId() + TrangThaiAllEnum.CHO_DUYET_LDTC.getId())) {
        trangThai = TrangThaiAllEnum.DA_DUYET_LDTC.getId();
      }
      //duyet
      else if (condition.equals(TrangThaiAllEnum.CHO_DUYET_LDTC.getId() + TrangThaiAllEnum.DA_DUYET_LDTC.getId())) {
        trangThai = TrangThaiAllEnum.DA_DUYET_LDTC.getId();
      }
      //tu choi
      else if (condition.equals(TrangThaiAllEnum.CHO_DUYET_LDTC.getId() + TrangThaiAllEnum.TU_CHOI_LDTC.getId())) {
        trangThai = TrangThaiAllEnum.TU_CHOI_LDTC.getId();
      }
    } else if (capDvi.equals(CAP_CUC)) {
      //gui duyet
      if (condition.equals(TrangThaiAllEnum.DU_THAO.getId() + TrangThaiAllEnum.CHO_DUYET_LDTC.getId())) {
        trangThai = TrangThaiAllEnum.CHO_DUYET_TP.getId();
      } else if (condition.equals(TrangThaiAllEnum.CHO_DUYET_TP.getId() + TrangThaiAllEnum.CHO_DUYET_LDTC.getId())) {
        trangThai = TrangThaiAllEnum.CHO_DUYET_LDC.getId();
      } else if (condition.equals(TrangThaiAllEnum.TU_CHOI_TP.getId() + TrangThaiAllEnum.CHO_DUYET_LDTC.getId())) {
        trangThai = TrangThaiAllEnum.CHO_DUYET_TP.getId();
      }
      //duyet
      else if (condition.equals(TrangThaiAllEnum.CHO_DUYET_TP.getId() + TrangThaiAllEnum.DA_DUYET_LDTC.getId())) {
        trangThai = TrangThaiAllEnum.DA_DUYET_LDC.getId();
      } else if (condition.equals(TrangThaiAllEnum.CHO_DUYET_LDC.getId() + TrangThaiAllEnum.DA_DUYET_LDTC.getId())) {
        trangThai = TrangThaiAllEnum.DA_DUYET_LDC.getId();
      }
      //tu choi
      else if (condition.equals(TrangThaiAllEnum.CHO_DUYET_TP.getId() + TrangThaiAllEnum.TU_CHOI_LDTC.getId())) {
        trangThai = TrangThaiAllEnum.TU_CHOI_TP.getId();
      } else if (condition.equals(TrangThaiAllEnum.CHO_DUYET_LDC.getId() + TrangThaiAllEnum.TU_CHOI_LDTC.getId())) {
        trangThai = TrangThaiAllEnum.TU_CHOI_LDC.getId();
      }
    }

    currentRow.get().setTrangThai(trangThai);
    currentRow.get().setLyDoTuChoi(DataUtils.safeToString(req.getLyDo()));
    deXuatCuuTroRepository.save(currentRow.get());
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

  @Transactional(rollbackFor = Exception.class)
  public void deleteListId(List<Long> listId) {
    deXuatCuuTroDtlRepository.deleteAllByIdDxuatIn(listId);
    deXuatCuuTroKhoRepository.deleteAllByIdDxuatIn(listId);
    deXuatCuuTroRepository.deleteAllByIdIn(listId);
    fileDinhKemService.deleteMultiple(listId, Lists.newArrayList(XhDxCuuTroHdr.TABLE_NAME));
  }
}