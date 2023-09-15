package com.tcdt.qlnvhang.service.xuathang.xuatkhac.ktvattubaohanh;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.ktvattu.XhXkVtPhieuXuatNhapKhoRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.vattubaohanh.XhXkVtBhBbKtNhapKhoRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.vattubaohanh.XhXkVtBhPhieuXuatNhapKhoRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.vattubaohanh.XhXkVtBhQdGiaonvXnRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattubaohanh.XhXkVtBhBbKtNhapKhoRequest;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattubaohanh.XhXkVtBhBbKtNhapKho;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattubaohanh.XhXkVtBhBbLayMauHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattubaohanh.XhXkVtBhQdGiaonvXnHdr;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import com.tcdt.qlnvhang.util.ExportExcel;
import fr.opensagres.xdocreport.core.XDocReportException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class XhXkVtBhBbKtNhapKhoService extends BaseServiceImpl {


  @Autowired
  private XhXkVtBhBbKtNhapKhoRepository xhXkVtBhBbKtNhapKhoRepository;

  @Autowired
  private XhXkVtBhQdGiaonvXnRepository xhXkVtBhQdGiaonvXnRepository;

  @Autowired
  private XhXkVtBhPhieuXuatNhapKhoRepository xhXkVtBhPhieuXuatNhapKhoRepository;

  @Autowired
  private UserInfoRepository userInfoRepository;

  @Autowired
  private FileDinhKemService fileDinhKemService;

  public Page<XhXkVtBhBbKtNhapKho> searchPage(CustomUserDetails currentUser, XhXkVtBhBbKtNhapKhoRequest req) throws Exception {
    req.setDvql(ObjectUtils.isEmpty(req.getDvql()) ? currentUser.getDvql() : req.getDvql());
    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
    Page<XhXkVtBhBbKtNhapKho> search = xhXkVtBhBbKtNhapKhoRepository.search(req, pageable);
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    search.getContent().forEach(s -> {
      s.setMapDmucDvi(mapDmucDvi);
      s.setMapVthh(mapVthh);
      s.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(s.getTrangThai()));
      s.setListPhieuNhapKho(xhXkVtBhPhieuXuatNhapKhoRepository.findAllByIdBbKtNhapKho(s.getId()));
    });
    return search;
  }


  @Transactional
  public XhXkVtBhBbKtNhapKho save(CustomUserDetails currentUser, XhXkVtBhBbKtNhapKhoRequest objReq) throws Exception {
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    Optional<XhXkVtBhBbKtNhapKho> optional = xhXkVtBhBbKtNhapKhoRepository.findBySoBienBan(objReq.getSoBienBan());
    if (optional.isPresent()) {
      throw new Exception("Số biên bản đã tồn tại");
    }
    XhXkVtBhBbKtNhapKho data = new XhXkVtBhBbKtNhapKho();
    BeanUtils.copyProperties(objReq, data);
    data.setTrangThai(Contains.DUTHAO);
    XhXkVtBhBbKtNhapKho created = xhXkVtBhBbKtNhapKhoRepository.save(data);
    // cập nhật trạng thái đang thực hiện cho QD giao nv nhập hàng
    List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), created.getId(), XhXkVtBhBbKtNhapKho.TABLE_NAME);
    created.setFileDinhKems(fileDinhKems);
    //save lại số bb vào phiếu xuất kho
    this.updatePhieuXk( created, false);
    return created;
  }

  @Transactional
  public XhXkVtBhBbKtNhapKho update(CustomUserDetails currentUser, XhXkVtBhBbKtNhapKhoRequest objReq) throws Exception {
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    Optional<XhXkVtBhBbKtNhapKho> optional = xhXkVtBhBbKtNhapKhoRepository.findById(objReq.getId());
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu cần sửa");
    }
    Optional<XhXkVtBhBbKtNhapKho> soDx = xhXkVtBhBbKtNhapKhoRepository.findBySoBienBan(objReq.getSoBienBan());
    if (soDx.isPresent()) {
      if (!soDx.get().getId().equals(objReq.getId())) {
        throw new Exception("Số biên bản đã tồn tại");
      }
    }
    XhXkVtBhBbKtNhapKho data = optional.get();
    BeanUtils.copyProperties(objReq, data);
    XhXkVtBhBbKtNhapKho updated = xhXkVtBhBbKtNhapKhoRepository.save(data);
    fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(XhXkVtBhBbKtNhapKho.TABLE_NAME));
    List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), updated.getId(), XhXkVtBhBbKtNhapKho.TABLE_NAME);
    updated.setFileDinhKems(fileDinhKems);
    //Update lại phiếu nhập kho khi sửa số BC kết quả kiểm định mẫu
    this.updatePhieuXk( updated, false);
    return updated;
  }

  public XhXkVtBhBbKtNhapKho detail(Long id) throws Exception {
    if (ObjectUtils.isEmpty(id)) throw new Exception("Tham số không hợp lệ.");
    Optional<XhXkVtBhBbKtNhapKho> optional = xhXkVtBhBbKtNhapKhoRepository.findById(id);
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    XhXkVtBhBbKtNhapKho model = optional.get();
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    List<FileDinhKem> fileDinhKem = fileDinhKemService.search(model.getId(), Arrays.asList(XhXkVtBhBbKtNhapKho.TABLE_NAME));
    model.setFileDinhKems(fileDinhKem);
    model.setMapDmucDvi(mapDmucDvi);
    model.setMapVthh(mapVthh);
    model.setTenDvi(mapDmucDvi.get(model.getMaDvi()));
    model.setTenTrangThai(TrangThaiAllEnum.getLabelById(model.getTrangThai()));
    model.setListPhieuNhapKho(xhXkVtBhPhieuXuatNhapKhoRepository.findAllByIdBbKtNhapKho(id).stream().filter(item -> item.getLoaiPhieu().equals("NHAP")).collect(Collectors.toList()));
    return model;
  }

  @Transactional
  public void delete(IdSearchReq idSearchReq) throws Exception {
    Optional<XhXkVtBhBbKtNhapKho> optional = xhXkVtBhBbKtNhapKhoRepository.findById(idSearchReq.getId());
    if (!optional.isPresent()) {
      throw new Exception("Bản ghi không tồn tại");
    }
    XhXkVtBhBbKtNhapKho data = optional.get();
    fileDinhKemService.delete(data.getId(), Lists.newArrayList(XhXkVtBhBbKtNhapKho.TABLE_NAME));
    this.updatePhieuXk( data, true);
    xhXkVtBhBbKtNhapKhoRepository.delete(data);
  }

  @Transactional
  public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
    List<XhXkVtBhBbKtNhapKho> list = xhXkVtBhBbKtNhapKhoRepository.findAllByIdIn(idSearchReq.getIdList());
    if (list.isEmpty()) {
      throw new Exception("Bản ghi không tồn tại");
    }
    fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(XhXkVtBhBbKtNhapKho.TABLE_NAME));
    xhXkVtBhBbKtNhapKhoRepository.deleteAll(list);
  }

  @Transactional
  public XhXkVtBhBbKtNhapKho approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
    if (StringUtils.isEmpty(statusReq.getId())) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    Optional<XhXkVtBhBbKtNhapKho> optional = xhXkVtBhBbKtNhapKhoRepository.findById(Long.valueOf(statusReq.getId()));
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    }

    String status = statusReq.getTrangThai() + optional.get().getTrangThai();
    switch (status) {
      case Contains.CHODUYET_KTVBQ + Contains.DUTHAO:
      case Contains.CHODUYET_KT + Contains.CHODUYET_KTVBQ:
      case Contains.CHODUYET_LDCC + Contains.CHODUYET_KT:
      case Contains.CHODUYET_KTVBQ + Contains.TUCHOI_KTVBQ:
      case Contains.CHODUYET_KTVBQ + Contains.TUCHOI_KT:
      case Contains.CHODUYET_KTVBQ + Contains.TUCHOI_LDCC:
        optional.get().setNguoiGduyetId(currentUser.getUser().getId());
        optional.get().setNgayGduyet(LocalDate.now());
        break;
      case Contains.TUCHOI_LDCC + Contains.CHODUYET_LDCC:
      case Contains.TUCHOI_KT + Contains.CHODUYET_KT:
      case Contains.TUCHOI_KTVBQ + Contains.CHODUYET_KTVBQ:
        optional.get().setNguoiPduyetId(currentUser.getUser().getId());
        optional.get().setNgayPduyet(LocalDate.now());
        optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
        break;
      case Contains.DADUYET_LDCC + Contains.CHODUYET_LDCC:
        optional.get().setNguoiPduyetId(currentUser.getUser().getId());
        optional.get().setNgayPduyet(LocalDate.now());
        break;
      default:
        throw new Exception("Phê duyệt không thành công");
    }
    optional.get().setTrangThai(statusReq.getTrangThai());
    XhXkVtBhBbKtNhapKho model = xhXkVtBhBbKtNhapKhoRepository.save(optional.get());
    return model;
  }

  public void export(CustomUserDetails currentUser, XhXkVtBhBbKtNhapKhoRequest objReq, HttpServletResponse response) throws Exception {
    PaggingReq paggingReq = new PaggingReq();
    paggingReq.setPage(0);
    paggingReq.setLimit(Integer.MAX_VALUE);
    objReq.setPaggingReq(paggingReq);
    Page<XhXkVtBhBbKtNhapKho> page = this.searchPage(currentUser, objReq);
    List<XhXkVtBhBbKtNhapKho> data = page.getContent();

    String title = "Danh sách biên bản kết thúc nhập kho";
    String[] rowsName = new String[]{"STT", "Số QĐ giao NVNH", "Năm báo cáo", "Điểm kho", "Lô kho",
        "Chủng loại hàng hóa", "Số BB kết thúc NK", "Ngày kết thúc NK", "Số phiếu NK", "Ngày NK", "Số BB lấy mẫu/BG mẫu",
        "Số phiếu KĐ chất lượng", "Trạng thái"};
    String fileName = "danh-sach-bien-ban-ket-thuc-nhap-kho.xlsx";
    List<Object[]> dataList = new ArrayList<Object[]>();
    Object[] objs = null;
    for (int i = 0; i < data.size(); i++) {
      XhXkVtBhBbKtNhapKho dx = data.get(i);
      objs = new Object[rowsName.length];
      objs[0] = i;
      objs[1] = dx.getSoCanCu();
      objs[2] = dx.getNamKeHoach();
      objs[3] = dx.getTenDiemKho();
      objs[4] = (dx.getTenLoKho() != null && !dx.getTenLoKho().isEmpty()) ? (dx.getTenLoKho() + ' ' + dx.getTenNganKho()) : dx.getTenNganKho();
      objs[5] = dx.getTenCloaiVthh();
      objs[6] = dx.getSoBienBan();
      objs[7] = dx.getNgayKetThucNhap();
      Object[] finalObjs = objs;
      dx.getListPhieuNhapKho().forEach(s -> {
        finalObjs[8] = s.getSoPhieu();
        finalObjs[9] = s.getNgayXuatNhap();
        finalObjs[10] = s.getSoBbLayMau();
        finalObjs[11] = s.getSoPhieuKdcl();
      });
      objs[12] = dx.getTenTrangThai();
      dataList.add(objs);
    }
    ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
    ex.export();
  }

  public void updatePhieuXk(XhXkVtBhBbKtNhapKho bbKtNhapKho, boolean xoa) {
    if (!DataUtils.isNullObject(bbKtNhapKho.getListPhieuNhapKho())) {
      bbKtNhapKho.getListPhieuNhapKho().forEach(item -> {
        if (xoa) {
          item.setIdBbKtNhapKho(null);
          item.setSoBbKtNhapKho(null);
        } else {
          item.setIdBbKtNhapKho(bbKtNhapKho.getId());
          item.setSoBbKtNhapKho(bbKtNhapKho.getSoBienBan());
        }
        xhXkVtBhPhieuXuatNhapKhoRepository.save(item);
      });
    }
    if (!DataUtils.isNullObject(bbKtNhapKho.getIdCanCu())) {
      Optional<XhXkVtBhQdGiaonvXnHdr> qdGiaonvXn = xhXkVtBhQdGiaonvXnRepository.findById(bbKtNhapKho.getIdCanCu());
      if (qdGiaonvXn.isPresent()) {
        XhXkVtBhQdGiaonvXnHdr item = qdGiaonvXn.get();
        if (xoa) {
          item.setIdBbKtNhapKho(null);
          item.setSoBbKtNhapKho(null);
        } else {
          item.setIdBbKtNhapKho(bbKtNhapKho.getId());
          item.setSoBbKtNhapKho(bbKtNhapKho.getSoBienBan());
        }
        xhXkVtBhQdGiaonvXnRepository.save(item);
      }
    }
  }
  public ReportTemplateResponse preview(HashMap<String, Object> body) throws Exception {
    try {
      String fileName = DataUtils.safeToString(body.get("tenBaoCao"));
      String fileTemplate = "xuatkhac/" + fileName;
      FileInputStream inputStream = new FileInputStream(baseReportFolder + fileTemplate);
      XhXkVtBhBbKtNhapKho detail = this.detail(DataUtils.safeToLong(body.get("id")));
      return docxToPdfConverter.convertDocxToPdf(inputStream, detail);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (XDocReportException e) {
      e.printStackTrace();
    }
    return null;
  }
}

