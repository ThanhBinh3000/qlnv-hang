package com.tcdt.qlnvhang.table;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = PhieuNhapXuatHistory.TABLE_NAME)
public class PhieuNhapXuatHistory implements Serializable {
  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "PHIEU_NHAP_XUAT_HISTORY";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = PhieuNhapXuatHistory.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = PhieuNhapXuatHistory.TABLE_NAME + "_SEQ", allocationSize = 1, name = PhieuNhapXuatHistory.TABLE_NAME + "_SEQ")
  private Long id;
  private Long soLuong;
  private Long donGia;
  private Long thanhTien;
  private BigDecimal soLuongChungTu;
  private Long idPhieu;
  private String soPhieu;
  //nhap 1 xuat -1
  private Integer loaiNhapXuat;
  private String loaiHinhNhapXuat;
  private String loaiVthh;
  private String cloaiVthh;
  private String maKho;
  private LocalDate ngayDuyet;
  //Vd BDG NDT CTVT
  private String kieu;
  private String bang;
  private Integer namNhap;
  private LocalDate ngayTao;

  @Transient
  private String maCuc;
  @Transient
  private String maChiCuc;
  @Transient
  private String maDiemKho;
  @Transient
  private String maNhaKho;
  @Transient
  private String maNganKho;
  @Transient
  private String maLoKho;

  public void setMaKho(String maKho) {
    this.maKho = maKho;
    if (!ObjectUtils.isEmpty(this.maKho)) {
      setMaCuc(maKho.length() >= 6 ? maKho.substring(0, 6) : "");
      setMaChiCuc(maKho.length() >= 8 ? maKho.substring(0, 8) : "");
      setMaDiemKho(maKho.length() >= 10 ? maKho.substring(0, 10) : "");
      setMaNhaKho(maKho.length() >= 12 ? maKho.substring(0, 12) : "");
      setMaNganKho(maKho.length() >= 14 ? maKho.substring(0, 14) : "");
      setMaLoKho(maKho.length() >= 16 ? maKho.substring(0, 16) : "");
    }
  }
}
