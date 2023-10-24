package com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.tieuhuy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.util.DataUtils;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Entity
@Table(name = XhThBaoCaoKqDtl.TABLE_NAME)
@Data
public class XhThBaoCaoKqDtl implements Serializable {

  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_TH_BAO_CAO_KQ_DTL";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhThBaoCaoKqDtl.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhThBaoCaoKqDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhThBaoCaoKqDtl.TABLE_NAME + "_SEQ")
  private Long id;
  private Long idHdr;
  private Long idDsHdr;
  private BigDecimal slThucTe;
  private BigDecimal donGiaThucTe;
  private String dviToChuc;
  private String lyDoTieuHuy;
  private String ketQuaTieuHuy;
  @Transient
  private XhThDanhSachHdr xhThDanhSachHdr;

}