package com.tcdt.qlnvhang.entities.vattu.phieunhapkho;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "NH_PHIEU_NHAP_KHO_VT_CT")
@Data
@NoArgsConstructor
public class NhPhieuNhapKhoVtCt implements Serializable {

    private static final long serialVersionUID = -7535197300286109582L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PHIEU_NHAP_KHO_VT_CT_SEQ")
    @SequenceGenerator(sequenceName = "PHIEU_NHAP_KHO_VT_CT_SEQ", allocationSize = 1, name = "PHIEU_NHAP_KHO_VT_CT_SEQ")
    @Column(name = "ID")
    private Long id;

    @Column(name = "PHIEU_NK_ID")
    private Long phieuNkId; // NH_PHIEU_NHAP_KHO_VT

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

    @Column(name = "THANH_TIEN")
    private BigDecimal thanhTien;

    @Column(name = "VTHH")
    private String vthh;

    @Column(name = "STT")
    private Integer stt;
}
