package com.tcdt.qlnvhang.table;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = XhDxCuuTroKho.TABLE_NAME)
@Data
public class XhDxCuuTroKho extends BaseEntity implements Serializable {
  /**
   *
   */
  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_DX_CUU_TRO_KHO";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhDxCuuTroKho.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhDxCuuTroKho.TABLE_NAME
      + "_SEQ", allocationSize = 1, name = XhDxCuuTroKho.TABLE_NAME + "_SEQ")
  private Long id;
  private Long idDxuat;
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
