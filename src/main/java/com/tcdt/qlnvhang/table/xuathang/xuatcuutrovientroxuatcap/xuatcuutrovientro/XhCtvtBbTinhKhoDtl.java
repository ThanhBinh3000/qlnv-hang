package com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = XhCtvtPhieuXuatKho.TABLE_NAME)
@Data
public class XhCtvtBbTinhKhoDtl implements Serializable {
  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_CTVT_PHIEU_XUAT_KHO";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhCtvtPhieuXuatKho.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhCtvtPhieuXuatKho.TABLE_NAME
      + "_SEQ", allocationSize = 1, name = XhCtvtPhieuXuatKho.TABLE_NAME + "_SEQ")
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
