package com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = XhCtvtBbTinhKhoDtl.TABLE_NAME)
@Data
public class XhCtvtBbTinhKhoDtl implements Serializable {
  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_CTVT_BB_TINH_KHO_DTL";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhCtvtBbTinhKhoDtl.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhCtvtBbTinhKhoDtl.TABLE_NAME
      + "_SEQ", allocationSize = 1, name = XhCtvtBbTinhKhoDtl.TABLE_NAME + "_SEQ")
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
  @ManyToOne
  @JsonIgnore
  @JoinColumn(name = "idHdr", updatable = false, insertable = false)
  private XhCtvtBbTinhKhoHdr xhCtvtBbTinhKhoHdr;
}
