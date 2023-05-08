package com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.table.xuathang.xuatcuutrovientroxuatcap.xuatcap.XhXcapQuyetDinhPdDtl;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = XhXcapQuyetDinhPdDtl.TABLE_NAME)
@Data
public class XhTlQuyetDinhTlDtl implements Serializable {

  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_TL_QUYET_DINH_TL_DTL";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhTlQuyetDinhTlDtl.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhTlQuyetDinhTlDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhTlQuyetDinhTlDtl.TABLE_NAME + "_SEQ")
  private Long id;
  private String maDvi;
  private String loaiVthh;
  private String cloaiVthh;
  private String donViTinh;
  private String maDiemKho;
  private String maNhaKho;
  private String maNganKho;
  private String maLoKho;
  private LocalDate ngayNhapKho;
  private BigDecimal soLuongTon;
  private BigDecimal soLuongTl;
  private BigDecimal soLuongCon;
  private BigDecimal donGiaTl;
  private BigDecimal thanhTien;
  private String lyDoTl;
  private String ketQua;

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
  private String tenDvi;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "idHdr")
  @JsonIgnore
  private XhTlQuyetDinhTlHdr quyetDinhTlHdr;
}