package com.tcdt.qlnvhang.entities.nhaphang.nhapkhac;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "HH_NK_PHIEU_KTCL_CT")
@Data
public class HhNkPhieuKtclCt {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_NK_PHIEU_KTCL_CT_SEQ")
    @SequenceGenerator(sequenceName = "HH_NK_PHIEU_KTCL_CT_SEQ", allocationSize = 1, name = "HH_NK_PHIEU_KTCL_CT_SEQ")
    private Long id;
    private Long phieuKtChatLuongId;
    private String tenTchuan;
    private String ketQuaKiemTra;
    private String phuongPhap;
    private String danhGia;
    private String chiSoNhap;
}
