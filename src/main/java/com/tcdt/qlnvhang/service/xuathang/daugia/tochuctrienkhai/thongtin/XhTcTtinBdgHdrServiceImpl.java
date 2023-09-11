package com.tcdt.qlnvhang.service.xuathang.daugia.tochuctrienkhai.thongtin;

import com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.pheduyet.XhQdPdKhBdgDtl;
import com.tcdt.qlnvhang.entities.xuathang.daugia.tochuctrienkhai.thongtin.XhTcTtinBdgDtl;
import com.tcdt.qlnvhang.entities.xuathang.daugia.tochuctrienkhai.thongtin.XhTcTtinBdgHdr;
import com.tcdt.qlnvhang.entities.xuathang.daugia.tochuctrienkhai.thongtin.XhTcTtinBdgNlq;
import com.tcdt.qlnvhang.entities.xuathang.daugia.tochuctrienkhai.thongtin.XhTcTtinBdgPlo;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.daugia.kehoach.pheduyet.XhQdPdKhBdgDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.tochuctrienkhai.thongtin.XhTcTtinBdgDtlRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.tochuctrienkhai.thongtin.XhTcTtinBdgHdrRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.tochuctrienkhai.thongtin.XhTcTtinBdgNlqRepository;
import com.tcdt.qlnvhang.repository.xuathang.daugia.tochuctrienkhai.thongtin.XhTcTtinBdgPloRepository;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.tochuctrienkhai.thongtin.ThongTinDauGiaDtlReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.tochuctrienkhai.thongtin.ThongTinDauGiaNtgReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.tochuctrienkhai.thongtin.ThongTinDauGiaPloReq;
import com.tcdt.qlnvhang.request.xuathang.daugia.tochuctrienkhai.thongtin.ThongTinDauGiaReq;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class XhTcTtinBdgHdrServiceImpl extends BaseServiceImpl {

  @Autowired
  private XhTcTtinBdgHdrRepository xhTcTtinBdgHdrRepository;
  @Autowired
  private XhTcTtinBdgDtlRepository xhTcTtinBdgDtlRepository;
  @Autowired
  private XhTcTtinBdgPloRepository xhTcTtinBdgPloRepository;
  @Autowired
  private XhTcTtinBdgNlqRepository xhTcTtinBdgNlqRepository;
  @Autowired
  private XhQdPdKhBdgDtlRepository xhQdPdKhBdgDtlRepository;

  public Page<XhTcTtinBdgHdr> searchPage(CustomUserDetails currentUser, ThongTinDauGiaReq req) throws Exception {
    String dvql = currentUser.getDvql();
    if (currentUser.getUser().getCapDvi().equals(Contains.CAP_CUC)) {
      req.setDvql(dvql);
    } else if (currentUser.getUser().getCapDvi().equals(Contains.CAP_TONG_CUC)) {
      req.setDvql(dvql.substring(0, 4));
      req.setTrangThai(Contains.HOANTHANHCAPNHAT);
    }
    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
    Page<XhTcTtinBdgHdr> search = xhTcTtinBdgHdrRepository.searchPage(req, pageable);
    Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    search.getContent().forEach(data -> {
      data.setMapVthh(mapDmucVthh);
      data.setMapDmucDvi(mapDmucDvi);
      data.setTrangThai(data.getTrangThai());
    });
    return search;
  }

  @Transactional
  public XhTcTtinBdgHdr create(CustomUserDetails currentUser, ThongTinDauGiaReq req) throws Exception {
    if (currentUser == null) throw new Exception("Bad request.");
    XhTcTtinBdgHdr data = new XhTcTtinBdgHdr();
    BeanUtils.copyProperties(req, data);
    data.setMaDvi(currentUser.getUser().getDepartment());
    data.setTrangThai(Contains.DANG_THUC_HIEN);
    data.setId(Long.valueOf(req.getMaThongBao().split("/")[0]));
    Optional<XhQdPdKhBdgDtl> optional = xhQdPdKhBdgDtlRepository.findById(data.getIdQdPdDtl());
    if (optional.isPresent()) {
      optional.get().setTrangThai(Contains.DANGCAPNHAT);
      xhQdPdKhBdgDtlRepository.save(optional.get());
    } else {
      throw new Exception("Không tìm thấy Quyết định phê duyệt kế hoạch bán đấu giá");
    }
    List<XhTcTtinBdgHdr> byIdQdPdDtl = xhTcTtinBdgHdrRepository.findByIdQdPdDtlOrderByLanDauGia(data.getIdQdPdDtl());
    data.setLanDauGia(byIdQdPdDtl.size() + 1);
    XhTcTtinBdgHdr created = xhTcTtinBdgHdrRepository.save(data);
    this.saveDetail(req, created.getId(), false);
    return created;
  }

  void saveDetail(ThongTinDauGiaReq req, Long idHdr, Boolean check) {
    if (check == true) {
      xhTcTtinBdgDtlRepository.deleteAllByIdHdr(idHdr);
    }
    for (ThongTinDauGiaDtlReq dtlReq : req.getChildren()) {
      XhTcTtinBdgDtl dtl = new XhTcTtinBdgDtl();
      BeanUtils.copyProperties(dtlReq, dtl, "id");
      dtl.setIdHdr(idHdr);
      xhTcTtinBdgDtlRepository.save(dtl);
      if (check == true) {
        xhTcTtinBdgPloRepository.deleteAllByIdDtl(dtlReq.getId());
      }
      for (ThongTinDauGiaPloReq ploReq : dtlReq.getChildren()) {
        XhTcTtinBdgPlo ploDtl = new XhTcTtinBdgPlo();
        BeanUtils.copyProperties(ploReq, ploDtl, "id");
        ploDtl.setIdDtl(dtl.getId());
        ploDtl.setId(null);
        if (req.getKetQua().equals(0) && check == true) {
          ploDtl.setSoLanTraGia(null);
          ploDtl.setDonGiaTraGia(null);
          ploDtl.setToChucCaNhan(null);
        }
        xhTcTtinBdgPloRepository.save(ploDtl);
      }
    }
    if (check == true) {
      xhTcTtinBdgNlqRepository.deleteAllByIdHdr(idHdr);
    }
    if (req.getKetQua().equals(1)) {
      for (ThongTinDauGiaNtgReq nlqReq : req.getListNguoiTgia()) {
        XhTcTtinBdgNlq nlq = new XhTcTtinBdgNlq();
        BeanUtils.copyProperties(nlqReq, nlq, "id");
        nlq.setId(null);
        nlq.setIdHdr(idHdr);
        xhTcTtinBdgNlqRepository.save(nlq);
      }
    }
  }

  @Transactional
  public XhTcTtinBdgHdr update(CustomUserDetails currentUser, ThongTinDauGiaReq req) throws Exception {
    if (currentUser == null) throw new Exception("Bad request.");
    Optional<XhTcTtinBdgHdr> optional = xhTcTtinBdgHdrRepository.findById(req.getId());
    if (!optional.isPresent()) throw new Exception("Không tìm thấy dữ liệu cần sửa");
    XhTcTtinBdgHdr data = optional.get();
    BeanUtils.copyProperties(req, data, "id", "maDvi");
    XhTcTtinBdgHdr updated = xhTcTtinBdgHdrRepository.save(data);
    this.saveDetail(req, updated.getId(), true);
    return updated;
  }

  public List<XhTcTtinBdgHdr> detail(List<Long> ids) throws Exception {
    List<XhTcTtinBdgHdr> list = xhTcTtinBdgHdrRepository.findByIdIn(ids);
    if (DataUtils.isNullOrEmpty(list)) throw new Exception("Không tìm thấy dữ liệu");
    Map<String, String> mapDmucVthh = getListDanhMucHangHoa();
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    List<XhTcTtinBdgHdr> allById = xhTcTtinBdgHdrRepository.findAllById(ids);
    for (XhTcTtinBdgHdr data : allById) {
      List<XhTcTtinBdgDtl> listDtl = xhTcTtinBdgDtlRepository.findAllByIdHdr(data.getId());
      for (XhTcTtinBdgDtl dataDtl : listDtl) {
        List<XhTcTtinBdgPlo> listPhanLo = xhTcTtinBdgPloRepository.findAllByIdDtl(dataDtl.getId());
        listPhanLo.forEach(dataPhanLo -> {
          dataPhanLo.setTenDiemKho(StringUtils.isEmpty(dataPhanLo.getMaDiemKho()) ? null : mapDmucDvi.get(dataPhanLo.getMaDiemKho()));
          dataPhanLo.setTenNhaKho(StringUtils.isEmpty(dataPhanLo.getMaNhaKho()) ? null : mapDmucDvi.get(dataPhanLo.getMaNhaKho()));
          dataPhanLo.setTenNganKho(StringUtils.isEmpty(dataPhanLo.getMaNganKho()) ? null : mapDmucDvi.get(dataPhanLo.getMaNganKho()));
          dataPhanLo.setTenLoKho(StringUtils.isEmpty(dataPhanLo.getMaLoKho()) ? null : mapDmucDvi.get(dataPhanLo.getMaLoKho()));
        });
        dataDtl.setTenDvi(StringUtils.isEmpty(dataDtl.getMaDvi()) ? null : mapDmucDvi.get(dataDtl.getMaDvi()));
        dataDtl.setChildren(listPhanLo);
      }
      data.setMapDmucDvi(mapDmucDvi);
      data.setMapVthh(mapDmucVthh);
      data.setChildren(listDtl);
      data.setListNguoiTgia(xhTcTtinBdgNlqRepository.findAllByIdHdr(data.getId()));
    }
    return allById;
  }

  @Transactional
  public void delete(IdSearchReq idSearchReq) throws Exception {
    Optional<XhTcTtinBdgHdr> optional = xhTcTtinBdgHdrRepository.findById(idSearchReq.getId());
    if (!optional.isPresent()) throw new Exception("Bản ghi không tồn tại");
    XhTcTtinBdgHdr data = optional.get();
    if (!data.getTrangThai().equals(Contains.DANG_THUC_HIEN)) {
      throw new Exception("Chỉ thực hiện xóa bản ghi ở chưa cập nhập");
    }
    List<XhTcTtinBdgDtl> listDtl = xhTcTtinBdgDtlRepository.findAllByIdHdr(data.getId());
    for (XhTcTtinBdgDtl dtl : listDtl) {
      xhTcTtinBdgPloRepository.deleteAllByIdDtl(dtl.getId());
    }
    xhTcTtinBdgDtlRepository.deleteAllByIdHdr(data.getId());
    xhTcTtinBdgNlqRepository.deleteAllByIdHdr(data.getId());
    xhTcTtinBdgHdrRepository.delete(data);
  }

  public XhTcTtinBdgHdr approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception {
    if (currentUser == null) throw new Exception("Bad request.");
    if (StringUtils.isEmpty(statusReq.getId())) throw new Exception("Không tìm thấy dữ liệu");
    Optional<XhTcTtinBdgHdr> optional = xhTcTtinBdgHdrRepository.findById(Long.valueOf(statusReq.getId()));
    if (!optional.isPresent()) throw new Exception("Không tìm thấy dữ liệu");
    XhTcTtinBdgHdr data = optional.get();
    String status = statusReq.getTrangThai() + data.getTrangThai();
    switch (status) {
      case Contains.DA_HOAN_THANH + Contains.DANG_THUC_HIEN:
        data.setNguoiPduyetId(currentUser.getUser().getId());
        data.setNgayPduyet(LocalDate.now());
        break;
      default:
        throw new Exception("Phê duyệt không thành công");
    }
    data.setTrangThai(statusReq.getTrangThai());
    XhTcTtinBdgHdr created = xhTcTtinBdgHdrRepository.save(data);
    if (statusReq.getTrangThai().equals(Contains.DA_HOAN_THANH)) {
      Optional<XhQdPdKhBdgDtl> dauGia = xhQdPdKhBdgDtlRepository.findById(data.getIdQdPdDtl());
      XhQdPdKhBdgDtl dauGiaDtl = dauGia.get();
      if (dauGia.isPresent()) {
        int slDviTsan = dauGiaDtl.getSlDviTsan();
        BigDecimal soDviTsanThanhCong = BigDecimal.ZERO;
        BigDecimal soDviTsanKhongThanh = BigDecimal.ZERO;
        soDviTsanThanhCong = xhQdPdKhBdgDtlRepository.countSlDviTsanThanhCong(data.getIdQdPdDtl(), data.getMaDvi());
        soDviTsanKhongThanh = new BigDecimal(slDviTsan).subtract(soDviTsanThanhCong);
        dauGiaDtl.setSoDviTsanThanhCong(soDviTsanThanhCong);
        dauGiaDtl.setSoDviTsanKhongThanh(soDviTsanKhongThanh);
        dauGiaDtl.setKetQuaDauGia(dauGiaDtl.getSoDviTsanThanhCong() + "/" + dauGiaDtl.getSlDviTsan());
        xhQdPdKhBdgDtlRepository.save(dauGiaDtl);
      }
    }
    return created;
  }

  public ReportTemplateResponse preview(HashMap<String, Object> body) throws Exception {
    try {
      Map<String, Map<String, Object>> mapDmucDvi = getListDanhMucDviObject(null, null, "01");
      String maDviCuc = body.get("maDvi").toString().substring(0, 6);
      if (mapDmucDvi.containsKey((maDviCuc))) {
        Map<String, Object> objDonVi = mapDmucDvi.get(maDviCuc);
        body.put("tenDvi", objDonVi.get("tenDvi").toString().toUpperCase());
      }
      FileInputStream inputStream = new FileInputStream(baseReportFolder + "/bandaugia/Thông tin đấu giá.docx");
      return docxToPdfConverter.convertDocxToPdf(inputStream, body);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}

