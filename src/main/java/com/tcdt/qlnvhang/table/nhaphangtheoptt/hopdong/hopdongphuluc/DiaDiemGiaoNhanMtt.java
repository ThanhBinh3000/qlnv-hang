package com.tcdt.qlnvhang.table.nhaphangtheoptt.hopdong.hopdongphuluc;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
  private Date ngayPduyet;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
  private Date ngayHlucPhuLuc;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
  private Date thoiGianDuKienSauDc;
  private String tgianThienHdSauDc;

  private String noiDungPl;
  private String noiDungDc;
  private String ghiChu;

  private String diaChi;
  private String trangThaiPhuLuc;

  private BigDecimal soLuongChiTieu;

  private BigDecimal soLuongKhDd;

  private BigDecimal donGia;

  private BigDecimal donGiaVat;

  private BigDecimal tongSoLuong;

  private BigDecimal tongThanhTien;

  private BigDecimal tongThanhTienVat;

  private BigDecimal soLuong;
  private BigDecimal soLuongHd;

//  phụ lục
  private Long idHdDtl;

  @Transient
  private String tenDviHd;
  @Transient
  private String diaChiHd;

  @Transient
  private List<DiaDiemGiaoNhanMttCt> children = new ArrayList<>();
}
