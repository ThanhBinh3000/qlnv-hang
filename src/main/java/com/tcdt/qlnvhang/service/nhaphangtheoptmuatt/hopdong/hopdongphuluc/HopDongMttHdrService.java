package com.tcdt.qlnvhang.service.nhaphangtheoptmuatt.hopdong.hopdongphuluc;

import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.HhQdPdKqMttSlddDtlRepository;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.HhQdPduyetKqcgRepository;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.HhQdPheduyetKqMttSLDDRepository;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.hopdong.hopdongphuluc.DiaDiemGiaoNhanMttCtRepository;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.hopdong.hopdongphuluc.DiaDiemGiaoNhanMttRepository;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.hopdong.hopdongphuluc.HopDongMttHdrRepository;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.hopdong.hopdongphuluc.DiaDiemGiaoNhanMttCtReq;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.hopdong.hopdongphuluc.DiaDiemGiaoNhanMttReq;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.hopdong.hopdongphuluc.HopDongMttHdrReq;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.HhQdPheduyetKqMttSLDD;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhChiTietKqTTinChaoGia;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhQdPdKQMttSlddDtl;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhQdPduyetKqcgHdr;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.hopdong.hopdongphuluc.DiaDiemGiaoNhanMtt;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.hopdong.hopdongphuluc.DiaDiemGiaoNhanMttCt;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.hopdong.hopdongphuluc.HopDongMttHdr;
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

import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.*;

@Service
public class HopDongMttHdrService extends BaseServiceImpl {
  @Autowired
  private HopDongMttHdrRepository hopDongHdrRepository;

  @Autowired
  private DiaDiemGiaoNhanMttRepository diaDiemGiaoNhanRepository;

  @Autowired
  private DiaDiemGiaoNhanMttCtRepository diaDiemGiaoNhanMttCtRepository;

  @Autowired
  private HhQdPduyetKqcgRepository hhQdPduyetKqcgRepository;

  @Autowired
  private FileDinhKemService fileDinhKemService;
  @Autowired
  private HhQdPdKqMttSlddDtlRepository hhQdPdKqMttSlddDtlRepository;
  @Autowired
  private HhQdPheduyetKqMttSLDDRepository hhQdPheduyetKqMttSLDDRepository;


  public Page<HopDongMttHdr> searchPage(HopDongMttHdrReq req) throws Exception {
    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit(), Sort.by("id").descending());
    Page<HopDongMttHdr> page = hopDongHdrRepository.searchPage(
            req,
            pageable
    );
    Map<String, String> hashMapVthh = getListDanhMucHangHoa();
    Map<String, String> hashMapDvi = getListDanhMucDvi(null, null, "01");
    Map<String, String> hashMapLoaiHdong = getListDanhMucChung("LOAI_HDONG");
    page.getContent().forEach(f -> {
      f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
      f.setTenTrangThaiPhuLuc(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThaiPhuLuc()));
      f.setTenTrangThaiNh(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThaiNh()));
      f.setTenDvi(hashMapDvi.get(f.getMaDvi()));
      f.setTenLoaiVthh(hashMapVthh.get(f.getLoaiVthh()));
      f.setTenCloaiVthh(hashMapVthh.get(f.getCloaiVthh()));
      f.setTenLoaiHdong(hashMapLoaiHdong.get(f.getLoaiHdong()));
    });
    return page;
  }

  @Transactional
  public HopDongMttHdr savePl(HopDongMttHdrReq req) throws Exception {
    saveDetail(req, req.getIdHd());
    return hopDongHdrRepository.findById(req.getIdHd()).get();
  }



  @Transactional
  public HopDongMttHdr save(HopDongMttHdrReq req) throws Exception {
    UserInfo userInfo = getUser();
    if (userInfo == null) {
      throw new Exception("Bad request.");
    }

    Optional<HopDongMttHdr> qOpHdong = hopDongHdrRepository.findBySoHd(req.getSoHd());
    HopDongMttHdr dataMap = new HopDongMttHdr();

    if (DataUtils.isNullObject(req.getIdHd())) {
      if (qOpHdong.isPresent()) {
        throw new Exception("Hợp đồng số" + req.getSoHd() + "đã tồn tại");
      }

      Optional<HhQdPduyetKqcgHdr> checkSoQdKq = hhQdPduyetKqcgRepository.findBySoQdKq(req.getSoQdKq());
      if (!checkSoQdKq.isPresent()){
        throw new Exception("Số quyết định phê duyệt kết quả chào giá " + req.getSoQdKq() + " không tồn tại");
      }else {
        checkSoQdKq.get().setTrangThaiHd(NhapXuatHangTrangThaiEnum.DANG_THUC_HIEN.getId());
        hhQdPduyetKqcgRepository.save(checkSoQdKq.get());
      }
    }

    BeanUtils.copyProperties(req, dataMap, "id");
    dataMap.setNguoiTaoId(userInfo.getId());
    dataMap.setNgayTao(new Date());
    dataMap.setTrangThai(Contains.DU_THAO);
    dataMap.setTrangThaiPhuLuc(Contains.DUTHAO);
    dataMap.setMaDvi(userInfo.getDvql());

    HopDongMttHdr created = hopDongHdrRepository.save(dataMap);
    if (!DataUtils.isNullObject(req.getFileDinhKem())) {
      List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(Collections.singletonList(req.getFileDinhKem()), created.getId(), HopDongMttHdr.TABLE_NAME);
      created.setFileDinhKem(fileDinhKem.get(0));
    }
    if (!DataUtils.isNullOrEmpty(req.getFileDinhKems())) {
      List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), created.getId(), HopDongMttHdr.TABLE_NAME);
      created.setFileDinhKems(fileDinhKems);
    }

    if (!DataUtils.isNullObject(req.getIdHd())) {
      if (!DataUtils.isNullOrEmpty(req.getFileDinhKems())) {
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), created.getId(), HopDongMttHdr.TABLE_NAME);
        created.setFileDinhKems(fileDinhKems);
      }
    }

    saveDetail(req, dataMap.getId());
    return created;

  }

  void saveDetail(HopDongMttHdrReq req, Long idHdr){
    diaDiemGiaoNhanRepository.deleteAllByIdHdr(idHdr);
    for (DiaDiemGiaoNhanMttReq diaDiemReq : req.getChildren()){
      DiaDiemGiaoNhanMtt  diaDiem = new DiaDiemGiaoNhanMtt();
      BeanUtils.copyProperties(diaDiemReq, diaDiem, "id");
      diaDiem.setIdHdr(idHdr);
      DiaDiemGiaoNhanMtt create = diaDiemGiaoNhanRepository.save(diaDiem);
      List<DiaDiemGiaoNhanMtt> phuLucDtl = diaDiemGiaoNhanRepository.findAllByIdHdDtl(diaDiemReq.getId());
      if (!DataUtils.isNullOrEmpty(phuLucDtl)) {
        phuLucDtl.forEach(s -> {
          s.setIdHdDtl(create.getId());
        });
        diaDiemGiaoNhanRepository.saveAll(phuLucDtl);
      }

      diaDiemGiaoNhanMttCtRepository.deleteAllByIdDiaDiem(diaDiemReq.getId());
      for (DiaDiemGiaoNhanMttCtReq diaDiemCtReq: diaDiemReq.getChildren()){
        DiaDiemGiaoNhanMttCt diaDiemCt = new DiaDiemGiaoNhanMttCt();
        BeanUtils.copyProperties(diaDiemCtReq, diaDiemCt, "id");
        diaDiemCt.setId(null);
        diaDiemCt.setIdDiaDiem(diaDiem.getId());
        diaDiemGiaoNhanMttCtRepository.save(diaDiemCt);
      }
    }

    //        Bắt đầu Phụ lục DTL
    for (DiaDiemGiaoNhanMttReq phuLucReq : req.getPhuLucDtl()){
      DiaDiemGiaoNhanMtt phuLuc = new DiaDiemGiaoNhanMtt();
      BeanUtils.copyProperties(phuLucReq, phuLuc, "id");
      phuLuc.setId(null);
      phuLuc.setIdHdr(idHdr);
      diaDiemGiaoNhanRepository.save(phuLuc);
    }
    //       Kết thúc Phụ lục DTL
  }

  @Transactional
  public HopDongMttHdr update(HopDongMttHdrReq req) throws Exception {
    UserInfo userInfo = getUser();
    if (userInfo == null) {
      throw new Exception("Bad request.");
    }

    if (StringUtils.isEmpty(req.getId())) {
      throw new Exception("Sửa thất bại, không tìm thấy dữ liệu");
    }

    Optional<HopDongMttHdr> qOptional = hopDongHdrRepository.findById(req.getId());
    if (!qOptional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu cần sửa");
    }

    if (DataUtils.isNullObject(req.getIdHd())) {
      if (qOptional.get().getSoHd() != null && !qOptional.get().getSoHd().equals(req.getSoHd())) {
        Optional<HopDongMttHdr> qOpHdong = hopDongHdrRepository.findBySoHd(req.getSoHd());
        if (qOpHdong.isPresent())
          throw new Exception("Hợp đồng số " + req.getSoHd() + " đã tồn tại");
      }

      if (qOptional.get().getSoHd() != null && !qOptional.get().getSoQdKq().equals(req.getSoQdKq())) {
        Optional<HhQdPduyetKqcgHdr> checkSoQdKq = hhQdPduyetKqcgRepository.findBySoQdKq(req.getSoQdKq());
        if (!checkSoQdKq.isPresent())
          throw new Exception("Số quyết định phê duyệt kết quả chào giá " + req.getSoQdKq() + " không tồn tại");
      }
    }
    HopDongMttHdr dataDB = qOptional.get();
    BeanUtils.copyProperties(req, dataDB, "id");

    dataDB.setNgaySua(new Date());
    dataDB.setNguoiSuaId(userInfo.getId());

    HopDongMttHdr created = hopDongHdrRepository.save(dataDB);

    if (!DataUtils.isNullObject(req.getFileDinhKem())) {
      List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(Arrays.asList(req.getFileDinhKem()), created.getId(), HopDongMttHdr.TABLE_NAME);
      dataDB.setFileDinhKem(fileDinhKem.get(0));
    }

    if (!DataUtils.isNullOrEmpty(req.getFileDinhKems())) {
      List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), created.getId(), HopDongMttHdr.TABLE_NAME);
      dataDB.setFileDinhKems(fileDinhKems);
    }

    if (!DataUtils.isNullObject(req.getIdHd())) {
      if (!DataUtils.isNullOrEmpty(req.getFileDinhKems())) {
        List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(req.getFileDinhKems(), created.getId(), HopDongMttHdr.TABLE_NAME);
        dataDB.setFileDinhKems(fileDinhKems);
      }
    }

    saveDetail(req, dataDB.getId());
    return dataDB;
  }


  public HopDongMttHdr detail(Long id) throws Exception {
    if (StringUtils.isEmpty(id)) {
      throw new UnsupportedOperationException("Không tồn tại bản ghi");
    }

    Optional<HopDongMttHdr> qOptional = hopDongHdrRepository.findById(id);

    if (!qOptional.isPresent()) {
      throw new UnsupportedOperationException("Không tồn tại bản ghi");
    }

    HopDongMttHdr data = qOptional.get();;

    Map<String, String> hashMapVthh = getListDanhMucHangHoa();
    Map<String, String> hashMapDvi = getListDanhMucDvi(null, null, "01");
    Map<String, String> hashMapLoaiHdong = getListDanhMucChung("LOAI_HDONG");

    data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
    data.setTenTrangThaiPhuLuc(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThaiPhuLuc()));
    data.setTenDvi(hashMapDvi.get(data.getMaDvi()));
    data.setTenLoaiVthh(hashMapVthh.get(data.getLoaiVthh()));
    data.setTenCloaiVthh(hashMapVthh.get(data.getCloaiVthh()));
    data.setTenLoaiHdong(hashMapLoaiHdong.get(data.getLoaiHdong()));

    List<FileDinhKem> fileDinhKem = fileDinhKemService.search(data.getId(), Arrays.asList(HopDongMttHdr.TABLE_NAME));
    if (!DataUtils.isNullOrEmpty(fileDinhKem)) {
      data.setFileDinhKem(fileDinhKem.get(0));
    }
    data.setFileDinhKems(fileDinhKem);

    List<HhQdPheduyetKqMttSLDD> listGthau = hhQdPheduyetKqMttSLDDRepository.findAllById(Collections.singleton(data.getIdQdPdSldd()));
    for (HhQdPheduyetKqMttSLDD hhQdPheduyetKqMttSLDD : listGthau) {
      hhQdPheduyetKqMttSLDD.setTenDvi(hashMapDvi.get(hhQdPheduyetKqMttSLDD.getMaDvi()));
      List<HhQdPdKQMttSlddDtl> hhQdPdKQMttSlddDtls = new ArrayList<>();
      for (HhQdPdKQMttSlddDtl hhQdPdKQMttSlddDtl : hhQdPdKqMttSlddDtlRepository.findAllByIdDiaDiem(hhQdPheduyetKqMttSLDD.getId())) {
        hhQdPdKQMttSlddDtl.setTenDiemKho(hashMapDvi.get(hhQdPdKQMttSlddDtl.getMaDiemKho()));
        hhQdPdKQMttSlddDtls.add(hhQdPdKQMttSlddDtl);
      }
      hhQdPheduyetKqMttSLDD.setChildren(hhQdPdKQMttSlddDtls);
    }

    List<DiaDiemGiaoNhanMtt> allByIdHdr = diaDiemGiaoNhanRepository.findAllByIdHdr(data.getId());
    allByIdHdr.forEach(item ->{
      item.setTenDvi(hashMapDvi.get(item.getMaDvi()));
      if (!DataUtils.isNullObject(qOptional.get().getIdHd())) {
        Optional<DiaDiemGiaoNhanMtt> byIdHdDtl = diaDiemGiaoNhanRepository.findById(item.getIdHdDtl());
        if (!DataUtils.isNullObject(byIdHdDtl)) {
          item.setTenDviHd(hashMapDvi.get(byIdHdDtl.get().getMaDvi()));
          item.setDiaChiHd(byIdHdDtl.get().getDiaChi());
        }
      }
    });
    data.setPhuLucDtl(allByIdHdr);
    data.setChildren(listGthau);

//    for (DiaDiemGiaoNhanMtt diaDiem : allByIdHdr){
//      List<DiaDiemGiaoNhanMttCt> diaDiemCt = diaDiemGiaoNhanMttCtRepository.findAllByIdDiaDiem(diaDiem.getId());
//      diaDiemCt.forEach(f ->{
//        f.setTenDiemKho(hashMapDvi.get(f.getMaDiemKho()));
//      });
//      diaDiem.setChildren(diaDiemCt);
//    }
//
//    //        Bắt đầu phụ lục
//    data.setPhuLucDtl(allByIdHdr);
//    if (!DataUtils.isNullObject(data.getIdHd())) {
//      List<FileDinhKem> fileDinhKems = fileDinhKemService.search(data.getId(), Arrays.asList(HopDongMttHdr.TABLE_NAME));
//      data.setFileDinhKems(fileDinhKems);
//    }
//
//    List<HopDongMttHdr> phuLucList = new ArrayList<>();
//    for (HopDongMttHdr phuLuc : hopDongHdrRepository.findAllByIdHd(id)){
//      List<DiaDiemGiaoNhanMtt> diaDiem = diaDiemGiaoNhanRepository.findAllByIdHdr(phuLuc.getId());
//      diaDiem.forEach(f->{
//        f.setTenDvi(hashMapDvi.get(f.getMaDvi()));
//      });
//      phuLuc.setTenTrangThaiPhuLuc(NhapXuatHangTrangThaiEnum.getTenById(phuLuc.getTrangThaiPhuLuc()));
//      phuLuc.setPhuLucDtl(diaDiem);
//      phuLucList.add(phuLuc);
//    }
//    data.setPhuLuc(phuLucList);
    return data;
  }


  @Transient
  public void delete(Long id) throws Exception {
    if (StringUtils.isEmpty(id)) {
      throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");
    }

    Optional<HopDongMttHdr> optional = hopDongHdrRepository.findById(id);
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu cần xoá");
    }

    if (DataUtils.isNullObject(optional.get().getIdHd())) {
      if (!optional.get().getTrangThai().equals(Contains.DUTHAO)) {
        throw new Exception("Chỉ được xóa với bản ghi là dự thảo");
      }
    }else {
      if (!optional.get().getTrangThaiPhuLuc().equals(Contains.DUTHAO)) {
        throw new Exception("Chỉ được xóa với bản ghi là dự thảo");
      }
    }

    List<DiaDiemGiaoNhanMtt> diaDiemList = diaDiemGiaoNhanRepository.findAllByIdHdr(id);
    for (DiaDiemGiaoNhanMtt diaDiem : diaDiemList){
      diaDiemGiaoNhanMttCtRepository.deleteAllByIdDiaDiem(diaDiem.getId());
    }

    hopDongHdrRepository.delete(optional.get());
    diaDiemGiaoNhanRepository.deleteAllByIdHdr(optional.get().getId());
    fileDinhKemService.delete(optional.get().getId(), Collections.singleton(HopDongMttHdr.TABLE_NAME));
  }

  @Transactional
  public void deleteMulti(List<Long> listMulti) throws Exception {
    if (Objects.isNull(listMulti)) {
      throw new Exception("Xoá thất bại, không tìm thấy dữ liệu");
    }
    for (Long id : listMulti) {
      this.delete(id);
    }
  }

  public HopDongMttHdr approve(HopDongMttHdrReq req) throws Exception {
    if (StringUtils.isEmpty(req.getId())) {
      throw new Exception("Không tìm thấy dữ liệu");
    }

    Optional<HopDongMttHdr> optional = hopDongHdrRepository.findById(req.getId());
    if (!optional.isPresent()){
      throw new Exception("Không tìm thấy dữ liệu");
    }

    if (DataUtils.isNullObject(optional.get().getIdHd())) {
      String status = req.getTrangThai() + optional.get().getTrangThai();
      if ((Contains.DAKY + Contains.DUTHAO).equals(status)) {
        optional.get().setNguoiPduyetId(getUser().getId());
        optional.get().setNgayPduyet(getDateTimeNow());
      } else {
        throw new Exception("Phê duyệt không thành công");
      }
      optional.get().setTrangThai(req.getTrangThai());
    }else {
      String status = req.getTrangThaiPhuLuc() + optional.get().getTrangThaiPhuLuc();
      if ((Contains.DAKY + Contains.DUTHAO).equals(status)) {
        optional.get().setNguoiPduyetId(getUser().getId());
        optional.get().setNgayPduyet(getDateTimeNow());
      } else {
        throw new Exception("Phê duyệt không thành công");
      }
      optional.get().setTrangThaiPhuLuc(req.getTrangThaiPhuLuc());
    }

    return hopDongHdrRepository.save(optional.get());
  }

  public void export( HopDongMttHdrReq objReq, HttpServletResponse response) throws Exception {
    PaggingReq paggingReq = new PaggingReq();
    paggingReq.setPage(0);
    paggingReq.setLimit(Integer.MAX_VALUE);
    objReq.setPaggingReq(paggingReq);
    Page<HopDongMttHdr> page = this.searchPage( objReq);
    List<HopDongMttHdr> data = page.getContent();

    String title = "Danh sách hợp đồng mua trực tiếp";
    String[] rowsName = new String[]{"STT", "Năm KH", "QĐ PD KH MTT", "QĐ PD KQ chào giá", "SL HĐ cần ký", "SL HĐ đã ký", "Thời hạn nhập kho", "Loại hàng hóa", "Chủng loại hàng hóa", "Tổng giá trị hợp tuổi", "Trạng thái ký HĐ", "Trạng thái NH",};
    String fileName = "danh-sach-hop-dong-mua-truc-tiep.xlsx";
    List<Object[]> dataList = new ArrayList<Object[]>();
    Object[] objs = null;
    for (int i = 0; i < data.size(); i++) {
      HopDongMttHdr dx = data.get(i);
      objs = new Object[rowsName.length];
      objs[0] = i;
      objs[1] = dx.getSoQd();
      objs[2] = dx.getSoQdKq();
      objs[3] = dx.getSoLuong();
      objs[4] = dx.getSoLuong();
      objs[5] = dx.getNgayMkho();
      objs[6] = dx.getTenLoaiVthh();
      objs[7] = dx.getTenCloaiVthh();
      objs[8] = dx.getThanhTien();
      objs[9] = dx.getTenTrangThai();
      objs[10] = dx.getTenTrangThaiNh();
      dataList.add(objs);
    }
    ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
    ex.export();
  }
}
