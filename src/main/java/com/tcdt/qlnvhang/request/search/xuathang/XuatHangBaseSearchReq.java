package com.tcdt.qlnvhang.request.search.xuathang;


import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class XuatHangBaseSearchReq extends BaseRequest {

  String namKeHoach;
  String soQd;
  String soQuyetDinh;
  LocalDate ngayKyTu;
  LocalDate ngayKyDen;
  String trichYeu;
  String loaiVthh;
  String pagType = "";
  String pagTypeLT;
  String pagTypeVT;
  String dvql;
  List<String> dsTrangThai;

  public String getPagType() {
    if (pagType.equals("LT")) {
      pagTypeLT = "1";
    }
    if (pagType.equals("VT")) {
      pagTypeVT = "1";
    }
    return pagType;
  }
}
