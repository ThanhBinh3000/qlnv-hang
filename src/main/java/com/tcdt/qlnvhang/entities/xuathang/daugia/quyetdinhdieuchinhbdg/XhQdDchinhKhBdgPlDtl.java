package com.tcdt.qlnvhang.entities.xuathang.daugia.quyetdinhdieuchinhbdg;

import com.tcdt.qlnvhang.entities.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = XhQdDchinhKhBdgPlDtl.TABLE_NAME)
@Data
public class XhQdDchinhKhBdgPlDtl extends BaseEntity implements Serializable {
  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_QD_DC_KH_BDG_PL_DTL";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = TABLE_NAME + "_SEQ", allocationSize = 1, name = TABLE_NAME + "_SEQ")
  private Long id;

  private Long idPhanLo;

  private String maDiemKho;

  private String maNhaKho;

  private String maNganKho;

  private String maLoKho;

  private String maDviTsan;

  private BigDecimal duDau;

  private BigDecimal soLuong;

  private BigDecimal donGiaDeXuat;

  private BigDecimal donGiaVat;

  private String dviTinh;

  // Transient
  @Transient
  private String tenDiemKho;

  @Transient
  private String tenNhaKho;

  @Transient
  private String tenNganKho;

  @Transient
  private String tenLoKho;

}
