package com.tcdt.qlnvhang.table;

import com.tcdt.qlnvhang.entities.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = XhQdCuuTroKho.TABLE_NAME)
@Data
public class XhQdCuuTroKho extends BaseEntity implements Serializable {
  /**
   *
   */
  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_QD_CUU_TRO_KHO";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhQdCuuTroKho.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhQdCuuTroKho.TABLE_NAME
      + "_SEQ", allocationSize = 1, name = XhQdCuuTroKho.TABLE_NAME + "_SEQ")
  private Long id;
  private Long idQd;
  private Long idQdDtl;
  private String maDvi;
  //  private String loaiVthh;
  private String maChiCuc;
  private String maDiemKho;
  private String maNhaKho;
  private String maNganKho;
  private String maLoKho;
  private Long soLuong;
  private Long donGia;
  private String cloaiVthh;
  private String maDvTaiSan;
  private Long tonKho;
  private String donViTinh;
  @Transient
  private String tenChiCuc;
  @Transient
  private String tenDiemKho;
  @Transient
  private String tenNhaKho;
  @Transient
  private String tenNganKho;
  @Transient
  private String tenLoKho;
  @Transient
  private String tenCloaiVthh;
}
