package com.tcdt.qlnvhang.request.xuathang.daugia.tochuctrienkhai.thongtin;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ThongTinDauGiaDtlReq extends BaseRequest {
  private Long id;

  private Long idTtinHdr;

  private String maDvi;

  private String diaChi;

  private BigDecimal soLuong;
  private BigDecimal soLuongChiCuc;

  public void setSoLuongChiCuc(BigDecimal soLuongChiCuc) {
    this.soLuongChiCuc = soLuongChiCuc;
    this.soLuong = this.soLuongChiCuc;
  }

  private List<ThongTinDauGiaPloReq> children = new ArrayList<>();
}
