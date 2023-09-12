package com.tcdt.qlnvhang.service.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.*;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.PaggingReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.SearchXhCtvtQuyetDinhPdHdr;
import com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtQuyetDinhPdHdrReq;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtDeXuatHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtQuyetDinhPdHdr;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtTongHopHdr;
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
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class XhCtvtQdPdHdrService extends BaseServiceImpl {


  @Autowired
  private XhCtvtQdPdHdrRepository xhCtvtQdPdHdrRepository;

  @Autowired
  private XhCtvtQdPdDtlRepository xhCtvtQdPdDtlRepository;

  @Autowired

  private XhCtvtQdPdDxRepository xhCtvtQdPdDxRepository;

  @Autowired
  private XhCtvtTongHopHdrRepository xhCtvtTongHopHdrRepository;

  @Autowired
  private XhCtvtDeXuatHdrRepository xhCtvtDeXuatHdrRepository;

  @Autowired
  private FileDinhKemService fileDinhKemService;


  public Page<XhCtvtQuyetDinhPdHdr> searchPage(CustomUserDetails currentUser, SearchXhCtvtQuyetDinhPdHdr req) throws Exception {
//    req.setDvql(currentUser.getDvql());
    //cuc xem cac quyet dinh tu tong cuc
    if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
      req.setMaDviDx(currentUser.getDvql());
    }

    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
    System.out.println(req);
    Page<XhCtvtQuyetDinhPdHdr> search = xhCtvtQdPdHdrRepository.search(req, pageable);
    Map<String, Map<String, Object>> mapDmucDvi = getListDanhMucDviObject(null, null, "01");

    Map<String, String> mapVthh = getListDanhMucHangHoa();
    search.getContent().forEach(s -> {
      if (mapDmucDvi.containsKey((s.getMaDvi()))) {
        Map<String, Object> objDonVi = mapDmucDvi.get(s.getMaDvi());
        s.setTenDvi(objDonVi.get("tenDvi").toString());
      }
      if (mapVthh.get((s.getLoaiVthh())) != null) {
        s.setTenLoaiVthh(mapVthh.get(s.getLoaiVthh()));
      }
      if (mapVthh.get((s.getCloaiVthh())) != null) {
        s.setTenCloaiVthh(mapVthh.get(s.getCloaiVthh()));
      }
      s.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(s.getTrangThai()));
    });
    return search;
  }

  @Transactional
  public XhCtvtQuyetDinhPdHdr save(CustomUserDetails currentUser, XhCtvtQuyetDinhPdHdrReq objReq) throws Exception {
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    if (!DataUtils.isNullObject(objReq.getSoBbQd())) {
      Optional<XhCtvtQuyetDinhPdHdr> optional = xhCtvtQdPdHdrRepository.findBySoBbQd(objReq.getSoBbQd());
      if (optional.isPresent()) {
        throw new Exception("số quyết định đã tồn tại");
      }
    }

    XhCtvtQuyetDinhPdHdr data = new XhCtvtQuyetDinhPdHdr();
    BeanUtils.copyProperties(objReq, data);
    data.setMaDvi(currentUser.getUser().getDepartment());
    data.setTrangThai(TrangThaiAllEnum.DU_THAO.getId());
    XhCtvtQuyetDinhPdHdr created = xhCtvtQdPdHdrRepository.save(data);

    if (!DataUtils.isNullOrEmpty(created.getMaTongHop())) {
      Optional<XhCtvtTongHopHdr> tongHopHdr = xhCtvtTongHopHdrRepository.findById(created.getIdTongHop());
      XhCtvtTongHopHdr tongHop = tongHopHdr.get();
      tongHop.setSoQdPd(created.getSoBbQd());
      tongHop.setIdQdPd(created.getId());
      tongHop.setNgayKyQd(created.getNgayKy());
      xhCtvtTongHopHdrRepository.save(tongHop);
    }
    if (!DataUtils.isNullOrEmpty(created.getSoDx())) {
      Optional<XhCtvtDeXuatHdr> deXuatHdr = xhCtvtDeXuatHdrRepository.findById(created.getIdDx());
      XhCtvtDeXuatHdr deXuat = deXuatHdr.get();
      deXuat.setSoQdPd(created.getSoBbQd());
      deXuat.setIdQdPd(created.getId());
      deXuat.setNgayKyQd(created.getNgayKy());
      xhCtvtDeXuatHdrRepository.save(deXuat);
    }
    return created;
  }

  @Transactional
  public XhCtvtQuyetDinhPdHdr update(CustomUserDetails currentUser, XhCtvtQuyetDinhPdHdrReq objReq) throws Exception {
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    Optional<XhCtvtQuyetDinhPdHdr> optional = xhCtvtQdPdHdrRepository.findById(objReq.getId());
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu cần sửa");
    }
    Optional<XhCtvtQuyetDinhPdHdr> soDx = xhCtvtQdPdHdrRepository.findBySoBbQd(objReq.getSoBbQd());
    if (soDx.isPresent()) {
      if (!soDx.get().getId().equals(objReq.getId())) {
        throw new Exception("số quyết định đã tồn tại");
      }
    }

    XhCtvtQuyetDinhPdHdr data = optional.get();
    BeanUtils.copyProperties(objReq, data, "id");
    XhCtvtQuyetDinhPdHdr created = xhCtvtQdPdHdrRepository.save(data);
    return created;
  }


  public List<XhCtvtQuyetDinhPdHdr> detail(List<Long> ids) throws Exception {
    if (DataUtils.isNullOrEmpty(ids)) throw new Exception("Tham số không hợp lệ.");
    List<XhCtvtQuyetDinhPdHdr> optional = xhCtvtQdPdHdrRepository.findByIdIn(ids);
    if (DataUtils.isNullOrEmpty(optional)) {
      throw new Exception("Không tìm thấy dữ liệu");
    }

    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    List<XhCtvtQuyetDinhPdHdr> allById = xhCtvtQdPdHdrRepository.findAllById(ids);
    allById.forEach(data -> {
      data.getQuyetDinhPdDtl().forEach(s -> {
        s.setMapDmucDvi(mapDmucDvi);
        s.setMapVthh(mapVthh);
      });
    });
    return allById;
  }

  @Transactional
  public void delete(IdSearchReq idSearchReq) throws Exception {
    Optional<XhCtvtQuyetDinhPdHdr> optional = xhCtvtQdPdHdrRepository.findById(idSearchReq.getId());
    if (!optional.isPresent()) {
      throw new Exception("Bản ghi không tồn tại");
    }
    XhCtvtQuyetDinhPdHdr data = optional.get();
    xhCtvtQdPdHdrRepository.delete(data);

    String maThop = data.getMaTongHop();
    if (!DataUtils.isNullOrEmpty(maThop)) {
      XhCtvtTongHopHdr tongHop = xhCtvtTongHopHdrRepository.findById(data.getIdTongHop()).orElse(null);
      if (tongHop != null) {
        tongHop.setSoQdPd(null);
        tongHop.setIdQdPd(null);
        tongHop.setNgayKyQd(null);
        xhCtvtTongHopHdrRepository.save(tongHop);
      }
    }
    String soDx = data.getSoDx();
    if (!DataUtils.isNullOrEmpty(soDx)) {
      XhCtvtDeXuatHdr deXuat = xhCtvtDeXuatHdrRepository.findById(data.getIdDx()).orElse(null);
      if (deXuat != null) {
        deXuat.setSoQdPd(null);
        deXuat.setIdQdPd(null);
        deXuat.setNgayKyQd(null);
        xhCtvtDeXuatHdrRepository.save(deXuat);
      }
    }
  }

  @Transactional
  public void deleteMulti(IdSearchReq idSearchReq) throws Exception {
    List<XhCtvtQuyetDinhPdHdr> list = xhCtvtQdPdHdrRepository.findAllByIdIn(idSearchReq.getIdList());

    if (list.isEmpty()) {
      throw new Exception("Bản ghi không tồn tại");
    }

    xhCtvtQdPdHdrRepository.deleteAll(list);
    List<Long> listIdThop = list.stream().map(XhCtvtQuyetDinhPdHdr::getIdTongHop).collect(Collectors.toList());
    if (!CollectionUtils.isEmpty(listIdThop)) {
      List<XhCtvtTongHopHdr> tongHopHdr = xhCtvtTongHopHdrRepository.findByIdIn(listIdThop);
      tongHopHdr.stream().map(item -> {
        item.setNgayKyQd(null);
        item.setIdQdPd(null);
        item.setSoQdPd(null);
        return item;
      }).collect(Collectors.toList());
    }
    List<Long> listIdDx = list.stream().map(XhCtvtQuyetDinhPdHdr::getIdDx).collect(Collectors.toList());
    if (!CollectionUtils.isEmpty(listIdThop)) {
      List<XhCtvtDeXuatHdr> deXuatHdr = xhCtvtDeXuatHdrRepository.findByIdIn(listIdDx);
      deXuatHdr.stream().map(item -> {
        item.setNgayKyQd(null);
        item.setIdQdPd(null);
        item.setSoQdPd(null);
        return item;
      }).collect(Collectors.toList());
    }
  }


  public XhCtvtQuyetDinhPdHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {

    if (StringUtils.isEmpty(statusReq.getId())) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    Optional<XhCtvtQuyetDinhPdHdr> optional = xhCtvtQdPdHdrRepository.findById(Long.valueOf(statusReq.getId()));
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    }

    String status = statusReq.getTrangThai() + optional.get().getTrangThai();
    switch (status) {
      case Contains.BAN_HANH + Contains.DUTHAO:
        optional.get().setNgayPduyet(LocalDate.now());
        optional.get().setNguoiPduyetId(currentUser.getUser().getId());
        break;
      default:
        throw new Exception("Phê duyệt không thành công");
    }
    optional.get().setTrangThai(statusReq.getTrangThai());
    XhCtvtQuyetDinhPdHdr created = xhCtvtQdPdHdrRepository.save(optional.get());
    return created;
  }


  public void export(CustomUserDetails currentUser, SearchXhCtvtQuyetDinhPdHdr objReq, HttpServletResponse response) throws Exception {
    PaggingReq paggingReq = new PaggingReq();
    paggingReq.setPage(0);
    paggingReq.setLimit(Integer.MAX_VALUE);
    objReq.setPaggingReq(paggingReq);
    Page<XhCtvtQuyetDinhPdHdr> page = this.searchPage(currentUser, objReq);
    List<XhCtvtQuyetDinhPdHdr> data = page.getContent();

    String title = "Danh sách quyết định phương án xuất cứu trợ, viện trợ ";
    String[] rowsName = new String[]{"STT", "Số quyết định", "Ngày ký quyết định", "Mã tổng hợp", "Ngày tổng hợp", "Số công văn/đề xuất", "Ngày đề xuất", "Loại hàng hóa", "Tổng SL đề xuất cứu trợ,viện trợ (kg)", "Tổng SL xuất kho cứu trợ,viện trợ (kg)", "SL xuất CT,VT chuyển sang xuất cấp", "Trích yếu", "Trạng thái quyết định",};
    String fileName = "danh-sach-phuong-an-xuat-cuu-tro-vien-tro.xlsx";
    List<Object[]> dataList = new ArrayList<Object[]>();
    Object[] objs = null;
    for (int i = 0; i < data.size(); i++) {
      XhCtvtQuyetDinhPdHdr dx = data.get(i);
      objs = new Object[rowsName.length];
      objs[0] = i;
      objs[1] = dx.getSoBbQd();
      objs[2] = dx.getNgayKy();
      objs[3] = dx.getMaTongHop();
      objs[4] = dx.getNgayThop();
      objs[5] = dx.getSoDx();
      objs[6] = dx.getNgayDx();
      objs[7] = dx.getTenLoaiVthh();
      objs[8] = dx.getTongSoLuongDx();
      objs[9] = dx.getTongSoLuong();
      objs[10] = dx.getSoLuongXuaCap();
      objs[11] = dx.getTrichYeu();
      objs[12] = dx.getTenTrangThai();
      dataList.add(objs);
    }
    ExportExcel ex = new ExportExcel(title, fileName, rowsName, dataList, response);
    ex.export();
  }

  public List<XhCtvtQuyetDinhPdHdr> searchQdPaXc(CustomUserDetails currentUser, SearchXhCtvtQuyetDinhPdHdr req) throws Exception {
    return xhCtvtQdPdHdrRepository.searchQdPaXuatCap(req);
  }

  public ReportTemplateResponse preview(HashMap<String, Object> body) throws Exception {
    try {
      String fileName = DataUtils.safeToString(body.get("tenBaoCao"));
      String fileTemplate = "xuatcuutrovientro/" + fileName;
      FileInputStream inputStream = new FileInputStream(baseReportFolder + fileTemplate);
      List<XhCtvtQuyetDinhPdHdr> detail = this.detail(Arrays.asList(DataUtils.safeToLong(body.get("id"))));
      Object children = body.get("children");
      HashMap<Object, Object> hashMap = new HashMap<>();
      hashMap.put("nam", detail.get(0).getNam());
      hashMap.put("tenLoaiVthh", detail.get(0).getTenLoaiVthh());
      hashMap.put("children", children);
      return docxToPdfConverter.convertDocxToPdf(inputStream, hashMap);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (XDocReportException e) {
      e.printStackTrace();
    }
    return null;
  }
}