package com.tcdt.qlnvhang.service.nhaphangtheoptmuatt.hopdong.hopdongphuluc;

import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.nhaphangtheoptmtt.hopdong.hopdongphuluc.HopDongMttHdrRepository;
import com.tcdt.qlnvhang.request.nhaphangtheoptt.hopdong.hopdongphuluc.HopDongMttHdrReq;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.table.FileDinhKem;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.hopdong.hopdongphuluc.HopDongMttHdr;
import com.tcdt.qlnvhang.util.DataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class HopDongMttHdrService extends BaseServiceImpl {
  @Autowired
  private FileDinhKemService fileDinhKemService;
  @Autowired
  private HopDongMttHdrRepository hopDongHdrRepository;

  public Page<HopDongMttHdr> searchPage(CustomUserDetails currentUser, HopDongMttHdrReq req) throws Exception {
    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());

    req.setMaDvi(currentUser.getUser().getDepartment());
    Page<HopDongMttHdr> search = hopDongHdrRepository.search(req, pageable);
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    search.getContent().forEach(s -> {
      if (mapDmucDvi.get((s.getMaDvi())) != null) {
        s.setTenDvi(mapDmucDvi.get(s.getMaDvi()));
      }
      if (mapVthh.get((s.getLoaiVthh())) != null) {
        s.setTenLoaiVthh(mapVthh.get(s.getLoaiVthh()));
      }
      if (mapVthh.get((s.getCloaiVthh())) != null) {
        s.setTenCloaiVthh(mapVthh.get(s.getCloaiVthh()));
      }
    });
    return search;
  }

  public List<HopDongMttHdr> detail(CustomUserDetails currentUser, List<Long> ids) throws Exception {
    if (DataUtils.isNullOrEmpty(ids))
      throw new Exception("Tham số không hợp lệ.");
    Map<String, String> mapDmucDvi = getListDanhMucDvi(null, null, "01");
    Map<String, String> mapVthh = getListDanhMucHangHoa();
    List<HopDongMttHdr> allById = hopDongHdrRepository.findAllById(ids);
    allById.forEach(data -> {
      List<FileDinhKem> canCu = fileDinhKemService.search(data.getId(), Arrays.asList(HopDongMttHdr.TABLE_NAME + "_CAN_CU"));
      List<FileDinhKem> dinhKem = fileDinhKemService.search(data.getId(), Arrays.asList(HopDongMttHdr.TABLE_NAME + "_DINH_KEM"));
      data.setCanCu(canCu);
      data.setFileDinhKem(dinhKem);
      List<HopDongMttHdr> listPhuLuc = hopDongHdrRepository.findByIdHd(data.getId());
      data.setPhuLuc(listPhuLuc);
      data.setTenDvi(mapDmucDvi.get(data.getMaDvi()));
      data.setTenLoaiVthh(mapVthh.get(data.getLoaiVthh()));
      data.setTenCloaiVthh(mapVthh.get(data.getCloaiVthh()));
    });
    return allById;
  }
}
