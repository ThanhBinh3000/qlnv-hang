package com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bienbanguihang;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
import com.tcdt.qlnvhang.util.Contains;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "NH_BIEN_BAN_GUI_HANG")
@EqualsAndHashCode(callSuper = false)
public class NhBienBanGuiHang extends TrangThaiBaseEntity implements Serializable {
    private static final long serialVersionUID = -5870328760935333890L;
    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BIEN_BAN_GUI_HANG_SEQ")
//    @SequenceGenerator(sequenceName = "BIEN_BAN_GUI_HANG_SEQ", allocationSize = 1, name = "BIEN_BAN_GUI_HANG_SEQ")
    @Column(name = "ID")
    private Long id;

    @Column(name = "MA_DVI")
    private String maDvi;

    @Transient
    private String tenDvi;

    @Transient
    private String tenDviCha;

    @Column(name = "SO_QD_GIAO_NV_NH")
    private String soQdGiaoNvNh; // HhQdGiaoNvuNhapxuatHdr

    @Column(name = "ID_QD_GIAO_NV_NH")
    private Long idQdGiaoNvNh; // HhQdGiaoNvuNhapxuatHdr

    @Column(name = "SO_BIEN_BAN_GUI_HANG")
    private String soBienBanGuiHang;

    @Column(name = "SO_HD")
    private String soHd;

    @Column(name = "NGAY_HD")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayHd;

    @Column(name = "LOAI_VTHH")
    private String loaiVthh;

    @Transient
    private String tenLoaiVthh;

    @Column(name = "CLOAI_VTHH")
    private String cloaiVthh;

    @Transient
    private String tenCloaiVthh;

    @Column(name = "DON_VI_TINH")
    private String donViTinh;

    @Column(name = "TINH_TRANG")
    private String tinhTrang;

    @Column(name = "CHAT_LUONG")
    private String chatLuong;

    @Column(name = "PHUONG_PHAP")
    private String phuongPhap;

    @Column(name = "GHI_CHU")
    private String ghiChu;

    @Column(name = "BEN_NHAN")
    private String benNhan;

    @Column(name = "BEN_GIAO")
    private String benGiao;

    @Column(name = "TRACH_NHIEM_BEN_NHAN")
    private String trachNhiemBenNhan;

    @Column(name = "TRACH_NHIEM_BEN_GIAO")
    private String trachNhiemBenGiao;

    @Column(name = "NAM")
    private Integer nam;

    @Column(name = "ID_DDIEM_GIAO_NV_NH")
    private Long idDdiemGiaoNvNh;

    @Column(name = "MA_DIEM_KHO")
    private String maDiemKho;

    @Transient
    private String tenDiemKho;

    @Column(name = "MA_NHA_KHO")
    private String maNhaKho;

    @Transient
    private String tenNhaKho;

    @Column(name = "MA_NGAN_KHO")
    private String maNganKho;

    @Transient
    private String tenNganKho;

    @Column(name = "MA_LO_KHO")
    private String maLoKho;

    @Transient
    private String tenLoKho;

    @Column(name = "SO_LUONG_DDIEM_GIAO_NV_NH")
    private BigDecimal soLuongDdiemGiaoNvNh;

    @Transient
    private List<NhBienBanGuiHangCt> children = new ArrayList<>();
}
