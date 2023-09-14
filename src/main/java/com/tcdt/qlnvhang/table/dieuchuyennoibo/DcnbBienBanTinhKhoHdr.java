package com.tcdt.qlnvhang.table.dieuchuyennoibo;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = DcnbBienBanTinhKhoHdr.TABLE_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DcnbBienBanTinhKhoHdr extends BaseEntity implements Serializable, Cloneable{
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "DCNB_BIEN_BAN_TINH_KHO_HDR";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DcnbBienBanTinhKhoHdr.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = DcnbBienBanTinhKhoHdr.TABLE_NAME + "_SEQ", allocationSize = 1, name = DcnbBienBanTinhKhoHdr.TABLE_NAME + "_SEQ")
    private Long id;

    @Column(name = "LOAI_DC")
    private String loaiDc;

    @Column(name = "NAM")
    private Integer nam;

    @Column(name = "SO_BB_TINH_KHO")
    private String soBbTinhKho;

    @Column(name = "BANG_KE_CAN_HANG_ID")
    private Long bangKeCanHangId;

    @Column(name = "SO_BANG_KE")
    private String soBangKe;

    @Column(name = "NGAY_LAP")
    private LocalDate ngayLap;

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
    private LocalDate ngayKetThucXuat;

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

    @Column(name = "LOAI_VTHH")
    private String loaiVthh;

    @Column(name = "CLOAI_VTHH")
    private String cloaiVthh;

    @Column(name = "TEN_LOAI_VTHH")
    private String tenLoaiVthh;

    @Column(name = "TEN_CLOAI_VTHH")
    private String tenCloaiVthh;

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

    @Column(name = "THU_KHO")
    private String thuKho;

    @Column(name = "THU_KHO_ID")
    private Long thuKhoId;

    @Column(name = "THAY_DOI_THU_KHO")
    private Boolean thayDoiThuKho;

    @Column(name = "TRANG_THAI")
    @Access(value=AccessType.PROPERTY)
    private String trangThai;

    @Column(name = "LY_DO_TU_CHOI")
    private String lyDoTuChoi;

    @Column(name = "NGUOI_GDUYET")
    private Long nguoiGDuyet;

    @Column(name = "NGAY_GDUYET")
    private LocalDate ngayGDuyet;

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

    @Column(name = "NGAY_PDUYET_LDCCUC")
    private LocalDate ngayPduyetLdcc;

    @Column(name = "LANH_DAO_CHI_CUC")
    private String lanhDaoChiCuc;

    @Column(name = "LANH_DAO_CHI_CUC_ID")
    private Long lanhDaoChiCucId;

    @Column(name = "TONG_SL_XUAT_THEO_QD")
    private BigDecimal tongSlXuatTheoQd;

    @Column(name = "TONG_SL_XUAT_THEO_TT")
    private BigDecimal tongSlXuatTheoTt;

    @Column(name = "SL_CON_LAI_THEO_SS")
    private BigDecimal slConLaiTheoSs;

    @Column(name = "SL_CON_LAI_THEO_TT")
    private BigDecimal slConLaiTheoTt;

    @Column(name = "CHENH_LECH_SL_CON_LAI")
    private BigDecimal chenhLechSlConLai;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "SO_PHIEU_KN_CHAT_LUONG")
    private String soPhieuKnChatLuong;
    @Column(name = "PHIEU_KN_CHAT_LUONG_HDR_ID")
    private Long phieuKnChatLuongHdrId;
    @Column(name = "KE_HOACH_DC_DTL_ID")
    private Long keHoachDcDtlId;
    @Transient
    private List<FileDinhKem> fileBbTinhKhoDaKy = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "HDR_ID")
    private List<DcnbBienBanTinhKhoDtl> dcnbBienBanTinhKhoDtl = new ArrayList<>();
    @Transient
    private String tenTrangThai;
    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
        this.tenTrangThai = TrangThaiAllEnum.getLabelById(this.trangThai);
    }
}
