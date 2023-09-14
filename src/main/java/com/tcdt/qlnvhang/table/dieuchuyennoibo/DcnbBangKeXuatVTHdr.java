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
@Table(name = DcnbBangKeXuatVTHdr.TABLE_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DcnbBangKeXuatVTHdr extends BaseEntity implements Serializable, Cloneable{
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "DCNB_BANG_KE_XUAT_VT_HDR";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DcnbBangKeXuatVTHdr.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = DcnbBangKeXuatVTHdr.TABLE_NAME + "_SEQ", allocationSize = 1, name = DcnbBangKeXuatVTHdr.TABLE_NAME + "_SEQ")
    private Long id;
    @Column(name = "LOAI_DC")
    private String loaiDc;
    @Column(name = "LOAI_VTHH")
    private String loaiVthh;
    @Column(name = "CLOAI_VTHH")
    private String cloaiVthh;
    @Column(name = "NAM")
    private Integer nam;
    @Column(name = "SO_BANG_KE")
    private String soBangKe;
    @Column(name = "NGAY_NHAP")
    private LocalDate ngayNhap;
    @Column(name = "MA_DVI")
    private String maDvi;
    @Column(name = "TEN_MA_DVI")
    private String tenDvi;
    @Column(name = "QHNS_ID")
    private Long qhnsId;
    @Column(name = "MA_QHNS")
    private String maQhns;
    @Column(name = "QDINH_DCC_ID")
    private Long qDinhDccId;
    @Column(name = "SO_QDINH_DCC")
    private String soQdinhDcc;
    @Column(name = "NGAY_KY_QDINH_DCC")
    private LocalDate ngayKyQdinhDcc;
    @Column(name = "SO_HOP_DONG")
    private String soHopDong;
    @Column(name = "PHIEU_XUAT_KHO_ID")
    private Long phieuXuatKhoId;
    @Column(name = "SO_PHIEU_XUAT_KHO")
    private String soPhieuXuatKho;
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
    @Column(name = "MA_LANH_DAO_CHI_CUC")
    private String maLanhDaoChiCuc;
    @Column(name = "TEN_LANH_DAO_CHI_CUC")
    private String tenLanhDaoChiCuc;
    @Column(name = "THU_KHO_ID")
    private Long thuKhoId;
    @Column(name = "TEN_THU_KHO")
    private String tenThuKho;
    @Column(name = "PHU_TRACH_ID")
    private Long phuTrachId;
    @Column(name = "TEN_PHU_TRACH")
    private String tenPhuTrach;
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
    @Column(name = "TYPE")
    private String type;
    @Column(name = "KE_HOACH_DC_DTL_ID")
    private Long keHoachDcDtlId;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "HDR_ID")
    private List<DcnbBangKeXuatVTDtl> dcnbBangKeXuatVTDtl = new ArrayList<>();
    @Transient
    private String tenTrangThai;
    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
        this.tenTrangThai = TrangThaiAllEnum.getLabelById(this.trangThai);
    }
}
