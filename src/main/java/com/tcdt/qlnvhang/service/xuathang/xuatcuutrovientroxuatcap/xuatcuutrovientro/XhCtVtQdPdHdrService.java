package com.tcdt.qlnvhang.service.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.*;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.SearchXhCtvtQuyetDinhPdHdr;
import com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtVtQuyetDinhPdHdrReq;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtVtQuyetDinhPdHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtDeXuatHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtTongHopHdr;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class XhCtVtQdPdHdrService extends BaseServiceImpl {


  @Autowired
  private XhCtVtQdPdHdrRepository xhCtVtQdPdHdrRepository;

  @Autowired
  private XhCtVtQdPdDtlRepository xhCtVtQdPdDtlRepository;

  @Autowired

  private XhCtVtQdPdDxRepository xhCtVtQdPdDxRepository;

  @Autowired
  private XhCtvtTongHopHdrRepository xhCtvtTongHopHdrRepository;

  @Autowired
  private XhCtvtDeXuatHdrRepository xhCtvtDeXuatHdrRepository;

  @Autowired
  private FileDinhKemService fileDinhKemService;


  public Page<XhCtVtQuyetDinhPdHdr> searchPage(CustomUserDetails currentUser, SearchXhCtvtQuyetDinhPdHdr req) throws Exception {
    req.setDvql(currentUser.getDvql());
    //cuc xem cac quyet dinh tu tong cuc
    if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
      req.setMaDviGiao(currentUser.getDvql());
    }

    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
    Page<XhCtVtQuyetDinhPdHdr> search = xhCtVtQdPdHdrRepository.search(req, pageable);
    Map<String, Map<String, Object>> mapDmucDvi = getListDanhMucDviObject(null, null, "01");

    Map<String, String> mapVthh = getListDanhMucHangHoa();
    search.getContent().forEach(s -> {
      if (mapDmucDvi.containsKey((s.getMaDvi()))) {
        Map<String, Object> objDonVi = mapDmucDvi.get(s.getMaDvi());
        s.setTenDvi(objDonVi.get("tenDvi").toString());
      }
      if (mapVthh.get((s.getLoaiVthh())) != null) {
        s.setTenLoaiVthh(mapVthh.get(s.getLoaiVthh()));
      }
      if (mapVthh.get((s.getCloaiVthh())) != null) {
        s.setTenCloaiVthh(mapVthh.get(s.getCloaiVthh()));
      }
      s.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(s.getTrangThai()));
    });
    return search;
  }

  @Transactional
  public XhCtVtQuyetDinhPdHdr save(CustomUserDetails currentUser, XhCtVtQuyetDinhPdHdrReq objReq) throws Exception {
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    if (!DataUtils.isNullObject(objReq.getSoQd())) {
      Optional<XhCtVtQuyetDinhPdHdr> optional = xhCtVtQdPdHdrRepository.findBySoQd(objReq.getSoQd());
      if (optional.isPresent()) {
        throw new Exception("số quyết định đã tồn tại");
      }
    }
    XhCtVtQuyetDinhPdHdr data = new XhCtVtQuyetDinhPdHdr();
    BeanUtils.copyProperties(objReq, data);
    data.setMaDvi(currentUser.getUser().getDepartment());
    data.setTrangThai(Contains.DUTHAO);

    data.getQuyetDinhPdDtl().forEach(s -> {
      s.setXhCtVtQuyetDinhPdHdr(data);
      s.getQuyetDinhPdDx().forEach(s1 -> s1.setXhCtVtQuyetDinhPdDtl(s));
    });
    //save child
    XhCtVtQuyetDinhPdHdr created = xhCtVtQdPdHdrRepository.save(data);
//    created.getQuyetDinhPdDtl().forEach(s -> s.setIdHdr(created.getId()));
//    List<XhCtVtQuyetDinhPdDtl> dtl = xhCtVtQdPdDtlRepository.saveAll(created.getQuyetDinhPdDtl());
//    dtl.forEach(s -> {
//      s.getQuyetDinhPdDx().forEach(s1 -> s1.setIdHdr(s.getId()));
//      xhCtVtQdPdDxRepository.saveAll(s.getQuyetDinhPdDx());
//    });

    if (!DataUtils.isNullOrEmpty(objReq.getCanCu())) {
      fileDinhKemService.saveListFileDinhKem(objReq.getCanCu(), created.getId(), XhCtVtQuyetDinhPdHdr.TABLE_NAME + "_CAN_CU");
    }
    if (!DataUtils.isNullObject(objReq.getFileDinhKem())) {
      fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKem(), created.getId(), XhCtVtQuyetDinhPdHdr.TABLE_NAME);
    }

    if (!DataUtils.isNullOrEmpty(created.getMaTongHop())) {
      Optional<XhCtvtTongHopHdr> tongHopHdr = xhCtvtTongHopHdrRepository.findById(created.getIdTongHop());
      XhCtvtTongHopHdr tongHop = tongHopHdr.get();
      tongHop.setSoQdPd(created.getSoQd());
      tongHop.setIdQdPd(created.getId());
      tongHop.setNgayKyQd(created.getNgayKy());
      xhCtvtTongHopHdrRepository.save(tongHop);
    }
    if (!DataUtils.isNullOrEmpty(created.getSoDx())) {
      Optional<XhCtvtDeXuatHdr> deXuatHdr = xhCtvtDeXuatHdrRepository.findById(created.getIdDx());
      XhCtvtDeXuatHdr deXuat = deXuatHdr.get();
      deXuat.setSoQdPd(created.getSoQd());
      deXuat.setIdQdPd(created.getId());
      deXuat.setNgayKyQd(created.getNgayKy());
      xhCtvtDeXuatHdrRepository.save(deXuat);
    }
//    this.saveCtiet(created.getId(), objReq);
    return created;
  }

//  @Transactional()
//  void saveCtiet(Long id, XhCtVtQuyetDinhPdHdrReq objReq) {
//    for (XhCtVtQuyetDinhPdDtl quyetDinhPdDtlReq : objReq.getQuyetDinhPdDtl()) {
//      XhCtVtQuyetDinhPdDtl quyetDinhPdDtl = new XhCtVtQuyetDinhPdDtl();
//      BeanUtils.copyProperties(quyetDinhPdDtlReq, quyetDinhPdDtl);
//      quyetDinhPdDtl.setId(null);
//      quyetDinhPdDtl.setIdHdr(id);
//      XhCtVtQuyetDinhPdDtl dtl = xhCtVtQdPdDtlRepository.save(quyetDinhPdDtl);
//      for (XhCtVtQuyetDinhPdDx quyetDinhPdDxReq : quyetDinhPdDtlReq.getQuyetDinhPdDx()) {
//        XhCtVtQuyetDinhPdDx quyetDinhPdDx = new XhCtVtQuyetDinhPdDx();
//        BeanUtils.copyProperties(quyetDinhPdDxReq, quyetDinhPdDx);
//        quyetDinhPdDx.setId(null);
//        quyetDinhPdDx.setIdHdr(dtl.getId());
//        xhCtVtQdPdDxRepository.save(quyetDinhPdDx);
//      }
//    }
//  }

  @Transactional
  public XhCtVtQuyetDinhPdHdr update(CustomUserDetails currentUser, XhCtVtQuyetDinhPdHdrReq objReq) throws Exception {
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    Optional<XhCtVtQuyetDinhPdHdr> optional = xhCtVtQdPdHdrRepository.findById(objReq.getId());
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu cần sửa");
    }
    Optional<XhCtVtQuyetDinhPdHdr> soDx = xhCtVtQdPdHdrRepository.findBySoQd(objReq.getSoQd());
    if (soDx.isPresent()) {
      if (!soDx.get().getId().equals(objReq.getId())) {
        throw new Exception("số quyết định đã tồn tại");
      }
    }
    if (!DataUtils.safeToString(objReq.getIdDx()).equals(DataUtils.safeToString(optional.get().getIdDx())) ||
        !DataUtils.safeToString(objReq.getIdTongHop()).equals(DataUtils.safeToString(optional.get().getIdTongHop()))) {
//      List<XhCtVtQuyetDinhPdDtl> quyetDinhPdDtl = xhCtVtQdPdDtlRepository.findByIdHdr(objReq.getId());
//      xhCtVtQdPdDtlRepository.deleteAll(quyetDinhPdDtl);
//      List<Long> XhCtVtQuyetDinhPdDx = quyetDinhPdDtl.stream().map(XhCtVtQuyetDinhPdDtl::getId).collect(Collectors.toList());
//      List<XhCtVtQuyetDinhPdDx> quyetDinhPdDx = xhCtVtQdPdDxRepository.findByIdHdrIn(XhCtVtQuyetDinhPdDx);
//      xhCtVtQdPdDxRepository.deleteAll(quyetDinhPdDx);

//      objReq.getQuyetDinhPdDtl().forEach(s -> {
//        s.setIdHdr(objReq.getId());
//        XhCtVtQuyetDinhPdDtl dtl = xhCtVtQdPdDtlRepository.save(s);
//        dtl.getQuyetDinhPdDx().forEach(s1 -> s1.setIdHdr(dtl.getId()));
//      });


    }

    XhCtVtQuyetDinhPdHdr data = optional.get();
    objReq.getQuyetDinhPdDtl().forEach(s -> {
      s.setXhCtVtQuyetDinhPdHdr(null);
      s.getQuyetDinhPdDx().forEach(s1 -> s1.setXhCtVtQuyetDinhPdDtl(null));
    });
    BeanUtils.copyProperties(objReq, data, "id");
    data.getQuyetDinhPdDtl().forEach(s -> {
      s.setXhCtVtQuyetDinhPdHdr(data);
      s.getQuyetDinhPdDx().forEach(s1 -> s1.setXhCtVtQuyetDinhPdDtl(s));
    });
    XhCtVtQuyetDinhPdHdr created = xhCtVtQdPdHdrRepository.save(data);


    fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(XhCtVtQuyetDinhPdHdr.TABLE_NAME + "_CAN_CU"));

    fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(XhCtVtQuyetDinhPdHdr.TABLE_NAME));

    if (!DataUtils.isNullOrEmpty(objReq.getCanCu())) {
      fileDinhKemService.saveListFileDinhKem(objReq.getCanCu(), created.getId(), XhCtVtQuyetDinhPdHdr.TABLE_NAME + "_CAN_CU");
    }
    if (!DataUtils.isNullObject(objReq.getFileDinhKem())) {
      fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKem(), created.getId(), XhCtVtQuyetDinhPdHdr.TABLE_NAME);
    }
/*
    List<XhCtVtQuyetDinhPdDtl> quyetDinhPdDtl = xhCtVtQdPdDtlRepository.findByIdHdr(objReq.getId());
    xhCtVtQdPdDtlRepository.deleteAll(quyetDinhPdDtl);

    List<Long> XhCtVtQuyetDinhPdDx = quyetDinhPdDtl.stream().map(XhCtVtQuyetDinhPdDtl::getId).collect(Collectors.toList());
    List<XhCtVtQuyetDinhPdDx> quyetDinhPdDx = xhCtVtQdPdDxRepository.findByIdHdrIn(XhCtVtQuyetDinhPdDx);
    xhCtVtQdPdDxRepository.deleteAll(quyetDinhPdDx);
    this.saveCtiet(created.getId(), objReq);*/
    return created;
  }


  public List<XhCtVtQuyetDinhPdHdr> detail(List<Long> ids) throws Exception {
    if (DataUtils.isNullOrEmpty(ids)) throw new Exception("Tham số không hợp lệ.");
    List<XhCtVtQuyetDinhPdHdr> optional = xhCtVtQdPdHdrRepository.findByIdIn(ids);
    if (DataUtils.isNullOrEmpty(optional)) {
      throw new Exception("Không tìm thấy dữ liệu");
    }

    Map<String, Map<String, Object>> mapDmucDvi = getListDanhMucDviObject(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    List<XhCtVtQuyetDinhPdHdr> allById = xhCtVtQdPdHdrRepository.findAllById(ids);
    allById.forEach(data -> {
      if (mapDmucDvi.containsKey(data.getMaDvi())) {
        data.setTenDvi(mapDmucDvi.get(data.getMaDvi()).get("tenDvi").toString());
      }
      data.setTenLoaiVthh(mapVthh.get(data.getLoaiVthh()));
      data.setTenCloaiVthh(mapVthh.get(data.getCloaiVthh()));
      data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));

      List<FileDinhKem> fileDinhKem = fileDinhKemService.search(data.getId(), Arrays.asList(XhCtVtQuyetDinhPdHdr.TABLE_NAME));
      data.setFileDinhKem(fileDinhKem);


      List<FileDinhKem> canCu = fileDinhKemService.search(data.getId(), Arrays.asList(XhCtVtQuyetDinhPdHdr.TABLE_NAME + "_CAN_CU"));
      data.setCanCu(canCu);

      data.getQuyetDinhPdDtl().forEach(s -> {
        if (mapDmucDvi.containsKey(s.getMaDviDx())) {
          s.setTenDviDx(mapDmucDvi.get(s.getMaDviDx()).get("tenDvi").toString());
        }
        s.getQuyetDinhPdDx().forEach(s1 -> {
          if (mapDmucDvi.containsKey(s1.getMaDviCuc())) {
            s1.setTenCuc(mapDmucDvi.get(s1.getMaDviCuc()).get("tenDvi").toString());
          }
          if (mapDmucDvi.containsKey(s1.getMaDviChiCuc())) {
            s1.setTenChiCuc(mapDmucDvi.get(s1.getMaDviChiCuc()).get("tenDvi").toString());
          }
          s1.setTenCloaiVthh(mapVthh.get(s1.getCloaiVthh()));
        });
      });

//      List<XhCtVtQuyetDinhPdDtl> ListDtl = xhCtVtQdPdDtlRepository.findByXhCtVtQuyetDinhPdHdrId(data.getId());
//      for (XhCtVtQuyetDinhPdDtl quyetDinhPdDtl : ListDtl) {
//        if (mapDmucDvi.containsKey(quyetDinhPdDtl.getMaDviDx())) {
//          quyetDinhPdDtl.setTenDviDx(mapDmucDvi.get(quyetDinhPdDtl.getMaDviDx()).get("tenDvi").toString());
//        }
//        List<XhCtVtQuyetDinhPdDx> listQuyetDinhPdDx = xhCtVtQdPdDxRepository.findByXhCtVtQuyetDinhPdDtlIn(Collections.singletonList(quyetDinhPdDtl.getId()));
//        for (XhCtVtQuyetDinhPdDx quyetDinhPdDx : listQuyetDinhPdDx) {
//          if (mapDmucDvi.containsKey(quyetDinhPdDx.getMaDviCuc())) {
//            quyetDinhPdDx.setTenCuc(mapDmucDvi.get(quyetDinhPdDx.getMaDviCuc()).get("tenDvi").toString());
//          }
//          if (mapDmucDvi.containsKey(quyetDinhPdDx.getMaDviChiCuc())) {
//            quyetDinhPdDx.setTenChiCuc(mapDmucDvi.get(quyetDinhPdDx.getMaDviChiCuc()).get("tenDvi").toString());
//          }
//          quyetDinhPdDx.setTenCloaiVthh(mapVthh.get(quyetDinhPdDx.getCloaiVthh()));
//        }
//        quyetDinhPdDtl.setQuyetDinhPdDx(listQuyetDinhPdDx);
//        data.setQuyetDinhPdDtl(ListDtl);
//      }
    });
    return allById;
  }

  @Transactional
  public void delete(IdSearchReq idSearchReq) throws Exception {
    Optional<XhCtVtQuyetDinhPdHdr> optional = xhCtVtQdPdHdrRepository.findById(idSearchReq.getId());
    if (!optional.isPresent()) {
      throw new Exception("Bản ghi không tồn tại");
    }
    XhCtVtQuyetDinhPdHdr data = optional.get();

//    List<XhCtVtQuyetDinhPdDtl> quyetDinhPdDtl = xhCtVtQdPdDtlRepository.findByXhCtVtQuyetDinhPdHdrId(data.getId());
//    xhCtVtQdPdDtlRepository.deleteAll(quyetDinhPdDtl);
//
//    List<Long> XhCtVtQuyetDinhPdDx = quyetDinhPdDtl.stream().map(XhCtVtQuyetDinhPdDtl::getId).collect(Collectors.toList());
//    List<XhCtVtQuyetDinhPdDx> quyetDinhPdDx = xhCtVtQdPdDxRepository.findByXhCtVtQuyetDinhPdDtlIn(XhCtVtQuyetDinhPdDx);
//    xhCtVtQdPdDxRepository.deleteAll(quyetDinhPdDx);

    fileDinhKemService.delete(data.getId(), Lists.newArrayList(XhCtVtQuyetDinhPdHdr.TABLE_NAME + "_CAN_CU"));
    fileDinhKemService.delete(data.getId(), Lists.newArrayList(XhCtVtQuyetDinhPdHdr.TABLE_NAME));
    xhCtVtQdPdHdrRepository.delete(data);

    String maThop = data.getMaTongHop();
    if (!DataUtils.isNullOrEmpty(maThop)) {
      XhCtvtTongHopHdr tongHop = xhCtvtTongHopHdrRepository.findById(data.getIdTongHop()).orElse(null);
      if (tongHop != null) {
        tongHop.setSoQdPd(null);
        tongHop.setIdQdPd(null);
        tongHop.setNgayKyQd(null);
        xhCtvtTongHopHdrRepository.save(tongHop);
      }
    }
    String soDx = data.getSoDx();
    if (!DataUtils.isNullOrEmpty(soDx)) {
      XhCtvtDeXuatHdr deXuat = xhCtvtDeXuatHdrRepository.findById(data.getIdDx()).orElse(null);
      if (deXuat != null) {
        deXuat.setSoQdPd(null);
        deXuat.setIdQdPd(null);
        deXuat.setNgayKyQd(null);
        xhCtvtDeXuatHdrRepository.save(deXuat);
      }
    }
  }

  @Transactional
  public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
    List<XhCtVtQuyetDinhPdHdr> list = xhCtVtQdPdHdrRepository.findAllByIdIn(idSearchReq.getIdList());

    if (list.isEmpty()) {
      throw new Exception("Bản ghi không tồn tại");
    }
//    List<Long> listId = list.stream().map(XhCtVtQuyetDinhPdHdr::getId).collect(Collectors.toList());
//    List<XhCtVtQuyetDinhPdDtl> listDtl = xhCtVtQdPdDtlRepository.findByXhCtVtQuyetDinhPdHdrIdIn(listId);
//    List<Long> XhCtVtQuyetDinhPdDx = listDtl.stream().map(XhCtVtQuyetDinhPdDtl::getId).collect(Collectors.toList());
//    List<XhCtVtQuyetDinhPdDx> quyetDinhPdDx = xhCtVtQdPdDxRepository.findByXhCtVtQuyetDinhPdDtlIn(XhCtVtQuyetDinhPdDx);
//    xhCtVtQdPdDxRepository.deleteAll(quyetDinhPdDx);
//    xhCtVtQdPdDtlRepository.deleteAll(listDtl);
    fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(XhCtVtQuyetDinhPdHdr.TABLE_NAME));
    fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(XhCtVtQuyetDinhPdHdr.TABLE_NAME + "_CAN_CU"));
    xhCtVtQdPdHdrRepository.deleteAll(list);
    List<Long> listIdThop = list.stream().map(XhCtVtQuyetDinhPdHdr::getIdTongHop).collect(Collectors.toList());
    if (!CollectionUtils.isEmpty(listIdThop)) {
      List<XhCtvtTongHopHdr> tongHopHdr = xhCtvtTongHopHdrRepository.findByIdIn(listIdThop);
      tongHopHdr.stream().map(item -> {
        item.setNgayKyQd(null);
        item.setIdQdPd(null);
        item.setSoQdPd(null);
        return item;
      }).collect(Collectors.toList());
    }
    List<Long> listIdDx = list.stream().map(XhCtVtQuyetDinhPdHdr::getIdDx).collect(Collectors.toList());
    if (!CollectionUtils.isEmpty(listIdThop)) {
      List<XhCtvtDeXuatHdr> deXuatHdr = xhCtvtDeXuatHdrRepository.findByIdIn(listIdDx);
      deXuatHdr.stream().map(item -> {
        item.setNgayKyQd(null);
        item.setIdQdPd(null);
        item.setSoQdPd(null);
        return item;
      }).collect(Collectors.toList());
    }
  }


  public XhCtVtQuyetDinhPdHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {

    if (StringUtils.isEmpty(statusReq.getId())) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    Optional<XhCtVtQuyetDinhPdHdr> optional = xhCtVtQdPdHdrRepository.findById(Long.valueOf(statusReq.getId()));
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    }

    String status = statusReq.getTrangThai() + optional.get().getTrangThai();
    switch (status) {
      case Contains.BAN_HANH + Contains.DUTHAO:
        optional.get().setNgayPduyet(LocalDate.now());
        optional.get().setNguoiPduyetId(currentUser.getUser().getId());
        break;
      default:
        throw new Exception("Phê duyệt không thành công");
    }
    optional.get().setTrangThai(statusReq.getTrangThai());
    XhCtVtQuyetDinhPdHdr created = xhCtVtQdPdHdrRepository.save(optional.get());
    return created;
  }


  public void export(CustomUserDetails currentUser, SearchXhCtvtQuyetDinhPdHdr objReq, HttpServletResponse response) throws Exception {
    PaggingReq paggingReq = new PaggingReq();
    paggingReq.setPage(0);
    paggingReq.setLimit(Integer.MAX_VALUE);
    objReq.setPaggingReq(paggingReq);
    Page<XhCtVtQuyetDinhPdHdr> page = this.searchPage(currentUser, objReq);
    List<XhCtVtQuyetDinhPdHdr> data = page.getContent();

    String title = "Danh sách quyết định phương án xuất cứu trợ, viện trợ ";
    String[] rowsName = new String[]{"STT", "Số quyết định", "Ngày ký quyết định", "Mã tổng hợp", "Ngày tổng hợp", "Số công văn/đề xuất", "Ngày đề xuất", "Loại hàng hóa", "Tổng SL đề xuất cứu trợ,viện trợ (kg)", "Tổng SL xuất kho cứu trợ,viện trợ (kg)", "SL xuất CT,VT chuyển sang xuất cấp", "Trích yếu", "Trạng thái quyết định",};
    String fileName = "danh-sach-phuong-an-xuat-cuu-tro-vien-tro.xlsx";
    List<Object[]> dataList = new ArrayList<Object[]>();
    Object[] objs = null;
    for (int i = 0; i < data.size(); i++) {
      XhCtVtQuyetDinhPdHdr dx = data.get(i);
      objs = new Object[rowsName.length];
      objs[0] = i;
      objs[1] = dx.getSoQd();
      objs[2] = dx.getNgayKy();
      objs[3] = dx.getMaTongHop();
      objs[4] = dx.getNgayThop();
      objs[5] = dx.getSoDx();
      objs[6] = dx.getNgayDx();
      objs[7] = dx.getTenLoaiVthh();
      objs[8] = dx.getTongSoLuongDx();
      objs[9] = dx.getTongSoLuong();
      objs[10] = dx.getSoLuongXuaCap();
      objs[11] = dx.getTrichYeu();
      objs[12] = dx.getTenTrangThai();
      dataList.add(objs);
    }
    ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
    ex.export();
  }

  public List<XhCtVtQuyetDinhPdHdr> searchQdPaXc(CustomUserDetails currentUser, SearchXhCtvtQuyetDinhPdHdr req) throws Exception {
//        req.setDvql(currentUser.getDvql());
//        //cuc xem cac quyet dinh tu tong cuc
//        if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
//            req.setMaDviGiao(currentUser.getDvql());
//        }
    return xhCtVtQdPdHdrRepository.searchQdPaXuatCap(req);
  }
}