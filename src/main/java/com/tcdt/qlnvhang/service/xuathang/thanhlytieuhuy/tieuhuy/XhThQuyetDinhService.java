package com.tcdt.qlnvhang.service.xuathang.thanhlytieuhuy.tieuhuy;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.tieuhuy.XhThHoSoRepository;
import com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.tieuhuy.XhThQuyetDinhRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.tieuhuy.SearchXhThQuyetDinh;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.tieuhuy.XhThQuyetDinhHdrReq;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.tieuhuy.XhThHoSoHdr;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.tieuhuy.XhThQuyetDinhHdr;
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
public class XhThQuyetDinhService extends BaseServiceImpl {


  @Autowired
  private XhThQuyetDinhRepository xhThQuyetDinhRepository;

  @Autowired
  private XhThHoSoRepository xhThHoSoRepository;

  @Autowired
  private FileDinhKemService fileDinhKemService;

  public Page<XhThQuyetDinhHdr> searchPage(CustomUserDetails currentUser, SearchXhThQuyetDinh req) throws Exception {
    String dvql = currentUser.getDvql();
    if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
      req.setDvql(dvql.substring(0, 4));
    } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_TONG_CUC)) {
      req.setDvql(dvql);
    }
    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
    Page<XhThQuyetDinhHdr> search = xhThQuyetDinhRepository.search(req, pageable);
    Map<String, Map<String, Object>> mapDmucDvi = getListDanhMucDviObject(null, null, "01");

    Map<String, String> mapVthh = getListDanhMucHangHoa();
    search.getContent().forEach(s -> {
      if (mapDmucDvi.containsKey((s.getMaDvi()))) {
        Map<String, Object> objDonVi = mapDmucDvi.get(s.getMaDvi());
        s.setTenDvi(objDonVi.get("tenDvi").toString());
      }
      s.setTenTrangThai(TrangThaiAllEnum.getLabelById(s.getTrangThai()));
    });
    return search;
  }

  @Transactional
  public XhThQuyetDinhHdr save(CustomUserDetails currentUser, XhThQuyetDinhHdrReq objReq) throws Exception {
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    if (!DataUtils.isNullObject(objReq.getSoQd())) {
      Optional<XhThQuyetDinhHdr> optional = xhThQuyetDinhRepository.findBySoQd(objReq.getSoQd());
      if (optional.isPresent()) {
        throw new Exception("số quyết định đã tồn tại");
      }
    }
    XhThQuyetDinhHdr data = new XhThQuyetDinhHdr();
    BeanUtils.copyProperties(objReq, data);
    data.setMaDvi(currentUser.getUser().getDepartment());
    data.setTrangThai(Contains.DUTHAO);

    data.getQuyetDinhDtl().forEach(s -> {
      s.setQuyetDinhHdr(data);
    });

    XhThQuyetDinhHdr created = xhThQuyetDinhRepository.save(data);

    if (!DataUtils.isNullObject(data.getIdHoSo())) {
      Optional<XhThHoSoHdr> hoSo = xhThHoSoRepository.findById(data.getIdHoSo());
      if (hoSo.isPresent()) {
        hoSo.get().setIdQd(created.getId());
        hoSo.get().setSoQd(created.getSoQd());
        xhThHoSoRepository.save(hoSo.get());
      }
    }

    if (!DataUtils.isNullOrEmpty(objReq.getCanCu())) {
      fileDinhKemService.saveListFileDinhKem(objReq.getCanCu(), created.getId(), XhThQuyetDinhHdr.TABLE_NAME + "_CAN_CU");
    }
    if (!DataUtils.isNullObject(objReq.getFileDinhKem())) {
      fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKem(), created.getId(), XhThQuyetDinhHdr.TABLE_NAME);
    }

    return created;
  }

  @Transactional
  public XhThQuyetDinhHdr update(CustomUserDetails currentUser, XhThQuyetDinhHdrReq objReq) throws Exception {
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    Optional<XhThQuyetDinhHdr> optional = xhThQuyetDinhRepository.findById(objReq.getId());
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu cần sửa");
    }
    Optional<XhThQuyetDinhHdr> soDx = xhThQuyetDinhRepository.findBySoQd(objReq.getSoQd());
    if (soDx.isPresent()) {
      if (!soDx.get().getId().equals(objReq.getId())) {
        throw new Exception("số quyết định đã tồn tại");
      }
    }

    XhThQuyetDinhHdr data = optional.get();
    objReq.getQuyetDinhDtl().forEach(s -> {
      s.setQuyetDinhHdr(null);
    });
    BeanUtils.copyProperties(objReq, data, "id","maDvi");
    data.getQuyetDinhDtl().forEach(s -> {
      s.setQuyetDinhHdr(data);
    });
    XhThQuyetDinhHdr created = xhThQuyetDinhRepository.save(data);

    if (!DataUtils.isNullObject(data.getIdHoSo())) {
      Optional<XhThHoSoHdr> hoSo = xhThHoSoRepository.findById(data.getIdHoSo());
      if (hoSo.isPresent()) {
        hoSo.get().setIdQd(created.getId());
        hoSo.get().setSoQd(created.getSoQd());
        xhThHoSoRepository.save(hoSo.get());
      }
    }

    fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(XhThQuyetDinhHdr.TABLE_NAME + "_CAN_CU"));

    fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(XhThQuyetDinhHdr.TABLE_NAME));

    if (!DataUtils.isNullOrEmpty(objReq.getCanCu())) {
      fileDinhKemService.saveListFileDinhKem(objReq.getCanCu(), created.getId(), XhThQuyetDinhHdr.TABLE_NAME + "_CAN_CU");
    }
    if (!DataUtils.isNullObject(objReq.getFileDinhKem())) {
      fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKem(), created.getId(), XhThQuyetDinhHdr.TABLE_NAME);
    }
    return created;
  }


  public List<XhThQuyetDinhHdr> detail(List<Long> ids) throws Exception {
    if (DataUtils.isNullOrEmpty(ids)) throw new Exception("Tham số không hợp lệ.");
    List<XhThQuyetDinhHdr> optional = xhThQuyetDinhRepository.findByIdIn(ids);
    if (DataUtils.isNullOrEmpty(optional)) {
      throw new Exception("Không tìm thấy dữ liệu");
    }

    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    List<XhThQuyetDinhHdr> allById = xhThQuyetDinhRepository.findAllById(ids);
    allById.forEach(data -> {
      if (mapDmucDvi.get((data.getMaDvi())) != null) {
        data.setTenDvi(mapDmucDvi.get(data.getMaDvi()));
      }
      data.setTenTrangThai(TrangThaiAllEnum.getLabelById(data.getTrangThai()));

      List<FileDinhKem> fileDinhKem = fileDinhKemService.search(data.getId(), Arrays.asList(XhThQuyetDinhHdr.TABLE_NAME));
      data.setFileDinhKem(fileDinhKem);

      List<FileDinhKem> canCu = fileDinhKemService.search(data.getId(), Arrays.asList(XhThQuyetDinhHdr.TABLE_NAME + "_CAN_CU"));
      data.setCanCu(canCu);

      data.getQuyetDinhDtl().forEach(s -> {
        s.setMapDmucDvi(mapDmucDvi);
        s.setTenLoaiVthh(StringUtils.isEmpty(s.getLoaiVthh()) ? null : mapVthh.get(s.getLoaiVthh()));
        s.setTenCloaiVthh(StringUtils.isEmpty(s.getCloaiVthh()) ? null : mapVthh.get(s.getCloaiVthh()));
      });

    });
    return allById;
  }

  @Transactional
  public void delete(IdSearchReq idSearchReq) throws Exception {
    Optional<XhThQuyetDinhHdr> optional = xhThQuyetDinhRepository.findById(idSearchReq.getId());
    if (!optional.isPresent()) {
      throw new Exception("Bản ghi không tồn tại");
    }
    XhThQuyetDinhHdr data = optional.get();

    if (!DataUtils.isNullObject(data.getIdHoSo())) {
      Optional<XhThHoSoHdr> hoSo = xhThHoSoRepository.findById(data.getIdHoSo());
      if (hoSo.isPresent()) {
        hoSo.get().setIdQd(null);
        hoSo.get().setSoQd(null);
        xhThHoSoRepository.save(hoSo.get());
      }
    }

    fileDinhKemService.delete(data.getId(), Lists.newArrayList(XhThQuyetDinhHdr.TABLE_NAME + "_CAN_CU"));
    fileDinhKemService.delete(data.getId(), Lists.newArrayList(XhThQuyetDinhHdr.TABLE_NAME));
    xhThQuyetDinhRepository.delete(data);

  }

  @Transactional
  public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
    List<XhThQuyetDinhHdr> list = xhThQuyetDinhRepository.findAllByIdIn(idSearchReq.getIdList());

    if (list.isEmpty()) {
      throw new Exception("Bản ghi không tồn tại");
    }

    List<Long> listHoSo = list.stream().map(XhThQuyetDinhHdr::getIdHoSo).collect(Collectors.toList());
    List<XhThHoSoHdr> listObjQdPd = xhThHoSoRepository.findByIdIn(listHoSo);
    listObjQdPd.forEach(s -> {
      s.setIdQd(null);
      s.setSoQd(null);
    });
    xhThHoSoRepository.saveAll(listObjQdPd);

    fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(XhThQuyetDinhHdr.TABLE_NAME));
    fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(XhThQuyetDinhHdr.TABLE_NAME + "_CAN_CU"));
    xhThQuyetDinhRepository.deleteAll(list);

  }


  public XhThQuyetDinhHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {

    if (StringUtils.isEmpty(statusReq.getId())) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    Optional<XhThQuyetDinhHdr> optional = xhThQuyetDinhRepository.findById(Long.valueOf(statusReq.getId()));
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
    XhThQuyetDinhHdr created = xhThQuyetDinhRepository.save(optional.get());
    return created;
  }


  public void export(CustomUserDetails currentUser, SearchXhThQuyetDinh objReq, HttpServletResponse response) throws Exception {
    PaggingReq paggingReq = new PaggingReq();
    paggingReq.setPage(0);
    paggingReq.setLimit(Integer.MAX_VALUE);
    objReq.setPaggingReq(paggingReq);
    Page<XhThQuyetDinhHdr> page = this.searchPage(currentUser, objReq);
    List<XhThQuyetDinhHdr> data = page.getContent();

    String title = "Danh sách quyết định tiêu hủy hàng DTQG ";
    String[] rowsName = new String[]{"STT", "Số quyết định", "Trích yếu", "Ngày ký",
        "Hồ sơ đề nghị thanh lý", "Trạng thái"};
    String fileName = "danh-sach-quyet-dinh-tieu-huy-hang-dtqg.xlsx";
    List<Object[]> dataList = new ArrayList<Object[]>();
    Object[] objs = null;
    for (int i = 0; i < data.size(); i++) {
      XhThQuyetDinhHdr qd = data.get(i);
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
