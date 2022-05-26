package com.tcdt.qlnvhang.table.khotang;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "KT_DTQGKV")
@Data
public class KtDtqgkv implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "KT_DTQGKV_SEQ")
  @SequenceGenerator(sequenceName = "KT_DTQGKV_SEQ", allocationSize = 1, name = "KT_DTQGKV_SEQ")
  Long id;
  String maDtqgkv;
  String tenDtqgkv;
  String diaChi;
  Long tinhthanhId;
  Long quanhuyenId;
  Long phuongxaId;
  String moTa;
  Long quyhoachDuyetId;
  Long dtqgkvHientrangId;
  BigDecimal tichLuongKhaDung;
  BigDecimal tichLuongChua;
  BigDecimal tichLuongChuaLt;
  BigDecimal tichLuongChuaVt;
  BigDecimal tichLuongKdLt;
  BigDecimal tichLuongKdVt;
  BigDecimal tichLuongThietKe;
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
  Date ngayDongboSauCung;

  @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY,
          cascade = CascadeType.ALL)
  private List<KtTongKho> child;
}
