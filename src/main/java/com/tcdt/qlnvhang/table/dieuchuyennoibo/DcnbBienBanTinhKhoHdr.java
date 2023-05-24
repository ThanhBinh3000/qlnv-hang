package com.tcdt.qlnvhang.table.dieuchuyennoibo;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = DcnbBienBanTinhKhoHdr.TABLE_NAME)
@Getter
@Setter
public class DcnbBienBanTinhKhoHdr extends BaseEntity implements Serializable, Cloneable{
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "DCNB_BIEN_BAN_TINH_KHO_HDR";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DcnbBienBanLayMauDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = DcnbBienBanLayMauDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = DcnbBienBanLayMauDtl.TABLE_NAME + "_SEQ")
    private Long id;

    @Column(name = "NAM")
    private Integer nam;

    @Column(name = "SO_BB_TINH_KHO")
    private String soBbTinhKho;

    @Column(name = "BANG_KE_CAN_HANG_ID")
    private Long bangKeCanHangId;

    @Column(name = "SO_BANG_KE")
    private String soBangKe;

    @Column(name = "NGAY_NHAP")
    private LocalDate ngayNhap;

    @Column(name = "MA_DVI")
    private String maDvi;

    @Column(name = "TEN_DVI")
    private String tenDvi;

    @Column(name = "QHNS_ID")
    private Long qhnsId;

    @Column(name = "MA_QHNS")
    private String maQhns;

    @Column(name = "NGAY_BAT_DAU_XUAT")
    private LocalDate ngayBatDauXuat;

    @Column(name = "NGAY_KET_THUC_XUAT")
    private LocalDate ngayKeThucXuat;

    @Column(name = "THOI_HAN_XUAT_HANG")
    private LocalDate thoiHanXuatHang;

    @Column(name = "QDINH_DCC_ID")
    private Long qDinhDccId;

    @Column(name = "SO_QDINH_DCC")
    private String soQdinhDcc;

    @Column(name = "NGAY_XUAT_KHO")
    private LocalDate ngayXuatKho;

    @Column(name = "THOI_HAN_DIEU_CHUYEN")
    private LocalDate thoiHanDieuChuyen;

    @Column(name = "NGAY_KY_QD_DCC")
    private LocalDate ngayKyQdDcc;

    @Column(name = "PHIEU_XUAT_KHO_ID")
    private Long phieuXuatKhoId;

    @Column(name = "SO_PHIEU_XUAT_KHO")
    private String soPhieuXuatKho;

    @Column(name = "SO_BB_LAY_MAU")
    private String soBbLayMau;

    @Column(name = "LOAI_VTHH")
    private String loaiVthh;

    @Column(name = "CLOAI_VTHH")
    private String cloaiVthh;

    @Column(name = "DON_VI_TINH")
    private String donViTinh;

    @Column(name = "MA_DIEM_KHO")
    private String maDiemKho;

    @Column(name = "TEN_DIEM_KHO")
    private String tenDiemKho;

    @Column(name = "DIA_DIEM_KHO")
    private String diaDaDiemKho;

    @Column(name = "MA_NHA_KHO")
    private String maNhaKho;

    @Column(name = "TEN_NHA_KHO")
    private String tenNhaKho;

    @Column(name = "MA_NGAN_KHO")
    private String maNganKho;

    @Column(name = "TEN_NGAN_KHO")
    private String tenNganKho;

    @Column(name = "MA_LO_KHO")
    private String maLoKho;

    @Column(name = "TEN_LO_KHO")
    private String tenLoKho;

    @Column(name = "THAY_DOI_THU_KHO")
    private Boolean thayDoiThuKho;

    @Column(name = "TRANG_THAI")
    private String trangThai;

    @Column(name = "LY_DO_TU_CHOI")
    private String lyDoTuChoi;

    @Column(name = "NGUOI_GDUYET")
    private Long nguoiGDuyet;

    @Column(name = "NGAY_GDUYET")
    private LocalDate ngayGDuyet;

    @Column(name = "NGUOI_PDUYET")
    private Long nguoiPDuyet;

    @Column(name = "NGAY_PDUYET")
    private LocalDate ngayPDuyet;

    @Column(name = "LOAI_DC")
    private String loaiDc;

    @Column(name = "NGUYEN_NHAN")
    private String nguyeNhan;

    @Column(name = "KIEN_NGHI")
    private String kienNghi;

    @Column(name = "GHI_CHU")
    private String ghiChu;

    @Column(name = "NGAY_PDUYET_KTV_BAO_QUAN")
    private LocalDate ngayPduyetKtvBQ;

    @Column(name = "KTV_BAO_QUAN")
    private String ktvBaoQuan;

    @Column(name = "KTV_BAO_QUAN_ID")
    private Long ktvBaoQuanId;

    @Column(name = "NGAY_PDUYET_KE_TOAN")
    private LocalDate ngayPduyetKt;

    @Column(name = "KE_TOAN")
    private String keToan;

    @Column(name = "KE_TOAN_ID")
    private Long keToanId;

    @Column(name = "NGAY_PDUYET_LDCC")
    private LocalDate ngayPduyetLdcc;

    @Column(name = "LANH_DAO_CHI_CUC")
    private String lanhDaoChiCuc;

    @Column(name = "LANH_DAO_CHI_CUC_ID")
    private Long lanhDaoChiCucId;

    @Column(name = "TONG_SL_XUAT_THEO_QD")
    private Double tongSlXuatTheoQd;

    @Column(name = "TONG_SL_XUAT_THEO_TT")
    private Double tongSlXuatTheoTt;

    @Column(name = "SL_CON_LAI_THEO_SS")
    private Double slConLaiTheoSs;

    @Column(name = "SL_CON_LAI_THEO_TT")
    private Double slConLaiTheoTt;

    @Column(name = "CHENH_LECH_SL_CON_LAI")
    private Double chenhLechSlConLai;

    @Transient
    private List<FileDinhKem> fileBbTinhKhoDaKy = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "HDR_ID")
    private List<DcnbBienBanTinhKhoDtl> dcnbBienBanTinhKhoDtl = new ArrayList<>();
}
