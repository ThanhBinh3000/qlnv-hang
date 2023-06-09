package com.tcdt.qlnvhang.table.dieuchuyennoibo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = DcnbKeHoachDcDtlTT.TABLE_NAME)
@Getter
@Setter
public class DcnbKeHoachDcDtlTT {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "DCNB_KE_HOACH_DC_DTL_TT";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = TABLE_NAME +"_SEQ")
    @SequenceGenerator(sequenceName =  TABLE_NAME +"_SEQ", allocationSize = 1, name = TABLE_NAME +"_SEQ")
    private Long id;

    @Column(name = "KE_HOACH_DC_DTL_ID")
    private Long keHoachDcDtlId;

    @Column(name = "KE_HOACH_DC_HDR_ID")
    private Long keHoachDcHdrId;

    @Column(name = "HDR_ID")
    private Long hdrId;

    @Column(name = "KE_HOACH_DC_DTL_PARENT_ID")
    private Long keHoachDcParentDtlId;

    @Column(name = "KE_HOACH_DC_HDR_PARENT_ID")
    private Long keHoachDcParentHdrId;
    @Column(name = "TYPE")
    private String type;
}
