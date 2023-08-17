package com.tcdt.qlnvhang.service.xuathang.xuatkhac.ktvattubaohanh;

import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.vattubaohanh.XhXkVtBhBaoCaoKqRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.vattubaohanh.XhXkVtBhBbBaoHanhRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattubaohanh.XhXkVtBhBaoCaoKqRequest;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattubaohanh.*;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;

@Service
public class XhXkVtBhBaoCaoKqService extends BaseServiceImpl {


  @Autowired
  private XhXkVtBhBaoCaoKqRepository xhXkVtBhBaoCaoKqRepository;

  @Autowired
  private XhXkVtBhBbBaoHanhRepository xhXkVtBhBbBaoHanhRepository;


  @Autowired
  private UserInfoRepository userInfoRepository;

  @Autowired
  private FileDinhKemService fileDinhKemService;

  public Page<XhXkVtBhBaoCaoKqHdr> searchPage(CustomUserDetails currentUser, XhXkVtBhBaoCaoKqRequest req) throws Exception {
    req.setDvql(currentUser.getDvql());
    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
    Page<XhXkVtBhBaoCaoKqHdr> search = xhXkVtBhBaoCaoKqRepository.search(req, pageable);
//        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
//        Map<String, String> mapVthh = getListDanhMucHangHoa();
    search.getContent().forEach(s -> {
      s.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(s.getTrangThai()));
    });
    return search;
  }

  @Transactional
  public XhXkVtBhBaoCaoKqHdr save(CustomUserDetails currentUser, XhXkVtBhBaoCaoKqRequest objReq) throws Exception {
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    Optional<XhXkVtBhBaoCaoKqHdr> optional = xhXkVtBhBaoCaoKqRepository.findBySoBaoCao(objReq.getSoBaoCao());
    if (optional.isPresent()) {
      throw new Exception("Số báo cáo đã tồn tại");
    }
    XhXkVtBhBaoCaoKqHdr data = new XhXkVtBhBaoCaoKqHdr();
    BeanUtils.copyProperties(objReq, data);
    data.setTrangThai(Contains.DUTHAO);
    XhXkVtBhBaoCaoKqHdr created = xhXkVtBhBaoCaoKqRepository.save(data);
    List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), created.getId(), XhXkVtBhBaoCaoKqHdr.TABLE_NAME);
    created.setFileDinhKems(fileDinhKems);

    //update số lần lấy mẫu
    this.updateBaoCaoKq(data, false);

    return created;
  }

  @Transactional()
  public XhXkVtBhBaoCaoKqHdr update(CustomUserDetails currentUser, XhXkVtBhBaoCaoKqRequest objReq) throws Exception {
    if (objReq.getId() == null) {
      throw new Exception("Bad request!");
    }
    Optional<XhXkVtBhBaoCaoKqHdr> optional = xhXkVtBhBaoCaoKqRepository.findById(objReq.getId());
    if (!optional.isPresent()) throw new Exception("Không tồn tại bản ghi!");

    if (objReq.getSoBaoCao().contains("/") && !ObjectUtils.isEmpty(objReq.getSoBaoCao().split("/")[0])) {
      Optional<XhXkVtBhBaoCaoKqHdr> optionalBySoBb = xhXkVtBhBaoCaoKqRepository.findBySoBaoCao(objReq.getSoBaoCao());
      if (optionalBySoBb.isPresent() && optionalBySoBb.get().getId() != objReq.getId()) {
        if (!optionalBySoBb.isPresent()) throw new Exception("Số báo cáo đã tồn tại!");
      }
    }
    XhXkVtBhBaoCaoKqHdr data = optional.get();
    BeanUtils.copyProperties(objReq, data);
    XhXkVtBhBaoCaoKqHdr created = xhXkVtBhBaoCaoKqRepository.save(data);
    fileDinhKemService.delete(data.getId(), Collections.singleton(XhXkVtBhBaoCaoKqHdr.TABLE_NAME));
    //save file đính kèm
    fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKemReq(), created.getId(), XhXkVtBhBaoCaoKqHdr.TABLE_NAME);
    //update số lần lấy mẫu
    this.updateBaoCaoKq(data, false);

    return detail(created.getId());
  }


  @Transactional()
  public XhXkVtBhBaoCaoKqHdr detail(Long id) throws Exception {
    if (ObjectUtils.isEmpty(id)) throw new Exception("Tham số không hợp lệ.");
    Optional<XhXkVtBhBaoCaoKqHdr> optional = xhXkVtBhBaoCaoKqRepository.findById(id);
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    XhXkVtBhBaoCaoKqHdr model = optional.get();
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    List<FileDinhKem> fileDinhKem = fileDinhKemService.search(model.getId(), Arrays.asList(XhXkVtBhBaoCaoKqHdr.TABLE_NAME));
    model.setFileDinhKems(fileDinhKem);
    model.setTenDvi(mapDmucDvi.get(model.getMaDvi()));
    model.setTenTrangThai(TrangThaiAllEnum.getLabelById(model.getTrangThai()));
    model.getBhBaoCaoKqDtl().forEach(item -> {
      item.setMapVthh(mapVthh);
      item.setMapDmucDvi(mapDmucDvi);
      item.setTenTrangThaiBh(TrangThaiAllEnum.getLabelById(item.getTrangThaiBh()));
      item.setTenTrangThaiNh(TrangThaiAllEnum.getLabelById(item.getTrangThaiNh()));
    });
    return model;
  }

  @Transactional
  public void delete(IdSearchReq idSearchReq) throws Exception {
    Optional<XhXkVtBhBaoCaoKqHdr> optional = xhXkVtBhBaoCaoKqRepository.findById(idSearchReq.getId());
    if (!optional.isPresent()) {
      throw new Exception("Bản ghi không tồn tại");
    }
    if (optional.get().getTrangThai().equals(TrangThaiAllEnum.DU_THAO.getId()) || optional.get().getTrangThai().equals(TrangThaiAllEnum.TU_CHOI_LDC.getId())) {
      XhXkVtBhBaoCaoKqHdr data = optional.get();
      fileDinhKemService.deleteMultiple(Collections.singleton(data.getId()), Collections.singleton(XhXkVtBhBaoCaoKqHdr.TABLE_NAME));
      //update số lần lấy mẫu
      this.updateBaoCaoKq(data, true);
      xhXkVtBhBaoCaoKqRepository.delete(data);
    } else {
      throw new Exception("Bản ghi đang ở trạng thái " + TrangThaiAllEnum.getLabelById(optional.get().getTrangThai()) + " không thể xóa.");
    }
  }

  @Transient
  public XhXkVtBhBaoCaoKqHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {

    if (StringUtils.isEmpty(statusReq.getId())) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    Optional<XhXkVtBhBaoCaoKqHdr> optional = xhXkVtBhBaoCaoKqRepository.findById(Long.valueOf(statusReq.getId()));
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    String status = statusReq.getTrangThai() + optional.get().getTrangThai();
    switch (status) {
      case Contains.CHODUYET_LDC + Contains.DUTHAO:
      case Contains.CHODUYET_LDC + Contains.TUCHOI_LDC:
        optional.get().setNguoiGduyetId(currentUser.getUser().getId());
        optional.get().setNgayGduyet(LocalDate.now());
        break;
      case Contains.TUCHOI_LDC + Contains.CHODUYET_LDC:
        optional.get().setNguoiPduyetId(currentUser.getUser().getId());
        optional.get().setNgayPduyet(LocalDate.now());
        optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
        break;
      case Contains.DADUYET_LDC + Contains.CHODUYET_LDC:
        optional.get().setNguoiPduyetId(currentUser.getUser().getId());
        optional.get().setNgayPduyet(LocalDate.now());
        break;
      default:
        throw new Exception("Phê duyệt không thành công");
    }
    optional.get().setTrangThai(statusReq.getTrangThai());
    XhXkVtBhBaoCaoKqHdr created = xhXkVtBhBaoCaoKqRepository.save(optional.get());
    return created;
  }

  public void export(CustomUserDetails currentUser, XhXkVtBhBaoCaoKqRequest objReq, HttpServletResponse response) throws Exception {
    PaggingReq paggingReq = new PaggingReq();
    paggingReq.setPage(0);
    paggingReq.setLimit(Integer.MAX_VALUE);
    objReq.setPaggingReq(paggingReq);
    Page<XhXkVtBhBaoCaoKqHdr> page = this.searchPage(currentUser, objReq);
    List<XhXkVtBhBaoCaoKqHdr> data = page.getContent();

    String title = "Danh sách báo cáo kết quả kiểm định";
    String[] rowsName = new String[]{"STT", "Năm báo cáo", "Đơn vị gửi báo cáo", "Số báo cáo", "Tên báo cáo", "Ngày báo cáo", "Số QĐ giao NVXH để lấy mẫu", "Số QĐ xuất giảm VT của Tổng Cục",
        "Số QĐ giao NV xuất giảm VT của cục", "Trạng thái"};
    String fileName = "danh-sach-bao-cao-ket-qua-kiem-dinh.xlsx";
    List<Object[]> dataList = new ArrayList<Object[]>();
    Object[] objs = null;
    for (int i = 0; i < data.size(); i++) {
      XhXkVtBhBaoCaoKqHdr dx = data.get(i);
      objs = new Object[rowsName.length];
      objs[0] = i;
      objs[1] = dx.getNam();
      objs[2] = dx.getTenDvi();
      objs[3] = dx.getSoBaoCao();
      objs[4] = dx.getTenBaoCao();
      objs[5] = dx.getNgayBaoCao();
      objs[6] = dx.getSoCanCu();
      objs[7] = null;
      objs[8] = null;
      objs[9] = dx.getTenTrangThai();
      dataList.add(objs);
    }
    ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
    ex.export();
  }

  public void updateBaoCaoKq(XhXkVtBhBaoCaoKqHdr baoCaoKqHdr, boolean xoa) {
    if (!DataUtils.isNullObject(baoCaoKqHdr.getIdCanCu())) {
      Long[] idsBaoCaoKq = Arrays.stream(baoCaoKqHdr.getIdCanCu().split(","))
          .map(String::trim)
          .map(Long::valueOf)
          .toArray(Long[]::new);

      List<XhXkVtBhBbBaoHanh> listBbBaoHanh = xhXkVtBhBbBaoHanhRepository.findByIdIn(Arrays.asList(idsBaoCaoKq));
      if (!listBbBaoHanh.isEmpty()) {
        listBbBaoHanh.forEach(item -> {
          if (xoa) {
            item.setIdBaoCaoKq(null);
            item.setSoBaoCaoKq(null);
          } else {
            item.setIdBaoCaoKq(baoCaoKqHdr.getId());
            item.setSoBaoCaoKq(baoCaoKqHdr.getSoBaoCao());
          }
        });
        xhXkVtBhBbBaoHanhRepository.saveAll(listBbBaoHanh);
      }
    }
  }
}

