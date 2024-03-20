package com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bienbanguihang;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "NH_BIEN_BAN_GUI_HANG_CT")
@Data
public class NhBienBanGuiHangCt implements Serializable {
    private static final long serialVersionUID = -1130590655733872367L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BIEN_BAN_GUI_HANG_CT_SEQ")
    @SequenceGenerator(sequenceName = "BIEN_BAN_GUI_HANG_CT_SEQ", allocationSize = 1, name = "BIEN_BAN_GUI_HANG_CT_SEQ")
    @Column(name = "ID")
    private Long id;

    @Column(name = "CHUC_VU")
    private String chucVu;

    @Column(name = "DAI_DIEN")
    private String daiDien;

    @Column(name = "BIEN_BAN_GUI_HANG_ID")
    private Long bienBanGuiHangId;

    @Column(name = "LOAI_BEN")
    private String loaiBen;

    @Column(name = "ID_VIRTUAL")
    private BigDecimal idVirtual;
}
