package com.tcdt.qlnvhang.entities.nhaphang.vattu.hosokythuat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "NH_HO_SO_KY_THUAT_CT")
@EqualsAndHashCode(callSuper = false)
public class NhHoSoKyThuatCt implements Serializable {
    private static final long serialVersionUID = 2660274262212943813L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HO_SO_KY_THUAT_CT_SEQ")
    @SequenceGenerator(sequenceName = "HO_SO_KY_THUAT_CT_SEQ", allocationSize = 1, name = "HO_SO_KY_THUAT_CT_SEQ")
    @Column(name = "ID")
    private Long id;

    @Column(name = "HO_SO_KY_THUAT_ID")
    private Long hoSoKyThuatId;

    @Column(name = "LOAI_DAI_DIEN")
    private String loaiDaiDien;

    @Column(name = "DAI_DIEN")
    private String daiDien;

    @Column(name = "STT")
    private Integer stt;
}
