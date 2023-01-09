package com.tcdt.qlnvhang.request.xuathang.xuattheophuongthucdaugia.tochuctrienkhai.thongtin;

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

  private List<ThongTinDauGiaPloReq> children = new ArrayList<>();
}
