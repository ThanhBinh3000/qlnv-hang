package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchDcnbQuyetDinhDcTc extends BaseRequest {
  private Integer nam;
  private String maDvi;
  private String loaiDc;
  private String loaiQdinh;
  private LocalDate ngayHieuLucTu;
  private LocalDate ngayHieuLucDen;
  private LocalDate ngayDuyetTcTu;
  private LocalDate ngayDuyetTcDen;
  private String soQdinh;
  private String trichYeu;
  private String trangThai;
}
