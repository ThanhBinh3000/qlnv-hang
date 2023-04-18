package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SearchDcnbKeHoachDc extends BaseRequest {
  private Integer nam;
  private String maDvi;
  private String loaiDc;
  private LocalDate ngayLapKhTu;
  private LocalDate ngayLapKhDen;
  private LocalDate ngayDuyetLdcTu;
  private LocalDate ngayDuyetLdcDen;
  private String soDxuat;
  private String nguonChi;
  private String loaiVthh;
  private String cloaiVthh;
  private String trichYeu;
  private String trangThai;
}
