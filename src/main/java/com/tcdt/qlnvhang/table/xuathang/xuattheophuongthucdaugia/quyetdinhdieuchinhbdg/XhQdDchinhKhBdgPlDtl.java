package com.tcdt.qlnvhang.table.xuathang.xuattheophuongthucdaugia.quyetdinhdieuchinhbdg;

import com.tcdt.qlnvhang.entities.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = XhQdDchinhKhBdgPlDtl.TABLE_NAME)
@Data
public class XhQdDchinhKhBdgPlDtl extends BaseEntity implements Serializable {
  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_QD_DC_KH_BDG_PL_DTL";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = TABLE_NAME + "_SEQ", allocationSize = 1, name = TABLE_NAME + "_SEQ")
  private Long id;
  private Long idPhanLo;
  private String maDvi;
  @Transient
  private String tenDvi;
  private String maDiemKho;
  @Transient
  private String tenDiemKho;
  private String maNhaKho;
  @Transient
  private String tenNhakho;
  private String maNganKho;
  @Transient
  private String tenNganKho;
  private String maLoKho;
  @Transient
  private String tenLoKho;
  private String loaiVthh;
  @Transient
  private String tenLoaiVthh;
  private String cloaiVthh;
  @Transient
  private String tenCloaiVthh;
  private String maDviTsan;
  private BigDecimal duDau;
  private BigDecimal soLuong;
  private BigDecimal giaKhongVat;
  private BigDecimal giaKhoiDiem;
  private BigDecimal donGiaVat;
  private BigDecimal giaKhoiDiemDduyet;
  private BigDecimal tienDatTruoc;
  private BigDecimal tienDatTruocDduyet;
  private BigDecimal soLuongChiTieu;
  private BigDecimal soLuongKh;
  private String dviTinh;
  private BigDecimal tongSoLuong;
  private BigDecimal tongTienDatTruoc;
  private BigDecimal tongTienDatTruocDd;
  private Long idQdHdr;

}
