package com.tcdt.qlnvhang.table.xuathang.suachuahang;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = ScBaoCaoDtl.TABLE_NAME)
@Data
public class ScBaoCaoDtl implements Serializable {
  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "SC_BAO_CAO_DTL";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ScBaoCaoDtl.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = ScBaoCaoDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = ScBaoCaoDtl.TABLE_NAME + "_SEQ")
  private Long id;
  private Long idHdr;
  private Long idDanhSachHdr;
  private BigDecimal soLuongXuat;
  private BigDecimal soLuongNhap;
  private BigDecimal tongKinhPhiThucTe;
  @Transient
  private ScDanhSachHdr scDanhSachHdr;

}
