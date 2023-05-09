package com.tcdt.qlnvhang.table.dieuchuyennoibo;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.enums.NhapXuatHangTrangThaiEnum;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = DcnbKeHoachDcHdr.TABLE_NAME)
@Getter
@Setter
public class DcnbKeHoachDcHdr extends BaseEntity implements Serializable, Cloneable{

  private static final long serialVersionUID = 1L;
  public static final String TABLE_NAME = "DCNB_KE_HOACH_DC_HDR";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DcnbKeHoachDcHdr.TABLE_NAME + "_SEQ")
  @SequenceGenerator(sequenceName = DcnbKeHoachDcHdr.TABLE_NAME
      + "_SEQ", allocationSize = 1, name = DcnbKeHoachDcHdr.TABLE_NAME + "_SEQ")
  private Long id;
  @Column(name = "PARENT_ID")
  private Long parentId;
  @Column(name = "LOAI_DC")
  private String loaiDc;
  @Column(name = "TEN_LOAI_DC")
  private String tenLoaiDc;
  @Column(name = "TYPE")
  private String type; // DC, NDC, NDCTS
  @Column(name = "NAM")
  private Integer nam;
  @Column(name = "SO_DXUAT")
  private String soDxuat;
  @Column(name = "NGAY_LAP_KH")
  private LocalDate ngayLapKh;
  @Column(name = "NGAY_DUYET_LDCC")
  private LocalDate ngayDuyetLdcc;
  @Column(name = "NGUOI_DUYET_LDCC_ID")
  private Long nguoiDuyetLdccId;
  @Column(name = "TRICH_YEU")
  private String trichYeu;
  @Column(name = "LY_DO_DC")
  private String lyDoDc;
  @Column(name = "MA_DVI")
  @Access(value=AccessType.PROPERTY)
  private String maDvi;
  @Column(name = "MA_DVI_PQ")
  private String maDviPq;
  @Column(name = "TEN_DVI")
  private String tenDvi;
  @Column(name = "MA_CUC_NHAN")
  private String maCucNhan;
  @Column(name = "TEN_CUC_NHAN")
  private String tenCucNhan;
  @Column(name = "TRACH_NHIEM_DVI_TH")
  private String trachNhiemDviTh;
  @Column(name = "TRANG_THAI")
  @Access(value=AccessType.PROPERTY)
  private String trangThai;
  @Column(name = "LY_DO_TU_CHOI")
  private String lyDoTuChoi;
  @Column(name = "ID_THOP")
  private Long idThop;
  @Column(name = "MA_THOP")
  private String maThop;
  @Column(name = "ID_QD_DC")
  private Long idQdDc;
  @Column(name = "SO_QD_DC")
  private String soQdDc;
  @Column(name = "NGAY_GDUYET")
  private LocalDate ngayGduyet;
  @Column(name = "NGUOI_GDUYET_ID")
  private Long nguoiGduyetId;
  @Column(name = "NGAY_PDUYET")
  private LocalDate ngayPduyet;
  @Column(name = "NGUOI_PDUYET_ID")
  private Long nguoiPduyetId;
  @Column(name = "MA_DVI_CUC")
  private String maDviCuc;
  @Column(name = "TEN_DVI_CUC")
  private String tenDviCuc;
  @Column(name = "TONG_DU_TOAN_KP")
  private BigDecimal tongDuToanKp;
  @Column(name = "DA_XDINH_DIEM_NHAP")
  private Boolean daXdinhDiemNhap;
  @Transient
  private String tenTrangThai;
  @Transient
  private List<FileDinhKem> canCu = new ArrayList<>();

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "HDR_ID")
  private List<DcnbKeHoachDcDtl> danhSachHangHoa = new ArrayList<>();

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "KE_HOACH_DC_HDR_ID")
  private List<DcnbPhuongAnDc> phuongAnDieuChuyen = new ArrayList<>();

  public void setTrangThai(String trangThai) {
    this.trangThai = trangThai;
    this.tenTrangThai = TrangThaiAllEnum.getLabelById(this.trangThai);
  }
}
