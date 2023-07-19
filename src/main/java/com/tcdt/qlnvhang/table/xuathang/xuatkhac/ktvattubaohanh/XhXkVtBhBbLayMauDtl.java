package com.tcdt.qlnvhang.table.xuathang.xuatkhac.ktvattubaohanh;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = XhXkVtBhBbLayMauDtl.TABLE_NAME)
@Data
public class XhXkVtBhBbLayMauDtl implements Serializable {

  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_XK_VT_BH_BB_LAY_MAU_DTL";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhXkVtBhBbLayMauDtl.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhXkVtBhBbLayMauDtl.TABLE_NAME
      + "_SEQ", allocationSize = 1, name = XhXkVtBhBbLayMauDtl.TABLE_NAME + "_SEQ")
  private Long id;
  private String daiDien;
  private String loaiDaiDien;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "idHdr")
  @JsonIgnore
  private XhXkVtBhBbLayMauHdr bbLayMauHdr;
}
