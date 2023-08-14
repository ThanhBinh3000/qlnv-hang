package com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcuutrovientro;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = XhCtvtDeXuatPa.TABLE_NAME)
@Getter
@Setter
public class XhCtvtDeXuatPa implements Serializable {

  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_CTVT_DE_XUAT_PA";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhCtvtDeXuatPa.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhCtvtDeXuatPa.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhCtvtDeXuatPa.TABLE_NAME + "_SEQ")
  private Long id;
  private String noiDung;
  private BigDecimal soLuongXuat;
  private String maDvi;
  private String maDviCuc;
  private BigDecimal tonKhoCuc;
  private BigDecimal soLuongXuatCuc;
  private String maDviChiCuc;
  private BigDecimal tonKhoChiCuc;
  private BigDecimal tonKhoCloaiVthh;
  private String loaiVthh;
  private String cloaiVthh;
  private BigDecimal soLuongXuatChiCuc;
  private String donViTinh;
  private BigDecimal donGiaKhongVat;
  private BigDecimal thanhTien;
  private BigDecimal soLuongXuatCap;
  @Transient
  private String tenLoaiVthh;
  @Transient
  private String tenCloaiVthh;
  @Transient
  private String tenDvi;
  @Transient
  private String tenCuc;
  @Transient
  private String tenChiCuc;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "idHdr")
  @JsonIgnore
  private XhCtvtDeXuatHdr xhCtvtDeXuatHdr;
}
