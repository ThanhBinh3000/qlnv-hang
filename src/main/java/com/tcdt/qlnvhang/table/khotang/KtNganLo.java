package com.tcdt.qlnvhang.table.khotang;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "KT_NGAN_LO")
@Data
public class KtNganLo implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "KT_NGAN_LO_SEQ")
  @SequenceGenerator(sequenceName = "KT_NGAN_LO_SEQ", allocationSize = 1, name = "KT_NGAN_LO_SEQ")
  Long id;
  String maNganlo;
  String tenNganlo;
  @Temporal(TemporalType.DATE)
  Date namSudung;
  String nhiemVu;
  String loaikhoId;
  String chatluongId;
  String tinhtrangId;
  String nganloHientrangId;
  BigDecimal dienTichDat;
//  BigDecimal tichLuongTk;
  BigDecimal tichLuongTkLt;
  BigDecimal tichLuongTkVt;
  BigDecimal tichLuongChua;
//  Long ngankhoId;
  Long quyhoachDuyetId;
  BigDecimal tichLuongChuaLt;
  BigDecimal tichLuongChuaVt;
  BigDecimal theTichChuaLt;
  BigDecimal tichLuongKhaDung;
  BigDecimal tichLuongKdLt;
  BigDecimal tichLuongKdVt;
  String huongSuDung;
  BigDecimal tichLuongKdLtvt;
  Integer trangThaiTl;
  Integer namNhap;
  BigDecimal tichLuongChuaLtGao;
  BigDecimal tichLuongChuaLtThoc;
  BigDecimal theTichChuaVt;
  BigDecimal theTichTk;
  Long updateStatus;
  Date lastUpdate;
  String trangThai;
  Date ngayTao;
  String nguoiTao;
  Date ngaySua;
  String nguoiSua;
  String ldoTuchoi;
  Date ngayGuiDuyet;
  String nguoiGuiDuyet;
  Date ngayPduyet;
  String nguoiPduyet;
  @Transient
  String lhKho;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
  @JoinColumn(name = "NGANKHO_ID",updatable = false, insertable = false)
  @JsonIgnore
  private KtNganKho parent;
}
