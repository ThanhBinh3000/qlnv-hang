package com.tcdt.qlnvhang.table.dieuchuyennoibo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = DcnbBbThuaThieuDtl.TABLE_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DcnbBbThuaThieuDtl {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "DCNB_BB_THUA_THIEU_DTL";


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DcnbBbThuaThieuDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = DcnbBbThuaThieuDtl.TABLE_NAME
            + "_SEQ", allocationSize = 1, name = DcnbBbThuaThieuDtl.TABLE_NAME + "_SEQ")
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
    @Column(name = "DON_VI_TINH")
    private String donViTinh;
    @Column(name = "NAM_NHAP")
    private BigDecimal namNhap;

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
    @Column(name = "SL_THUA")
    private BigDecimal slThua;
    @Column(name = "KP_THUA")
    private BigDecimal kpThua;
    @Column(name = "SL_THIEU")
    private BigDecimal slThieu;
    @Column(name = "KP_THIEU")
    private BigDecimal kpThieu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HDR_ID", insertable = false, updatable = false)
    @JsonIgnore
    private DcnbBbThuaThieuHdr dcnbBbThuaThieuHdr;
}
