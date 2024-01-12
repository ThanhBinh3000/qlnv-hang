package com.tcdt.qlnvhang.service.suachuahang.impl;

import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.suachuahang.ScDanhSachRepository;
import com.tcdt.qlnvhang.repository.xuathang.suachuahang.ScPhieuNhapKhoHdrRepository;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlDanhSachRequest;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScDanhSachHdr;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScPhieuNhapKhoHdr;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ScDanhSachServiceImpl extends BaseServiceImpl {


  @Autowired
  private ScDanhSachRepository scDanhSachRepository;

  @Autowired
  private ScPhieuNhapKhoHdrRepository scPhieuNhapKhoHdrRepository;

  @Autowired
  private FileDinhKemService fileDinhKemService;

  public Page<ScDanhSachHdr> searchPage(CustomUserDetails currentUser, XhTlDanhSachRequest req) throws Exception {
    String dvql = currentUser.getDvql();
    if (!currentUser.getUser().getCapDvi().equals(Contains.CAP_TONG_CUC)) {
      req.setDvql(dvql.substring(0, 6));
    }
    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
    Page<ScDanhSachHdr> search = scDanhSachRepository.searchPage(req, pageable);

    //set label
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    search.getContent().forEach(s -> {
      s.setMapDmucDvi(mapDmucDvi);
      s.setMapVthh(mapVthh);
      s.setTenTrangThai(TrangThaiAllEnum.getLabelById(s.getTrangThai()));
      s.setNamNhap(scDanhSachRepository.getNamNhap(s.getMaDiaDiem(), s.getId()));
    });
    return search;
  }

  public List<ScDanhSachHdr> detail(List<Long> ids) throws Exception {
    if (DataUtils.isNullOrEmpty(ids)) throw new Exception("Tham số không hợp lệ.");
    List<ScDanhSachHdr> optional = scDanhSachRepository.findByIdIn(ids);
    if (DataUtils.isNullOrEmpty(optional)) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    List<ScDanhSachHdr> allById = scDanhSachRepository.findAllById(ids);
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    allById.forEach(data -> {
      data.setTenTrangThai(TrangThaiAllEnum.getLabelById(data.getTrangThai()));
      data.setMapDmucDvi(mapDmucDvi);
      data.setMapVthh(mapVthh);
      data.setNamNhap(scDanhSachRepository.getNamNhap(data.getMaDiaDiem(), data.getId()));
    });
    return allById;
  }

  public ScDanhSachHdr detail(Long ids) throws Exception {
    if (Objects.isNull(ids)) throw new Exception("Tham số không hợp lệ.");
    Optional<ScDanhSachHdr> optional = scDanhSachRepository.findById(ids);
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    }

    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    optional.get().setTenTrangThai(TrangThaiAllEnum.getLabelById(optional.get().getTrangThai()));
    optional.get().setMapDmucDvi(mapDmucDvi);
    optional.get().setMapVthh(mapVthh);
    optional.get().setNamNhap(scDanhSachRepository.getNamNhap(optional.get().getMaDiaDiem(), optional.get().getId()));
    return optional.get();
  }

  public ScDanhSachHdr detail(XhTlDanhSachRequest req) throws Exception {
    if (Objects.isNull(req.getId())){
      throw new Exception("Tham số không hợp lệ.");
    }
    Optional<ScDanhSachHdr> optional = scDanhSachRepository.findById(req.getId());
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    }

    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    optional.get().setTenTrangThai(TrangThaiAllEnum.getLabelById(optional.get().getTrangThai()));
    optional.get().setMapDmucDvi(mapDmucDvi);
    optional.get().setMapVthh(mapVthh);
    optional.get().setScPhieuNhapKhoList(scPhieuNhapKhoHdrRepository.findAllByIdScDanhSachHdrAndIdQdNh(optional.get().getId(),req.getIdQdNh()));
    return optional.get();
  }

  public List<ScDanhSachHdr> getDanhSachTaoBbKt(XhTlDanhSachRequest req){
    List<ScDanhSachHdr> scPhieuNhapKhoHdrs = scDanhSachRepository.searchListTaoBienBanKt(req);
    List<ScDanhSachHdr> listReturn = new ArrayList<>();
    scPhieuNhapKhoHdrs.forEach(item -> {
      try {
        ScDanhSachHdr detail = this.detail(item.getId());
        listReturn.add(detail);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
    return listReturn;
  }
}
