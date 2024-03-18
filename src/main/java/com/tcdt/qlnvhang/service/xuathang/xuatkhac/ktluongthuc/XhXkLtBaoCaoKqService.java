package com.tcdt.qlnvhang.service.xuathang.xuatkhac.ktluongthuc;

import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.ktluongthuc.XhXkLtBaoCaoKqRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatkhac.ktluongthuc.XhXkTongHopRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.xuatkhac.ktluongthuc.XhXkLtBaoCaoKqRequest;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.report.ReportTemplate;
import com.tcdt.qlnvhang.table.report.ReportTemplateRequest;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.kthanghoa.XhXkTongHopHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktluongthuc.XhXkLtBaoCaoKq;
import com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktluongthuc.XhXkLtBbLayMauHdr;
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
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@Service
public class XhXkLtBaoCaoKqService extends BaseServiceImpl {


  @Autowired
  private XhXkLtBaoCaoKqRepository xhXkLtBaoCaoKqRepository;

  @Autowired
  private XhXkTongHopRepository xhXkTongHopRepository;

  @Autowired
  private FileDinhKemService fileDinhKemService;

  public Page<XhXkLtBaoCaoKq> searchPage(CustomUserDetails currentUser, XhXkLtBaoCaoKqRequest req) throws Exception {
    req.setDvql(currentUser.getDvql());
    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
    Page<XhXkLtBaoCaoKq> search = xhXkLtBaoCaoKqRepository.search(req, pageable);
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    search.getContent().forEach(s -> {
      s.setTenDvi(mapDmucDvi.get(s.getMaDvi()));
      s.setTenDviNhan(mapDmucDvi.get(s.getTenDviNhan()));
      s.setTenTrangThai(TrangThaiAllEnum.getLabelById(s.getTrangThai()));
    });
    return search;
  }

  @Transactional
  public XhXkLtBaoCaoKq save(CustomUserDetails currentUser, XhXkLtBaoCaoKqRequest objReq) throws Exception {
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    Optional<XhXkLtBaoCaoKq> optional = xhXkLtBaoCaoKqRepository.findBySoBaoCao(objReq.getSoBaoCao());
    if (optional.isPresent()) {
      throw new Exception("số biên bản đã tồn tại");
    }
    XhXkLtBaoCaoKq data = new XhXkLtBaoCaoKq();
    BeanUtils.copyProperties(objReq, data);
    data.setMaDvi(currentUser.getUser().getDepartment());
    data.setTrangThai(Contains.DUTHAO);
    XhXkLtBaoCaoKq created = xhXkLtBaoCaoKqRepository.save(data);
    if (!DataUtils.isNullObject(data.getIdTongHop())) {
//      List<Long> idTongHopList = Collections.singletonList(data.getIdTongHop());
//      data.setListIdTongHop(idTongHopList);
    }
    this.updateTongHop(created, false);
    List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), created.getId(), XhXkLtBaoCaoKq.TABLE_NAME);
    created.setFileDinhKems(fileDinhKems);
    List<FileDinhKem> fileDinhKemsBc = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKemsBc(), created.getId(), XhXkLtBaoCaoKq.TABLE_NAME + "_BC");
    created.setFileDinhKems(fileDinhKemsBc);
    return created;
  }


  @Transactional
  public XhXkLtBaoCaoKq update(CustomUserDetails currentUser, XhXkLtBaoCaoKqRequest objReq) throws Exception {
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    Optional<XhXkLtBaoCaoKq> optional = xhXkLtBaoCaoKqRepository.findById(objReq.getId());
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu cần sửa");
    }
    Optional<XhXkLtBaoCaoKq> soDx = xhXkLtBaoCaoKqRepository.findBySoBaoCao(objReq.getSoBaoCao());
    if (soDx.isPresent()) {
      if (!soDx.get().getId().equals(objReq.getId())) {
        throw new Exception("số biên bản đã tồn tại");
      }
    }
    XhXkLtBaoCaoKq data = optional.get();
    BeanUtils.copyProperties(objReq, data, "id", "maDvi");
    XhXkLtBaoCaoKq created = xhXkLtBaoCaoKqRepository.save(data);
    if (!DataUtils.isNullObject(data.getIdTongHop())) {
//      List<Long> idTongHopList = Collections.singletonList(data.getIdTongHop());
//      data.setListIdTongHop(idTongHopList);
    }
    this.updateTongHop(created, false);
    fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(XhXkLtBaoCaoKq.TABLE_NAME));
    List<FileDinhKem> fileDinhKems = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), created.getId(), XhXkLtBaoCaoKq.TABLE_NAME);
    created.setFileDinhKems(fileDinhKems);
    fileDinhKemService.delete(objReq.getId(), Lists.newArrayList(XhXkLtBaoCaoKq.TABLE_NAME+"_BC"));
    List<FileDinhKem> fileDinhKemsBc = fileDinhKemService.saveListFileDinhKem(objReq.getFileDinhKems(), created.getId(), XhXkLtBaoCaoKq.TABLE_NAME+"_BC");
    created.setFileDinhKems(fileDinhKemsBc);
    return created;
  }


  public List<XhXkLtBaoCaoKq> detail(List<Long> ids) throws Exception {
    if (DataUtils.isNullOrEmpty(ids)) throw new Exception("Tham số không hợp lệ.");
    List<XhXkLtBaoCaoKq> optional = xhXkLtBaoCaoKqRepository.findByIdIn(ids);
    if (DataUtils.isNullOrEmpty(optional)) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    List<XhXkLtBaoCaoKq> allById = xhXkLtBaoCaoKqRepository.findAllById(ids);
    allById.forEach(data -> {
      data.setTenDvi(mapDmucDvi.get(data.getMaDvi()));
      data.setTenDviNhan(mapDmucDvi.get(data.getTenDviNhan()));
      data.setTenTrangThai(TrangThaiAllEnum.getLabelById(data.getTrangThai()));
      List<FileDinhKem> fileDinhKems = fileDinhKemService.search(data.getId(), Arrays.asList(XhXkLtBaoCaoKq.TABLE_NAME));
      data.setFileDinhKems(fileDinhKems);
      List<FileDinhKem> fileDinhKemsBc = fileDinhKemService.search(data.getId(), Arrays.asList(XhXkLtBaoCaoKq.TABLE_NAME+"_BC"));
      data.setFileDinhKems(fileDinhKemsBc);
      if (!DataUtils.isNullObject(data.getIdTongHop())) {
//        List<Long> idTongHopList = Collections.singletonList(data.getIdTongHop());
//        data.setListIdTongHop(idTongHopList);
      }
    });

    return allById;
  }

  @Transient
  public void delete(IdSearchReq idSearchReq) throws Exception {
    Optional<XhXkLtBaoCaoKq> optional = xhXkLtBaoCaoKqRepository.findById(idSearchReq.getId());
    if (!optional.isPresent()) {
      throw new Exception("Bản ghi không tồn tại");
    }
    XhXkLtBaoCaoKq data = optional.get();
    fileDinhKemService.delete(data.getId(), Lists.newArrayList(XhXkLtBaoCaoKq.TABLE_NAME));
    fileDinhKemService.delete(data.getId(), Lists.newArrayList(XhXkLtBaoCaoKq.TABLE_NAME+"_BC"));
    this.updateTongHop(data, true);
    xhXkLtBaoCaoKqRepository.delete(data);
  }

  @Transactional
  public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
    List<XhXkLtBaoCaoKq> list = xhXkLtBaoCaoKqRepository.findAllByIdIn(idSearchReq.getIdList());

    if (list.isEmpty()) {
      throw new Exception("Bản ghi không tồn tại");
    }
    fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(XhXkLtBaoCaoKq.TABLE_NAME));
    fileDinhKemService.deleteMultiple(idSearchReq.getIdList(), Lists.newArrayList(XhXkLtBaoCaoKq.TABLE_NAME+"_BC"));
    xhXkLtBaoCaoKqRepository.deleteAll(list);
  }

  @Transient
  public XhXkLtBaoCaoKq approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {

    if (StringUtils.isEmpty(statusReq.getId())) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    Optional<XhXkLtBaoCaoKq> optional = xhXkLtBaoCaoKqRepository.findById(Long.valueOf(statusReq.getId()));
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
    XhXkLtBaoCaoKq created = xhXkLtBaoCaoKqRepository.save(optional.get());
    this.updateTongHop(created, false);
    return created;
  }

  public void export(CustomUserDetails currentUser, XhXkLtBaoCaoKqRequest objReq, HttpServletResponse response) throws Exception {
    PaggingReq paggingReq = new PaggingReq();
    paggingReq.setPage(0);
    paggingReq.setLimit(Integer.MAX_VALUE);
    objReq.setPaggingReq(paggingReq);
    Page<XhXkLtBaoCaoKq> page = this.searchPage(currentUser, objReq);
    List<XhXkLtBaoCaoKq> data = page.getContent();

    String title = "Danh sách báo cáo kết quả kiểm định mẫu ";
    String[] rowsName = new String[]{"STT", "Năm báo cáo","Số báo cáo","Tên báo cáo","Ngày báo cáo", "Mã DS LT <= 6 tháng hết hạn lưu kho", "Trạng thái"};
    String fileName = "danh-sach-bao-cao-ket-qua-kiem-dinh-mau.xlsx";
    List<Object[]> dataList = new ArrayList<Object[]>();
    Object[] objs = null;
    for (int i = 0; i < data.size(); i++) {
      XhXkLtBaoCaoKq dx = data.get(i);
      objs = new Object[rowsName.length];
      objs[0] = i;
      objs[1] = dx.getNam();
      objs[2] = dx.getSoBaoCao();
      objs[3] = dx.getTenBaoCao();
      objs[4] = dx.getNgayBaoCao();
      objs[5] = dx.getMaDanhSach();
      objs[6] = dx.getTenTrangThai();
      dataList.add(objs);
    }
    ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
    ex.export();
  }

  public void updateTongHop(XhXkLtBaoCaoKq baoCao, boolean xoa) {
    if (!DataUtils.isNullObject(baoCao.getIdTongHop())) {
      Long[] idCanCu = Arrays.stream(baoCao.getIdTongHop().split(","))
          .map(String::trim)
          .map(Long::valueOf)
          .toArray(Long[]::new);
      List<XhXkTongHopHdr> listTongHop = xhXkTongHopRepository.findByIdIn(Arrays.asList(idCanCu));
      for (XhXkTongHopHdr xhXkTongHopHdr: listTongHop){
        if (xoa){
          xhXkTongHopHdr.setIdBaoCao(null);
          xhXkTongHopHdr.setSoBaoCao(null);
        }else {
          xhXkTongHopHdr.setIdBaoCao(baoCao.getId());
          xhXkTongHopHdr.setSoBaoCao(baoCao.getSoBaoCao());
        }
        xhXkTongHopRepository.save(xhXkTongHopHdr);
      }
    }
  }

  public ReportTemplateResponse preview(HashMap<String, Object> body) throws Exception {
    try {
      String fileName = DataUtils.safeToString(body.get("tenBaoCao"));
      String fileTemplate = "xuatkhac/luongthuc/" + fileName;
      FileInputStream inputStream = new FileInputStream(baseReportFolder + fileTemplate);
      List<XhXkLtBaoCaoKq>  detail = this.detail(Arrays.asList(DataUtils.safeToLong(body.get("id"))));
      Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
      Map<String, String> mapVthh = getListDanhMucHangHoa();
      Long[] idCanCu = Arrays.stream(detail.get(0).getIdTongHop().split(","))
          .map(String::trim)
          .map(Long::valueOf)
          .toArray(Long[]::new);
      List<XhXkTongHopHdr> tongHopHdrList = xhXkTongHopRepository.findByIdIn(Arrays.asList(idCanCu));
      for (XhXkTongHopHdr xhXkTongHopHdr : tongHopHdrList) {
        xhXkTongHopHdr.getTongHopDtl().forEach(tongHopDtl -> {
          tongHopDtl.setMapDmucDvi(mapDmucDvi);
          tongHopDtl.setMapVthh(mapVthh);
        });
      }
      return docxToPdfConverter.convertDocxToPdf(inputStream, detail.get(0),tongHopHdrList);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (XDocReportException e) {
      e.printStackTrace();
    }
    return null;
  }
}

