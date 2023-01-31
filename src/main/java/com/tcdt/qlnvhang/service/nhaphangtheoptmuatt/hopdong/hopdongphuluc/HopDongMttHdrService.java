package com.tcdt.qlnvhang.service.nhaphangtheoptmuatt.hopdong.hopdongphuluc;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.hopdong.hopdongphuluc.DiaDiemGiaoNhanMttRepository;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.hopdong.hopdongphuluc.HopDongMttHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.hopdong.hopdongphuluc.DiaDiemGiaoNhanMttReq;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.hopdong.hopdongphuluc.HopDongMttHdrReq;
import com.tcdt.qlnvhang.service.SecurityContextService;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.UserInfo;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.hopdong.hopdongphuluc.DiaDiemGiaoNhanMtt;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.hopdong.hopdongphuluc.HopDongMttHdr;
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
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HopDongMttHdrService extends BaseServiceImpl {
  @Autowired
  private FileDinhKemService fileDinhKemService;
  @Autowired
  private HopDongMttHdrRepository hopDongHdrRepository;

  @Autowired
  private DiaDiemGiaoNhanMttRepository diaDiemGiaoNhanRepository;

  public Page<HopDongMttHdr> searchPage(CustomUserDetails currentUser, HopDongMttHdrReq req) throws Exception {
    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
    Page<HopDongMttHdr> search = hopDongHdrRepository.search(req, pageable);
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
      s.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(s.getTrangThai()));
      s.setTenTrangThaiNh(NhapXuatHangTrangThaiEnum.getTenById(s.getTrangThaiNh()));
    });
    return search;
  }

  @Transactional
  public HopDongMttHdr save(HopDongMttHdrReq objReq) throws Exception {
    UserInfo userInfo = SecurityContextService.getUser();
    if (userInfo == null)
      throw new Exception("Bad request.");
    Optional<HopDongMttHdr> optional = hopDongHdrRepository.findBySoHd(objReq.getSoHd());
    if (optional.isPresent()) {
      throw new Exception("số hợp đồng đã tồn tại");
    }
    HopDongMttHdr data = new HopDongMttHdr();
    BeanUtils.copyProperties(objReq, data);
    data.setTrangThai(Contains.DUTHAO);
    Map<String, String> hashMapDmHh = getListDanhMucHangHoa();
    Map<String, String> hashMapDmdv = getListDanhMucDvi(null, null, "01");
    data.setTenDvi(StringUtils.isEmpty(userInfo.getDvql()) ? null : hashMapDmdv.get(userInfo.getDvql()));
    data.setMaDvi(userInfo.getDepartment());
    data.setTenLoaiVthh(StringUtils.isEmpty(data.getLoaiVthh()) ? null : hashMapDmHh.get(data.getLoaiVthh()));
    HopDongMttHdr created = hopDongHdrRepository.save(data);

    if (!DataUtils.isNullObject(objReq.getFileDinhKem())) {
      List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKem(), data.getId(), HopDongMttHdr.TABLE_NAME + "_DINH_KEM");
      created.setFileDinhKem(fileDinhKem);
    }
    List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(objReq.getCanCu(), data.getId(), HopDongMttHdr.TABLE_NAME + "_CAN_CU");
    created.setCanCu(canCu);
    for (HopDongMttHdrReq phuLucReq : objReq.getPhuLuc()) {
      HopDongMttHdr phuLuc = ObjectMapperUtils.map(phuLucReq, HopDongMttHdr.class);
      phuLuc.setId(null);
      phuLuc.setIdHd(created.getId());
      hopDongHdrRepository.save(phuLuc);
    }

    for (DiaDiemGiaoNhanMttReq listDtl : objReq.getDiaDiemGiaoNhan()) {
      DiaDiemGiaoNhanMtt dtl = ObjectMapperUtils.map(listDtl, DiaDiemGiaoNhanMtt.class);
      dtl.setId(null);
      dtl.setIdHdr(created.getId());
      diaDiemGiaoNhanRepository.save(dtl);
    }
    return created;
  }

  @Transactional
  public HopDongMttHdr update(HopDongMttHdrReq objReq) throws Exception {
    UserInfo userInfo = SecurityContextService.getUser();
    if (userInfo == null)
      throw new Exception("Bad request.");
    Optional<HopDongMttHdr> optional = hopDongHdrRepository.findById(objReq.getId());

    Optional<HopDongMttHdr> soBienBan = hopDongHdrRepository.findBySoHd(objReq.getSoHd());
    if (soBienBan.isPresent()) {
      if (!soBienBan.get().getId().equals(objReq.getId())) {
        throw new Exception("số hợp đồng đã tồn tại");
      }
    }
    HopDongMttHdr data = optional.get();
//    HopDongMttHdr dataMap = new ModelMapper().map(objReq,HopDongMttHdr.class);
//    updateObjectToObject(data,dataMap);
    BeanUtils.copyProperties(objReq, data);
    HopDongMttHdr created = hopDongHdrRepository.save(data);
    fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(HopDongMttHdr.TABLE_NAME + "_DINH_KEM", HopDongMttHdr.TABLE_NAME + "_CAN_CU"));
    if (!DataUtils.isNullObject(objReq.getFileDinhKem())) {
      List<FileDinhKem> fileDinhKem = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKem(), created.getId(), HopDongMttHdr.TABLE_NAME + "_DINH_KEM");
      created.setFileDinhKem(fileDinhKem);
    }
    List<FileDinhKem> canCu = fileDinhKemService.saveListFileDinhKem(objReq.getCanCu(), data.getId(), HopDongMttHdr.TABLE_NAME + "_CAN_CU");
    created.setCanCu(canCu);
    List<HopDongMttHdr> listPhuLuc = hopDongHdrRepository.findByIdHd(objReq.getId());
    hopDongHdrRepository.deleteAll(listPhuLuc);
    for (HopDongMttHdrReq phuLucReq : objReq.getPhuLuc()) {
      HopDongMttHdr phuLuc = new HopDongMttHdr();
      BeanUtils.copyProperties(phuLucReq, phuLuc);
      phuLuc.setId(null);
      phuLuc.setIdHd(created.getId());
      hopDongHdrRepository.save(phuLuc);
    }
    List<DiaDiemGiaoNhanMtt> DiaDiemGiaoNhanMtts = diaDiemGiaoNhanRepository.findAllByIdHdr(objReq.getId());
    diaDiemGiaoNhanRepository.deleteAll(DiaDiemGiaoNhanMtts);
    for (DiaDiemGiaoNhanMttReq listDtl : objReq.getDiaDiemGiaoNhan()) {
      DiaDiemGiaoNhanMtt dtl = new DiaDiemGiaoNhanMtt();
      BeanUtils.copyProperties(listDtl, dtl);
      dtl.setId(null);
      dtl.setIdHdr(data.getId());
      diaDiemGiaoNhanRepository.save(dtl);
    }
    return created;
  }


  public List<HopDongMttHdr> detail(List<Long> ids) throws Exception {
    if (DataUtils.isNullOrEmpty(ids))
      throw new Exception("Tham số không hợp lệ.");
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    List<HopDongMttHdr> allById = hopDongHdrRepository.findAllById(ids);
    allById.forEach(data -> {
      List<FileDinhKem> canCu = fileDinhKemService.search(data.getId(), Arrays.asList(HopDongMttHdr.TABLE_NAME + "_CAN_CU"));
      List<FileDinhKem> dinhKem = fileDinhKemService.search(data.getId(), Arrays.asList(HopDongMttHdr.TABLE_NAME + "_DINH_KEM"));
      data.setCanCu(canCu);
      data.setFileDinhKem(dinhKem);
      List<HopDongMttHdr> listPhuLuc = hopDongHdrRepository.findByIdHd(data.getId());
      data.setPhuLuc(listPhuLuc);
      data.setTenDvi(mapDmucDvi.get(data.getMaDvi()));
      data.setTenLoaiVthh(mapVthh.get(data.getLoaiVthh()));
      data.setTenCloaiVthh(mapVthh.get(data.getCloaiVthh()));
      data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
    });
    return allById;
  }


  @Transient
  public void delete(IdSearchReq idSearchReq) throws Exception {
    Optional<HopDongMttHdr> optional = hopDongHdrRepository.findById(idSearchReq.getId());
    if (!optional.isPresent()) {
      throw new Exception("Bản ghi không tồn tại");
    }
    if (!optional.get().getTrangThai().equals(Contains.DUTHAO)
        && !optional.get().getTrangThai().equals(Contains.TUCHOI_LDCC)) {
      throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối");
    }
    HopDongMttHdr data = optional.get();
    List<HopDongMttHdr> listPhuLuc = hopDongHdrRepository.findByIdHd(idSearchReq.getId());
    hopDongHdrRepository.deleteAll(listPhuLuc);
    List<DiaDiemGiaoNhanMtt> listDtl = diaDiemGiaoNhanRepository.findAllByIdHdr(data.getId());
    diaDiemGiaoNhanRepository.deleteAll(listDtl);
    fileDinhKemService.delete(data.getId(), Lists.newArrayList(HopDongMttHdr.TABLE_NAME + "_DINH_KEM"));
    fileDinhKemService.delete(data.getId(), Lists.newArrayList(HopDongMttHdr.TABLE_NAME + "_CAN_CU"));
    hopDongHdrRepository.delete(data);
  }

  @Transient
  public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
    List<HopDongMttHdr> list = hopDongHdrRepository.findAllByIdIn(idSearchReq.getIdList());

    if (list.isEmpty()) {
      throw new Exception("Bản ghi không tồn tại");
    }
    for (HopDongMttHdr dxuatKhMttHdr : list) {
      if (!dxuatKhMttHdr.getTrangThai().equals(Contains.DUTHAO)
          && !dxuatKhMttHdr.getTrangThai().equals(Contains.TUCHOI_LDCC)) {
        throw new Exception("Chỉ thực hiện xóa với quyết định ở trạng thái bản nháp hoặc từ chối");
      }
    }
    List<Long> listId = list.stream().map(HopDongMttHdr::getId).collect(Collectors.toList());
    List<HopDongMttHdr> listPhuLuc = hopDongHdrRepository.findByIdHdIn(listId);
    hopDongHdrRepository.deleteAll(listPhuLuc);
    List<DiaDiemGiaoNhanMtt> listDtl = diaDiemGiaoNhanRepository.findAllByIdHdrIn(listId);
    diaDiemGiaoNhanRepository.deleteAll(listDtl);
    fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(HopDongMttHdr.TABLE_NAME + "_DINH_KEM"));
    fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(HopDongMttHdr.TABLE_NAME + "_CAN_CU"));
    hopDongHdrRepository.deleteAll(list);
  }

  public HopDongMttHdr approve(StatusReq statusReq) throws Exception {
    UserInfo userInfo = SecurityContextService.getUser();
    if (StringUtils.isEmpty(statusReq.getId())) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    Optional<HopDongMttHdr> optional = hopDongHdrRepository.findById(Long.valueOf(statusReq.getId()));
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    }

    String status = statusReq.getTrangThai() + optional.get().getTrangThai();
    switch (status) {
      case Contains.DAKY + Contains.DUTHAO:
        optional.get().setNguoiKy(userInfo.getUsername());
        optional.get().setNgayKy(LocalDate.now());
        break;
      default:
        throw new Exception("Phê duyệt không thành công");
    }
    optional.get().setTrangThai(statusReq.getTrangThai());
    HopDongMttHdr created = hopDongHdrRepository.save(optional.get());
    return created;
  }

  public void export(CustomUserDetails currentUser, HopDongMttHdrReq objReq, HttpServletResponse response) throws Exception {
    PaggingReq paggingReq = new PaggingReq();
    paggingReq.setPage(0);
    paggingReq.setLimit(Integer.MAX_VALUE);
    objReq.setPaggingReq(paggingReq);
    Page<HopDongMttHdr> page = this.searchPage(currentUser, objReq);
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
      objs[1] = dx.getSoQdPdKh();
      objs[2] = dx.getSoQdPdKq();
      objs[3] = dx.getSoLuong();
      objs[4] = dx.getSoLuong();
      objs[5] = dx.getTgianNkho();
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
