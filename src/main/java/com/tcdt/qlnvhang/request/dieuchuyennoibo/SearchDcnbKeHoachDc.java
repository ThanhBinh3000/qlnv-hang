package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchDcnbKeHoachDc extends BaseRequest {
  private Integer nam;
  private String maDvi;
  private String loaiDc;
  private String type;
  private LocalDate ngayLapKhTu;
  private LocalDate ngayLapKhDen;
  private LocalDate ngayDuyetLdccTu;
  private LocalDate ngayDuyetLdccDen;
  private String soDxuat;
  private String nguonChi;
  private String loaiVthh;
  private String cloaiVthh;
  private String trichYeu;
  private String trangThai;
  private Long bbLayMauId;
  private Long idQdDc;
}
