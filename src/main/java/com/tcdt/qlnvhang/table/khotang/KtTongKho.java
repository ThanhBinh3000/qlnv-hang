package com.tcdt.qlnvhang.table.khotang;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "KT_TONG_KHO")
@Data
public class KtTongKho implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "KT_TONG_KHO_SEQ")
  @SequenceGenerator(sequenceName = "KT_TONG_KHO_SEQ", allocationSize = 1, name = "KT_TONG_KHO_SEQ")
  Long id;
  String maTongKho;
  String tenTongKho;
  String diaChi;
  Long tinhthanhId;
  Long quanhuyenId;
  Long phuongxaId;
  BigDecimal tichLuong;
  String nhiemVu;
  String chatLuongId;
  String tinhTrangId;
  Long hosoPhaply;
  Long hosoKhotang;
  BigDecimal giatriConlai;
  String khoCuId;
  Long capKho;
  @Temporal(TemporalType.DATE)
  Date ngayChuyen;
//  Long dtqgkvId;
  Long quyhoachDuyetId;
  String tongkhoHientrangId;
  BigDecimal tichLuongThietKe;
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
  private List<KtDiemKho> child;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
  @JoinColumn(name = "DTQGKV_ID", referencedColumnName = "ID", updatable = false, insertable = false)
  @JsonIgnore
  private KtDtqgkv parent;
}
