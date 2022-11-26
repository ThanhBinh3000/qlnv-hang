package com.tcdt.qlnvhang.entities.nhaphang.vattu.bienbanchuanbikho;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "NH_BIEN_BAN_CHUAN_BI_KHO_CT")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NhBienBanChuanBiKhoCt implements Serializable {

    private static final long serialVersionUID = -7498145675622743869L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BB_CHUAN_BI_KHO_CT_SEQ")
    @SequenceGenerator(sequenceName = "BB_CHUAN_BI_KHO_CT_SEQ", allocationSize = 1, name = "BB_CHUAN_BI_KHO_CT_SEQ")
    @Column(name = "ID")
    private Long id;

    @Column(name = "ID_BB_CHUAN_BI_KHO")
    private Long idBbChuanBiKho;

    @Column(name = "NOI_DUNG")
    private String noiDung;

    @Column(name = "DON_VI_TINH")
    private String donViTinh;

    @Column(name = "SO_LUONG_TRONG_NAM")
    private BigDecimal soLuongTrongNam;

    @Column(name = "DON_GIA_TRONG_NAM")
    private BigDecimal donGiaTrongNam;

    @Column(name = "SO_LUONG_QT")
    private BigDecimal soLuongQt;

    @Column(name = "DON_GIA_QT")
    private BigDecimal thanhTienQt;

}
