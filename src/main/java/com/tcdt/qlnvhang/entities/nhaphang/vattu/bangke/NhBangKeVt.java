package com.tcdt.qlnvhang.entities.nhaphang.vattu.bangke;

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
public class NhBangKeVt extends TrangThaiBaseEntity implements Serializable {
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

    @Column(name = "PHIEU_NHAP_KHO_ID")
    private Long phieuNhapKhoId;// NH_PHIEU_NHAP_KHO

    @Column(name = "MA_VAT_TU_CHA")
    private String maVatTuCha;

    @Column(name = "MA_VAT_TU")
    private String maVatTu;

    @Column(name = "DON_VI_TINH")
    private String donViTinh;

    @Column(name = "DIA_CHI_NGUOI_GIAO")
    private String diaChiNguoiGiao;

    @Column(name = "HOP_DONG_ID")
    private Long hopDongId;

    @Column(name = "NGAY_NHAP")
    private LocalDate ngayNhap;

    @Column(name = "NGAY_TAO_BANG_KE")
    private LocalDate ngayTaoBangKe;

    @Column(name = "NGUOI_GUI_DUYET_ID")
    private Long nguoiGuiDuyetId;

    @Column(name = "NGAY_GUI_DUYET")
    private LocalDate ngayGuiDuyet;

    @Column(name = "NGUOI_PDUYET_ID")
    private Long nguoiPduyetId;

    @Column(name = "NGAY_PDUYET")
    private LocalDate ngayPduyet;

    @Column(name = "MA_DVI")
    private String maDvi;

    @Column(name = "CAP_DVI")
    private String capDvi;

    @Column(name = "SO")
    private Integer so;

    @Column(name = "NAM")
    private Integer nam;

    @Transient
    private List<NhBangKeVtCt> chiTiets = new ArrayList<>();
}
