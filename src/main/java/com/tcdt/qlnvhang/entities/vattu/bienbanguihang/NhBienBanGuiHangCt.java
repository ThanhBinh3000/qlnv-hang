package com.tcdt.qlnvhang.entities.vattu.bienbanguihang;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "NH_BIEN_BAN_GUI_HANG_CT")
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

    @Column(name = "STT")
    private Integer stt;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChucVu() {
        return this.chucVu;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }

    public String getDaiDien() {
        return this.daiDien;
    }

    public void setDaiDien(String daiDien) {
        this.daiDien = daiDien;
    }

    public Long getBienBanGuiHangId() {
        return this.bienBanGuiHangId;
    }

    public void setBienBanGuiHangId(Long bienBanGuiHangId) {
        this.bienBanGuiHangId = bienBanGuiHangId;
    }

    public String getLoaiBen() {
        return this.loaiBen;
    }

    public void setLoaiBen(String loaiBen) {
        this.loaiBen = loaiBen;
    }
}
