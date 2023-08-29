package com.tcdt.qlnvhang.service.feign;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.request.feign.KeHoachRequest;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import com.tcdt.qlnvhang.util.Request;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class KeHoachService extends BaseServiceImpl {

  @Autowired
  private KeHoachClient keHoachClient;

  @Autowired
  private Gson gson;

  @Autowired
  private HttpServletRequest request;

  public BigDecimal getChiTieuNhapXuat(Integer nam, String loaiVthh, String maDvi, String typeChiTieu) throws Exception {

    KeHoachRequest obj = new KeHoachRequest();
    obj.setNamKh(nam);
    obj.setLoaiVthh(loaiVthh);
    obj.setMaDvi(maDvi);
    ResponseEntity<String> response = keHoachClient.getDetailByCode(getAuthorizationToken(request), obj);
    String str = Request.getAttrFromJson(response.getBody(), "data");

    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode jsonNode = objectMapper.readTree(str);
    if (!ObjectUtils.isEmpty(jsonNode)) {
      // Thóc
      if (loaiVthh.equals("0101")) {
        return typeChiTieu.equals("NHAP") ? jsonNode.get("ntnThoc").decimalValue() : jsonNode.get("xtnTongThoc").decimalValue();
      }
      // Gạo
      if (loaiVthh.equals("0102")) {
        return typeChiTieu.equals("NHAP") ? jsonNode.get("ntnGao").decimalValue() : jsonNode.get("xtnTongGao").decimalValue();
      }
    }
    return new BigDecimal(0);
  }

//
//
//    public Map<String, QlnvDmVattu> getMapVatTu(Collection<String> maVatTus) {
//        if (CollectionUtils.isEmpty(maVatTus))
//            return Collections.emptyMap();
//        return qlnvDmVattuRepository.findByMaIn(maVatTus).stream()
//                .collect(Collectors.toMap(QlnvDmVattu::getMa, Function.identity()));
//    }
//
//    public Map<String, QlnvDmDonvi> getMapDonVi(Collection<String> maDvis) {
//        if (CollectionUtils.isEmpty(maDvis))
//            return Collections.emptyMap();
//
//        return qlnvDmDonviRepository.findByMaDviIn(maDvis).stream()
//                .collect(Collectors.toMap(QlnvDmDonvi::getMaDvi, Function.identity()));
//    }
//
//    public Map<String, String> getListDanhMucDonVi(String capDvi) {
//        ResponseEntity<String> response = qlnvDmClient.getAllDanhMucDonVi(capDvi);
//        String str = Request.getAttrFromJson(response.getBody(), "data");
//        HashMap<String, String> data = new HashMap<String, String>();
//        List<Map<String, Object>> retMap = new Gson().fromJson(str, new TypeToken<List<HashMap<String, Object>>>() {
//        }.getType());
//        for (Map<String, Object> map : retMap) {
//            data.put(String.valueOf(map.get("maDvi")), String.valueOf(map.get("tenDvi")));
//        }
//        return data;
//    }
//
//    public Map<String, String> getListDanhMucHangHoa() {
//        ResponseEntity<String> response = qlnvDmClient.getDanhMucHangHoa();
//        String str = Request.getAttrFromJson(response.getBody(), "data");
//        HashMap<String, String> data = new HashMap<String, String>();
//        List<Map<String, Object>> retMap = new Gson().fromJson(str, new TypeToken<List<HashMap<String, Object>>>() {
//        }.getType());
//        for (Map<String, Object> map : retMap) {
//            data.put(String.valueOf(map.get("ma")), String.valueOf(map.get("ten")));
//        }
//        return data;
//    }
//
//    public Map<String, String> getListDanhMucChung(String loai) {
//        ResponseEntity<String> response = qlnvDmClient.getDanhMucChung(loai);
//        String str = Request.getAttrFromJson(response.getBody(), "data");
//        HashMap<String, String> data = new HashMap<String, String>();
//        List<Map<String, Object>> retMap = new Gson().fromJson(str, new TypeToken<List<HashMap<String, Object>>>() {
//        }.getType());
//        for (Map<String, Object> map : retMap) {
//            data.put(String.valueOf(map.get("ma")), String.valueOf(map.get("giaTri")));
//        }
//        return data;
//    }
//
//    public QlnvDmDonvi getDonViByMa(String maDvi) {
//        return qlnvDmDonviRepository.findByMaDvi(maDvi);
//    }
//
//
//    public String getTieuChuanCluongByMaLoaiVthh(String maLoai) {
//        String tentieuChuanCluong = null;
//        try {
//            ResponseEntity<String> response = qlnvDmClient.getTchuanCluong(maLoai);
//            String str = Request.getAttrFromJson(response.getBody(), "data");
//            if (str != null && !str.equals("") && !str.equals("null")) {
//                JSONObject object = new JSONObject(str);
//                if (object.has("tenQchuan")) {
//                    tentieuChuanCluong = object.getString("tenQchuan");
//                }
//            }
//        } catch (Exception e) {
//            return tentieuChuanCluong;
//        }
//        return tentieuChuanCluong;
//    }

  public Map<String, Map<String, Object>> getThongTinGia(Long nam, String loaiGia, String loaiVthh, String cloaiVthh, String maDvi) throws Exception {
    KeHoachRequest req = new KeHoachRequest();
    req.setNamKeHoach(nam);
    req.setLoaiGia(loaiGia);
    req.setLoaiVthh(loaiVthh);
    req.setCloaiVthh(cloaiVthh);
    req.setMaDvi(maDvi);
    req.setTrangThai(TrangThaiAllEnum.BAN_HANH.getId());
    ResponseEntity<String> response = keHoachClient.getThongTinGia(getAuthorizationToken(request), req);
    String str = Request.getAttrFromJson(response.getBody(), "data");

    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode jsonNode = objectMapper.readTree(str);
    HashMap<String, Map<String, Object>> data = new HashMap<String, Map<String, Object>>();
    List<Map<String, Object>> retMap = new Gson().fromJson(str, new TypeToken<List<HashMap<String, Object>>>() {
    }.getType());
    for (Map<String, Object> map : retMap) {
      data.put(String.valueOf(map.get("maChiCuc")), map);
    }
    return data;
  }
}
