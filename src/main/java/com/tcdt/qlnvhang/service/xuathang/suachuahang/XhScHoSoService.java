package com.tcdt.qlnvhang.service.xuathang.suachuahang;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.suachuahang.XhScHoSoRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlHoSoRequest;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.XhScHoSoHdr;
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
public class XhScHoSoService extends BaseServiceImpl {


  @Autowired
  private XhScHoSoRepository xhScHoSoRepository;

  @Autowired
  private FileDinhKemService fileDinhKemService;

  public Page<XhScHoSoHdr> searchPage(CustomUserDetails currentUser, XhTlHoSoRequest req) throws Exception {
    String dvql = currentUser.getDvql();
    if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
      req.setDvql(dvql.substring(0, 4));
    } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_TONG_CUC)) {
      req.setDvql(dvql);
    }
    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
    Page<XhScHoSoHdr> search = xhScHoSoRepository.searchPage(req, pageable);
    Map<String, Map<String, Object>> mapDmucDvi = getListDanhMucDviObject(null, null, "01");
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
  public XhScHoSoHdr save(CustomUserDetails currentUser, XhTlHoSoRequest objReq) throws Exception {
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    if (!DataUtils.isNullObject(objReq.getSoQd())) {
      Optional<XhScHoSoHdr> optional = xhScHoSoRepository.findBySoHoSo(objReq.getSoQd());
      if (optional.isPresent()) {
        throw new Exception("số hồ sơ đã tồn tại");
      }
    }
    XhScHoSoHdr data = new XhScHoSoHdr();
    BeanUtils.copyProperties(objReq, data);
    data.setMaDvi(currentUser.getUser().getDepartment());
    data.setTrangThai(Contains.DUTHAO);

    data.getHoSoDtl().forEach(s -> {
      s.setHoSoHdr(data);
    });

    XhScHoSoHdr created = xhScHoSoRepository.save(data);

    if (!DataUtils.isNullOrEmpty(objReq.getCanCu())) {
      fileDinhKemService.saveListFileDinhKem(objReq.getCanCu(), created.getId(), XhScHoSoHdr.TABLE_NAME + "_CAN_CU");
    }
    if (!DataUtils.isNullObject(objReq.getFileDinhKem())) {
      fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKem(), created.getId(), XhScHoSoHdr.TABLE_NAME);
    }

    return created;
  }

  @Transactional
  public XhScHoSoHdr update(CustomUserDetails currentUser, XhTlHoSoRequest objReq) throws Exception {
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    Optional<XhScHoSoHdr> optional = xhScHoSoRepository.findById(objReq.getId());
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu cần sửa");
    }
    Optional<XhScHoSoHdr> soDx = xhScHoSoRepository.findBySoHoSo(objReq.getSoHoSo());
    if (soDx.isPresent()) {
      if (!soDx.get().getId().equals(objReq.getId())) {
        throw new Exception("số quyết định đã tồn tại");
      }
    }

    XhScHoSoHdr data = optional.get();
    objReq.getHoSoDtl().forEach(s -> {
      s.setHoSoHdr(null);
    });
    BeanUtils.copyProperties(objReq, data, "id");
    data.getHoSoDtl().forEach(s -> {
      s.setHoSoHdr(data);
    });
    XhScHoSoHdr created = xhScHoSoRepository.save(data);

    fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(XhScHoSoHdr.TABLE_NAME + "_CAN_CU"));

    fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(XhScHoSoHdr.TABLE_NAME));

    if (!DataUtils.isNullOrEmpty(objReq.getCanCu())) {
      fileDinhKemService.saveListFileDinhKem(objReq.getCanCu(), created.getId(), XhScHoSoHdr.TABLE_NAME + "_CAN_CU");
    }
    if (!DataUtils.isNullObject(objReq.getFileDinhKem())) {
      fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKem(), created.getId(), XhScHoSoHdr.TABLE_NAME);
    }
    return created;
  }


  public List<XhScHoSoHdr> detail(List<Long> ids) throws Exception {
    if (DataUtils.isNullOrEmpty(ids)) throw new Exception("Tham số không hợp lệ.");
    List<XhScHoSoHdr> optional = xhScHoSoRepository.findByIdIn(ids);
    if (DataUtils.isNullOrEmpty(optional)) {
      throw new Exception("Không tìm thấy dữ liệu");
    }

    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    List<XhScHoSoHdr> allById = xhScHoSoRepository.findAllById(ids);
    allById.forEach(data -> {
      data.setTenTrangThai(TrangThaiAllEnum.getLabelById(data.getTrangThai()));
      List<FileDinhKem> fileDinhKem = fileDinhKemService.search(data.getId(), Arrays.asList(XhScHoSoHdr.TABLE_NAME));
      data.setFileDinhKem(fileDinhKem);

      List<FileDinhKem> canCu = fileDinhKemService.search(data.getId(), Arrays.asList(XhScHoSoHdr.TABLE_NAME + "_CAN_CU"));
      data.setCanCu(canCu);

      data.getHoSoDtl().forEach(s -> {
        s.setMapDmucDvi(mapDmucDvi);
        s.setMapVthh(mapVthh);
      });

    });
    return allById;
  }

  @Transactional
  public void delete(IdSearchReq idSearchReq) throws Exception {
    Optional<XhScHoSoHdr> optional = xhScHoSoRepository.findById(idSearchReq.getId());
    if (!optional.isPresent()) {
      throw new Exception("Bản ghi không tồn tại");
    }
    XhScHoSoHdr data = optional.get();

    fileDinhKemService.delete(data.getId(), Lists.newArrayList(XhScHoSoHdr.TABLE_NAME + "_CAN_CU"));
    fileDinhKemService.delete(data.getId(), Lists.newArrayList(XhScHoSoHdr.TABLE_NAME));
    xhScHoSoRepository.delete(data);

  }

  @Transient
  public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
    List<XhScHoSoHdr> list = xhScHoSoRepository.findAllByIdIn(idSearchReq.getIdList());

    if (list.isEmpty()) {
      throw new Exception("Bản ghi không tồn tại");
    }

    fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(XhScHoSoHdr.TABLE_NAME));
    fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(XhScHoSoHdr.TABLE_NAME + "_CAN_CU"));
    xhScHoSoRepository.deleteAll(list);

  }


  public XhScHoSoHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {

    if (StringUtils.isEmpty(statusReq.getId())) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    Optional<XhScHoSoHdr> optional = xhScHoSoRepository.findById(Long.valueOf(statusReq.getId()));
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    }

    String status = statusReq.getTrangThai() + optional.get().getTrangThai();
    switch (status) {
      case Contains.CHODUYET_TP + Contains.DUTHAO:
      case Contains.CHODUYET_TP + Contains.TUCHOI_TP:
      case Contains.CHODUYET_TP + Contains.TUCHOI_LDC:
      case Contains.CHODUYET_TP + Contains.TUCHOI_LDV:
      case Contains.CHODUYET_TP + Contains.TUCHOI_LDTC:
      case Contains.CHODUYET_LDC + Contains.CHODUYET_TP:
      case Contains.CHODUYET_LDV + Contains.DADUYET_LDC:
        optional.get().setNguoiGduyetId(currentUser.getUser().getId());
        optional.get().setNgayGduyet(LocalDate.now());
        break;
      case Contains.CHODUYET_LDV + Contains.CHODUYET_LDC:
        optional.get().setNguoiPduyetId(currentUser.getUser().getId());
        optional.get().setNgayDuyetLan1(LocalDate.now());
        break;
      case Contains.CHODUYET_LDTC + Contains.CHODUYET_LDV:
        optional.get().setNguoiPduyetId(currentUser.getUser().getId());
        optional.get().setNgayDuyetLan2(LocalDate.now());
        break;
      case Contains.DADUYET_LDTC + Contains.CHODUYET_LDTC:
        optional.get().setNguoiPduyetId(currentUser.getUser().getId());
        optional.get().setNgayDuyetLan3(LocalDate.now());
        break;
      case Contains.TUCHOI_TP + Contains.CHODUYET_TP:
      case Contains.TUCHOI_LDC + Contains.CHODUYET_LDC:
      case Contains.TUCHOI_LDV + Contains.CHODUYET_LDV:
      case Contains.TUCHOI_LDTC + Contains.CHODUYET_LDTC:
        optional.get().setNguoiPduyetId(currentUser.getUser().getId());
        optional.get().setNgayPduyet(LocalDate.now());
        optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
        break;
      case Contains.DA_DUYET_BTC + Contains.DADUYET_LDTC:
        optional.get().setNguoiPduyetId(currentUser.getUser().getId());
        optional.get().setNgayPduyet(LocalDate.now());
        break;
      default:
        throw new Exception("Phê duyệt không thành công");
    }
    optional.get().setTrangThai(statusReq.getTrangThai());
    XhScHoSoHdr created = xhScHoSoRepository.save(optional.get());
    return created;
  }


  public void export(CustomUserDetails currentUser, XhTlHoSoRequest objReq, HttpServletResponse response) throws Exception {
    PaggingReq paggingReq = new PaggingReq();
    paggingReq.setPage(0);
    paggingReq.setLimit(Integer.MAX_VALUE);
    objReq.setPaggingReq(paggingReq);
    Page<XhScHoSoHdr> page = this.searchPage(currentUser, objReq);
    List<XhScHoSoHdr> data = page.getContent();

    String title = "Danh sách quyết định thanh lý hàng DTQG ";
    String[] rowsName = new String[]{"STT", "Số quyết định", "Trích yếu", "Ngày ký",
        "Hồ sơ đề nghị thanh lý", "Trạng thái"};
    String fileName = "danh-sach-quyet-dinh-thanh-ly-hang-dtqg.xlsx";
    List<Object[]> dataList = new ArrayList<Object[]>();
    Object[] objs = null;
    for (int i = 0; i < data.size(); i++) {
      XhScHoSoHdr qd = data.get(i);
      objs = new Object[rowsName.length];
      objs[0] = i;
      objs[1] = qd.getSoQd();
      objs[2] = qd.getTrichYeu();
      objs[4] = qd.getSoHoSo();
      objs[5] = qd.getTenTrangThai();
      dataList.add(objs);
    }
    ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
    ex.export();
  }
}
