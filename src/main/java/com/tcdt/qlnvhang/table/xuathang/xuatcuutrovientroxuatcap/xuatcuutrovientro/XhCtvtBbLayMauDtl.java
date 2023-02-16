package com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = XhCtvtBbLayMauDtl.TABLE_NAME)
@Data
public class XhCtvtBbLayMauDtl implements Serializable {

  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_CTVT_BB_LAY_MAU_DTL";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhCtvtBbLayMauDtl.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhCtvtBbLayMauDtl.TABLE_NAME
      + "_SEQ", allocationSize = 1, name = XhCtvtBbLayMauDtl.TABLE_NAME + "_SEQ")
  private Long id;
  private Long idHdr;
  private String daiDien;
  private String loaiDaiDien;
}
