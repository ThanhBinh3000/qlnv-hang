package com.tcdt.qlnvhang.service.xuathang.thanhlytieuhuy.thanhly;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly.XhTlQuyetDinhTlHdrReqository;
import com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtVtQdPdHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.SearchXhTlQuyetDinhTl;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlQuyetDinhTlHdrReq;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlQuyetDinhTlHdr;
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
public class XhTlQuyetDinhTlHdrService extends BaseServiceImpl {


  @Autowired
  private XhTlQuyetDinhTlHdrReqository xhTlQuyetDinhTlHdrReqository;

  @Autowired
  private FileDinhKemService fileDinhKemService;

  public Page<XhTlQuyetDinhTlHdr> searchPage(CustomUserDetails currentUser, SearchXhTlQuyetDinhTl req) throws Exception {
    String dvql = currentUser.getDvql();
    if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
      req.setDvql(dvql.substring(0, 4));
    } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_TONG_CUC)) {
      req.setDvql(dvql);
    }
    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
    Page<XhTlQuyetDinhTlHdr> search = xhTlQuyetDinhTlHdrReqository.search(req, pageable);
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
  public XhTlQuyetDinhTlHdr save(CustomUserDetails currentUser, XhTlQuyetDinhTlHdrReq objReq) throws Exception {
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    if (!DataUtils.isNullObject(objReq.getSoQd())) {
      Optional<XhTlQuyetDinhTlHdr> optional = xhTlQuyetDinhTlHdrReqository.findBySoQd(objReq.getSoQd());
      if (optional.isPresent()) {
        throw new Exception("số quyết định đã tồn tại");
      }
    }
    XhTlQuyetDinhTlHdr data = new XhTlQuyetDinhTlHdr();
    BeanUtils.copyProperties(objReq, data);
    data.setMaDvi(currentUser.getUser().getDepartment());
    data.setTrangThai(Contains.DUTHAO);

    data.getQuyetDinhPdDtl().forEach(s -> {
      s.setQuyetDinhTlHdr(data);
    });

    XhTlQuyetDinhTlHdr created = xhTlQuyetDinhTlHdrReqository.save(data);

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
      fileDinhKemService.saveListFileDinhKem(objReq.getCanCu(), created.getId(), XhTlQuyetDinhTlHdr.TABLE_NAME + "_CAN_CU");
    }
    if (!DataUtils.isNullObject(objReq.getFileDinhKem())) {
      fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKem(), created.getId(), XhTlQuyetDinhTlHdr.TABLE_NAME);
    }

    return created;
  }

  @Transactional
  public XhTlQuyetDinhTlHdr update(CustomUserDetails currentUser, XhTlQuyetDinhTlHdrReq objReq) throws Exception {
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    Optional<XhTlQuyetDinhTlHdr> optional = xhTlQuyetDinhTlHdrReqository.findById(objReq.getId());
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu cần sửa");
    }
    Optional<XhTlQuyetDinhTlHdr> soDx = xhTlQuyetDinhTlHdrReqository.findBySoQd(objReq.getSoQd());
    if (soDx.isPresent()) {
      if (!soDx.get().getId().equals(objReq.getId())) {
        throw new Exception("số quyết định đã tồn tại");
      }
    }

    XhTlQuyetDinhTlHdr data = optional.get();
    objReq.getQuyetDinhPdDtl().forEach(s -> {
      s.setQuyetDinhTlHdr(null);
    });
    BeanUtils.copyProperties(objReq, data, "id");
    data.getQuyetDinhPdDtl().forEach(s -> {
      s.setQuyetDinhTlHdr(data);
    });
    XhTlQuyetDinhTlHdr created = xhTlQuyetDinhTlHdrReqository.save(data);

    //update so xuat cap vao bang qd cuu tro
//    if (!DataUtils.isNullObject(data.getIdQd())) {
//      Optional<XhCtVtQuyetDinhPdHdr> qdCuuTro = xhCtVtQdPdHdrRepository.findById(data.getIdQd());
//      if (qdCuuTro.isPresent()) {
//        qdCuuTro.get().setIdXc(created.getId());
//        qdCuuTro.get().setSoXc(created.getSoQd());
//        xhCtVtQdPdHdrRepository.save(qdCuuTro.get());
//      }
//    }

    fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(XhTlQuyetDinhTlHdr.TABLE_NAME + "_CAN_CU"));

    fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(XhTlQuyetDinhTlHdr.TABLE_NAME));

    if (!DataUtils.isNullOrEmpty(objReq.getCanCu())) {
      fileDinhKemService.saveListFileDinhKem(objReq.getCanCu(), created.getId(), XhTlQuyetDinhTlHdr.TABLE_NAME + "_CAN_CU");
    }
    if (!DataUtils.isNullObject(objReq.getFileDinhKem())) {
      fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKem(), created.getId(), XhTlQuyetDinhTlHdr.TABLE_NAME);
    }
    return created;
  }


  public List<XhTlQuyetDinhTlHdr> detail(List<Long> ids) throws Exception {
    if (DataUtils.isNullOrEmpty(ids)) throw new Exception("Tham số không hợp lệ.");
    List<XhTlQuyetDinhTlHdr> optional = xhTlQuyetDinhTlHdrReqository.findByIdIn(ids);
    if (DataUtils.isNullOrEmpty(optional)) {
      throw new Exception("Không tìm thấy dữ liệu");
    }

    Map<String, Map<String, Object>> mapDmucDvi = getListDanhMucDviObject(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    List<XhTlQuyetDinhTlHdr> allById = xhTlQuyetDinhTlHdrReqository.findAllById(ids);
    allById.forEach(data -> {
      if (mapDmucDvi.containsKey(data.getMaDvi())) {
        data.setTenDvi(mapDmucDvi.get(data.getMaDvi()).get("tenDvi").toString());
      }
      data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));

      List<FileDinhKem> fileDinhKem = fileDinhKemService.search(data.getId(), Arrays.asList(XhTlQuyetDinhTlHdr.TABLE_NAME));
      data.setFileDinhKem(fileDinhKem);

      List<FileDinhKem> canCu = fileDinhKemService.search(data.getId(), Arrays.asList(XhTlQuyetDinhTlHdr.TABLE_NAME + "_CAN_CU"));
      data.setCanCu(canCu);

      data.getQuyetDinhPdDtl().forEach(s -> {
        if (mapDmucDvi.containsKey(s.getMaDvi())) {
          s.setTenDvi(mapDmucDvi.get(s.getMaDvi()).get("tenDvi").toString());
        }
        s.setTenLoaiVthh(StringUtils.isEmpty(s.getLoaiVthh()) ? null : mapVthh.get(s.getLoaiVthh()));
        s.setTenCloaiVthh(StringUtils.isEmpty(s.getCloaiVthh()) ? null : mapVthh.get(s.getCloaiVthh()));
      });

    });
    return allById;
  }

  @Transactional
  public void delete(IdSearchReq idSearchReq) throws Exception {
    Optional<XhTlQuyetDinhTlHdr> optional = xhTlQuyetDinhTlHdrReqository.findById(idSearchReq.getId());
    if (!optional.isPresent()) {
      throw new Exception("Bản ghi không tồn tại");
    }
    XhTlQuyetDinhTlHdr data = optional.get();

    //update so xuat cap vao bang qd cuu tro
//    Optional<XhCtVtQuyetDinhPdHdr> qdCuuTro = xhCtVtQdPdHdrRepository.findById(data.getIdQdPd());
//    if (qdCuuTro.isPresent()) {
//      qdCuuTro.get().setIdXc(null);
//      qdCuuTro.get().setSoXc(null);
//      xhCtVtQdPdHdrRepository.save(qdCuuTro.get());
//    }

    fileDinhKemService.delete(data.getId(), Lists.newArrayList(XhTlQuyetDinhTlHdr.TABLE_NAME + "_CAN_CU"));
    fileDinhKemService.delete(data.getId(), Lists.newArrayList(XhTlQuyetDinhTlHdr.TABLE_NAME));
    xhTlQuyetDinhTlHdrReqository.delete(data);

  }

  @Transient
  public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
    List<XhTlQuyetDinhTlHdr> list = xhTlQuyetDinhTlHdrReqository.findAllByIdIn(idSearchReq.getIdList());

    if (list.isEmpty()) {
      throw new Exception("Bản ghi không tồn tại");
    }

    //update so xuat cap vao bang qd cuu tro
//    List<Long> listIdQdPd = list.stream().map(XhTlQuyetDinhTlHdr::getIdQdPd).collect(Collectors.toList());
//    List<XhCtVtQuyetDinhPdHdr> listObjQdPd = xhCtVtQdPdHdrRepository.findByIdIn(listIdQdPd);
//    listObjQdPd.forEach(s -> {
//      s.setIdXc(null);
//      s.setSoXc(null);
//    });
//    xhCtVtQdPdHdrRepository.saveAll(listObjQdPd);

    fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(XhTlQuyetDinhTlHdr.TABLE_NAME));
    fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(XhTlQuyetDinhTlHdr.TABLE_NAME + "_CAN_CU"));
    xhTlQuyetDinhTlHdrReqository.deleteAll(list);

  }


  public XhTlQuyetDinhTlHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {

    if (StringUtils.isEmpty(statusReq.getId())) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    Optional<XhTlQuyetDinhTlHdr> optional = xhTlQuyetDinhTlHdrReqository.findById(Long.valueOf(statusReq.getId()));
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
    XhTlQuyetDinhTlHdr created = xhTlQuyetDinhTlHdrReqository.save(optional.get());
    return created;
  }


  public void export(CustomUserDetails currentUser, SearchXhTlQuyetDinhTl objReq, HttpServletResponse response) throws Exception {
    PaggingReq paggingReq = new PaggingReq();
    paggingReq.setPage(0);
    paggingReq.setLimit(Integer.MAX_VALUE);
    objReq.setPaggingReq(paggingReq);
    Page<XhTlQuyetDinhTlHdr> page = this.searchPage(currentUser, objReq);
    List<XhTlQuyetDinhTlHdr> data = page.getContent();

    String title = "Danh sách quyết định thanh lý hàng DTQG ";
    String[] rowsName = new String[]{"STT", "Số quyết định", "Trích yếu", "Ngày ký",
        "Hồ sơ đề nghị thanh lý", "Trạng thái"};
    String fileName = "danh-sach-phuong-quyet-dinh-thanh-ly-hang-dtqg.xlsx";
    List<Object[]> dataList = new ArrayList<Object[]>();
    Object[] objs = null;
    for (int i = 0; i < data.size(); i++) {
      XhTlQuyetDinhTlHdr qd = data.get(i);
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
