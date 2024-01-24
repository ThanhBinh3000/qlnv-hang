package com.tcdt.qlnvhang.service.xuathang.xuatkhac.ktvattubaohanh;

import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.vattubaohanh.*;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattubaohanh.XhXkVtBhBaoCaoKdmRequest;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattubaohanh.XhXkVtBhBaoCaoKdm;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattubaohanh.XhXkVtBhPhieuKdclHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattubaohanh.XhXkVtBhPhieuKtclHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattubaohanh.XhXkVtBhPhieuXuatNhapKho;
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

import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class XhXkVtBhBaoCaoKdmService extends BaseServiceImpl {


  @Autowired
  private XhXkVtBhBaoCaoKdmRepository xhXkVtBhBaoCaoKdmRepository;

  @Autowired
  private XhXkVtBhQdGiaonvXnRepository xhXkVtBhQdGiaonvXnRepository;

  @Autowired
  private XhXkVtBhQdGiaonvXnService xhXkVtBhQdGiaonvXnService;

  @Autowired
  private XhXkVtBhPhieuKtclService xhXkVtBhPhieuKtclService;


  @Autowired
  private XhXkVtBhPhieuXuatNhapKhoRepository xhXkVtBhPhieuXuatNhapKhoRepository;

  @Autowired
  private XhXkVtBhPhieuKtclRepository xhXkVtBhPhieuKtclRepository;

  @Autowired
  private XhXkVtBhPhieuKdclRepository xhXkVtBhPhieuKdclRepository;


  @Autowired
  private UserInfoRepository userInfoRepository;

  @Autowired
  private FileDinhKemService fileDinhKemService;

  public Page<XhXkVtBhBaoCaoKdm> searchPage(CustomUserDetails currentUser, XhXkVtBhBaoCaoKdmRequest req) throws Exception {
    req.setDvql(currentUser.getDvql());
    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
    Page<XhXkVtBhBaoCaoKdm> search = xhXkVtBhBaoCaoKdmRepository.search(req, pageable);
        Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
        Map<String, String> mapVthh = getListDanhMucHangHoa();
    search.getContent().forEach(f -> {
      f.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(f.getTrangThai()));
      List<XhXkVtBhPhieuKdclHdr> listPhieuKdcl = xhXkVtBhPhieuKdclRepository.findAllByIdBaoCaoKdm(f.getId());
      listPhieuKdcl.forEach(s -> {
        s.setTenTrangThai(TrangThaiAllEnum.getLabelById(s.getTrangThai()));
        s.setMapVthh(mapVthh);
        s.setMapDmucDvi(mapDmucDvi);
      });
      f.setMapDmucDvi(mapDmucDvi);
      f.setPhieuKdcl(listPhieuKdcl);
    });
    return search;
  }

  @Transactional
  public XhXkVtBhBaoCaoKdm save(CustomUserDetails currentUser, XhXkVtBhBaoCaoKdmRequest objReq) throws Exception {
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    Optional<XhXkVtBhBaoCaoKdm> optional = xhXkVtBhBaoCaoKdmRepository.findBySoBaoCao(objReq.getSoBaoCao());
    if (optional.isPresent()) {
      throw new Exception("Số báo cáo đã tồn tại");
    }
    XhXkVtBhBaoCaoKdm data = new XhXkVtBhBaoCaoKdm();
    BeanUtils.copyProperties(objReq, data);
    data.setTrangThai(Contains.DUTHAO);
    XhXkVtBhBaoCaoKdm created = xhXkVtBhBaoCaoKdmRepository.save(data);
    List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), created.getId(), XhXkVtBhBaoCaoKdm.TABLE_NAME);
    created.setFileDinhKems(fileDinhKems);

    //lưu lại số báo cáo vào qd giao nv xh
    Long[] idCanCu = Arrays.stream(objReq.getIdCanCu().split(","))
        .map(String::trim)
        .map(Long::valueOf)
        .toArray(Long[]::new);
    updateSoBc(created, idCanCu);
    return created;
  }

  @Transactional()
  public XhXkVtBhBaoCaoKdm update(CustomUserDetails currentUser, XhXkVtBhBaoCaoKdmRequest objReq) throws Exception {
    if (objReq.getId() == null) {
      throw new Exception("Bad request!");
    }
    Optional<XhXkVtBhBaoCaoKdm> optional = xhXkVtBhBaoCaoKdmRepository.findById(objReq.getId());
    if (!optional.isPresent()) throw new Exception("Không tồn tại bản ghi!");

    if (objReq.getSoBaoCao().contains("/") && !ObjectUtils.isEmpty(objReq.getSoBaoCao().split("/")[0])) {
      Optional<XhXkVtBhBaoCaoKdm> optionalBySoBb = xhXkVtBhBaoCaoKdmRepository.findBySoBaoCao(objReq.getSoBaoCao());
      if (optionalBySoBb.isPresent() && optionalBySoBb.get().getId() != objReq.getId()) {
        if (!optionalBySoBb.isPresent()) throw new Exception("Số báo cáo đã tồn tại!");
      }
    }
    XhXkVtBhBaoCaoKdm data = optional.get();
    BeanUtils.copyProperties(objReq, data);
    XhXkVtBhBaoCaoKdm created = xhXkVtBhBaoCaoKdmRepository.save(data);
    fileDinhKemService.delete(data.getId(), Collections.singleton(XhXkVtBhBaoCaoKdm.TABLE_NAME));
    //save file đính kèm
    fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), created.getId(), XhXkVtBhBaoCaoKdm.TABLE_NAME);
    //lưu lại số báo cáo vào qd giao nv xh
    Long[] idCanCu = Arrays.stream(objReq.getIdCanCu().split(","))
        .map(String::trim)
        .map(Long::valueOf)
        .toArray(Long[]::new);
    updateSoBc(created, idCanCu);
    return detail(created.getId());
  }

  @Transactional()
  public void updateSoBc(XhXkVtBhBaoCaoKdm data, Long[] idCanCu) {
    if (data.getLoaiCanCu().equals(Contains.QD_GNV)) {
      List<XhXkVtBhPhieuXuatNhapKho> allByIdCanCuIn = xhXkVtBhPhieuXuatNhapKhoRepository.findAllByIdCanCuIn(Arrays.asList(idCanCu));
      if (!allByIdCanCuIn.isEmpty()) {
        allByIdCanCuIn.forEach(item -> {
          item.setSoBcKqkdMau(data.getSoBaoCao());
          item.setIdBcKqkdMau(data.getId());
        });
        xhXkVtBhPhieuXuatNhapKhoRepository.saveAll(allByIdCanCuIn);
      }
      List<XhXkVtBhPhieuKdclHdr> listPhieuKdcl = xhXkVtBhPhieuKdclRepository.findByIdQdGiaoNvXhIn(Arrays.asList(idCanCu));
      if (!listPhieuKdcl.isEmpty()) {
        listPhieuKdcl.forEach(item -> {
          item.setSoBaoCaoKdm(data.getSoBaoCao());
          item.setIdBaoCaoKdm(data.getId());
        });
        xhXkVtBhPhieuKdclRepository.saveAll(listPhieuKdcl);
      }
    } else {
      List<XhXkVtBhPhieuKtclHdr> listPhieuKtcl = xhXkVtBhPhieuKtclRepository.findByIdIn(Arrays.asList(idCanCu));
      if (!listPhieuKtcl.isEmpty()) {
        listPhieuKtcl.forEach(item -> {
          item.setSoBaoCaoKdm(data.getSoBaoCao());
          item.setIdBaoCaoKdm(data.getId());
        });
        xhXkVtBhPhieuKtclRepository.saveAll(listPhieuKtcl);
      }
    }
  }

  @Transactional()
  public XhXkVtBhBaoCaoKdm detail(Long id) throws Exception {
    if (ObjectUtils.isEmpty(id)) throw new Exception("Tham số không hợp lệ.");
    Optional<XhXkVtBhBaoCaoKdm> optional = xhXkVtBhBaoCaoKdmRepository.findById(id);
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    XhXkVtBhBaoCaoKdm model = optional.get();
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    List<FileDinhKem> fileDinhKem = fileDinhKemService.search(model.getId(), Arrays.asList(XhXkVtBhBaoCaoKdm.TABLE_NAME));
    model.setFileDinhKems(fileDinhKem);
    model.setTenDvi(mapDmucDvi.get(model.getMaDvi()));
    model.setTenTrangThai(TrangThaiAllEnum.getLabelById(model.getTrangThai()));
    Long[] listIdCanCu = Arrays.stream(model.getIdCanCu().split(","))
        .map(String::trim)
        .map(Long::valueOf)
        .toArray(Long[]::new);
    if (model.getLoaiCanCu().equals(Contains.QD_GNV)) {
      List<XhXkVtBhPhieuKdclHdr> listPhieuKdcl = xhXkVtBhPhieuKdclRepository.findAllByIdBaoCaoKdm(model.getId());
      listPhieuKdcl.forEach(s -> {
        s.setTenTrangThai(TrangThaiAllEnum.getLabelById(s.getTrangThai()));
        s.setMapVthh(mapVthh);
        s.setMapDmucDvi(mapDmucDvi);
      });
      model.setPhieuKdcl(listPhieuKdcl);
    } else {
      List<XhXkVtBhPhieuKtclHdr> listPhieuKtcl = xhXkVtBhPhieuKtclRepository.findByIdIn(Arrays.asList(listIdCanCu));
      listPhieuKtcl.forEach(s -> {
        s.setMapVthh(mapVthh);
        s.setMapDmucDvi(mapDmucDvi);
      });
      model.setPhieuKtcl(listPhieuKtcl);
    }
    return model;
  }

  @Transactional
  public void delete(IdSearchReq idSearchReq) throws Exception {
    Optional<XhXkVtBhBaoCaoKdm> optional = xhXkVtBhBaoCaoKdmRepository.findById(idSearchReq.getId());
    if (!optional.isPresent()) {
      throw new Exception("Bản ghi không tồn tại");
    }
    if (optional.get().getTrangThai().equals(TrangThaiAllEnum.DU_THAO.getId()) || optional.get().getTrangThai().equals(TrangThaiAllEnum.TU_CHOI_LDC.getId())) {
      XhXkVtBhBaoCaoKdm data = optional.get();
      fileDinhKemService.deleteMultiple(Collections.singleton(data.getId()), Collections.singleton(XhXkVtBhBaoCaoKdm.TABLE_NAME));
      //update mẫu bị hủy cho phiếu xuất kho
      //lưu lại số báo cáo vào qd giao nv xh
      Long[] idCanCu = Arrays.stream(data.getIdCanCu().split(","))
          .map(String::trim)
          .map(Long::valueOf)
          .toArray(Long[]::new);
      if (data.getLoaiCanCu().equals(Contains.QD_GNV)) {
        List<XhXkVtBhPhieuXuatNhapKho> allByIdCanCuIn = xhXkVtBhPhieuXuatNhapKhoRepository.findAllByIdCanCuIn(Arrays.asList(idCanCu));
        if (!allByIdCanCuIn.isEmpty()) {
          allByIdCanCuIn.forEach(item -> {
            item.setSoBcKqkdMau(null);
            item.setIdBcKqkdMau(null);
          });
          xhXkVtBhPhieuXuatNhapKhoRepository.saveAll(allByIdCanCuIn);
        }
        List<XhXkVtBhPhieuKdclHdr> listPhieuKdcl = xhXkVtBhPhieuKdclRepository.findAllByIdBaoCaoKdm(data.getId());
        if (!listPhieuKdcl.isEmpty()) {
          listPhieuKdcl.forEach(item -> {
            item.setSoBaoCaoKdm(null);
            item.setIdBaoCaoKdm(null);
          });
          xhXkVtBhPhieuKdclRepository.saveAll(listPhieuKdcl);
        }
      } else {
        List<XhXkVtBhPhieuKtclHdr> listPhieuKtcl = xhXkVtBhPhieuKtclRepository.findByIdIn(Arrays.asList(idCanCu));
        listPhieuKtcl.forEach(item -> {
          item.setSoBaoCaoKdm(null);
          item.setIdBaoCaoKdm(null);
        });
        xhXkVtBhPhieuKtclRepository.saveAll(listPhieuKtcl);
      }

      xhXkVtBhBaoCaoKdmRepository.delete(data);
    } else {
      throw new Exception("Bản ghi đang ở trạng thái " + TrangThaiAllEnum.getLabelById(optional.get().getTrangThai()) + " không thể xóa.");
    }
  }

  @Transient
  public XhXkVtBhBaoCaoKdm approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {

    if (StringUtils.isEmpty(statusReq.getId())) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    Optional<XhXkVtBhBaoCaoKdm> optional = xhXkVtBhBaoCaoKdmRepository.findById(Long.valueOf(statusReq.getId()));
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    String status = statusReq.getTrangThai() + optional.get().getTrangThai();
    switch (status) {
      case Contains.CHODUYET_TP + Contains.DUTHAO:
      case Contains.CHODUYET_LDC + Contains.CHODUYET_TP:
      case Contains.CHODUYET_TP + Contains.TUCHOI_TP:
      case Contains.CHODUYET_TP + Contains.TUCHOI_LDC:
        optional.get().setNguoiGduyetId(currentUser.getUser().getId());
        optional.get().setNgayGduyet(LocalDate.now());
        break;
      case Contains.TUCHOI_TP + Contains.CHODUYET_TP:
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
    XhXkVtBhBaoCaoKdm created = xhXkVtBhBaoCaoKdmRepository.save(optional.get());
    return created;
  }

  public void export(CustomUserDetails currentUser, XhXkVtBhBaoCaoKdmRequest objReq, HttpServletResponse response) throws Exception {
    PaggingReq paggingReq = new PaggingReq();
    paggingReq.setPage(0);
    paggingReq.setLimit(Integer.MAX_VALUE);
    objReq.setPaggingReq(paggingReq);
    Page<XhXkVtBhBaoCaoKdm> page = this.searchPage(currentUser, objReq);
    List<XhXkVtBhBaoCaoKdm> data = page.getContent();

    String title = "Danh sách báo cáo kết quả kiểm định";
    String[] rowsName = new String[]{"STT", "Năm báo cáo", "Đơn vị gửi báo cáo", "Số báo cáo", "Tên báo cáo", "Ngày báo cáo", "Số QĐ giao NVXH để lấy mẫu", "Số QĐ xuất giảm VT của Tổng Cục",
        "Số QĐ giao NV xuất giảm VT của cục", "Trạng thái"};
    String fileName = "danh-sach-bao-cao-ket-qua-kiem-dinh.xlsx";
    List<Object[]> dataList = new ArrayList<Object[]>();
    Object[] objs = null;
    for (int i = 0; i < data.size(); i++) {
      XhXkVtBhBaoCaoKdm dx = data.get(i);
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

  public ReportTemplateResponse preview(HashMap<String, Object> body) throws Exception {
    try {
      String fileName = DataUtils.safeToString(body.get("tenBaoCao"));
      String fileTemplate = "xuatkhac/" + fileName;
      FileInputStream inputStream = new FileInputStream(baseReportFolder + fileTemplate);
      XhXkVtBhBaoCaoKdm detail = this.detail(DataUtils.safeToLong(body.get("id")));
      Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
      Map<String, String> mapVthh = getListDanhMucHangHoa();
      List<XhXkVtBhPhieuKdclHdr> qdDtl = null;
      List<XhXkVtBhPhieuKtclHdr> pktcl = null;
      Long[] idCanCu = Arrays.stream(detail.getIdCanCu().split(","))
          .map(String::trim)
          .map(Long::valueOf)
          .toArray(Long[]::new);
      if (detail.getLoaiCanCu().equals(Contains.QD_GNV)) {
        qdDtl = xhXkVtBhPhieuKdclRepository.findAllByIdBaoCaoKdm(detail.getId()).stream().map(item -> {
          item.setMapVthh(mapVthh);
          item.setTenDvi(mapDmucDvi.get(item.getMaDvi()));
          return item;
        }).collect(Collectors.toList());
      } else {
        pktcl = xhXkVtBhPhieuKtclRepository.findByIdIn(Arrays.asList(idCanCu)).stream().map(item -> {
          item.setMapVthh(mapVthh);
          item.setTenDvi(mapDmucDvi.get(item.getMaDvi()));
          return item;
        }).collect(Collectors.toList());
      }
      return docxToPdfConverter.convertDocxToPdf(inputStream, detail, qdDtl, pktcl);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (XDocReportException e) {
      e.printStackTrace();
    }
    return null;
  }
}

