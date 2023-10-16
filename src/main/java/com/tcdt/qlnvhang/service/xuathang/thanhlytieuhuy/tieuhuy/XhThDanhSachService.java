package com.tcdt.qlnvhang.service.xuathang.thanhlytieuhuy.tieuhuy;

import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.xuathang.thanhlytieuhuy.tieuhuy.XhThDanhSachRepository;
import com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.tieuhuy.XhThDanhSachReq;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.tieuhuy.XhThDanhSachHdr;
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
public class XhThDanhSachService extends BaseServiceImpl {


  @Autowired
  private XhThDanhSachRepository xhThDanhSachRepository;

  @Autowired
  private FileDinhKemService fileDinhKemService;

  public Page<XhThDanhSachHdr> searchPage(CustomUserDetails currentUser, XhThDanhSachReq req) throws Exception {
    String dvql = currentUser.getDvql();
    if (!currentUser.getUser().getCapDvi().equals(Contains.CAP_TONG_CUC)) {
      req.setDvql(dvql.substring(0, 6));
    }
    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
    Page<XhThDanhSachHdr> search = xhThDanhSachRepository.searchPage(req, pageable);

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

  public XhThDanhSachHdr detail(Long id) throws Exception {
    if (Objects.isNull(id)){
      throw new Exception("Tham số không hợp lệ.");
    }
    Optional<XhThDanhSachHdr> optional = xhThDanhSachRepository.findById(id);
    if (!optional.isPresent()) {
      throw new Exception("Không tìm thấy dữ liệu");
    }

    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    XhThDanhSachHdr data = optional.get();
    data.setTenTrangThai(TrangThaiAllEnum.getLabelById(data.getTrangThai()));
    data.setMapDmucDvi(mapDmucDvi);
    data.setMapVthh(mapVthh);
    return data;
  }
}
