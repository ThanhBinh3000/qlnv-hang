package com.tcdt.qlnvhang.service.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.thanhly.*;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly.XhTlDanhSachRequest;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.*;
import com.tcdt.qlnvhang.util.Contains;
import com.tcdt.qlnvhang.util.DataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class XhTlDanhSachService extends BaseServiceImpl {


  @Autowired
  private XhTlDanhSachRepository xhTlDanhSachRepository;

  @Autowired
  private XhTlKtraClHdrRepository xhTlKtraClHdrRepository;

  @Autowired
  private XhTlBangKeHdrRepository xhTlBangKeHdrRepository;

  @Autowired
  private FileDinhKemService fileDinhKemService;

  @Autowired
  private XhTlPhieuXuatKhoHdrRepository xhTlPhieuXuatKhoHdrRepository;

  @Autowired
  private XhTlTinhKhoHdrRepository xhTlTinhKhoHdrRepository;

  @Autowired
  private XhTlTinhKhoDtlRepository xhTlTinhKhoDtlRepository;

  @Autowired
  private XhTlHaoDoiHdrRepository xhTlHaoDoiHdrRepository;


  public Page<XhTlDanhSachHdr> searchPage(CustomUserDetails currentUser, XhTlDanhSachRequest req) throws Exception {
    String dvql = currentUser.getDvql();
    if (!currentUser.getUser().getCapDvi().equals(Contains.CAP_TONG_CUC)) {
      req.setDvql(dvql.substring(0, 6));
    }
    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
    Page<XhTlDanhSachHdr> search = xhTlDanhSachRepository.searchPage(req, pageable);

    //set label
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    search.getContent().forEach(s -> {
      s.setMapDmucDvi(mapDmucDvi);
      s.setMapVthh(mapVthh);
      s.setTenTrangThai(TrangThaiAllEnum.getLabelById(s.getTrangThai()));
    });
    return search;
  }

  public XhTlDanhSachHdr detail(Long id) throws Exception {
    Optional<XhTlDanhSachHdr> optional = xhTlDanhSachRepository.findById(id);
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    }
    XhTlDanhSachHdr data = optional.get();
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    data.setTenTrangThai(TrangThaiAllEnum.getLabelById(data.getTrangThai()));
    data.setMapDmucDvi(mapDmucDvi);
    data.setMapVthh(mapVthh);
    List<XhTlPhieuXuatKhoHdr> listPxk = xhTlPhieuXuatKhoHdrRepository.findAllByIdDsHdr(id);
    if(listPxk != null && listPxk.size() > 0){
      listPxk.forEach( pxk -> {
        // Lấy toàn bộ biên bản cân hàng theo địa điểm danh sách
        if(!Objects.isNull(pxk.getIdBangKeCanHang())){
          Optional<XhTlBangKeHdr> byId = xhTlBangKeHdrRepository.findById(pxk.getIdBangKeCanHang());
          byId.ifPresent(pxk::setXhTlBangKeHdr);
        }
      });
      data.setListXhTlPhieuXuatKhoHdr(listPxk);
    }

    Optional<XhTlTinhKhoHdr> bbTinhKhoOpt = xhTlTinhKhoHdrRepository.findByIdDsHdr(id);
    if(bbTinhKhoOpt.isPresent()){
      XhTlTinhKhoHdr xhTlTinhKhoHdr = bbTinhKhoOpt.get();
      xhTlTinhKhoHdr.setChildren(xhTlTinhKhoDtlRepository.findAllByIdHdr(xhTlTinhKhoHdr.getId()));

      Optional<XhTlHaoDoiHdr> byIdBbTinhKho = xhTlHaoDoiHdrRepository.findByIdBbTinhKho(xhTlTinhKhoHdr.getId());
      if(byIdBbTinhKho.isPresent()){
        xhTlTinhKhoHdr.setXhTlHaoDoiHdr(byIdBbTinhKho.get());
      }
      data.setXhTlTinhKhoHdr(xhTlTinhKhoHdr);
    }
    return data;
  }
}
