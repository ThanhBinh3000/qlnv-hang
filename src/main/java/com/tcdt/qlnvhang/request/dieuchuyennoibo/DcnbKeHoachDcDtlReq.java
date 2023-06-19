package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DcnbKeHoachDcDtlReq {
  private String maChiCucNhan;
  private LocalDate thoiGianDkDc;
  private String maKho;
  private String loaiVthh;
  private String cLoaiVthh;
  private BigDecimal tonKho;
  private String donViTinh;
  private BigDecimal soLuongDc;
  private BigDecimal duToanKphi;
  private BigDecimal tichLuongKd;
  private BigDecimal soLuongPhanBo;
  private String dviVanChuyen;
  private String pthucGiaoHang;
  private String pthucNhanHang;
  private String nguonChi;

}
