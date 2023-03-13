package com.tcdt.qlnvhang.service.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.hosokythuat.NhHoSoKyThuat;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.repository.vattu.hosokythuat.NhHoSoKyThuatRepository;
import com.tcdt.qlnvhang.repository.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.XhCtvtHoSoKyThuatRepository;
import com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro.SearchCtvtHoSoKyThuatReq;
import com.tcdt.qlnvhang.response.xuathang.xuatcuutrovientro.cuutro.HoSoKyThuatDTO;
import com.tcdt.qlnvhang.service.filedinhkem.FileDinhKemService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Map;
import java.util.Optional;

@Service
public class XhCtvtHoSoKyThuatService extends BaseServiceImpl {

  @Autowired
  private XhCtvtHoSoKyThuatRepository xhCtvtHoSoKyThuatRepository;
  @Autowired
  private NhHoSoKyThuatRepository nhHoSoKyThuatRepository;
  @Autowired
  private FileDinhKemService fileDinhKemService;

  public Page<HoSoKyThuatDTO> searchPage(CustomUserDetails currentUser, SearchCtvtHoSoKyThuatReq req) throws Exception {

    Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
    Page<HoSoKyThuatDTO> search = xhCtvtHoSoKyThuatRepository.search(req, pageable);
    Map<String, Map<String, Object>> mapDmucDvi = getListDanhMucDviObject(null, null, "01");

    Map<String, String> mapVthh = getListDanhMucHangHoa();
    search.getContent().forEach(s -> {
      if (mapDmucDvi.containsKey((s.getMaDvi()))) {
        Map<String, Object> objDonVi = mapDmucDvi.get(s.getMaDvi());
        s.setTenDvi(objDonVi.get("tenDvi").toString());
      }
      /*if (mapDmucDvi.containsKey(s.getMaDiemKho())) {
        s.setTenDiemKho(mapDmucDvi.get(s.getMaDiemKho()).get("tenDvi").toString());
      }
      if (mapDmucDvi.containsKey(s.getMaNhaKho())) {
        s.setTenNhaKho(mapDmucDvi.get(s.getMaNhaKho()).get("tenDvi").toString());
      }
      if (mapDmucDvi.containsKey(s.getMaNganKho())) {
        s.setTenNganKho(mapDmucDvi.get(s.getMaNganKho()).get("tenDvi").toString());
      }
      if (mapDmucDvi.containsKey(s.getMaLoKho())) {
        s.setTenLoKho(mapDmucDvi.get(s.getMaLoKho()).get("tenDvi").toString());
      }
      if (mapVthh.get((s.getLoaiVthh())) != null) {
        s.setTenLoaiVthh(mapVthh.get(s.getLoaiVthh()));
      }
      if (mapVthh.get((s.getCloaiVthh())) != null) {
        s.setTenCloaiVthh(mapVthh.get(s.getCloaiVthh()));
      }
      s.setTenTrangThai(NhapXuatHangTrangThaiEnum.getTenById(s.getTrangThai()));*/
    });
    return search;
  }

  @Transactional
  public Object update(CustomUserDetails currentUser, SearchCtvtHoSoKyThuatReq objReq) throws Exception {
    if (currentUser == null) {
      throw new Exception("Bad request.");
    }
    if (objReq.equals("DT")) {
      Optional<NhHoSoKyThuat> optional = nhHoSoKyThuatRepository.findById(objReq.getId());
      if (!optional.isPresent()) {
        throw new Exception("Không tìm thấy dữ liệu cần sửa");
      }

      NhHoSoKyThuat data = optional.get();

      data.setKqKiemTra(objReq.getKqKiemTra());
      data.setIdBbLayMauXuat(objReq.getIdBbLayMauXuat());
      NhHoSoKyThuat save = nhHoSoKyThuatRepository.save(data);
      return save;
    }
    // TODO: 3/10/2023 truong hop cac loai nhap khac
    else if (1 == 2) {
      return null;
    } else {
      return null;
    }
  }
}
