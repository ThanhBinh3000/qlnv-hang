package com.tcdt.qlnvhang.table.dieuchuyennoibo;

import com.tcdt.qlnvhang.entities.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

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

    @Column(name = "QHNS_ID")
    private Long qhnsId;

    @Column(name = "MA_QHNS")
    private String maQhns;

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

    @Column(name = "TYPE")
    private String type;
}
