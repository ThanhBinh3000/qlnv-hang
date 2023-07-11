package com.tcdt.qlnvhang.table.dieuchuyennoibo;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = DcnbHoSoKyThuatHdr.TABLE_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DcnbHoSoKyThuatHdr extends BaseEntity implements Serializable, Cloneable{

  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "DCNB_HO_SO_KY_THUAT_HDR";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DcnbHoSoKyThuatHdr.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = DcnbHoSoKyThuatHdr.TABLE_NAME
      + "_SEQ", allocationSize = 1, name = DcnbHoSoKyThuatHdr.TABLE_NAME + "_SEQ")
  private Long id;
  @Column(name = "MA_DVI")
  @Access(value=AccessType.PROPERTY)
  private String maDvi;
  @Column(name = "TEN_DVI")
  private String tenDvi;
  @Column(name = "SO_HO_SO_KY_THUAT")
  private String soHoSoKyThuat;
  @Column(name = "SO_BIEN_BAN_LAY_MAU")
  private String soBienBanLayMau;
  @Column(name = "DCNB_BIEN_BAN_LAY_MAU_ID")
  private Long bienBanLayMauId;
  @Column(name = "SO_QDNH")
  private String soQdnh;
  @Column(name = "ID_QDNH")
  private Long idQdnh;

  @Column(name = "TEN_CBTHSKT")
  private String tenCbthskt;
  @Column(name = "CBTHSKT_ID")
  private Long cbthsktId;
  @Column(name = "NGAY_DUYET_HSKT")
  private LocalDate ngayDuyetHskt;
  @Column(name = "MA_DIEM_KHO")
  private String maDiemKho;
  @Column(name = "TEN_DIEM_KHO")
  private String tenDiemKho;
  @Column(name = "MA_NGAN_KHO")
  private String maNganKho;
  @Column(name = "TEN_NGAN_KHO")
  private String tenNganKho;
  @Column(name = "MA_LO_KHO")
  private String maLoKho;
  @Column(name = "TEN_LO_KHO")
  private String tenLoKho;
  @Column(name = "TEN_CBTHSKT_KX")
  private String tenCbthsktKx;
  @Column(name = "CBTHSKT_KX_ID")
  private Long cbthsktKxId;
  @Column(name = "NGAY_KT_KX")
  private LocalDate ngayKtKx;
  @Column(name = "SO_BIEN_BAN_LAY_MAU_KX")
  private String soBienBanLayMauKx;
  @Column(name = "DCNB_BIEN_BAN_LAY_MAU_KX_ID")
  private Long soBienBanLayMauKxId;
  @Column(name = "KQ_KIEM_TRA")
  private Boolean kqKiemTra;
  @Column(name = "NHAP_CHI_TIET_THONG_TIN")
  private String nhapChiTietThongTin;
  @Column(name = "TRANG_THAI")
  @Access(value=AccessType.PROPERTY)
  private String trangThai;
  @Column(name = "LY_DO_TU_CHOI")
  private String lyDoTuChoi;
  @Column(name = "NGAY_GDUYET")
  private LocalDate ngayGduyet;
  @Column(name = "NGUOI_GDUYET_ID")
  private Long nguoiGduyetId;
  @Column(name = "NGAY_PDUYET")
  private LocalDate ngayPduyet;
  @Column(name = "NGUOI_PDUYET_ID")
  private Long nguoiPduyetId;
  @Column(name = "NGAY_PDUYET_TVQT")
  private LocalDate ngayPduyetTvqt;
  @Column(name = "NGUOI_PDUYET_TVQT")
  private Long nguoiPduyetIdTvqt;
  @Transient
  private String tenTrangThai;
  @Transient
  private List<DcnbHoSoTaiLieuDtl> danhSachHoSoTaiLieu = new ArrayList<>();
  @Transient
  private List<DcnbHoSoBienBanDtl> danhSachHoSoBienBan = new ArrayList<>();

  public void setTrangThai(String trangThai) {
    this.trangThai = trangThai;
    this.tenTrangThai = TrangThaiAllEnum.getLabelById(this.trangThai);
  }
}
