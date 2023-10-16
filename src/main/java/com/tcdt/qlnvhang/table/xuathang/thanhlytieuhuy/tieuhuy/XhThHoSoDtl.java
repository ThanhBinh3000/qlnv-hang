package com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.tieuhuy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly.XhTlDanhSachHdr;
import com.tcdt.qlnvhang.util.DataUtils;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Entity
@Table(name = XhThHoSoDtl.TABLE_NAME)
@Data
public class XhThHoSoDtl extends BaseEntity implements Serializable {
  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_TH_HO_SO_DTL";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhThHoSoDtl.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhThHoSoDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhThHoSoDtl.TABLE_NAME + "_SEQ")
  private Long id;
  private Long idHdr;
  private Long idDsHdr;
  private String ketQua;
  private BigDecimal donGiaDk;
  private BigDecimal donGiaPd;

  private BigDecimal slDaDuyet;
  @Transient
  private XhThDanhSachHdr xhThDanhSachHdr;

}
