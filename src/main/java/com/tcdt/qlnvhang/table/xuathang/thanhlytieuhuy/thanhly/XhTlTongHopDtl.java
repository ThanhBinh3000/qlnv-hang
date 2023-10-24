package com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScDanhSachHdr;
import com.tcdt.qlnvhang.util.DataUtils;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Entity
@Table(name = XhTlTongHopDtl.TABLE_NAME)
@Data
public class XhTlTongHopDtl implements Serializable {
  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_TL_TONG_HOP_DTL";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhTlTongHopDtl.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhTlTongHopDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhTlTongHopDtl.TABLE_NAME + "_SEQ")
  private Long id;
  private Long idTongHop;
  private Long idDsHdr;
  @Transient
  private XhTlDanhSachHdr xhTlDanhSachHdr;

}
