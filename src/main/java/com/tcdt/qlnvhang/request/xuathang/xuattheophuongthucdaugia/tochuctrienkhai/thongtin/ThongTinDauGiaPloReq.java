package com.tcdt.qlnvhang.request.xuathang.xuattheophuongthucdaugia.tochuctrienkhai.thongtin;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ThongTinDauGiaPloReq extends BaseRequest {

  private BigDecimal donGiaDeXuat;

  private BigDecimal donGiaVat;

  private BigDecimal duDau;

  private String dviTinh;

  private String maDviTsan;

  private String maDiemKho;

  private String maNhaKho;

  private String maNganKho;

  private String maLoKho;

  private BigDecimal soLuong;

  private Integer soLanTraGia;

  private BigDecimal donGiaTraGia;

  private String toChucCaNhan;
}
