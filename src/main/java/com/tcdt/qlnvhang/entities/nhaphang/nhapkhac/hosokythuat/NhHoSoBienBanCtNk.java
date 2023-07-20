package com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.hosokythuat;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "NH_HO_SO_BIEN_BAN_CT_NK")
@Data
public class NhHoSoBienBanCtNk implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "NH_HO_SO_BIEN_BAN_CT_NK";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NH_HO_SO_BB_CT_NK_SEQ")
    @SequenceGenerator(sequenceName = "NH_HO_SO_BB_CT_NK_SEQ", allocationSize = 1, name = "NH_HO_SO_BB_CT_NK_SEQ")
    private Long id;

    @Column(name = "ID_HO_SO_BIEN_BAN")
    private Long idHoSoBienBan;

    @Column(name = "DAI_DIEN")
    private String daiDien;

    @Column(name = "LOAI_DAI_DIEN")
    private String loaiDaiDien;

}
