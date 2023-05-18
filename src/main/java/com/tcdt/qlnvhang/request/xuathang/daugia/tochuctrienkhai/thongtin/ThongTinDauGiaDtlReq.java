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
  private BigDecimal soLuongChiCuc;

  private String diaChi;

  private BigDecimal soTienDatTruocChiCuc;

  public void setSoLuongChiCuc(BigDecimal soLuongChiCuc) {
    this.soLuongChiCuc = soLuongChiCuc;
  }

  private List<ThongTinDauGiaPloReq> children = new ArrayList<>();
}
