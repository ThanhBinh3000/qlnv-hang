package com.tcdt.qlnvhang.service.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly.XhTlDanhSachRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlDanhSachRequest;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlDanhSachHdr;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class XhTlDanhSachService extends BaseServiceImpl {


  @Autowired
  private XhTlDanhSachRepository xhTlDanhSachRepository;

  @Autowired
  private FileDinhKemService fileDinhKemService;

  public Page<XhTlDanhSachHdr> searchPage(CustomUserDetails currentUser, XhTlDanhSachRequest req) throws Exception {
    String dvql = currentUser.getDvql();
//    if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
//      req.setDvql(dvql.substring(0, 4));
//    } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_TONG_CUC)) {
//      req.setDvql(dvql);
//    }
    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
    Page<XhTlDanhSachHdr> search = xhTlDanhSachRepository.searchPage(req, pageable);

    //set label
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    search.getContent().forEach(s -> {
      s.getDanhSachDtl().forEach(s1 -> {
        s1.setMapDmucDvi(mapDmucDvi);
        s1.setMapVthh(mapVthh);
      });
      s.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(s.getTrangThai()));
    });
    return search;
  }

  @Transactional
  public XhTlDanhSachHdr save(CustomUserDetails currentUser, XhTlDanhSachRequest objReq) throws Exception {
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    if (!DataUtils.isNullObject(objReq.getMaDanhSach())) {
      Optional<XhTlDanhSachHdr> optional = xhTlDanhSachRepository.findByMaDanhSach(objReq.getMaDanhSach());
      if (optional.isPresent()) {
        throw new Exception("Mã danh sách tổng hợp đã tồn tại");
      }
    }
    XhTlDanhSachHdr data = new XhTlDanhSachHdr();
    BeanUtils.copyProperties(objReq, data);
    data.setMaDvi(currentUser.getUser().getDepartment());
    data.setTrangThai(Contains.DUTHAO);

    data.getDanhSachDtl().forEach(s -> {
      s.setDanhSachHdr(data);
    });

    XhTlDanhSachHdr created = xhTlDanhSachRepository.save(data);
    return created;
  }

  @Transactional
  public XhTlDanhSachHdr update(CustomUserDetails currentUser, XhTlDanhSachRequest objReq) throws Exception {
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    Optional<XhTlDanhSachHdr> optional = xhTlDanhSachRepository.findById(objReq.getId());
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu cần sửa");
    }
    Optional<XhTlDanhSachHdr> soDx = xhTlDanhSachRepository.findByMaDanhSach(objReq.getMaDanhSach());
    if (soDx.isPresent()) {
      if (!soDx.get().getId().equals(objReq.getId())) {
        throw new Exception("Mã danh sách tổng hợp đã tồn tại");
      }
    }

    XhTlDanhSachHdr data = optional.get();
    BeanUtils.copyProperties(objReq, data, "id");
    data.getDanhSachDtl().forEach(s -> {
      s.setDanhSachHdr(data);
    });
    XhTlDanhSachHdr created = xhTlDanhSachRepository.save(data);
    return created;
  }


  public List<XhTlDanhSachHdr> detail(List<Long> ids) throws Exception {
    if (DataUtils.isNullOrEmpty(ids)) throw new Exception("Tham số không hợp lệ.");
    List<XhTlDanhSachHdr> optional = xhTlDanhSachRepository.findByIdIn(ids);
    if (DataUtils.isNullOrEmpty(optional)) {
      throw new Exception("Không tìm thấy dữ liệu");
    }

    List<XhTlDanhSachHdr> allById = xhTlDanhSachRepository.findAllById(ids);
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    allById.forEach(data -> {
      data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
      data.getDanhSachDtl().forEach(s -> {
        s.setMapDmucDvi(mapDmucDvi);
        s.setMapVthh(mapVthh);
      });
    });
    return allById;
  }

  @Transactional
  public void delete(IdSearchReq idSearchReq) throws Exception {
    Optional<XhTlDanhSachHdr> optional = xhTlDanhSachRepository.findById(idSearchReq.getId());
    if (!optional.isPresent()) {
      throw new Exception("Bản ghi không tồn tại");
    }
    XhTlDanhSachHdr data = optional.get();
    xhTlDanhSachRepository.delete(data);
  }

  @Transient
  public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
    List<XhTlDanhSachHdr> list = xhTlDanhSachRepository.findByIdIn(idSearchReq.getIdList());

    if (list.isEmpty()) {
      throw new Exception("Bản ghi không tồn tại");
    }
    xhTlDanhSachRepository.deleteAll(list);

  }


  public XhTlDanhSachHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {

    if (StringUtils.isEmpty(statusReq.getId())) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    Optional<XhTlDanhSachHdr> optional = xhTlDanhSachRepository.findById(Long.valueOf(statusReq.getId()));
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    }

    String status = optional.get().getTrangThai() + statusReq.getTrangThai();
    if (status.equals(TrangThaiAllEnum.DU_THAO.getId() + TrangThaiAllEnum.CHO_DUYET_TP.getId())) {
      optional.get().setNguoiGduyetId(currentUser.getUser().getId());
      optional.get().setNgayGduyet(LocalDate.now());
    } else {
      throw new Exception("Phê duyệt không thành công");
    }
    optional.get().setTrangThai(statusReq.getTrangThai());
    XhTlDanhSachHdr created = xhTlDanhSachRepository.save(optional.get());
    return created;
  }


  /*public void export(CustomUserDetails currentUser, SearchXhTlQuyetDinhTl objReq, HttpServletResponse response) throws Exception {
    PaggingReq paggingReq = new PaggingReq();
    paggingReq.setPage(0);
    paggingReq.setLimit(Integer.MAX_VALUE);
    objReq.setPaggingReq(paggingReq);
    Page<XhTlDanhSachHdr> page = this.searchPage(currentUser, objReq);
    List<XhTlDanhSachHdr> data = page.getContent();

    String title = "Danh sách quyết định thanh lý hàng DTQG ";
    String[] rowsName = new String[]{"STT", "Số quyết định", "Trích yếu", "Ngày ký",
        "Hồ sơ đề nghị thanh lý", "Trạng thái"};
    String fileName = "danh-sach-phuong-quyet-dinh-thanh-ly-hang-dtqg.xlsx";
    List<Object[]> dataList = new ArrayList<Object[]>();
    Object[] objs = null;
    for (int i = 0; i < data.size(); i++) {
      XhTlDanhSachHdr qd = data.get(i);
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
  }*/
}
