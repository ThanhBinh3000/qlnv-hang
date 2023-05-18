package com.tcdt.qlnvhang.entities.xuathang.daugia.tochuctrienkhai.thongtin;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = XhTcTtinBdgDtl.TABLE_NAME)
public class XhTcTtinBdgDtl implements Serializable {
  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_TC_TTIN_BDG_DTL";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_TC_TTIN_BDG_DTL_SEQ")
  @SequenceGenerator(sequenceName = "XH_TC_TTIN_BDG_DTL_SEQ", allocationSize = 1, name = "XH_TC_TTIN_BDG_DTL_SEQ")
  private Long id;

  private Long idTtinHdr;

  private String maDvi;
  @Transient
  private String tenDvi;

  private BigDecimal soLuongChiCuc;

  private String diaChi;

  private BigDecimal soTienDatTruocChiCuc;

  @Transient
  List<XhTcTtinBdgPlo> children = new ArrayList<>();
}
