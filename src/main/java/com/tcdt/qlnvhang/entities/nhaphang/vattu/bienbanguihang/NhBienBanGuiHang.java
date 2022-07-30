package com.tcdt.qlnvhang.entities.nhaphang.vattu.bienbanguihang;

import com.tcdt.qlnvhang.entities.BaseEntity;
import com.tcdt.qlnvhang.entities.TrangThaiBaseEntity;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BIEN_BAN_GUI_HANG_SEQ")
    @SequenceGenerator(sequenceName = "BIEN_BAN_GUI_HANG_SEQ", allocationSize = 1, name = "BIEN_BAN_GUI_HANG_SEQ")
    @Column(name = "ID")
    private Long id;

    @Column(name = "MA_DVI")
    private String maDvi;

    @Column(name = "CAP_DVI")
    private String capDvi;

    @Column(name = "PHIEU_NK_TG_ID")
    private Long phieuNkTgId;

    @Column(name = "SO_BIEN_BAN")
    private String soBienBan;

    @Column(name = "HOP_DONG_ID")
    private Long hopDongId;

    @Column(name = "NGAY_HOP_DONG")
    private LocalDate ngayHopDong;

    @Column(name = "DON_VI_CUNG_CAP")
    private String donViCungCap;

    @Column(name = "NGAY_GUI")
    private LocalDate ngayGui;

    @Column(name = "LOAI_VTHH")
    private String loaiVthh;

    @Column(name = "MA_VAT_TU_CHA")
    private String maVatTuCha;

    @Column(name = "MA_VAT_TU")
    private String maVatTu;

    @Column(name = "SO_LUONG")
    private BigDecimal soLuong;

    @Column(name = "DON_VI_TINH")
    private String donViTinh;

    @Column(name = "TINH_TRANG")
    private String tinhTrang;

    @Column(name = "CHAT_LUONG")
    private String chatLuong;

    @Column(name = "GHI_CHU")
    private String ghiChu;

    @Column(name = "NGUOI_GUI_DUYET_ID")
    private Long nguoiGuiDuyetId;

    @Column(name = "NGAY_GUI_DUYET")
    private LocalDate ngayGuiDuyet;

    @Column(name = "NGUOI_PDUYET_ID")
    private Long nguoiPduyetId;

    @Column(name = "NGAY_PDUYET")
    private LocalDate ngayPduyet;

    @Column(name = "QDGNVNX_ID")
    private Long qdgnvnxId;

    @Column(name = "BEN_NHAN")
    private String benNhan;

    @Column(name = "BEN_GIAO")
    private String benGiao;

    @Column(name = "TRACH_NHIEM_BEN_NHAN")
    private String trachNhiemBenNhan;

    @Column(name = "TRACH_NHIEM_BEN_GIAO")
    private String trachNhiemBenGiao;

    @Column(name = "THOI_GIAN")
    private LocalDateTime thoiGian;

    private Integer so;
    private Integer nam;

    @Transient
    private List<NhBienBanGuiHangCt> chiTiets = new ArrayList<>();
}
