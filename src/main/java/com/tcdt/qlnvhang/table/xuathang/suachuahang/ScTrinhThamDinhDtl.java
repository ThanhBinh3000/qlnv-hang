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
@Table(name = ScTrinhThamDinhDtl.TABLE_NAME)
@Data
public class ScTrinhThamDinhDtl extends BaseEntity implements Serializable {
  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "SC_TRINH_THAM_DINH_DTL";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ScTrinhThamDinhDtl.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = ScTrinhThamDinhDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = ScTrinhThamDinhDtl.TABLE_NAME + "_SEQ")
  private Long id;

  private Long idHdr;

  private Long idDsHdr;

  @Transient
  private ScDanhSachHdr scDanhSachHdr;

}
