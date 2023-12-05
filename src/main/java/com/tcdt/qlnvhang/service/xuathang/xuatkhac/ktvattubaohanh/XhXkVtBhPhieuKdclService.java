package com.tcdt.qlnvhang.service.xuathang.xuatkhac.ktvattubaohanh;

import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.UserInfoRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.vattubaohanh.XhXkVtBhPhieuKdclRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.vattubaohanh.XhXkVtBhPhieuXuatNhapKhoRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.vattubaohanh.XhXkVtBhQdGiaonvXnRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktvattubaohanh.XhXkVtBhPhieuKdclRequest;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattubaohanh.XhXkVtBhPhieuKdclHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattubaohanh.XhXkVtBhPhieuXuatNhapKho;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattubaohanh.XhXkVtBhQdGiaonvXnDtl;
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

import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@Service
public class XhXkVtBhPhieuKdclService extends BaseServiceImpl {


  @Autowired
  private XhXkVtBhPhieuKdclRepository xhXkVtBhPhieuKdclRepository;

  @Autowired
  private XhXkVtBhQdGiaonvXnRepository xhXkVtBhQdGiaonvXnRepository;


  @Autowired
  private XhXkVtBhPhieuXuatNhapKhoRepository xhXkVtBhPhieuXuatNhapKhoRepository;

  @Autowired
  private UserInfoRepository userInfoRepository;

  @Autowired
  private FileDinhKemService fileDinhKemService;

  public Page<XhXkVtBhPhieuKdclHdr> searchPage(CustomUserDetails currentUser, XhXkVtBhPhieuKdclRequest req) throws Exception {
    String dvql = currentUser.getDvql();
    if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CHI_CUC)) {
      req.setDvql(dvql.substring(0, 6));
    } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
      req.setDvql(dvql);
    }
    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
    Page<XhXkVtBhPhieuKdclHdr> search = xhXkVtBhPhieuKdclRepository.search(req, pageable);
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    search.getContent().forEach(s -> {
      s.setMapDmucDvi(mapDmucDvi);
      s.setMapVthh(mapVthh);
//      if (s.getNguoiPduyetId() != null) {
//        s.setTenThuKho(ObjectUtils.isEmpty(s.getNguoiPduyetId()) ? null : userInfoRepository.findById(s.getNguoiPduyetId()).get().getFullName());
//      }
      s.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(s.getTrangThai()));
    });
    return search;
  }

  @Transactional
  public XhXkVtBhPhieuKdclHdr save(CustomUserDetails currentUser, XhXkVtBhPhieuKdclRequest objReq) throws Exception {
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    Optional<XhXkVtBhPhieuKdclHdr> optional = xhXkVtBhPhieuKdclRepository.findBySoPhieu(objReq.getSoPhieu());
    if (optional.isPresent()) {
      throw new Exception("Số phiếu đã tồn tại");
    }
    XhXkVtBhPhieuKdclHdr data = new XhXkVtBhPhieuKdclHdr();
    BeanUtils.copyProperties(objReq, data);
    data.setTrangThai(Contains.DUTHAO);
    data.getPhieuKdclDtl().forEach(s -> {
      s.setPhieuKdclHdr(data);
      s.setId(null);
    });
    XhXkVtBhPhieuKdclHdr created = xhXkVtBhPhieuKdclRepository.save(data);
    this.updateQdGiaoNvXh(created, false);
    List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), created.getId(), XhXkVtBhPhieuKdclHdr.TABLE_NAME);
    created.setFileDinhKems(fileDinhKems);
    return created;
  }

  @Transactional()
  public XhXkVtBhPhieuKdclHdr update(CustomUserDetails currentUser, XhXkVtBhPhieuKdclRequest objReq) throws Exception {
    if (objReq.getId() == null) {
      throw new Exception("Bad request!");
    }
    Optional<XhXkVtBhPhieuKdclHdr> optional = xhXkVtBhPhieuKdclRepository.findById(objReq.getId());
    if (!optional.isPresent()) throw new Exception("Không tồn tại bản ghi!");

    if (objReq.getSoPhieu().contains("/") && !ObjectUtils.isEmpty(objReq.getSoPhieu().split("/")[0])) {
      Optional<XhXkVtBhPhieuKdclHdr> optionalBySoPhieu = xhXkVtBhPhieuKdclRepository.findBySoPhieu(objReq.getSoPhieu());
      if (optionalBySoPhieu.isPresent() && optionalBySoPhieu.get().getId() != objReq.getId()) {
        if (!optionalBySoPhieu.isPresent()) throw new Exception("Số phiếu đã tồn tại!");
      }
    }
    XhXkVtBhPhieuKdclHdr dx = optional.get();
    dx.getPhieuKdclDtl().forEach(e -> e.setPhieuKdclHdr(null));
    BeanUtils.copyProperties(objReq, dx);
    dx.getPhieuKdclDtl().forEach(e -> e.setPhieuKdclHdr(dx));
    dx.setPhieuKdclDtl(objReq.getPhieuKdclDtl());
    XhXkVtBhPhieuKdclHdr created = xhXkVtBhPhieuKdclRepository.save(dx);
    this.updateQdGiaoNvXh(created, false);
    fileDinhKemService.delete(dx.getId(), Collections.singleton(XhXkVtBhPhieuKdclHdr.TABLE_NAME));
    //save file đính kèm
    fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKemReq(), created.getId(), XhXkVtBhPhieuKdclHdr.TABLE_NAME);
    return detail(created.getId());
  }


  @Transactional()
  public XhXkVtBhPhieuKdclHdr detail(Long id) throws Exception {
    if (ObjectUtils.isEmpty(id)) throw new Exception("Tham số không hợp lệ.");
    Optional<XhXkVtBhPhieuKdclHdr> optional = xhXkVtBhPhieuKdclRepository.findById(id);
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    XhXkVtBhPhieuKdclHdr model = optional.get();
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    List<FileDinhKem> fileDinhKem = fileDinhKemService.search(model.getId(), Arrays.asList(XhXkVtBhPhieuKdclHdr.TABLE_NAME));
    model.setFileDinhKems(fileDinhKem);
    model.setMapDmucDvi(mapDmucDvi);
    model.setMapVthh(mapVthh);
    model.setTenDvi(mapDmucDvi.get(model.getMaDvi()));
    model.setTenTrangThai(TrangThaiAllEnum.getLabelById(model.getTrangThai()));
    return model;
  }

  @Transactional
  public void delete(IdSearchReq idSearchReq) throws Exception {
    Optional<XhXkVtBhPhieuKdclHdr> optional = xhXkVtBhPhieuKdclRepository.findById(idSearchReq.getId());
    if (!optional.isPresent()) {
      throw new Exception("Bản ghi không tồn tại");
    }
    if (!optional.get().getTrangThai().equals(TrangThaiAllEnum.DU_THAO.getId())) {
      throw new Exception("Bản ghi có trạng thái khác dự thảo, không thể xóa.");
    }
    XhXkVtBhPhieuKdclHdr data = optional.get();
    this.updateQdGiaoNvXh(data, true);
    fileDinhKemService.deleteMultiple(Collections.singleton(data.getId()), Collections.singleton(XhXkVtBhPhieuKdclHdr.TABLE_NAME));
    xhXkVtBhPhieuKdclRepository.delete(data);
  }

  @Transient
  public XhXkVtBhPhieuKdclHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {

    if (StringUtils.isEmpty(statusReq.getId())) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    Optional<XhXkVtBhPhieuKdclHdr> optional = xhXkVtBhPhieuKdclRepository.findById(Long.valueOf(statusReq.getId()));
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    String status = statusReq.getTrangThai() + optional.get().getTrangThai();
    switch (status) {
      case Contains.CHO_DUYET_TP + Contains.DUTHAO:
      case Contains.CHO_DUYET_TP + Contains.TUCHOI_TP:
      case Contains.CHO_DUYET_TP + Contains.TUCHOI_LDC:
        optional.get().setNguoiGduyetId(currentUser.getUser().getId());
        optional.get().setNgayGduyet(LocalDate.now());
        break;
      case Contains.CHODUYET_LDC + Contains.CHODUYET_TP:
        optional.get().setNguoiGduyetId(currentUser.getUser().getId());
        optional.get().setTpKtbq(currentUser.getUser().getFullName());
        optional.get().setNgayGduyet(LocalDate.now());
        break;
      case Contains.TUCHOI_TP + Contains.CHO_DUYET_TP:
      case Contains.TUCHOI_LDC + Contains.CHODUYET_LDC:
        optional.get().setNguoiPduyetId(currentUser.getUser().getId());
        optional.get().setNgayPduyet(LocalDate.now());
        optional.get().setLyDoTuChoi(statusReq.getLyDoTuChoi());
        break;
      case Contains.DADUYET_LDC + Contains.CHODUYET_LDC:
        optional.get().setNguoiPduyetId(currentUser.getUser().getId());
        optional.get().setLanhDaoCuc(currentUser.getUser().getFullName());
        optional.get().setNgayPduyet(LocalDate.now());
        break;
      default:
        throw new Exception("Phê duyệt không thành công");
    }
    optional.get().setTrangThai(statusReq.getTrangThai());
    XhXkVtBhPhieuKdclHdr created = xhXkVtBhPhieuKdclRepository.save(optional.get());
    return created;
  }

  public void export(CustomUserDetails currentUser, XhXkVtBhPhieuKdclRequest objReq, HttpServletResponse response) throws Exception {
    PaggingReq paggingReq = new PaggingReq();
    paggingReq.setPage(0);
    paggingReq.setLimit(Integer.MAX_VALUE);
    objReq.setPaggingReq(paggingReq);
    Page<XhXkVtBhPhieuKdclHdr> page = this.searchPage(currentUser, objReq);
    List<XhXkVtBhPhieuKdclHdr> data = page.getContent();

    String title = "Danh sách phiếu kiểm định chất lượng";
    String[] rowsName = new String[]{"STT", "Số QĐ giao nhiệm vụ XH", "Năm KH", "Ngày lấy mẫu", "Điểm Kho",
        "Lô kho", "Chủng loại hàng hóa", "Số phiếu KĐCL", "Ngày kiểm định", "Số BB LM/BGM", "Trạng thái"};
    String fileName = "danh-sach-phieu-kiem-dinh-chat-luong.xlsx";
    List<Object[]> dataList = new ArrayList<Object[]>();
    Object[] objs = null;
    for (int i = 0; i < data.size(); i++) {
      XhXkVtBhPhieuKdclHdr dx = data.get(i);
      objs = new Object[rowsName.length];
      objs[0] = i;
      objs[1] = dx.getSoQdGiaoNvXh();
      objs[2] = dx.getNam();
      objs[3] = dx.getNgayLayMau();
      objs[4] = dx.getTenDiemKho();
      objs[5] = dx.getTenLoKho();
      objs[6] = dx.getTenCloaiVthh();
      objs[7] = dx.getSoPhieu();
      objs[8] = dx.getNgayLapPhieu();
      objs[9] = dx.getSoBbLayMau();
      objs[10] = dx.getTenTrangThai();
      dataList.add(objs);
    }
    ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
    ex.export();
  }

  public void updateQdGiaoNvXh(XhXkVtBhPhieuKdclHdr phieuKdcl, boolean xoa) {
    if (!DataUtils.isNullObject(phieuKdcl.getIdQdGiaoNvXh())) {
      Optional<XhXkVtBhQdGiaonvXnHdr> qdGiaonvXhHdr = xhXkVtBhQdGiaonvXnRepository.findById(phieuKdcl.getIdQdGiaoNvXh());
      if (qdGiaonvXhHdr.isPresent()) {
        XhXkVtBhQdGiaonvXnHdr item = qdGiaonvXhHdr.get();
        List<XhXkVtBhQdGiaonvXnDtl> qdGiaonvXhDtl = item.getQdGiaonvXhDtl();
        for (XhXkVtBhQdGiaonvXnDtl f : qdGiaonvXhDtl) {
          if (f.getMaDiaDiem().equals(phieuKdcl.getMaDiaDiem())) {
            if (xoa) {
              f.setIdPhieuKdcl(null);
              f.setSoPhieuKdcl(null);
              f.setIsDat(null);
              f.setMauBiHuy(null);
            } else {
              f.setIdPhieuKdcl(phieuKdcl.getId());
              f.setSoPhieuKdcl(phieuKdcl.getSoPhieu());
              f.setIsDat(phieuKdcl.getIsDat());
              f.setMauBiHuy(phieuKdcl.getMauBiHuy());
            }
          }
        }
        xhXkVtBhQdGiaonvXnRepository.save(item);
      }
      List<XhXkVtBhPhieuXuatNhapKho> allByIdCanCuIn = xhXkVtBhPhieuXuatNhapKhoRepository.findAllByIdCanCuIn(Arrays.asList(phieuKdcl.getIdQdGiaoNvXh()));
      if (!allByIdCanCuIn.isEmpty()) {
        allByIdCanCuIn.forEach(xuatNhapKho -> {
          if (xoa) {
            xuatNhapKho.setIdPhieuKdcl(null);
            xuatNhapKho.setSoPhieuKdcl(null);
          } else {
            xuatNhapKho.setIdPhieuKdcl(phieuKdcl.getId());
            xuatNhapKho.setSoPhieuKdcl(phieuKdcl.getSoPhieu());
          }
        });
        xhXkVtBhPhieuXuatNhapKhoRepository.saveAll(allByIdCanCuIn);
      }
    }
  }

  public ReportTemplateResponse preview(HashMap<String, Object> body) throws Exception {
    try {
      String fileName = DataUtils.safeToString(body.get("tenBaoCao"));
      String fileTemplate = "xuatkhac/" + fileName;
      FileInputStream inputStream = new FileInputStream(baseReportFolder + fileTemplate);
      XhXkVtBhPhieuKdclHdr detail = this.detail(DataUtils.safeToLong(body.get("id")));
      return docxToPdfConverter.convertDocxToPdf(inputStream, detail);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (XDocReportException e) {
      e.printStackTrace();
    }
    return null;
  }
}

