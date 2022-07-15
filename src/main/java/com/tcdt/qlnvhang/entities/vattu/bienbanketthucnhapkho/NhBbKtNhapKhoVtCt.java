package com.tcdt.qlnvhang.entities.vattu.bienbanketthucnhapkho;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "NH_BB_KT_NHAP_KHO_VT_CT")
@Data
@NoArgsConstructor
public class NhBbKtNhapKhoVtCt implements Serializable {
    private static final long serialVersionUID = -7346017560391585308L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BB_KT_NHAP_KHO_VT_CT_SEQ")
    @SequenceGenerator(sequenceName = "BB_KT_NHAP_KHO_VT_CT_SEQ", allocationSize = 1, name = "BB_KT_NHAP_KHO_VT_CT_SEQ")
    @Column(name = "ID")
    private Long id;

    @Column(name = "STT")
    private Integer stt;

    @Column(name = "SO_LUONG")
    private BigDecimal soLuong;

    @Column(name = "DON_GIA")
    private BigDecimal donGia;

    @Column(name = "THANH_TIEN")
    private BigDecimal thanhTien;

    @Column(name = "GHI_CHU")
    private String ghiChu;

    @Column(name = "BB_KT_NHAP_KHO_ID")
    private Long bbKtNhapKhoId; // NH_BB_KT_NHAP_KHO_VT
}
