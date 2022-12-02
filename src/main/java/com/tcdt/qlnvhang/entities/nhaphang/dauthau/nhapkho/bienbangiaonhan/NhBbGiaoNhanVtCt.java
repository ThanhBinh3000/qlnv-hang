package com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhapkho.bienbangiaonhan;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "NH_BB_GIAO_NHAN_VT_CT")
@Data
@NoArgsConstructor
public class NhBbGiaoNhanVtCt implements Serializable {
    private static final long serialVersionUID = 1146514785270810306L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BB_GIAO_NHAN_VT_CT_SEQ")
    @SequenceGenerator(sequenceName = "BB_GIAO_NHAN_VT_CT_SEQ", allocationSize = 1, name = "BB_GIAO_NHAN_VT_CT_SEQ")
    @Column(name = "ID")
    private Long id;

    @Column(name = "BB_GIAO_NHAN_VT_ID")
    private Long bbGiaoNhanVtId;

    @Column(name = "LOAI_DAI_DIEN")
    private String loaiDaiDien;

    @Column(name = "DAI_DIEN")
    private String daiDien;

    @Column(name = "STT")
    private Integer stt;

    @Column(name = "CHUC_VU")
    private String chucVu;

    @Column(name = "MA_DVI")
    private String maDvi;

    @Column(name = "TEN_DVI")
    private String tenDvi;
}
