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
import com.tcdt.qlnvhang.repository.xuatcuutro.*;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuatcuutro.XhDxCuuTroHdrSearchReq;
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
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.ss.usermodel.DateUtil;
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

import static com.tcdt.qlnvhang.util.Contains.*;

@Service
@Log4j2
public class TongHopCuuTroService extends BaseServiceImpl {
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


  public Page<XhThCuuTroHdr> searchPage(CustomUserDetails currentUser, XhThCuuTroHdrSearchReq req) throws Exception {
    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
    Page<XhThCuuTroHdr> search = tongHopCuuTroRepository.search(req, pageable);
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

  public XhThCuuTroHdr detail(CustomUserDetails currentUser, Long id) throws Exception {
    if (DataUtils.isNullObject(id))
      throw new Exception("Tham số không hợp lệ.");
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();

    //set thong tin de xuat
    Optional<XhThCuuTroHdr> currentHdr = tongHopCuuTroRepository.findById(id);
    if (currentHdr.isPresent()) {
      XhThCuuTroHdr data = currentHdr.get();
      List<XhDxCuuTroHdr> dxuatHdr = deXuatCuuTroRepository.findIdByIdTongHop(data.getId());
      dxuatHdr.forEach(s -> {
        List<XhDxCuuTroDtl> dataDtl = deXuatCuuTroDtlRepository.findByIdDxuat(s.getId());
       /* dataDtl.forEach(s -> {
          List<XhDxCuuTroKho> dataKho = deXuatCuuTroKhoRepository.findByIdDxuatDtl(s.getId());
          buildThongTinKho(dataKho);
          s.setPhuongAnXuat(DataUtils.isNullOrEmpty(dataKho) ? new ArrayList<>() : dataKho);
        });*/
        s.setThongTinChiTiet(DataUtils.isNullOrEmpty(dataDtl) ? new ArrayList<>() : dataDtl);
        s.setTenDvi(mapDmucDvi.get(s.getMaDvi()));
        s.setTenLoaiVthh(mapVthh.get(s.getLoaiVthh()));
        s.setTenCloaiVthh(mapVthh.get(s.getCloaiVthh()));
      });
      data.setThongTinDeXuat(dxuatHdr);

      //set thong tin tong hop
      List<XhThCuuTroDtl> tongHopDtl = tongHopCuuTroDtlRepository.findByIdTongHop(id);
      if (!DataUtils.isNullOrEmpty(tongHopDtl)) {
        tongHopDtl.forEach(s -> {
          s.setTenDvi(mapDmucDvi.get(s.getMaDvi()));
        });
        data.setThongTinTongHop(tongHopDtl);
      }
      data.setTenDvi(mapDmucDvi.get(data.getMaDvi()));
      data.setTenLoaiVthh(mapVthh.get(data.getLoaiVthh()));
      data.setTenCloaiVthh(mapVthh.get(data.getLoaiVthh()));
      return data;
    }

    //Map<String, String> mapLoaiGia = qlnvDmService.getListDanhMucChung("LOAI_GIA");
    return currentHdr.get();
  }

  @Transactional(rollbackFor = Exception.class)
  public XhThCuuTroHdr create(CustomUserDetails currentUser, XhThCuuTroHdrSearchReq req) throws Exception {
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    if (DataUtils.isNullOrEmpty(req.getLoaiHinhNhapXuat())) {
      throw new Exception("Loại hình nhập xuất thiếu hoặc không hợp lệ.");
    }
    /*if (DataUtils.isNullOrEmpty(req.getMaTongHop())) {
      throw new Exception("Mã tổng hợp thiếu hoặc không hợp lệ.");
    }*/
    if (DataUtils.isNullOrEmpty(req.getLoaiVthh())) {
      throw new Exception("Loại hàng hóa thiếu hoặc không hợp lệ.");
    }
    if (DataUtils.isNullOrEmpty(req.getCloaiVthh())) {
      throw new Exception("Chủng loại hàng hóa thiếu hoặc không hợp lệ.");
    }
    /*Optional<XhThCuuTroHdr> optional = tongHopCuuTroRepository.findFirstByMaTongHopAndNam(req.getMaTongHop(), req.getNam());
    if (optional.isPresent()) {
      throw new Exception(MessageFormat.format("Mã tổng hợp {0} đã tồn tại", req.getSoDxuat()));
    }*/
    XhThCuuTroHdr newRow = new XhThCuuTroHdr();
    BeanUtils.copyProperties(req, newRow, "id");
    newRow.setTrangThai(TrangThaiAllEnum.DU_THAO.getId());
    newRow.setMaDvi(currentUser.getDvql());
//    newRow.setCapDvi(currentUser.getUser().getCapDvi());
    newRow = tongHopCuuTroRepository.save(newRow);

    //luu thong tin chi tiet
    List<XhDxCuuTroHdr> listDxHdr = new ArrayList();
    if (!DataUtils.isNullOrEmpty(req.getThongTinDeXuat())) {
      listDxHdr = ObjectMapperUtils.mapAll(req.getThongTinDeXuat(), XhDxCuuTroHdr.class);
      XhThCuuTroHdr finalNewRow = newRow;
      listDxHdr.forEach(s -> {
        s.getThongTinChiTiet().forEach(s1 -> {
          XhThCuuTroDtl newRowDtl = new XhThCuuTroDtl();
          BeanUtils.copyProperties(s1, newRowDtl);
          newRowDtl.setIdTongHop(finalNewRow.getId());
          newRowDtl.setMaDvi(finalNewRow.getMaDvi());
          newRowDtl.setNgayDxuat(s.getNgayDxuat());
          newRowDtl.setThoiGianThucHien(s.getThoiGianThucHien());
          tongHopCuuTroDtlRepository.save(newRowDtl);
        });

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
      });
    }
    return newRow;
  }

  @Transactional(rollbackFor = Exception.class)
  public XhThCuuTroHdr update(CustomUserDetails currentUser, XhThCuuTroHdrSearchReq req) throws Exception {
    if (DataUtils.isNullObject(req.getId()))
      throw new Exception("Tham số không hợp lệ.");
    XhThCuuTroHdr currentRow = tongHopCuuTroRepository.findById(req.getId()).orElse(null);

    System.out.println(req);
    System.out.println(currentRow);
    if (DataUtils.isNullObject(currentRow))
      throw new Exception("Không tìm thấy dữ liệu.");
    XhThCuuTroHdr validateRow = tongHopCuuTroRepository.findFirstByMaTongHopAndNam(currentRow.getMaTongHop(), currentRow.getNam()).get();
    if (!DataUtils.isNullObject(validateRow) && currentRow.getId() != req.getId()) {
      throw new Exception(MessageFormat.format("Số đề xuất {0} đã tồn tại", req.getSoDxuat()));
    }
    DataUtils.copyProperties(req, currentRow, "id", "trangThaiTh", "fileDinhKem", "thongTinChiTiet", "phuongAnXuat");
    if (DataUtils.isNullOrEmpty(currentRow.getLoaiHinhNhapXuat())) {
      throw new Exception("Loại hình nhập xuất thiếu hoặc không hợp lệ.");
    }

    if (DataUtils.isNullOrEmpty(currentRow.getLoaiVthh())) {
      throw new Exception("Loại hàng hóa thiếu hoặc không hợp lệ.");
    }
    if (DataUtils.isNullOrEmpty(currentRow.getCloaiVthh())) {
      throw new Exception("Chủng loại hàng hóa thiếu hoặc không hợp lệ.");
    }
    currentRow = tongHopCuuTroRepository.save(currentRow);

    //luu thong tin chi tiet
    List<XhThCuuTroDtl> thongTinChiTiet = new ArrayList();
    if (!DataUtils.isNullOrEmpty(req.getThongTinTongHop())) {
      List<Long> listDtlReq = req.getThongTinTongHop().stream().map(XhThCuuTroDtl::getId).collect(Collectors.toList());
      List<XhThCuuTroDtl> listDtlCur = tongHopCuuTroDtlRepository.findByIdTongHop(currentRow.getId());
      List<Long> listRemoveId = listDtlCur.stream().map(XhThCuuTroDtl::getId).collect(Collectors.toList());
      listRemoveId.removeAll(listDtlReq);
      tongHopCuuTroDtlRepository.deleteAllByIdIn(listRemoveId);
      tongHopCuuTroKhoRepository.deleteAllByIdTongHopDtlIn(listRemoveId);

      thongTinChiTiet = ObjectMapperUtils.mapAll(req.getThongTinTongHop(), XhThCuuTroDtl.class);
      XhThCuuTroHdr finalNewRow = currentRow;
      thongTinChiTiet.forEach(s -> {
        List<XhThCuuTroKho> listRemoveKho = tongHopCuuTroKhoRepository.findByIdTongHopDtl(s.getId());
//        List<Long> listRemoveKhoId = listRemoveKho.stream().map(XhDxCuuTroKho::getId).collect(Collectors.toList());
        s.setIdTongHop(finalNewRow.getId());
        XhThCuuTroDtl newRowDtl = tongHopCuuTroDtlRepository.save(s);
        //luu nhap kho
        List<XhThCuuTroKho> phuongAnXuat = new ArrayList();
        if (!DataUtils.isNullOrEmpty(s.getPhuongAnXuat())) {
          phuongAnXuat = ObjectMapperUtils.mapAll(s.getPhuongAnXuat(), XhThCuuTroKho.class);
          phuongAnXuat.forEach(s1 -> {
            listRemoveKho.remove(s1);
            s1.setIdTongHop(finalNewRow.getId());
            s1.setIdTongHopDtl(newRowDtl.getId());
          });
          tongHopCuuTroKhoRepository.saveAll(phuongAnXuat);
        }
      });
    }
    return currentRow;
  }

  @Transactional(rollbackFor = Exception.class)
  public boolean deleteMultiple(CustomUserDetails currentUser, List<Long> ids) throws Exception {
    if (CollectionUtils.isEmpty(ids)) throw new Exception("Bad request.");

    List<XhThCuuTroHdr> listData = tongHopCuuTroRepository.findAllById(ids);
    List<XhThCuuTroHdr> listDataValid = listData.stream()
        .filter(s -> s.getTrangThai().equals(TrangThaiAllEnum.DU_THAO.getId()))
        .collect(Collectors.toList());
    tongHopCuuTroRepository.deleteAll(listDataValid);
    return true;
  }

  public void export(CustomUserDetails currentUser, XhThCuuTroHdrSearchReq req, HttpServletResponse response) throws Exception {
    PaggingReq paggingReq = new PaggingReq();
    paggingReq.setPage(0);
    paggingReq.setLimit(Integer.MAX_VALUE);
    req.setPaggingReq(paggingReq);
    Page<XhThCuuTroHdr> page = this.searchPage(currentUser, req);
    List<XhThCuuTroHdr> data = page.getContent();
    String title = "Danh sách Đề xuất phương án xuất cứu trợ, viện trợ";
    String[] rowsName = new String[]{"STT", "Loại hình nhập xuất", "Số công văn/đề xuất", "Đơn vị đề xuất", "Ngày đề xuất", "Loại hàng hoá", "Chủng loại hàng hóa", "Tổng SL xuất viện trợ, cứu trợ (kg)", "Trích yếu", "Trạng thái đề xuất", "Trạng thái tổng hợp"};
    String fileName = "danh-sach-de-xuat-phuong-an-xuat-cuu-tro-vien-tro.xlsx";
    List<Object[]> dataList = new ArrayList<Object[]>();
    Object[] objs = null;
    for (int i = 0; i < data.size(); i++) {
      XhThCuuTroHdr dx = data.get(i);
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

  public XhThCuuTroHdr synthetic(CustomUserDetails currentUser, XhDxCuuTroHdrSearchReq req) throws Exception {
    if (DataUtils.isNullObject(req.getNam()) ||
        DataUtils.isNullOrEmpty(req.getLoaiHinhNhapXuat()) ||
        DataUtils.isNullOrEmpty(req.getCloaiVthh())
    ) {
      log.error(req);
      throw new Exception("Tham số không hợp lệ.");
    }
    if (!currentUser.getUser().getCapDvi().equals(Contains.CAP_TONG_CUC)) {
      req.setDvql(currentUser.getDvql());
    }
    req.setListTrangThai(Arrays.asList(TrangThaiAllEnum.DA_DUYET_LDC.getId(), TrangThaiAllEnum.DA_DUYET_LDTC.getId()));
    req.setListTrangThaiTh(Arrays.asList(TrangThaiAllEnum.CHUA_TONG_HOP.getId()));
    PaggingReq paggingReq = new PaggingReq();
    paggingReq.setPage(0);
    paggingReq.setLimit(Integer.MAX_VALUE);
    req.setPaggingReq(paggingReq);
    Page<XhDxCuuTroHdr> page = deXuatCuuTroRepository.search(req, req.getPageable());


    if (page.isEmpty()) {
      throw new Exception("Không tìm thấy đề xuất cần tổng hợp.");
    } else {
      XhThCuuTroHdr xhThCuuTroHdr = new XhThCuuTroHdr();
      List<Long> listId = page.getContent().stream().map(XhDxCuuTroHdr::getId).collect(Collectors.toList());
      List<XhDxCuuTroHdr> dexuatHdr = deXuatCuuTroService.detail(currentUser, listId);
      xhThCuuTroHdr.setThongTinDeXuat(dexuatHdr);
      return xhThCuuTroHdr;
    }
  }


  @Transactional(rollbackFor = Exception.class)
  public XhThCuuTroHdr updateStatus(CustomUserDetails currentUser, StatusReq req) throws Exception {
    Optional<XhThCuuTroHdr> currentRow = tongHopCuuTroRepository.findById(req.getId());
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
      if (condition.equals(TrangThaiAllEnum.DU_THAO.getId() + TrangThaiAllEnum.CHO_DUYET_LDV.getId())) {
        trangThai = TrangThaiAllEnum.CHO_DUYET_LDV.getId();
      } else if (condition.equals(TrangThaiAllEnum.TU_CHOI_LDV.getId() + TrangThaiAllEnum.CHO_DUYET_LDV.getId())) {
        trangThai = TrangThaiAllEnum.CHO_DUYET_LDV.getId();
      }
      //duyet
      else if (condition.equals(TrangThaiAllEnum.CHO_DUYET_LDV.getId() + TrangThaiAllEnum.DA_DUYET_LDV.getId())) {
        trangThai = TrangThaiAllEnum.DA_DUYET_LDV.getId();
      }
      //tu choi
      else if (condition.equals(TrangThaiAllEnum.CHO_DUYET_LDV.getId() + TrangThaiAllEnum.TU_CHOI_LDV.getId())) {
        trangThai = TrangThaiAllEnum.TU_CHOI_LDV.getId();
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

    currentRow.get().setTrangThai(trangThai);
    currentRow.get().setLyDoTuChoi(DataUtils.safeToString(req.getLyDo()));

    tongHopCuuTroRepository.save(currentRow.get());

    // update id tong hop vao bang de xuat
    if (trangThai.equals(TrangThaiAllEnum.DA_DUYET_LDV.getId())) {
      List<XhThCuuTroDtl> tongHopDtl = tongHopCuuTroDtlRepository.findByIdTongHop(currentRow.get().getId());
      List<Long> idDxuat = tongHopDtl.stream().map(XhThCuuTroDtl::getIdDxuat).collect(Collectors.toList());
      List<XhDxCuuTroHdr> listDxuat = deXuatCuuTroRepository.findByIdIn(idDxuat);
      listDxuat.forEach(s -> {
        s.setIdTongHop(currentRow.get().getId());
        s.setTrangThaiTh(TrangThaiAllEnum.DA_TONG_HOP.getId());
      });
      deXuatCuuTroRepository.saveAll(listDxuat);
    }
    return currentRow.get();
  }
}