package com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bangke;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "NH_BANG_KE_VT")
@EqualsAndHashCode(callSuper = false)
public class NhBangKeVt extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 5802077466808854815L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BANG_KE_VT_SEQ")
    @SequenceGenerator(sequenceName = "BANG_KE_VT_SEQ", allocationSize = 1, name = "BANG_KE_VT_SEQ")
    @Column(name = "ID")
    private Long id;

    @Column(name = "QDGNVNX_ID")
    private Long qdgnvnxId;

    @Column(name = "SO_BANG_KE")
    private String soBangKe;

    @Column(name = "DIA_CHI_NGUOI_GIAO")
    private String diaChiNguoiGiao;

    @Column(name = "NAM")
    private Long nam;

    @Column(name = "LOAI_VTHH")
    private String loaiVthh;

    @Column(name = "CLOAI_VTHH")
    private String cloaiVthh;

    @Column(name = "NGAY_NHAP_KHO")
    private LocalDate ngayNhapKho;

    @Column(name = "SO_QD_GIAO_NV_NH")
    private String soQdGiaoNvNh;

    @Column(name = "ID_QD_GIAO_NV_NH")
    private Long idQdGiaoNvNh;

    @Column(name = "SO_HD")
    private String soHd;

    @Column(name = "NGAY_HD")
    private LocalDate ngayHd;

    @Column(name = "SO_PHIEU_NHAP_KHO")
    private String soPhieuNhapKho;

    @Column(name = "ID_DDIEM_GIAO_NV_NH")
    private Long idDdiemGiaoNvNh;

    @Column(name = "MA_DIEM_KHO")
    private String maDiemKho;

    @Column(name = "MA_NHA_KHO")
    private String maNhaKho;

    @Column(name = "MA_NGAN_KHO")
    private String maNganKho;

    @Column(name = "MA_LO_KHO")
    private String maLoKho;

    @Column(name = "NGUOI_GIAO_HANG")
    private String nguoiGiaoHang;

    @Column(name = "CMT_NGUOI_GIAO_HANG")
    private String cmtNguoiGiaoHang;

    @Column(name = "DON_VI_GIAO_HANG")
    private String donViGiaoHang;

    @Column(name = "THOI_GIAN_GIAO_NHAN")
    private LocalDate thoiGianGiapNhan;

    @Column(name = "MA_DVI")
    private String maDvi;


    @Transient
    private String tenLoaiVthh;
    @Transient
    private String tenCloaiVthh;
    @Transient
    private String tenTrangThai;
    @Transient
    private String tenDiemKho;
    @Transient
    private String tenNhaKho;
    @Transient
    private String tenNganKho;
    @Transient
    private String tenLoKho;

    @Transient
    private List<NhBangKeVtCt> chiTiets = new ArrayList<>();
}
