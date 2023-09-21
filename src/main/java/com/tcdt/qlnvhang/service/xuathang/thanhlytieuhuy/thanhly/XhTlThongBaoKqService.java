package com.tcdt.qlnvhang.service.xuathang.thanhlytieuhuy.thanhly;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly.XhTlHoSoHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly.XhTlThongBaoKqRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.SearchXhTlQuyetDinh;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlThongBaoKqReq;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlHoSoHdr;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlThongBaoKq;
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
public class XhTlThongBaoKqService extends BaseServiceImpl {


  @Autowired
  private XhTlThongBaoKqRepository xhTlThongBaoKqRepository;

  @Autowired
  private XhTlHoSoHdrRepository xhTlHoSoHdrRepository;

  @Autowired
  private FileDinhKemService fileDinhKemService;

  public Page<XhTlThongBaoKq> searchPage(CustomUserDetails currentUser, SearchXhTlQuyetDinh req) throws Exception {
    String dvql = currentUser.getDvql();
    if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
      req.setDvql(dvql.substring(0, 4));
    } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_TONG_CUC)) {
      req.setDvql(dvql);
    }
    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
    Page<XhTlThongBaoKq> search = xhTlThongBaoKqRepository.search(req, pageable);
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
  public XhTlThongBaoKq save(CustomUserDetails currentUser, XhTlThongBaoKqReq objReq) throws Exception {
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    if (!DataUtils.isNullObject(objReq.getSoThongBao())) {
      Optional<XhTlThongBaoKq> optional = xhTlThongBaoKqRepository.findBySoThongBao(objReq.getSoThongBao());
      if (optional.isPresent()) {
        throw new Exception("số quyết định đã tồn tại");
      }
    }
    XhTlThongBaoKq data = new XhTlThongBaoKq();
    BeanUtils.copyProperties(objReq, data);
    data.setMaDvi(currentUser.getUser().getDepartment());
    data.setTrangThai(Contains.DUTHAO);

    XhTlThongBaoKq created = xhTlThongBaoKqRepository.save(data);

    if (!DataUtils.isNullObject(data.getIdHoSo())) {
      Optional<XhTlHoSoHdr> hoSo = xhTlHoSoHdrRepository.findById(data.getIdHoSo());
      if (hoSo.isPresent()) {
        hoSo.get().setIdTb(created.getId());
        hoSo.get().setSoTb(created.getSoThongBao());
        xhTlHoSoHdrRepository.save(hoSo.get());
      }
    }
    if (!DataUtils.isNullObject(objReq.getFileDinhKem())) {
      fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKem(), created.getId(), XhTlThongBaoKq.TABLE_NAME);
    }

    return created;
  }

  @Transactional
  public XhTlThongBaoKq update(CustomUserDetails currentUser, XhTlThongBaoKqReq objReq) throws Exception {
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    Optional<XhTlThongBaoKq> optional = xhTlThongBaoKqRepository.findById(objReq.getId());
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu cần sửa");
    }
    Optional<XhTlThongBaoKq> soDx = xhTlThongBaoKqRepository.findBySoThongBao(objReq.getSoThongBao());
    if (soDx.isPresent()) {
      if (!soDx.get().getId().equals(objReq.getId())) {
        throw new Exception("số quyết định đã tồn tại");
      }
    }

    XhTlThongBaoKq data = optional.get();
    BeanUtils.copyProperties(objReq, data, "id","maDvi");
    XhTlThongBaoKq created = xhTlThongBaoKqRepository.save(data);

    if (!DataUtils.isNullObject(data.getIdHoSo())) {
      Optional<XhTlHoSoHdr> hoSo = xhTlHoSoHdrRepository.findById(data.getIdHoSo());
      if (hoSo.isPresent()) {
        hoSo.get().setIdTb(created.getId());
        hoSo.get().setSoTb(created.getSoThongBao());
        xhTlHoSoHdrRepository.save(hoSo.get());
      }
    }
    fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(XhTlThongBaoKq.TABLE_NAME + "_CAN_CU"));

    fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(XhTlThongBaoKq.TABLE_NAME));

    if (!DataUtils.isNullObject(objReq.getFileDinhKem())) {
      fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKem(), created.getId(), XhTlThongBaoKq.TABLE_NAME);
    }
    return created;
  }


  public List<XhTlThongBaoKq> detail(List<Long> ids) throws Exception {
    if (DataUtils.isNullOrEmpty(ids)) throw new Exception("Tham số không hợp lệ.");
    List<XhTlThongBaoKq> optional = xhTlThongBaoKqRepository.findByIdIn(ids);
    if (DataUtils.isNullOrEmpty(optional)) {
      throw new Exception("Không tìm thấy dữ liệu");
    }

    Map<String, Map<String, Object>> mapDmucDvi = getListDanhMucDviObject(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    List<XhTlThongBaoKq> allById = xhTlThongBaoKqRepository.findAllById(ids);
    allById.forEach(data -> {
      if (mapDmucDvi.containsKey(data.getMaDvi())) {
        data.setTenDvi(mapDmucDvi.get(data.getMaDvi()).get("tenDvi").toString());
      }
      data.setTenTrangThai(TrangThaiAllEnum.getLabelById(data.getTrangThai()));

      List<FileDinhKem> fileDinhKem = fileDinhKemService.search(data.getId(), Arrays.asList(XhTlThongBaoKq.TABLE_NAME));
      data.setFileDinhKem(fileDinhKem);

    });
    return allById;
  }

  @Transactional
  public void delete(IdSearchReq idSearchReq) throws Exception {
    Optional<XhTlThongBaoKq> optional = xhTlThongBaoKqRepository.findById(idSearchReq.getId());
    if (!optional.isPresent()) {
      throw new Exception("Bản ghi không tồn tại");
    }
    XhTlThongBaoKq data = optional.get();

    if (!DataUtils.isNullObject(data.getIdHoSo())) {
      Optional<XhTlHoSoHdr> hoSo = xhTlHoSoHdrRepository.findById(data.getIdHoSo());
      if (hoSo.isPresent()) {
        hoSo.get().setIdTb(null);
        hoSo.get().setSoTb(null);
        xhTlHoSoHdrRepository.save(hoSo.get());
      }
    }

    fileDinhKemService.delete(data.getId(), Lists.newArrayList(XhTlThongBaoKq.TABLE_NAME));
    xhTlThongBaoKqRepository.delete(data);

  }

  @Transactional
  public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
    List<XhTlThongBaoKq> list = xhTlThongBaoKqRepository.findAllByIdIn(idSearchReq.getIdList());

    if (list.isEmpty()) {
      throw new Exception("Bản ghi không tồn tại");
    }

    List<Long> listHoSo = list.stream().map(XhTlThongBaoKq::getIdHoSo).collect(Collectors.toList());
    List<XhTlHoSoHdr> listObjQdPd = xhTlHoSoHdrRepository.findByIdIn(listHoSo);
    listObjQdPd.forEach(s -> {
      s.setIdTb(null);
      s.setSoTb(null);
    });
    xhTlHoSoHdrRepository.saveAll(listObjQdPd);

    fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(XhTlThongBaoKq.TABLE_NAME));
    xhTlThongBaoKqRepository.deleteAll(list);

  }

  public XhTlThongBaoKq approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {

    if (StringUtils.isEmpty(statusReq.getId())) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    Optional<XhTlThongBaoKq> optional = xhTlThongBaoKqRepository.findById(Long.valueOf(statusReq.getId()));
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    }

    String status = statusReq.getTrangThai() + optional.get().getTrangThai();
    switch (status) {
      case Contains.DA_HOAN_THANH + Contains.DUTHAO:
        optional.get().setNguoiPduyetId(currentUser.getUser().getId());
        optional.get().setNgayPduyet(LocalDate.now());
        break;
      default:
        throw new Exception("Phê duyệt không thành công");
    }
    optional.get().setTrangThai(statusReq.getTrangThai());
    XhTlThongBaoKq created = xhTlThongBaoKqRepository.save(optional.get());
    return created;
  }


  public void export(CustomUserDetails currentUser, SearchXhTlQuyetDinh objReq, HttpServletResponse response) throws Exception {
    PaggingReq paggingReq = new PaggingReq();
    paggingReq.setPage(0);
    paggingReq.setLimit(Integer.MAX_VALUE);
    objReq.setPaggingReq(paggingReq);
    Page<XhTlThongBaoKq> page = this.searchPage(currentUser, objReq);
    List<XhTlThongBaoKq> data = page.getContent();

    String title = "Danh sách thông báo kết quả trình hồ sơ thanh lý hàng DTQG ";
    String[] rowsName = new String[]{"STT", "Số thông báo KQ trình hồ sơ", "Trích yếu", "Ngày ký",
        "Hồ sơ đề nghị thanh lý", "Trạng thái"};
    String fileName = "danh-sach-bao-cao-ket-qua-ban-thanh-ly-hang-dtqg.xlsx";
    List<Object[]> dataList = new ArrayList<Object[]>();
    Object[] objs = null;
    for (int i = 0; i < data.size(); i++) {
      XhTlThongBaoKq qd = data.get(i);
      objs = new Object[rowsName.length];
      objs[0] = i;
      objs[1] = qd.getSoThongBao();
      objs[2] = qd.getNoiDung();
      objs[3] = qd.getNgayPduyet();
      objs[4] = qd.getSoHoSo();
      objs[5] = qd.getTenTrangThai();
      dataList.add(objs);
    }
    ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
    ex.export();
  }
}
