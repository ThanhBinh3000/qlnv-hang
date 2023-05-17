package com.tcdt.qlnvhang.request.xuathang.daugia.tochuctrienkhai.thongtin;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ThongTinDauGiaPloReq extends BaseRequest {

  private Long id;

  private Long idTtinDtl;

  private String maDiemKho;

  private String maNhaKho;

  private String maNganKho;

  private String maLoKho;

  private String maDviTsan;

  private BigDecimal tonKho;
  private BigDecimal soLuongDeXuat;

  private String donViTinh;

  private BigDecimal donGiaDuocDuyet;

  private BigDecimal donGiaDeXuat;

  private Integer soLanTraGia;

  private BigDecimal donGiaTraGia;

  private String toChucCaNhan;

  public void setSoLuongDeXuat(BigDecimal soLuongDeXuat) {
    this.soLuongDeXuat = soLuongDeXuat;
  }
}
