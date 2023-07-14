package com.tcdt.qlnvhang.table.xuathang.suachuahang;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = ScQuyetDinhNhapHangDtl.TABLE_NAME)
@Data
public class ScQuyetDinhNhapHangDtl implements Serializable {
  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "SC_QUYET_DINH_NHAP_HANG_DTL";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SC_QD_NH_DTL_SEQ")
  @SequenceGenerator(sequenceName = "SC_QD_NH_DTL_SEQ", allocationSize = 1, name = "SC_QD_NH_DTL_SEQ")
  private Long id;
  private Long idHdr;
  private Long idDsHdr;
  private BigDecimal soLuongNhap;
  @Transient
  private ScDanhSachHdr scDanhSachHdr;

}
