package com.tcdt.qlnvhang.table.xuathang.suachuahang;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBangKeXuatVTDtl;
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
@Table(name = ScBangKeXuatVatTuHdr.TABLE_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScBangKeXuatVatTuHdr extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "SC_BANG_KE_XUAT_VT_HDR";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ScBangKeXuatVatTuHdr.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = ScBangKeXuatVatTuHdr.TABLE_NAME + "_SEQ", allocationSize = 1, name = ScBangKeXuatVatTuHdr.TABLE_NAME + "_SEQ")
    private Long id;
    @Column(name = "NAM")
    private Integer nam;
    @Column(name = "MA_DVI")
    private String maDvi;
    @Column(name = "TEN_DVI")
    private String tenDvi;
    @Column(name = "QHNS_ID")
    private Long qhnsId;
    @Column(name = "MA_QHNS")
    private String maQhns;
    @Column(name = "SO_BANG_KE")
    private String soBangKe;
    @Column(name = "NGAY_NHAP")
    private LocalDate ngayNhap;
    @Column(name = "SO_QD_GIAO_NV")
    private String soQdGiaoNv;
    @Column(name = "NGAY_KY_QD_GIAO_NV")
    private LocalDate ngayKyQdGiaoNv;
    @Column(name = "PHIEU_XUAT_KHO_ID")
    private Long phieuXuatKhoId;
    @Column(name = "SO_PHIEU_XUAT_KHO")
    private String soPhieuXuatKho;
    @Column(name = "MA_LO_KHO")
    private String maLoKho;
    @Column(name = "TEN_LO_KHO")
    private String tenLoKho;
    @Column(name = "MA_NGAN_KHO")
    private String maNganKho;
    @Column(name = "TEN_NGAN_KHO")
    private String tenNganKho;
    @Column(name = "MA_NHA_KHO")
    private String maNhaKho;
    @Column(name = "TEN_NHA_KHO")
    private String tenNhaKho;
    @Column(name = "MA_DIEM_KHO")
    private String maDiemKho;
    @Column(name = "TEN_DIEM_KHO")
    private String tenDiemKho;
    @Column(name = "DIA_DIEM_KHO")
    private String diaDiemKho;
    @Column(name = "MA_LANH_DAO_CHI_CUC")
    private String maLanhDaoChiCuc;
    @Column(name = "TEN_LANH_DAO_CHI_CUC")
    private String tenLanhDaoChiCuc;
    @Column(name = "THU_KHO_ID")
    private Long thuKhoId;
    @Column(name = "TEN_THU_KHO")
    private String tenThuKho;
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
    @Column(name = "LOAI_HANG")
    private String loaiHang;
    @Column(name = "CHUNG_LOAI_HANG")
    private String chungLoaiHang;
    @Column(name = "DON_VI_TINH")
    private String donViTinh;
    @Column(name = "TRANG_THAI")
    private String trangThai;
    @Column(name = "LY_DO_TU_CHOI")
    private String lyDoTuChoi;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "HDR_ID")
    private List<ScBangKeXuatVatTuDtl> scBangKeXuatVatTuDtls = new ArrayList<>();

}
