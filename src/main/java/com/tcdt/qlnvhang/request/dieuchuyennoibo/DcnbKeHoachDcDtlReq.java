package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
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
  private String maKhoNhan;
  private BigDecimal tichLuongKd;
  private BigDecimal soLuongPhanBo;
  private String dviVanChuyen;
  private String pthucGiaoHang;
  private String pthucNhanHang;
  private String nguonChi;

}
