package com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.phieunhapkhotamgui;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "NH_PHIEU_NHAP_KHO_TAM_GUI_CT")
@Data
@NoArgsConstructor
public class NhPhieuNhapKhoTamGuiCt implements Serializable {

    private static final long serialVersionUID = -7535197300286109582L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PHIEU_NHAP_KHO_TAM_GUI_CT_SEQ")
    @SequenceGenerator(sequenceName = "PHIEU_NHAP_KHO_TAM_GUI_CT_SEQ", allocationSize = 1, name = "PHIEU_NHAP_KHO_TAM_GUI_CT_SEQ")
    @Column(name = "ID")
    private Long id;

    @Column(name = "PHIEU_NK_TG_ID")
    private Long phieuNkTgId;

    @Column(name = "MO_TA_HANG_HOA")
    private String moTaHangHoa;

    @Column(name = "MA_SO")
    private String maSo;

    @Column(name = "DON_VI_TINH")
    private String donViTinh;

    @Column(name = "SO_LUONG_CHUNG_TU")
    private BigDecimal soLuongChungTu;

    @Column(name = "SO_LUONG_THUC_NHAP")
    private BigDecimal soLuongThucNhap;

    @Column(name = "DON_GIA")
    private BigDecimal donGia;



}
