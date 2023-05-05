package com.tcdt.qlnvhang.table.khotang;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "KT_NGAN_KHO")
@Data
public class KtNganKho implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "KT_NGAN_KHO_SEQ")
  @SequenceGenerator(sequenceName = "KT_NGAN_KHO_SEQ", allocationSize = 1, name = "KT_NGAN_KHO_SEQ")
  Long id;
  String maNgankho;
  String tenNgankho;
  @Temporal(TemporalType.DATE)
  Date namSudung;
  String nhiemVu;
  String loaikhoId;
  String chatluongId;
  String tinhtrangId;
  String ngankhoHientrangId;
  BigDecimal dienTichDat;
  //  BigDecimal tichLuongTk;
  BigDecimal tichLuongTkLt;
  BigDecimal tichLuongTkVt;
  BigDecimal tichLuongChua;
  //  Long nhakhoId;
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
  //  BigDecimal theTichTk;
  BigDecimal theTichTkLt;
  BigDecimal theTichTkVt;
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

  @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY,
      cascade = CascadeType.ALL)
  List<KtNganLo> child;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
  @JoinColumn(name = "NHAKHO_ID", referencedColumnName = "ID", updatable = false, insertable = false)
  @JsonIgnore
  KtNhaKho parent;
}
