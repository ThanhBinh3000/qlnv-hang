package com.tcdt.qlnvhang.table.xuathang.suachuahang;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.util.DataUtils;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Entity
@Table(name = ScTongHopDtl.TABLE_NAME)
@Data
public class ScTongHopDtl implements Serializable {
  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "SC_TONG_HOP_DTL";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ScTongHopDtl.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = ScTongHopDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = ScTongHopDtl.TABLE_NAME + "_SEQ")
  private Long id;
  private Long idHdr;
  private Long idDsHdr;

}
