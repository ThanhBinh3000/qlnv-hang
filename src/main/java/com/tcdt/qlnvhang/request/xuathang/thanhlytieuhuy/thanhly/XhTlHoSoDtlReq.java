package com.tcdt.qlnvhang.request.xuathang.thanhlytieuhuy.thanhly;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class XhTlHoSoDtlReq extends BaseRequest {
  private Long id;
  private BigDecimal donGiaDk;
  private BigDecimal slDaDuyet;
  private BigDecimal donGiaPd;
  private String ketQua;

}
