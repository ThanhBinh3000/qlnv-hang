package com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcap;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = XhXcapQuyetDinhPdDx.TABLE_NAME)
@Getter
@Setter
public class XhXcapQuyetDinhPdDx implements Serializable {

  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_XCAP_QUYET_DINH_PD_DX";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhXcapQuyetDinhPdDx.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhXcapQuyetDinhPdDx.TABLE_NAME
      + "_SEQ", allocationSize = 1, name = XhXcapQuyetDinhPdDx.TABLE_NAME + "_SEQ")
  private Long id;
  private String noiDung;
  private BigDecimal soLuongXuat;
  private String maDviCuc;
  private BigDecimal tonKhoCuc;
  private BigDecimal soLuongCon;
  private BigDecimal soLuongXuatCuc;
  private String maDviChiCuc;
  private BigDecimal tonKhoChiCuc;
  private String loaiVthh;
  private String cloaiVthh;
  private BigDecimal tonKhoCloaiVthh;
  private BigDecimal soLuongXuatChiCuc;
  private String donViTinh;
  private BigDecimal donGiaKhongVat;
  private BigDecimal thanhTien;


  @Transient
  private String tenCloaiVthh;
  @Transient
  private String tenCuc;
  @Transient
  private String tenChiCuc;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "idHdr")
  @JsonIgnore
  private XhXcapQuyetDinhPdDtl quyetDinhPdDtl;
}