package com.tcdt.qlnvhang.entities.xuathang.daugia.quyetdinhdieuchinhbdg;
import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = XhQdDchinhKhBdgPlDtl.TABLE_NAME)
@Data
public class XhQdDchinhKhBdgPlDtl implements Serializable {
  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_QD_DC_KH_BDG_PL_DTL";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = TABLE_NAME + "_SEQ", allocationSize = 1, name = TABLE_NAME + "_SEQ")
  private Long id;

  private Long idPhanLo;

  private String maDiemKho;
  @Transient
  private String tenDiemKho;

  private String maNhaKho;
  @Transient
  private String tenNhaKho;

  private String maNganKho;
  @Transient
  private String tenNganKho;

  private String maLoKho;
  @Transient
  private String tenLoKho;

  private String maDviTsan;

  private BigDecimal tonKho;

  private BigDecimal soLuongDeXuat;

  private BigDecimal donGiaDeXuat;

  @Transient
  private BigDecimal donGiaDuocDuyet;

  private String donViTinh;

  private String loaiVthh;
  @Transient
  private String tenLoaiVthh;

  private String cloaiVthh;
  @Transient
  private String tenCloaiVthh;

}
