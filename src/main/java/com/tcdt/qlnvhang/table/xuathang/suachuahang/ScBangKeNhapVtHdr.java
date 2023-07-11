package com.tcdt.qlnvhang.table.xuathang.suachuahang;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.enums.TrangThaiAllEnum;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBangKeNhapVTDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBangKeNhapVTHdr;
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
@Table(name = ScBangKeNhapVtHdr.TABLE_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScBangKeNhapVtHdr extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "SC_BANG_KE_NHAP_VT_HDR";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ScBangKeNhapVtHdr.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = ScBangKeNhapVtHdr.TABLE_NAME + "_SEQ", allocationSize = 1, name = ScBangKeNhapVtHdr.TABLE_NAME + "_SEQ")
    private Long id;
    private Integer nam;
    private String maDvi;
    private String tenDvi;
    private Long qhnsId;
    private String maQhns;
    private String soBangKe;
    private LocalDate ngayNhap;
    private Long qdGiaoNvNhapId;
    private String soQdGiaoNvNhap;
    private LocalDate ngayKyQdGiaoNvNhap;
    private Long phieuNhapKhoId;
    private String soPhieuNhapKho;
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
    @Column(name = "MA_LOAI_HANG")
    private String maLoaiHang;
    @Column(name = "MA_CLOAI_HANG")
    private String maCloaiHang;
    @Column(name = "TEN_LOAI_VTHH")
    private String tenLoaiHang;
    @Column(name = "TEN_CLOAI_VTHH")
    private String tenCloaiHang;
    @Column(name = "DON_VI_TINH")
    private String donViTinh;
    @Column(name = "TRANG_THAI")
    @Access(value=AccessType.PROPERTY)
    private String trangThai;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "HDR_ID")
    private List<ScBangKeNhapVtDtl> scBangKeNhapVtDtls = new ArrayList<>();
    @Transient
    private String tenTrangThai;
    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
        this.tenTrangThai = TrangThaiAllEnum.getLabelById(this.trangThai);
    }
}
