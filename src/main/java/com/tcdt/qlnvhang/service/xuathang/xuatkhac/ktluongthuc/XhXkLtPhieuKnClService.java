package com.tcdt.qlnvhang.service.xuathang.xuatkhac.ktluongthuc;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.ktluongthuc.XhXkLtPhieuKnClRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.ktluongthuc.XhXkTongHopRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktluongthuc.XhXkLtPhieuKnClRequest;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.table.report.ReportTemplateRequest;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.kthanghoa.XhXkTongHopDtl;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.kthanghoa.XhXkTongHopHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktluongthuc.XhXkLtPhieuKnClHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattu.XhXkVtPhieuKdclHdr;
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
import org.springframework.util.StringUtils;

import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@Service
public class XhXkLtPhieuKnClService extends BaseServiceImpl {

  @Autowired
  private XhXkLtPhieuKnClRepository xhXkLtPhieuKnClRepository;

  @Autowired
  private XhXkTongHopRepository xhXkTongHopRepository;

  @Autowired
  private FileDinhKemService fileDinhKemService;

  public Page<XhXkLtPhieuKnClHdr> searchPage(CustomUserDetails currentUser, XhXkLtPhieuKnClRequest req) throws Exception {
    req.setDvql(currentUser.getDvql());
    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
    Page<XhXkLtPhieuKnClHdr> search = xhXkLtPhieuKnClRepository.search(req, pageable);
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    search.getContent().forEach(s -> {
      s.setMapDmucDvi(mapDmucDvi);
      s.setMapVthh(mapVthh);
      s.setTenTrangThai(TrangThaiAllEnum.getLabelById(s.getTrangThai()));

    });
    return search;
  }

  @Transactional
  public XhXkLtPhieuKnClHdr save(CustomUserDetails currentUser, XhXkLtPhieuKnClRequest objReq) throws Exception {
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    Optional<XhXkLtPhieuKnClHdr> optional = xhXkLtPhieuKnClRepository.findBySoPhieu(objReq.getSoPhieu());
    if (optional.isPresent()) {
      throw new Exception("số biên bản đã tồn tại");
    }
    XhXkLtPhieuKnClHdr data = new XhXkLtPhieuKnClHdr();
    BeanUtils.copyProperties(objReq, data);
    data.setMaDvi(currentUser.getUser().getDepartment());
    data.setTrangThai(Contains.DUTHAO);
    data.getPhieuKnClDtl().forEach(s -> s.setPhieuKnClHdr(data));
    XhXkLtPhieuKnClHdr created = xhXkLtPhieuKnClRepository.save(data);
    this.updateTongHopDtl(created, false);
    List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), created.getId(), XhXkLtPhieuKnClHdr.TABLE_NAME);
    created.setFileDinhKems(fileDinhKems);
    return created;
  }


  @Transactional
  public XhXkLtPhieuKnClHdr update(CustomUserDetails currentUser, XhXkLtPhieuKnClRequest objReq) throws Exception {
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    Optional<XhXkLtPhieuKnClHdr> optional = xhXkLtPhieuKnClRepository.findById(objReq.getId());
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu cần sửa");
    }
    Optional<XhXkLtPhieuKnClHdr> soDx = xhXkLtPhieuKnClRepository.findBySoPhieu(objReq.getSoPhieu());
    if (soDx.isPresent()) {
      if (!soDx.get().getId().equals(objReq.getId())) {
        throw new Exception("số biên bản đã tồn tại");
      }
    }
    XhXkLtPhieuKnClHdr data = optional.get();
    BeanUtils.copyProperties(objReq, data, "id", "maDvi");
    data.getPhieuKnClDtl().forEach(s -> {
      s.setPhieuKnClHdr(data);
    });
    XhXkLtPhieuKnClHdr created = xhXkLtPhieuKnClRepository.save(data);
    this.updateTongHopDtl(created, false);
    fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(XhXkLtPhieuKnClHdr.TABLE_NAME));
    List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), created.getId(), XhXkLtPhieuKnClHdr.TABLE_NAME);
    created.setFileDinhKems(fileDinhKems);
    
    return created;
  }


  public List<XhXkLtPhieuKnClHdr> detail(List<Long> ids) throws Exception {
    if (DataUtils.isNullOrEmpty(ids)) throw new Exception("Tham số không hợp lệ.");
    List<XhXkLtPhieuKnClHdr> optional = xhXkLtPhieuKnClRepository.findByIdIn(ids);
    if (DataUtils.isNullOrEmpty(optional)) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    List<XhXkLtPhieuKnClHdr> allById = xhXkLtPhieuKnClRepository.findAllById(ids);
    allById.forEach(data -> {
      data.setMapDmucDvi(mapDmucDvi);
      data.setTenDvi(mapDmucDvi.get(data.getMaDvi()));
      data.setMapVthh(mapVthh);
      data.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(data.getTrangThai()));
      List<FileDinhKem> fileDinhKems = fileDinhKemService.search(data.getId(), Arrays.asList(XhXkLtPhieuKnClHdr.TABLE_NAME));
      data.setFileDinhKems(fileDinhKems);
     
    });

    return allById;
  }

  @Transient
  public void delete(IdSearchReq idSearchReq) throws Exception {
    Optional<XhXkLtPhieuKnClHdr> optional = xhXkLtPhieuKnClRepository.findById(idSearchReq.getId());
    if (!optional.isPresent()) {
      throw new Exception("Bản ghi không tồn tại");
    }
    XhXkLtPhieuKnClHdr data = optional.get();
    fileDinhKemService.delete(data.getId(), Lists.newArrayList(XhXkLtPhieuKnClHdr.TABLE_NAME));
    this.updateTongHopDtl(data, true);
    xhXkLtPhieuKnClRepository.delete(data);
  }

  @Transactional
  public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
    List<XhXkLtPhieuKnClHdr> list = xhXkLtPhieuKnClRepository.findAllByIdIn(idSearchReq.getIdList());

    if (list.isEmpty()) {
      throw new Exception("Bản ghi không tồn tại");
    }
    fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(XhXkLtPhieuKnClHdr.TABLE_NAME));
    xhXkLtPhieuKnClRepository.deleteAll(list);
  }

  @Transient
  public XhXkLtPhieuKnClHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {

    if (StringUtils.isEmpty(statusReq.getId())) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    Optional<XhXkLtPhieuKnClHdr> optional = xhXkLtPhieuKnClRepository.findById(Long.valueOf(statusReq.getId()));
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
    XhXkLtPhieuKnClHdr created = xhXkLtPhieuKnClRepository.save(optional.get());
    this.updateTongHopDtl(created, false);
    return created;
  }

  public void export(CustomUserDetails currentUser, XhXkLtPhieuKnClRequest objReq, HttpServletResponse response) throws Exception {
    PaggingReq paggingReq = new PaggingReq();
    paggingReq.setPage(0);
    paggingReq.setLimit(Integer.MAX_VALUE);
    objReq.setPaggingReq(paggingReq);
    Page<XhXkLtPhieuKnClHdr> page = this.searchPage(currentUser, objReq);
    List<XhXkLtPhieuKnClHdr> data = page.getContent();

    String title = "Danh sách phiếu kiểm nghiệm chất lượng ";
    String[] rowsName = new String[]{"STT", "Năm KH", "Mã DS LT <= 6 tháng hết hạn lưu kho", "Điểm Kho",  "Lô kho","Tồn kho","SL hết hạn (<= 6 tháng)",
        "DVT","Thời hạn lưu kho (tháng)", "Số BB LM/BGM", "Ngày lấy mẫu", "Số phiếu KNCL","Ngày kiểm nghiệm", "Trạng thái"};
    String fileName = "danh-sach-phieu-kiem-nghiem-chat-luong.xlsx";
    List<Object[]> dataList = new ArrayList<Object[]>();
    Object[] objs = null;
    for (int i = 0; i < data.size(); i++) {
      XhXkLtPhieuKnClHdr dx = data.get(i);
      objs = new Object[rowsName.length];
      objs[0] = i;
      objs[1] = dx.getNam();
      objs[2] = dx.getMaDanhSach();
      objs[3] = dx.getTenDiemKho();
      objs[4] = (dx.getTenLoKho() != null && !dx.getTenLoKho().isEmpty()) ? dx.getTenLoKho() : dx.getTenNganKho();
      objs[5] = dx.getSlTon();
      objs[6] = dx.getSlHetHan();
      objs[7] = dx.getDonViTinh();
      objs[8] = dx.getThoiHanLk();
      objs[9] = dx.getSoBienBan();
      objs[10] = dx.getNgayLayMau();
      objs[11] = dx.getSoPhieu();
      objs[12] = dx.getNgayKnMau();
      objs[13] = dx.getTenTrangThai();
      dataList.add(objs);
    }
    ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
    ex.export();
  }

  public void updateTongHopDtl(XhXkLtPhieuKnClHdr phieuKnCl, boolean xoa) {
    if (!DataUtils.isNullObject(phieuKnCl.getIdTongHop())) {
      Optional<XhXkTongHopHdr> listTongHop = xhXkTongHopRepository.findById(phieuKnCl.getIdTongHop());
      if (listTongHop.isPresent()) {
        XhXkTongHopHdr item = listTongHop.get();
        List<XhXkTongHopDtl> tongHopDtlList = item.getTongHopDtl();
        for (XhXkTongHopDtl f : tongHopDtlList) {
          if (f.getMaDiaDiem().equals(phieuKnCl.getMaDiaDiem())) {
            if (xoa) {
              f.setIdPhieuKnCl(null);
              f.setSoPhieuKnCl(null);
              f.setNgayLayMau(null);
              f.setTrangThaiKnCl(null);
              f.setKqThamDinh(null);
            } else {
              f.setIdPhieuKnCl(phieuKnCl.getId());
              f.setSoPhieuKnCl(phieuKnCl.getSoPhieu());
              f.setNgayKnMau(phieuKnCl.getNgayKnMau());
              f.setTrangThaiKnCl(phieuKnCl.getTrangThai());
              f.setKqThamDinh(phieuKnCl.getKqThamDinh());
            }

          }
        }
        xhXkTongHopRepository.save(item);
      }
    }
  }

  public ReportTemplateResponse preview(HashMap<String, Object> body) throws Exception {
    try {
      ReportTemplateRequest reportTemplateRequest = new ReportTemplateRequest();
      reportTemplateRequest.setFileName(DataUtils.safeToString(body.get("tenBaoCao")));
      ReportTemplate model = findByTenFile(reportTemplateRequest);
      byte[] byteArray = Base64.getDecoder().decode(model.getFileUpload());
      ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
//            FileInputStream inputStream = new FileInputStream("src/main/resources/reports/xuatcuutrovientro/Phiếu kiểm nghiệm chất lượng.docx");
      List<XhXkLtPhieuKnClHdr>  detail = this.detail(Arrays.asList(DataUtils.safeToLong(body.get("id"))));
      return docxToPdfConverter.convertDocxToPdf(inputStream, detail.get(0));
    } catch (IOException e) {
      e.printStackTrace();
    } catch (XDocReportException e) {
      e.printStackTrace();
    }
    return null;
  }
}

