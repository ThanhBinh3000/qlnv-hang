package com.tcdt.qlnvhang.service.xuathang.xuatcuutrovientroxuatcap.xuatcap;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcap.XhXcapQuyetDinhPdHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtQdPdHdrRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcap.SearchXhCtvtQdXuatCap;
import com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcap.XhXcapQuyetDinhPdHdrReq;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcap.XhXcapQuyetDinhPdHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtQuyetDinhPdHdr;
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

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class XhXcapQuyetDinhPdService extends BaseServiceImpl {


  @Autowired
  private XhXcapQuyetDinhPdHdrRepository xhXcapQuyetDinhPdHdrRepository;
  @Autowired
  private XhCtvtQdPdHdrRepository xhCtVtQdPdHdrRepository;

  @Autowired
  private FileDinhKemService fileDinhKemService;

  public Page<XhXcapQuyetDinhPdHdr> searchPage(CustomUserDetails currentUser, SearchXhCtvtQdXuatCap req) throws Exception {
    String dvql = currentUser.getDvql();
    if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
      req.setDvql(dvql.substring(0, 4));
    } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_TONG_CUC)) {
      req.setDvql(dvql);
    }
    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
    Page<XhXcapQuyetDinhPdHdr> search = xhXcapQuyetDinhPdHdrRepository.search(req, pageable);
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
      s.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(s.getTrangThai()));
    });
    return search;
  }

  @Transactional
  public XhXcapQuyetDinhPdHdr save(CustomUserDetails currentUser, XhXcapQuyetDinhPdHdrReq objReq) throws Exception {
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    if (!DataUtils.isNullObject(objReq.getSoQd())) {
      Optional<XhXcapQuyetDinhPdHdr> optional = xhXcapQuyetDinhPdHdrRepository.findBySoQd(objReq.getSoQd());
      if (optional.isPresent()) {
        throw new Exception("số quyết định đã tồn tại");
      }
    }
    XhXcapQuyetDinhPdHdr data = new XhXcapQuyetDinhPdHdr();
    BeanUtils.copyProperties(objReq, data);
    data.setMaDvi(currentUser.getUser().getDepartment());
    data.setTrangThai(Contains.DUTHAO);

    data.getQuyetDinhPdDtl().forEach(s -> {
      s.setQuyetDinhPdHdr(data);
      s.getQuyetDinhPdDx().forEach(s1 -> s1.setQuyetDinhPdDtl(s));
    });

    XhXcapQuyetDinhPdHdr created = xhXcapQuyetDinhPdHdrRepository.save(data);

    //update so xuat cap vao bang qd cuu tro
    if (!DataUtils.isNullObject(data.getIdQdPd())) {
      Optional<XhCtvtQuyetDinhPdHdr> qdCuuTro = xhCtVtQdPdHdrRepository.findById(data.getIdQdPd());
      if (qdCuuTro.isPresent()) {
        qdCuuTro.get().setIdXc(created.getId());
        qdCuuTro.get().setSoXc(created.getSoQd());
        xhCtVtQdPdHdrRepository.save(qdCuuTro.get());
      }
    }

    if (!DataUtils.isNullOrEmpty(objReq.getCanCu())) {
      fileDinhKemService.saveListFileDinhKem(objReq.getCanCu(), created.getId(), XhXcapQuyetDinhPdHdr.TABLE_NAME + "_CAN_CU");
    }
    if (!DataUtils.isNullObject(objReq.getFileDinhKem())) {
      fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKem(), created.getId(), XhXcapQuyetDinhPdHdr.TABLE_NAME);
    }

    return created;
  }

  @Transactional
  public XhXcapQuyetDinhPdHdr update(CustomUserDetails currentUser, XhXcapQuyetDinhPdHdrReq objReq) throws Exception {
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    Optional<XhXcapQuyetDinhPdHdr> optional = xhXcapQuyetDinhPdHdrRepository.findById(objReq.getId());
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu cần sửa");
    }
    Optional<XhXcapQuyetDinhPdHdr> soDx = xhXcapQuyetDinhPdHdrRepository.findBySoQd(objReq.getSoQd());
    if (soDx.isPresent()) {
      if (!soDx.get().getId().equals(objReq.getId())) {
        throw new Exception("số quyết định đã tồn tại");
      }
    }

    XhXcapQuyetDinhPdHdr data = optional.get();
    objReq.getQuyetDinhPdDtl().forEach(s -> {
      s.setQuyetDinhPdHdr(null);
      s.getQuyetDinhPdDx().forEach(s1 -> s1.setQuyetDinhPdDtl(null));
    });
    BeanUtils.copyProperties(objReq, data, "id");
    data.getQuyetDinhPdDtl().forEach(s -> {
      s.setQuyetDinhPdHdr(data);
      s.getQuyetDinhPdDx().forEach(s1 -> s1.setQuyetDinhPdDtl(s));
    });
    XhXcapQuyetDinhPdHdr created = xhXcapQuyetDinhPdHdrRepository.save(data);

    //update so xuat cap vao bang qd cuu tro
    if (!DataUtils.isNullObject(data.getIdQdPd())) {
      Optional<XhCtvtQuyetDinhPdHdr> qdCuuTro = xhCtVtQdPdHdrRepository.findById(data.getIdQdPd());
      if (qdCuuTro.isPresent()) {
        qdCuuTro.get().setIdXc(created.getId());
        qdCuuTro.get().setSoXc(created.getSoQd());
        xhCtVtQdPdHdrRepository.save(qdCuuTro.get());
      }
    }

    fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(XhXcapQuyetDinhPdHdr.TABLE_NAME + "_CAN_CU"));

    fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(XhXcapQuyetDinhPdHdr.TABLE_NAME));

    if (!DataUtils.isNullOrEmpty(objReq.getCanCu())) {
      fileDinhKemService.saveListFileDinhKem(objReq.getCanCu(), created.getId(), XhXcapQuyetDinhPdHdr.TABLE_NAME + "_CAN_CU");
    }
    if (!DataUtils.isNullObject(objReq.getFileDinhKem())) {
      fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKem(), created.getId(), XhXcapQuyetDinhPdHdr.TABLE_NAME);
    }
    return created;
  }


  public List<XhXcapQuyetDinhPdHdr> detail(List<Long> ids) throws Exception {
    if (DataUtils.isNullOrEmpty(ids)) throw new Exception("Tham số không hợp lệ.");
    List<XhXcapQuyetDinhPdHdr> optional = xhXcapQuyetDinhPdHdrRepository.findByIdIn(ids);
    if (DataUtils.isNullOrEmpty(optional)) {
      throw new Exception("Không tìm thấy dữ liệu");
    }

    Map<String, Map<String, Object>> mapDmucDvi = getListDanhMucDviObject(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    List<XhXcapQuyetDinhPdHdr> allById = xhXcapQuyetDinhPdHdrRepository.findAllById(ids);
    allById.forEach(data -> {
      if (mapDmucDvi.containsKey(data.getMaDvi())) {
        data.setTenDvi(mapDmucDvi.get(data.getMaDvi()).get("tenDvi").toString());
      }
      data.setTenLoaiVthh(mapVthh.get(data.getLoaiVthh()));
      data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));

      List<FileDinhKem> fileDinhKem = fileDinhKemService.search(data.getId(), Arrays.asList(XhXcapQuyetDinhPdHdr.TABLE_NAME));
      data.setFileDinhKem(fileDinhKem);

      List<FileDinhKem> canCu = fileDinhKemService.search(data.getId(), Arrays.asList(XhXcapQuyetDinhPdHdr.TABLE_NAME + "_CAN_CU"));
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

    });
    return allById;
  }

  @Transactional
  public void delete(IdSearchReq idSearchReq) throws Exception {
    Optional<XhXcapQuyetDinhPdHdr> optional = xhXcapQuyetDinhPdHdrRepository.findById(idSearchReq.getId());
    if (!optional.isPresent()) {
      throw new Exception("Bản ghi không tồn tại");
    }
    XhXcapQuyetDinhPdHdr data = optional.get();

    //update so xuat cap vao bang qd cuu tro
    Optional<XhCtvtQuyetDinhPdHdr> qdCuuTro = xhCtVtQdPdHdrRepository.findById(data.getIdQdPd());
    if (qdCuuTro.isPresent()) {
      qdCuuTro.get().setIdXc(null);
      qdCuuTro.get().setSoXc(null);
      xhCtVtQdPdHdrRepository.save(qdCuuTro.get());
    }

    fileDinhKemService.delete(data.getId(), Lists.newArrayList(XhXcapQuyetDinhPdHdr.TABLE_NAME + "_CAN_CU"));
    fileDinhKemService.delete(data.getId(), Lists.newArrayList(XhXcapQuyetDinhPdHdr.TABLE_NAME));
    xhXcapQuyetDinhPdHdrRepository.delete(data);

  }

  @Transactional
  public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
    List<XhXcapQuyetDinhPdHdr> list = xhXcapQuyetDinhPdHdrRepository.findAllByIdIn(idSearchReq.getIdList());

    if (list.isEmpty()) {
      throw new Exception("Bản ghi không tồn tại");
    }

    //update so xuat cap vao bang qd cuu tro
    List<Long> listIdQdPd = list.stream().map(XhXcapQuyetDinhPdHdr::getIdQdPd).collect(Collectors.toList());
    List<XhCtvtQuyetDinhPdHdr> listObjQdPd = xhCtVtQdPdHdrRepository.findByIdIn(listIdQdPd);
    listObjQdPd.forEach(s -> {
      s.setIdXc(null);
      s.setSoXc(null);
    });
    xhCtVtQdPdHdrRepository.saveAll(listObjQdPd);

    fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(XhXcapQuyetDinhPdHdr.TABLE_NAME));
    fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(XhXcapQuyetDinhPdHdr.TABLE_NAME + "_CAN_CU"));
    xhXcapQuyetDinhPdHdrRepository.deleteAll(list);

  }


  public XhXcapQuyetDinhPdHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {

    if (StringUtils.isEmpty(statusReq.getId())) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    Optional<XhXcapQuyetDinhPdHdr> optional = xhXcapQuyetDinhPdHdrRepository.findById(Long.valueOf(statusReq.getId()));
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
    XhXcapQuyetDinhPdHdr created = xhXcapQuyetDinhPdHdrRepository.save(optional.get());
    return created;
  }


  public void export(CustomUserDetails currentUser, SearchXhCtvtQdXuatCap objReq, HttpServletResponse response) throws Exception {
    PaggingReq paggingReq = new PaggingReq();
    paggingReq.setPage(0);
    paggingReq.setLimit(Integer.MAX_VALUE);
    objReq.setPaggingReq(paggingReq);
    Page<XhXcapQuyetDinhPdHdr> page = this.searchPage(currentUser, objReq);
    List<XhXcapQuyetDinhPdHdr> data = page.getContent();

    String title = "Danh sách quyết định xuất cấp thóc gia công cứu trợ, viện trợ ";
    String[] rowsName = new String[]{"STT", "Số QĐ xuất cấp", "Ngày hiệu lực QĐ xuất cấp", "Số QĐ chuyển xuất cấp",
        "Ngày hiệu lực QĐ chuyển xuất cấp", "SL gạo chuyển sang xuất cấp (kg)", "SL thóc xuất cấp", "Trích yếu", "Trạng thái QĐ xuất cấp",};
    String fileName = "danh-sach-phuong-quyet-dinh-xuat-thoc-gia-cong-cuu-tro-vien-tro.xlsx";
    List<Object[]> dataList = new ArrayList<Object[]>();
    Object[] objs = null;
    for (int i = 0; i < data.size(); i++) {
      XhXcapQuyetDinhPdHdr qd = data.get(i);
      objs = new Object[rowsName.length];
      objs[0] = i;
      objs[1] = qd.getSoQd();
      objs[2] = qd.getNgayHluc();
      objs[3] = qd.getSoQdPd();
      objs[4] = qd.getNgayHlucQdPd();
      objs[5] = qd.getTongSoLuongGao();
      objs[6] = qd.getTongSoLuongThoc();
      objs[7] = qd.getTrichYeu();
      objs[8] = qd.getTenTrangThai();

      dataList.add(objs);
    }
    ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
    ex.export();
  }
}
