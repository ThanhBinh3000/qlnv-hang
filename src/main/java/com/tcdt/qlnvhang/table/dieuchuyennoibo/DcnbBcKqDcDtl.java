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
    @Column(name = "DON_VI_TINH")
    private String donViTinh;
    @Column(name = "TEN_DON_VI_TINH")
    private String tenDonViTinh;
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
    private Boolean ketQua;
    @Column(name = "TINH_TRANG")
    private Boolean tinhTrang;
    @Column(name = "TYPE")
    private String type; // CHI_CUC, CUC

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HDR_ID", insertable = false, updatable = false)
    @JsonIgnore
    private DcnbBcKqDcHdr dcnbBcKqDcHdr;

    public DcnbBcKqDcDtl(String loaiVthh, String cloaiVthh, String tenLoaiVthh, String tenCloaiVthh, String maDiemKho, String tenDiemKho, String maNhaKho, String tenNhaKho, String maNganKho, String tenNganKho, String maLoKho, String tenLoKho, String donViTinh, String tenDonViTinh, BigDecimal slTon, BigDecimal slDieuChuyenQd, BigDecimal slXuatTt, BigDecimal slNhapTt, BigDecimal kinhPhiTheoQd, BigDecimal kinhPhiXuatTt, BigDecimal kinhPhiNhapTt, Boolean ketQua, Boolean tinhTrang, String type) {
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
        this.donViTinh = donViTinh;
        this.tenDonViTinh = tenDonViTinh;
        this.slTon = slTon;
        this.slDieuChuyenQd = slDieuChuyenQd;
        this.slXuatTt = slXuatTt;
        this.slNhapTt = slNhapTt;
        this.kinhPhiTheoQd = kinhPhiTheoQd;
        this.kinhPhiXuatTt = kinhPhiXuatTt;
        this.kinhPhiNhapTt = kinhPhiNhapTt;
        this.ketQua = ketQua;
        this.tinhTrang = tinhTrang;
        this.type = type;
    }
}
