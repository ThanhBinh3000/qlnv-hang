package com.tcdt.qlnvhang.service.xuathang.thanhlytieuhuy.tieuhuy;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.tieuhuy.XhThHoSoHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.tieuhuy.XhThThongBaoKqRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.tieuhuy.SearchXhThQuyetDinh;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.tieuhuy.XhThThongBaoKqReq;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.tieuhuy.XhThHoSoHdr;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.tieuhuy.XhThThongBaoKq;
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
public class XhThThongBaoKqService extends BaseServiceImpl {


  @Autowired
  private XhThThongBaoKqRepository xhThThongBaoKqRepository;

  @Autowired
  private XhThHoSoHdrRepository xhThHoSoHdrRepository;

  @Autowired
  private FileDinhKemService fileDinhKemService;

  public Page<XhThThongBaoKq> searchPage(CustomUserDetails currentUser, SearchXhThQuyetDinh req) throws Exception {
    String dvql = currentUser.getDvql();
    if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
      req.setDvql(dvql.substring(0, 4));
    } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_TONG_CUC)) {
      req.setDvql(dvql);
    }
    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
    Page<XhThThongBaoKq> search = xhThThongBaoKqRepository.search(req, pageable);
    Map<String, Map<String, Object>> mapDmucDvi = getListDanhMucDviObject(null, null, "01");

    Map<String, String> mapVthh = getListDanhMucHangHoa();
    search.getContent().forEach(s -> {
//      if (mapDmucDvi.containsKey((s.getMaDvi()))) {
//        Map<String, Object> objDonVi = mapDmucDvi.get(s.getMaDvi());
//        s.setTenDvi(objDonVi.get("tenDvi").toString());
//      }
      s.setTenTrangThai(TrangThaiAllEnum.getLabelById(s.getTrangThai()));
    });
    return search;
  }

  @Transactional
  public XhThThongBaoKq save(CustomUserDetails currentUser, XhThThongBaoKqReq objReq) throws Exception {
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    if (!DataUtils.isNullObject(objReq.getSoThongBao())) {
      Optional<XhThThongBaoKq> optional = xhThThongBaoKqRepository.findBySoThongBao(objReq.getSoThongBao());
      if (optional.isPresent()) {
        throw new Exception("số quyết định đã tồn tại");
      }
    }
    XhThThongBaoKq data = new XhThThongBaoKq();
    BeanUtils.copyProperties(objReq, data);
    data.setMaDvi(currentUser.getUser().getDepartment());
    data.setTrangThai(Contains.DUTHAO);

    XhThThongBaoKq created = xhThThongBaoKqRepository.save(data);

    if (!DataUtils.isNullObject(data.getIdHoSo())) {
      Optional<XhThHoSoHdr> hoSo = xhThHoSoHdrRepository.findById(data.getIdHoSo());
      if (hoSo.isPresent()) {
        hoSo.get().setIdTb(created.getId());
        hoSo.get().setSoTb(created.getSoThongBao());
        xhThHoSoHdrRepository.save(hoSo.get());
      }
    }
    if (!DataUtils.isNullObject(objReq.getFileDinhKemReq())) {
      fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKemReq(), created.getId(), XhThThongBaoKq.TABLE_NAME);
    }

    return created;
  }

  @Transactional
  public XhThThongBaoKq update(CustomUserDetails currentUser, XhThThongBaoKqReq objReq) throws Exception {
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    Optional<XhThThongBaoKq> optional = xhThThongBaoKqRepository.findById(objReq.getId());
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu cần sửa");
    }
    Optional<XhThThongBaoKq> soDx = xhThThongBaoKqRepository.findBySoThongBao(objReq.getSoThongBao());
    if (soDx.isPresent()) {
      if (!soDx.get().getId().equals(objReq.getId())) {
        throw new Exception("số quyết định đã tồn tại");
      }
    }

    XhThThongBaoKq data = optional.get();
    BeanUtils.copyProperties(objReq, data, "id","maDvi");
    XhThThongBaoKq created = xhThThongBaoKqRepository.save(data);

    if (!DataUtils.isNullObject(data.getIdHoSo())) {
      Optional<XhThHoSoHdr> hoSo = xhThHoSoHdrRepository.findById(data.getIdHoSo());
      if (hoSo.isPresent()) {
        hoSo.get().setIdTb(created.getId());
        hoSo.get().setSoTb(created.getSoThongBao());
        xhThHoSoHdrRepository.save(hoSo.get());
      }
    }

    fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(XhThThongBaoKq.TABLE_NAME));

    if (!DataUtils.isNullObject(objReq.getFileDinhKemReq())) {
      fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKemReq(), created.getId(), XhThThongBaoKq.TABLE_NAME);
    }
    return created;
  }


  public List<XhThThongBaoKq> detail(List<Long> ids) throws Exception {
    if (DataUtils.isNullOrEmpty(ids)) throw new Exception("Tham số không hợp lệ.");
    List<XhThThongBaoKq> optional = xhThThongBaoKqRepository.findByIdIn(ids);
    if (DataUtils.isNullOrEmpty(optional)) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    List<XhThThongBaoKq> allById = xhThThongBaoKqRepository.findAllById(ids);
    allById.forEach(data -> {
      data.setTenTrangThai(TrangThaiAllEnum.getLabelById(data.getTrangThai()));

      List<FileDinhKem> fileDinhKem = fileDinhKemService.search(data.getId(), Arrays.asList(XhThThongBaoKq.TABLE_NAME));
      data.setFileDinhKem(fileDinhKem);

    });
    return allById;
  }

  @Transactional
  public void delete(IdSearchReq idSearchReq) throws Exception {
    Optional<XhThThongBaoKq> optional = xhThThongBaoKqRepository.findById(idSearchReq.getId());
    if (!optional.isPresent()) {
      throw new Exception("Bản ghi không tồn tại");
    }
    XhThThongBaoKq data = optional.get();

    if (!DataUtils.isNullObject(data.getIdHoSo())) {
      Optional<XhThHoSoHdr> hoSo = xhThHoSoHdrRepository.findById(data.getIdHoSo());
      if (hoSo.isPresent()) {
        hoSo.get().setIdTb(null);
        hoSo.get().setSoTb(null);
        xhThHoSoHdrRepository.save(hoSo.get());
      }
    }

    fileDinhKemService.delete(data.getId(), Lists.newArrayList(XhThThongBaoKq.TABLE_NAME));
    xhThThongBaoKqRepository.delete(data);

  }

  @Transactional
  public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
    List<XhThThongBaoKq> list = xhThThongBaoKqRepository.findAllByIdIn(idSearchReq.getIdList());

    if (list.isEmpty()) {
      throw new Exception("Bản ghi không tồn tại");
    }

    List<Long> listHoSo = list.stream().map(XhThThongBaoKq::getIdHoSo).collect(Collectors.toList());
    List<XhThHoSoHdr> listObjQdPd = xhThHoSoHdrRepository.findByIdIn(listHoSo);
    listObjQdPd.forEach(s -> {
      s.setIdTb(null);
      s.setSoTb(null);
    });
    xhThHoSoHdrRepository.saveAll(listObjQdPd);
    fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(XhThThongBaoKq.TABLE_NAME));
    xhThThongBaoKqRepository.deleteAll(list);

  }

  public XhThThongBaoKq approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {

    if (StringUtils.isEmpty(statusReq.getId())) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    Optional<XhThThongBaoKq> optional = xhThThongBaoKqRepository.findById(Long.valueOf(statusReq.getId()));
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    }

    String status = statusReq.getTrangThai() + optional.get().getTrangThai();
    switch (status) {
      case Contains.DA_HOAN_THANH + Contains.DUTHAO:
//        optional.get().setNguoiPduyetId(currentUser.getUser().getId());
//        optional.get().setNgayPduyet(LocalDate.now());
        break;
      default:
        throw new Exception("Phê duyệt không thành công");
    }
    optional.get().setTrangThai(statusReq.getTrangThai());
    XhThThongBaoKq created = xhThThongBaoKqRepository.save(optional.get());
    return created;
  }


  public void export(CustomUserDetails currentUser, SearchXhThQuyetDinh objReq, HttpServletResponse response) throws Exception {
    PaggingReq paggingReq = new PaggingReq();
    paggingReq.setPage(0);
    paggingReq.setLimit(Integer.MAX_VALUE);
    objReq.setPaggingReq(paggingReq);
    Page<XhThThongBaoKq> page = this.searchPage(currentUser, objReq);
    List<XhThThongBaoKq> data = page.getContent();

    String title = "Danh sách thông báo kết quả trình hồ sơ thanh lý hàng DTQG ";
    String[] rowsName = new String[]{"STT", "Số thông báo KQ trình hồ sơ", "Trích yếu", "Ngày ký",
        "Hồ sơ đề nghị thanh lý", "Trạng thái"};
    String fileName = "danh-sach-bao-cao-ket-qua-ban-thanh-ly-hang-dtqg.xlsx";
    List<Object[]> dataList = new ArrayList<Object[]>();
    Object[] objs = null;
    for (int i = 0; i < data.size(); i++) {
      XhThThongBaoKq qd = data.get(i);
      objs = new Object[rowsName.length];
      objs[0] = i;
      objs[1] = qd.getSoThongBao();
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
