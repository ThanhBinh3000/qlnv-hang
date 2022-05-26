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
@Table(name = "KT_DIEM_KHO")
@Data
public class KtDiemKho implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "KT_DIEM_KHO_SEQ")
  @SequenceGenerator(sequenceName = "KT_DIEM_KHO_SEQ", allocationSize = 1, name = "KT_DIEM_KHO_SEQ")
  Long id;
  String maDiemkho;
  String tenDiemkho;
  String diaChi;
  Long tinhthanhId;
  Long quanhuyenId;
  Long phuongxaId;
  @Temporal(TemporalType.DATE)
  Date namSudung;
  String loaikhoId;
  String chatluongId;
  String tinhTrangId;
  String nhiemVu;
  Long tuyenKhoId;
  BigDecimal dienTichDat;
  BigDecimal tichLuongThietKe;
  BigDecimal tichLuongChua;
  String tinhtrangCsvc;
  Long hosoPhaply;
  Long hosoKhotang;
  BigDecimal giatriConlai;
  Long khoCuId;
  BigDecimal capKho;
  @Temporal(TemporalType.DATE)
  Date ngayChuyen;
//  Long tongkhoId;
  Long quyhoachDuyetId;
  @Lob
  Blob soDoMatBang;
  String diemkhoHientrangId;
  BigDecimal tichLuongKhaDung;
  BigDecimal dienTichDatSodo;
  String soGiayCnQsdd;
  BigDecimal tichLuongChuaLt;
  BigDecimal tichLuongChuaVt;
  BigDecimal tichLuongKdLt;
  BigDecimal tichLuongKdVt;
  BigDecimal tichLuongKdLtvt;
  BigDecimal tichLuongChuaLtGao;
  BigDecimal tichLuongChuaLtThoc;
  BigDecimal thanhTien;
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
  @JsonIgnore
  private List<KtNhaKho> child;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
  @JoinColumn(name = "TONGKHO_ID", referencedColumnName = "ID",updatable = false, insertable = false)
  @JsonIgnore
  private KtTongKho parent;
}
