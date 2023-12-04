package com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.tcdt.qlnvhang.entities.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = XhCtvtBangKeDtl.TABLE_NAME)
@Data
public class XhCtvtBangKeDtl extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_CTVT_BANG_KE_DTL";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhCtvtBangKeDtl.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhCtvtBangKeDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhCtvtBangKeDtl.TABLE_NAME + "_SEQ")
  private Long id;
  private Long idHdr;
  private String maCan;
  private String soBaoBi;
  private BigDecimal trongLuongBaoBi;
  private BigDecimal trongLuongCaBi;
  private String soSerial;
  private BigDecimal soLuong;
}
