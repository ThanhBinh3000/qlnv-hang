package com.tcdt.qlnvhang.table.nhaphangtheoptt.hopdong.hopdongphuluc;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = HopDongMttHdr.TABLE_NAME)
public class HopDongMttHdr extends BaseEntity implements Serializable {
  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "HH_HD_MTT_HDR";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = TABLE_NAME + "_SEQ", allocationSize = 1, name = TABLE_NAME + "_SEQ")
  private Long id;
  private Long idHd;
  private Integer nam;
  private Long idQdPdKq;
  private String soQdPdKq;
  private Long idQdPdKh;
  private String soQdPdKh;
  LocalDate ngayKyQdPdKh;
  LocalDate tgianNkho;
  private String soHd;
  private String tenHd;
  LocalDate ngayKy;
  LocalDate ngayHieuLuc;
  private String ngayKyGhiChu;
  private String loaiHdong;
  private String loaiHdongGhiChu;
  private Integer soNgayThien;
  LocalDate tgianGnhanTu;
  LocalDate tgianGnhanDen;
  private Integer tgianGnhanGhiChu;
  private String noiDung;
  private String dieuKien;

  private String maDvi;
  @Transient
  private String tenDvi;

  private String cdtTen;
  private String cdtDiaChi;
  private String cdtMst;
  private String cdtTenNguoiDdien;
  private String cdtChucVu;
  private String cdtSdt;
  private String cdtFax;
  private String cdtStk;
  private String cdtMoTai;
  private String cdtThongTinGiayUyQuyen;

  private Long nccId;
  private String nccTen;
  private String nccDiaChi;
  private String nccMst;
  private String nccTenNguoiDdien;
  private String nccChucVu;
  private String nccSdt;
  private String nccFax;
  private String nccStk;
  private String nccMoTai;
  private String nccThongTinGiayUyQuyen;

  private String loaiVthh;
  @Transient
  private String tenLoaiVthh;
  private String cloaiVthh;
  @Transient
  private String tenCloaiVthh;
  private String moTaHangHoa;
  private String donViTinh;
  private BigDecimal soLuong;
  private BigDecimal donGia;
  private BigDecimal donGiaVat;
  private BigDecimal thanhTien;
  private String thanhTienBangChu;
  private String ghiChu;
  private String nguoiKy;
  @Transient
  private List<FileDinhKem> fileDinhKem = new ArrayList<>();
  @Transient
  private List<FileDinhKem> canCu = new ArrayList<>();

  private String trangThai;
  @Transient
  private String tenTrangThai;

  private String trangThaiNh;
  @Transient
  private String tenTrangThaiNh;

  @Transient
  private List<DiaDiemGiaoNhanMtt> diaDiemGiaoNhan = new ArrayList<>();

  @Transient
  private List<HopDongMttHdr> phuLuc = new ArrayList<>();
}
