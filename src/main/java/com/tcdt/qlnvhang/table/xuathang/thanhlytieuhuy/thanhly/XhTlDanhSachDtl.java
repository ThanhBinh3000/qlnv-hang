package com.tcdt.qlnvhang.table.xuathang.thanhlytieuhuy.thanhly;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = XhTlDanhSachDtl.TABLE_NAME)
@Data
public class XhTlDanhSachDtl implements Serializable {
  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "XH_TL_DANH_SACH_DTL";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhTlDanhSachDtl.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = XhTlDanhSachDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhTlDanhSachDtl.TABLE_NAME + "_SEQ")
  private Long id;
  private Long idDx;
  private String soDx;
  private String maDviDx;
  private LocalDate ngayPduyetDx;
  private String trichYeuDx;
  private BigDecimal tongSoLuongDx;
  private BigDecimal soLuongXuatCap;
  private BigDecimal soLuongDeXuat;
  private BigDecimal thanhTienDx;
  private LocalDate ngayDx;
  private LocalDate ngayKetThucDx;
  private String type;
  @Transient
  private String tenDviDx;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "idHdr")
  @JsonIgnore
  private XhTlDanhSachHdr xhTlDanhSachHdr;

}
