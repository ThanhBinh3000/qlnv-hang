package com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class XhCtvtBbHaoDoiDtlReq {
  private Long id;
  private Long idHdr;
  private Long idPhieuKnCl;
  private String soPhieuKnCl;
  private Long idPhieuXuatKho;
  private String soPhieuXuatKho;
  private Long idBkCanHang;
  private String soBkCanHang;
  private LocalDate ngayXuatKho;
  private BigDecimal slXuat;
}
