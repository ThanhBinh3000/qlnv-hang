package com.tcdt.qlnvhang.table.nhaphangtheoptt.hopdong.hopdongphuluc;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = DiaDiemGiaoNhanMtt.TABLE_NAME)
@Data
public class DiaDiemGiaoNhanMtt implements Serializable {
  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "HH_HD_MTT_DIA_DIEM";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = TABLE_NAME + "_SEQ", allocationSize = 1, name = TABLE_NAME + "_SEQ")
  private Long id;

  private Long idHdr;

  private String maDvi;
  @Transient
  private String tenDvi;

  private String diaChi;

  private BigDecimal soLuongChiTieu;

  private BigDecimal soLuongKhDd;

  private BigDecimal donGia;

  private BigDecimal donGiaVat;

  private BigDecimal tongSoLuong;

  private BigDecimal tongThanhTien;

  private BigDecimal tongThanhTienVat;

  private BigDecimal soLuong;

//  phụ lục
  private Long idHdDtl;

  @Transient
  private String tenDviHd;
  @Transient
  private String diaChiHd;

  @Transient
  private List<DiaDiemGiaoNhanMttCt> children = new ArrayList<>();
}
