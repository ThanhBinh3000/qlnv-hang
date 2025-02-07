package com.tcdt.qlnvhang.table.dieuchuyennoibo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.entities.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;


@Entity
@Table(name = DcnbBcKqDcDtl.TABLE_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DcnbBcKqDcDtl extends BaseEntity implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "DCNB_BB_KQ_DC_DTL";


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DcnbBcKqDcDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = DcnbBcKqDcDtl.TABLE_NAME
            + "_SEQ", allocationSize = 1, name = DcnbBcKqDcDtl.TABLE_NAME + "_SEQ")
    private Long id;
    @Column(name = "HDR_ID", insertable = true, updatable = true)
    private Long hdrId;
    @Column(name = "LOAI_VTHH")
    private String loaiVthh;
    @Column(name = "CLOAI_VTHH")
    private String cloaiVthh;
    @Column(name = "TEN_LOAI_VTHH")
    private String tenLoaiVthh;
    @Column(name = "TEN_CLOAI_VTHH")
    private String tenCloaiVthh;
    @Column(name = "MA_DIEM_KHO")
    private String maDiemKho;
    @Column(name = "TEN_DIEM_KHO")
    private String tenDiemKho;
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

    @Column(name = "MA_DVI_NHAN")
    private String maDviNhan;
    @Column(name = "TEN_DVI_NHAN")
    private String tenDviNhan;
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

    @Column(name = "DON_VI_TINH")
    private String donViTinh;
    @Column(name = "SL_TON")
    private BigDecimal slTon;
    @Column(name = "SL_DIEU_CHUYEN_QD")
    private BigDecimal slDieuChuyenQd;
    @Column(name = "SL_XUAT_TT")
    private BigDecimal slXuatTt;
    @Column(name = "SL_NHAP_TT")
    private BigDecimal slNhapTt;
    @Column(name = "KINH_PHI_THEO_QD")
    private BigDecimal kinhPhiTheoQd;
    @Column(name = "KINH_PHI_XUAT_TT")
    private BigDecimal kinhPhiXuatTt;
    @Column(name = "KINH_PHI_NHAP_TT")
    private BigDecimal kinhPhiNhapTt;
    @Column(name = "TRICH_YEU")
    private BigDecimal trichYeu;
    @Column(name = "KET_QUA")
    private String ketQua;
    @Column(name = "TINH_TRANG")
    private Boolean tinhTrang;
    @Column(name = "NAM_NHAP")
    private BigDecimal namNhap;
    @Column(name = "TYPE")
    private String type; // CHI_CUC, CUC
    @Column(name = "KE_HOACH_DC_DTL_ID")
    private Long keHoachDcDtlId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HDR_ID", insertable = false, updatable = false)
    @JsonIgnore
    private DcnbBcKqDcHdr dcnbBcKqDcHdr;

    public DcnbBcKqDcDtl(String loaiVthh, String cloaiVthh, String tenLoaiVthh, String tenCloaiVthh, String maDiemKho, String tenDiemKho, String maNhaKho, String tenNhaKho, String maNganKho, String tenNganKho, String maLoKho, String tenLoKho,
                         String maDviNhan, String tenDviNhan, String maDiemKhoNhan, String tenDiemKhoNhan, String maNhaKhoNhan, String tenNhaKhoNhan, String maNganKhoNhan, String tenNganKhoNhan, String maLoKhoNhan, String tenLoKhoNhan,
                         String donViTinh, BigDecimal slTon, BigDecimal slDieuChuyenQd, BigDecimal slNhapTt, BigDecimal kinhPhiTheoQd, BigDecimal kinhPhiNhapTt, Long keHoachDcDtlId) {
        this.loaiVthh = loaiVthh;
        this.cloaiVthh = cloaiVthh;
        this.tenLoaiVthh = tenLoaiVthh;
        this.tenCloaiVthh = tenCloaiVthh;
        this.maDiemKho = maDiemKho;
        this.tenDiemKho = tenDiemKho;
        this.maNhaKho = maNhaKho;
        this.tenNhaKho = tenNhaKho;
        this.maNganKho = maNganKho;
        this.tenNganKho = tenNganKho;
        this.maLoKho = maLoKho;
        this.tenLoKho = tenLoKho;
        this.maDviNhan = maDviNhan;
        this.tenDviNhan = tenDviNhan;
        this.maDiemKhoNhan = maDiemKhoNhan;
        this.tenDiemKhoNhan = tenDiemKhoNhan;
        this.maNhaKhoNhan = maNhaKhoNhan;
        this.tenNhaKhoNhan = tenNhaKhoNhan;
        this.maNganKhoNhan = maNganKhoNhan;
        this.tenNganKhoNhan = tenNganKhoNhan;
        this.maLoKhoNhan = maLoKhoNhan;
        this.tenLoKhoNhan = tenLoKhoNhan;
        this.donViTinh = donViTinh;
        this.slTon = slTon;
        this.slDieuChuyenQd = slDieuChuyenQd;
        this.slNhapTt = slNhapTt;
        this.kinhPhiTheoQd = kinhPhiTheoQd;
        this.kinhPhiNhapTt = kinhPhiNhapTt;
        this.ketQua = "";
        this.tinhTrang = false;
        this.type = null;
        this.keHoachDcDtlId = keHoachDcDtlId;
    }
}
