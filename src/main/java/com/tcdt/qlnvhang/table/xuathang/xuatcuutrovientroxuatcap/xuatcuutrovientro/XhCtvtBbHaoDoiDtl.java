package com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = XhCtvtBbHaoDoiDtl.TABLE_NAME)
@Data
public class XhCtvtBbHaoDoiDtl implements Serializable {
  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_CTVT_BB_HAO_DOI_DTL";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhCtvtBbHaoDoiDtl.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhCtvtBbHaoDoiDtl.TABLE_NAME
      + "_SEQ", allocationSize = 1, name = XhCtvtBbHaoDoiDtl.TABLE_NAME + "_SEQ")
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