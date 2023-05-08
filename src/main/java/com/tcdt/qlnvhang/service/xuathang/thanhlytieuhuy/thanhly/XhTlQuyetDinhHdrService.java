package com.tcdt.qlnvhang.service.xuathang.thanhlytieuhuy.thanhly;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly.XhTlQuyetDinhHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.SearchXhTlQuyetDinh;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlQuyetDinhHdrReq;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlQuyetDinhHdr;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
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

@Service
public class XhTlQuyetDinhHdrService extends BaseServiceImpl {


  @Autowired
  private XhTlQuyetDinhHdrRepository xhTlQuyetDinhHdrRepository;

  @Autowired
  private FileDinhKemService fileDinhKemService;

  public Page<XhTlQuyetDinhHdr> searchPage(CustomUserDetails currentUser, SearchXhTlQuyetDinh req) throws Exception {
    String dvql = currentUser.getDvql();
    if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
      req.setDvql(dvql.substring(0, 4));
    } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_TONG_CUC)) {
      req.setDvql(dvql);
    }
    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
    Page<XhTlQuyetDinhHdr> search = xhTlQuyetDinhHdrRepository.search(req, pageable);
    Map<String, Map<String, Object>> mapDmucDvi = getListDanhMucDviObject(null, null, "01");

    Map<String, String> mapVthh = getListDanhMucHangHoa();
    search.getContent().forEach(s -> {
      if (mapDmucDvi.containsKey((s.getMaDvi()))) {
        Map<String, Object> objDonVi = mapDmucDvi.get(s.getMaDvi());
        s.setTenDvi(objDonVi.get("tenDvi").toString());
      }
      s.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(s.getTrangThai()));
    });
    return search;
  }

  @Transactional
  public XhTlQuyetDinhHdr save(CustomUserDetails currentUser, XhTlQuyetDinhHdrReq objReq) throws Exception {
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    if (!DataUtils.isNullObject(objReq.getSoQd())) {
      Optional<XhTlQuyetDinhHdr> optional = xhTlQuyetDinhHdrRepository.findBySoQd(objReq.getSoQd());
      if (optional.isPresent()) {
        throw new Exception("số quyết định đã tồn tại");
      }
    }
    XhTlQuyetDinhHdr data = new XhTlQuyetDinhHdr();
    BeanUtils.copyProperties(objReq, data);
    data.setMaDvi(currentUser.getUser().getDepartment());
    data.setTrangThai(Contains.DUTHAO);

    data.getQuyetDinhPdDtl().forEach(s -> {
      s.setQuyetDinhHdr(data);
    });

    XhTlQuyetDinhHdr created = xhTlQuyetDinhHdrRepository.save(data);

    //update so xuat cap vao bang qd cuu tro
//    if (!DataUtils.isNullObject(data.getIdQd())) {
//      Optional<XhCtVtQuyetDinhPdHdr> qdCuuTro = xhCtVtQdPdHdrRepository.findById(data.getIdQd());
//      if (qdCuuTro.isPresent()) {
//        qdCuuTro.get().setIdXc(created.getId());
//        qdCuuTro.get().setSoXc(created.getSoQd());
//        xhCtVtQdPdHdrRepository.save(qdCuuTro.get());
//      }
//    }

    if (!DataUtils.isNullOrEmpty(objReq.getCanCu())) {
      fileDinhKemService.saveListFileDinhKem(objReq.getCanCu(), created.getId(), XhTlQuyetDinhHdr.TABLE_NAME + "_CAN_CU");
    }
    if (!DataUtils.isNullObject(objReq.getFileDinhKem())) {
      fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKem(), created.getId(), XhTlQuyetDinhHdr.TABLE_NAME);
    }

    return created;
  }

  @Transactional
  public XhTlQuyetDinhHdr update(CustomUserDetails currentUser, XhTlQuyetDinhHdrReq objReq) throws Exception {
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    Optional<XhTlQuyetDinhHdr> optional = xhTlQuyetDinhHdrRepository.findById(objReq.getId());
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu cần sửa");
    }
    Optional<XhTlQuyetDinhHdr> soDx = xhTlQuyetDinhHdrRepository.findBySoQd(objReq.getSoQd());
    if (soDx.isPresent()) {
      if (!soDx.get().getId().equals(objReq.getId())) {
        throw new Exception("số quyết định đã tồn tại");
      }
    }

    XhTlQuyetDinhHdr data = optional.get();
    objReq.getQuyetDinhPdDtl().forEach(s -> {
      s.setQuyetDinhHdr(null);
    });
    BeanUtils.copyProperties(objReq, data, "id");
    data.getQuyetDinhPdDtl().forEach(s -> {
      s.setQuyetDinhHdr(data);
    });
    XhTlQuyetDinhHdr created = xhTlQuyetDinhHdrRepository.save(data);

    //update so xuat cap vao bang qd cuu tro
//    if (!DataUtils.isNullObject(data.getIdQd())) {
//      Optional<XhCtVtQuyetDinhPdHdr> qdCuuTro = xhCtVtQdPdHdrRepository.findById(data.getIdQd());
//      if (qdCuuTro.isPresent()) {
//        qdCuuTro.get().setIdXc(created.getId());
//        qdCuuTro.get().setSoXc(created.getSoQd());
//        xhCtVtQdPdHdrRepository.save(qdCuuTro.get());
//      }
//    }

    fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(XhTlQuyetDinhHdr.TABLE_NAME + "_CAN_CU"));

    fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(XhTlQuyetDinhHdr.TABLE_NAME));

    if (!DataUtils.isNullOrEmpty(objReq.getCanCu())) {
      fileDinhKemService.saveListFileDinhKem(objReq.getCanCu(), created.getId(), XhTlQuyetDinhHdr.TABLE_NAME + "_CAN_CU");
    }
    if (!DataUtils.isNullObject(objReq.getFileDinhKem())) {
      fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKem(), created.getId(), XhTlQuyetDinhHdr.TABLE_NAME);
    }
    return created;
  }


  public List<XhTlQuyetDinhHdr> detail(List<Long> ids) throws Exception {
    if (DataUtils.isNullOrEmpty(ids)) throw new Exception("Tham số không hợp lệ.");
    List<XhTlQuyetDinhHdr> optional = xhTlQuyetDinhHdrRepository.findByIdIn(ids);
    if (DataUtils.isNullOrEmpty(optional)) {
      throw new Exception("Không tìm thấy dữ liệu");
    }

    Map<String, Map<String, Object>> mapDmucDvi = getListDanhMucDviObject(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    List<XhTlQuyetDinhHdr> allById = xhTlQuyetDinhHdrRepository.findAllById(ids);
    allById.forEach(data -> {
      if (mapDmucDvi.containsKey(data.getMaDvi())) {
        data.setTenDvi(mapDmucDvi.get(data.getMaDvi()).get("tenDvi").toString());
      }
      data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));

      List<FileDinhKem> fileDinhKem = fileDinhKemService.search(data.getId(), Arrays.asList(XhTlQuyetDinhHdr.TABLE_NAME));
      data.setFileDinhKem(fileDinhKem);

      List<FileDinhKem> canCu = fileDinhKemService.search(data.getId(), Arrays.asList(XhTlQuyetDinhHdr.TABLE_NAME + "_CAN_CU"));
      data.setCanCu(canCu);

      data.getQuyetDinhPdDtl().forEach(s -> {

        s.setTenLoaiVthh(StringUtils.isEmpty(s.getLoaiVthh()) ? null : mapVthh.get(s.getLoaiVthh()));
        s.setTenCloaiVthh(StringUtils.isEmpty(s.getCloaiVthh()) ? null : mapVthh.get(s.getCloaiVthh()));
      });

    });
    return allById;
  }

  @Transactional
  public void delete(IdSearchReq idSearchReq) throws Exception {
    Optional<XhTlQuyetDinhHdr> optional = xhTlQuyetDinhHdrRepository.findById(idSearchReq.getId());
    if (!optional.isPresent()) {
      throw new Exception("Bản ghi không tồn tại");
    }
    XhTlQuyetDinhHdr data = optional.get();

    //update so xuat cap vao bang qd cuu tro
//    Optional<XhCtVtQuyetDinhPdHdr> qdCuuTro = xhCtVtQdPdHdrRepository.findById(data.getIdQdPd());
//    if (qdCuuTro.isPresent()) {
//      qdCuuTro.get().setIdXc(null);
//      qdCuuTro.get().setSoXc(null);
//      xhCtVtQdPdHdrRepository.save(qdCuuTro.get());
//    }

    fileDinhKemService.delete(data.getId(), Lists.newArrayList(XhTlQuyetDinhHdr.TABLE_NAME + "_CAN_CU"));
    fileDinhKemService.delete(data.getId(), Lists.newArrayList(XhTlQuyetDinhHdr.TABLE_NAME));
    xhTlQuyetDinhHdrRepository.delete(data);

  }

  @Transient
  public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
    List<XhTlQuyetDinhHdr> list = xhTlQuyetDinhHdrRepository.findAllByIdIn(idSearchReq.getIdList());

    if (list.isEmpty()) {
      throw new Exception("Bản ghi không tồn tại");
    }

    //update so xuat cap vao bang qd cuu tro
//    List<Long> listIdQdPd = list.stream().map(XhTlQuyetDinhHdr::getIdQdPd).collect(Collectors.toList());
//    List<XhCtVtQuyetDinhPdHdr> listObjQdPd = xhCtVtQdPdHdrRepository.findByIdIn(listIdQdPd);
//    listObjQdPd.forEach(s -> {
//      s.setIdXc(null);
//      s.setSoXc(null);
//    });
//    xhCtVtQdPdHdrRepository.saveAll(listObjQdPd);

    fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(XhTlQuyetDinhHdr.TABLE_NAME));
    fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(XhTlQuyetDinhHdr.TABLE_NAME + "_CAN_CU"));
    xhTlQuyetDinhHdrRepository.deleteAll(list);

  }


  public XhTlQuyetDinhHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {

    if (StringUtils.isEmpty(statusReq.getId())) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    Optional<XhTlQuyetDinhHdr> optional = xhTlQuyetDinhHdrRepository.findById(Long.valueOf(statusReq.getId()));
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    }

    String status = statusReq.getTrangThai() + optional.get().getTrangThai();
    switch (status) {
      case Contains.CHODUYET_LDV + Contains.DUTHAO:
      case Contains.CHODUYET_LDTC + Contains.CHODUYET_LDV:
      case Contains.CHODUYET_LDV + Contains.TUCHOI_LDV:
      case Contains.CHODUYET_LDV + Contains.TUCHOI_LDTC:
        optional.get().setNguoiGduyetId(currentUser.getUser().getId());
        optional.get().setNgayGduyet(LocalDate.now());
        break;
      case Contains.TUCHOI_LDV + Contains.CHODUYET_LDV:
      case Contains.TUCHOI_LDTC + Contains.CHODUYET_LDTC:
        optional.get().setNguoiPduyetId(currentUser.getUser().getId());
        optional.get().setNgayPduyet(LocalDate.now());
        optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
        break;
      case Contains.BAN_HANH + Contains.CHODUYET_LDTC:
        optional.get().setNguoiPduyetId(currentUser.getUser().getId());
        optional.get().setNgayPduyet(LocalDate.now());
        break;
      default:
        throw new Exception("Phê duyệt không thành công");
    }
    optional.get().setTrangThai(statusReq.getTrangThai());
    XhTlQuyetDinhHdr created = xhTlQuyetDinhHdrRepository.save(optional.get());
    return created;
  }


  public void export(CustomUserDetails currentUser, SearchXhTlQuyetDinh objReq, HttpServletResponse response) throws Exception {
    PaggingReq paggingReq = new PaggingReq();
    paggingReq.setPage(0);
    paggingReq.setLimit(Integer.MAX_VALUE);
    objReq.setPaggingReq(paggingReq);
    Page<XhTlQuyetDinhHdr> page = this.searchPage(currentUser, objReq);
    List<XhTlQuyetDinhHdr> data = page.getContent();

    String title = "Danh sách quyết định thanh lý hàng DTQG ";
    String[] rowsName = new String[]{"STT", "Số quyết định", "Trích yếu", "Ngày ký",
        "Hồ sơ đề nghị thanh lý", "Trạng thái"};
    String fileName = "danh-sach-phuong-quyet-dinh-thanh-ly-hang-dtqg.xlsx";
    List<Object[]> dataList = new ArrayList<Object[]>();
    Object[] objs = null;
    for (int i = 0; i < data.size(); i++) {
      XhTlQuyetDinhHdr qd = data.get(i);
      objs = new Object[rowsName.length];
      objs[0] = i;
      objs[1] = qd.getSoQd();
      objs[2] = qd.getTrichYeu();
      objs[3] = qd.getNgayKy();
      objs[4] = qd.getSoHoSo();
      objs[5] = qd.getTenTrangThai();
      dataList.add(objs);
    }
    ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
    ex.export();
  }
}
