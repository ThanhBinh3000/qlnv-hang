package com.tcdt.qlnvhang.table.khotang;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Blob;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "KT_NHA_KHO")
@Data
public class KtNhaKho implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "KT_NHA_KHO_SEQ")
  @SequenceGenerator(sequenceName = "KT_NHA_KHO_SEQ", allocationSize = 1, name = "KT_NHA_KHO_SEQ")
  Long id;
  String maNhakho;
  String tenNhakho;
  @Temporal(TemporalType.DATE)
  Date namSudung;
  String nhiemVu;
  String loaikhoId;
  String chatluongId;
  String tinhtrangId;
  String nhakhoHientrangId;
  BigDecimal dienTichDat;
  BigDecimal tichLuongTk;
//  Long diemkhoId;
  Long quyhoachDuyetId;
  @Lob
  Blob banVeKt;
  BigDecimal tichLuongChua;
  BigDecimal tichLuongKhaDung;
  BigDecimal tichLuongChuaLt;
  BigDecimal tichLuongChuaVt;
  BigDecimal tichLuongKdLt;
  BigDecimal tichLuongKdVt;
  BigDecimal tichLuongKdLtvt;
  BigDecimal tichLuongChuaLtGao;
  BigDecimal tichLuongChuaLtThoc;
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

  @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY,
          cascade = CascadeType.ALL)
  private List<KtNganKho> child;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
  @JoinColumn(name = "DIEMKHO_ID", referencedColumnName = "ID",updatable = false, insertable = false)
  @JsonIgnore
  private KtDiemKho parent;
}
