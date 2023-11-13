package com.tcdt.qlnvhang.entities.nhaphang.dauthau.hopdong;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import lombok.Data;

@Entity
@Table(name = "HH_HOP_DONG_DDIEM_NHAP_KHO")
@Data
public class HhHopDongDdiemNhapKho implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_DDIEM_NHAP_KHO_SEQ")
  @SequenceGenerator(sequenceName = "HH_DDIEM_NHAP_KHO_SEQ", allocationSize = 1, name = "HH_DDIEM_NHAP_KHO_SEQ")
  private Long id;
  String type;
  Long idHdongDtl;

  String maDvi;
  String maDiemKho;
  BigDecimal soLuong;
  BigDecimal donGiaVat;
  String dviTinh;
  String trangThai;
  @Transient
  String tenTrangThai;
  @Transient
  String tenDvi;
  @Transient
  String tenDiemKho;
  @Transient
  String diaDiemNhap;

  @Transient
  private List<HhHopDongDdiemNhapKhoVt> children;

  // preview
  @Transient
  private BigDecimal tongThanhTien;

  public String getTenTrangThai() {
    return TrangThaiAllEnum.getLabelById(trangThai);
  }
}
