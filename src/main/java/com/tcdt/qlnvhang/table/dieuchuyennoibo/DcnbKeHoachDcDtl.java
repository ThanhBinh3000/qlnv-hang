package com.tcdt.qlnvhang.table.dieuchuyennoibo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = DcnbKeHoachDcDtl.TABLE_NAME)
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
public class DcnbKeHoachDcDtl implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "DCNB_KE_HOACH_DC_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DcnbKeHoachDcDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = DcnbKeHoachDcDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = DcnbKeHoachDcDtl.TABLE_NAME + "_SEQ")
    private Long id;
    @Column(name = "PARENT_ID")
    private Long parentId;
    @Column(name = "MA_CHI_CUC_NHAN")
    private String maChiCucNhan;
    @Column(name = "TEN_CHI_CUC_NHAN")
    private String tenChiCucNhan;
    @Column(name = "THOI_GIAN_DK_DC")
    private LocalDate thoiGianDkDc;
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
    @Column(name = "TEN_DON_VI_TINH")
    private String tenDonViTinh;
    @Column(name = "TON_KHO")
    private BigDecimal tonKho;
    @Column(name = "SO_LUONG_DC")
    private BigDecimal soLuongDc;
    @Column(name = "DU_TOAN_KPHI")
    private BigDecimal duToanKphi;
    @Column(name = "TICH_LUONG_KD")
    private BigDecimal tichLuongKd;
    @Column(name = "SO_LUONG_PHAN_BO")
    private BigDecimal soLuongPhanBo;
    @Column(name = "SL_DC_CON_LAI")
    private BigDecimal slDcConLai;
    @Column(name = "CO_LO_KHO")
    private Boolean coLoKho;
    @Column(name = "MA_DIEM_KHO")
    private String maDiemKho;
    @Column(name = "TEN_DIEM_KHO")
    private String tenDiemKho;
    @Column(name = "MA_NHA_KHO")
    @Access(value=AccessType.PROPERTY)
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
    @Column(name = "MA_DIEM_KHO_NHAN")
    private String maDiemKhoNhan;
    @Column(name = "TEN_DIEM_KHO_NHAN")
    private String tenDiemKhoNhan;
    @Column(name = "MA_NHA_KHO_NHAN")
    private String maNhaKhoNhan;
    @Column(name = "TEN_NHA_KHO_NHAN")
    private String tenNhaKhoNhan;
    @Column(name = "MA_NGAN_KHO_NHAN")
    private String maNganKhoNhan;
    @Column(name = "TEN_NGAN_KHO_NHAN")
    private String tenNganKhoNhan;
    @Column(name = "MA_LO_KHO_NHAN")
    private String maLoKhoNhan;
    @Column(name = "TEN_LO_KHO_NHAN")
    private String tenLoKhoNhan;
    @Column(name = "CO_LO_KHO_NHAN")
    private Boolean coLoKhoNhan;
    @Column(name = "DA_XDINH_DIEM_NHAP")
    private Boolean daXdinhDiemNhap;
    @Column(name = "XD_LAI_DIEM_NHAP")
    private Boolean xdLaiDiemNhap;
    @Column(name = "BB_LAY_MAU_ID")
    private Long bbLayMauId;
//    @Column(name = "SO_BB_LAY_MAU")
//    private String soBbLayMau;
//    @Column(name = "PHIEU_KN_CHAT_LUONG_ID")
//    private Long phieuKnChatLuongId;
//    @Column(name = "SO_PHIEU_KN_CHAT_LUONG")
//    private String soPhieuKnChatLuong;
//    @Column(name = "BANG_KE_CAN_HANG_ID")
//    private Long bangKeCanHangId;
//    @Column(name = "SO_BANG_KE_CAN_HANG")
//    private String soBangKeCanHang;
//    @Column(name = "BIEN_BAN_TINH_KHO_ID")
//    private Long bienBanTinhKhoId;
//    @Column(name = "SO_BIEN_BAN_TINH_KHO")
//    private String soBienBanTinhKho;
    @Column(name = "THU_KHO")
    private String thuKho;
    @Column(name = "THU_KHO_NHAN")
    private String thuKhoNhan;
    @Column(name = "THAY_DOI_THU_KHO")
    private Boolean thayDoiThuKho;

    @Column(name = "HDR_ID", insertable = true, updatable = true)
    private Long hdrId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HDR_ID", insertable = false, updatable = false)
    @JsonIgnore
    private DcnbKeHoachDcHdr dcnbKeHoachDcHdr;
    @Transient
    private DcnbBienBanLayMauHdr dcnbBienBanLayMauHdr;
    @Transient
    private DcnbKeHoachDcDtlTT dcnbKeHoachDcDtlTT;
}
