package com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class XhCtvtQdGiaoNvXhDtlReq {
  private Long id;
  private Long idHdr;
  private String noiDung;
  private BigDecimal soLuongXuat;
  private String maDviChiCuc;
  private BigDecimal tonKhoChiCuc;
  private BigDecimal tonkhoCloaiVthh;
  private String loaiVthh;
  private String cloaiVthh;
  private BigDecimal soLuongXuatChiCuc;
  private BigDecimal soLuongGiao;
  private String donViTinh;
  private String maDiemKho;
  private String maNhaKho;
  private String maNganKho;
  private String maLoKho;
  private BigDecimal donGiaKhongVat;
  private BigDecimal thanhTien;
  private BigDecimal soLuongXuatCap;
  private String trangThai;
}
