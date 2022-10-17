package com.tcdt.qlnvhang.table.nhaphangtheoptt;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "HH_PHIEU_KN_CLUONG_DTL")
@Data
public class HhPhieuKnCluongDtl implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "HH_PHIEU_KN_CLUONG_DTL";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_PHIEU_KN_CLUONG_DTL_SEQ")
    @SequenceGenerator(sequenceName = "HH_PHIEU_KN_CLUONG_DTL_SEQ", allocationSize = 1, name = "HH_PHIEU_KN_CLUONG_DTL_SEQ")
    private Long id;
    private Long idHdr;
    private String ctieuCl;
    private String soCtieuCl;
    private String ketQuaPt;
    private String phuongPhap;
}
