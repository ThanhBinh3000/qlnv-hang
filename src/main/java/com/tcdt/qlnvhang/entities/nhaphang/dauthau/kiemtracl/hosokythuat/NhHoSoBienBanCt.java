package com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.hosokythuat;

import com.tcdt.qlnvhang.entities.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "NH_HO_SO_BIEN_BAN_CT")
@Data
public class NhHoSoBienBanCt extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "NH_HO_SO_BIEN_BAN_CT";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NH_HO_SO_BIEN_BAN_CT_SEQ")
    @SequenceGenerator(sequenceName = "NH_HO_SO_BIEN_BAN_CT_SEQ", allocationSize = 1, name = "NH_HO_SO_BIEN_BAN_CT_SEQ")
    private Long id;

    @Column(name = "ID_HO_SO_BIEN_BAN")
    private Long idHoSoBienBan;

    @Column(name = "DAI_DIEN")
    private String daiDien;

    @Column(name = "LOAI_DAI_DIEN")
    private String loaiDaiDien;

}
