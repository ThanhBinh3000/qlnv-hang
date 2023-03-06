package com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcap;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = XhXuatCapQdGiaoNvXhDtl.TABLE_NAME)
@Data
public class XhXuatCapQdGiaoNvXhDtl {

  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_XUAT_CAP_QD_GIAO_NV_XH_DTL";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhXuatCapQdGiaoNvXhDtl.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhXuatCapQdGiaoNvXhDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhXuatCapQdGiaoNvXhDtl.TABLE_NAME + "_SEQ")
  private Long id;
  private Long idHdr;
  private String noiDung;
  private BigDecimal soLuongXuat;
  private String maDviChiCuc;
  private BigDecimal tonKhoChiCuc;
  private BigDecimal tonkhoCloaiVthh;
  private String loaiVthh;
  private String cloaiVthh;
  private BigDecimal soLuongXuatChiCuc;
  private BigDecimal soLuongGiao;
  private String donViTinh;
  private String maDiemKho;
  private String maNhaKho;
  private String maNganKho;
  private String maLoKho;
  private BigDecimal donGiaKhongVat;
  private BigDecimal thanhTien;
  private BigDecimal soLuongXuatCap;
  private String trangThai;
  @Transient
  private String tenLoaiVthh;
  @Transient
  private String tenCloaiVthh;
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
  private String tenTrangThai;

}
