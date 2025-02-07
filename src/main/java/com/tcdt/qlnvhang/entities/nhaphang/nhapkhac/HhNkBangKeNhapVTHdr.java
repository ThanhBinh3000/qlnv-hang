package com.tcdt.qlnvhang.entities.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBienBanLayMauHdr;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = HhNkBangKeNhapVTHdr.TABLE_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HhNkBangKeNhapVTHdr extends BaseEntity implements Serializable, Cloneable{
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "HHNK_BANG_KE_NHAP_VT_HDR";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DcnbBienBanLayMauHdr.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = DcnbBienBanLayMauHdr.TABLE_NAME + "_SEQ", allocationSize = 1, name = DcnbBienBanLayMauHdr.TABLE_NAME + "_SEQ")
    private Long id;
    @Column(name = "NAM")
    private Integer nam;
    @Column(name = "MA_DVI")
    private String maDvi;
    @Column(name = "TEN_DVI")
    private String tenDvi;
    @Column(name = "MA_QHNS")
    private String maQhns;
    @Column(name = "SO_BANG_KE")
    private String soBangKe;
    @Column(name = "NGAY_TAO_BK")
    private LocalDate ngayTaoBk;
    private Long qdPdNkId;
    private String soQdPdNk;
    private LocalDate ngayQdPdNk;
    @Column(name = "PHIEU_NHAP_KHO_ID")
    private Long phieuNhapKhoId;
    @Column(name = "SO_PHIEU_NHAP_KHO")
    private String soPhieuNhapKho;
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
    @Column(name = "DIA_DIEM_KHO")
    private String diaDiemKho;
    @Column(name = "LOAI_VTHH")
    private String loaiVthh;
    @Column(name = "CLOAI_VTHH")
    private String cloaiVthh;
    @Column(name = "TEN_LOAI_VTHH")
    private String tenLoaiVthh;
    @Column(name = "TEN_CLOAI_VTHH")
    private String tenCloaiVthh;
    @Column(name = "THU_KHO_ID")
    private Long thuKhoId;
    @Column(name = "TEN_THU_KHO")
    private String tenThuKho;
    @Column(name = "NGUOI_PDUYET_TVQT")
    private Long nguoiPDuyetTvqt;
    @Column(name = "NGAY_PDUYET_TVQT")
    private LocalDate ngayPDuyetTvqt;
    @Column(name = "MA_LANH_DAO_CHI_CUC")
    private String maLanhDaoChiCuc;
    @Column(name = "TEN_LANH_DAO_CHI_CUC")
    private String tenLanhDaoChiCuc;
    @Column(name = "TEN_NGUOI_GIAO_HANG")
    private String tenNguoiGiaoHang;
    @Column(name = "CCCD")
    private String cccd;
    @Column(name = "DON_VI_NGUOI_GIAO_HANG")
    private String donViNguoiGiaoHang;
    @Column(name = "DIA_CHI_DON_VI_NGUOI_GIAO_HANG")
    private String diaChiDonViNguoiGiaoHang;
    @Column(name = "THOI_HAN_GIAO_NHAN")
    private LocalDate thoiHanGiaoNhan;
    @Column(name = "DON_VI_TINH")
    private String donViTinh;
    @Column(name = "TRANG_THAI")
    @Access(value=AccessType.PROPERTY)
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

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "HDR_ID")
    private List<HhNkBangKeNhapVTDtl> hhNkBangKeNhapVTDtl = new ArrayList<>();
    @Transient
    private String tenTrangThai;
    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
        this.tenTrangThai = TrangThaiAllEnum.getLabelById(this.trangThai);
    }
}
