package com.tcdt.qlnvhang.table.dieuchuyennoibo;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = DcnbDataLinkHdr.TABLE_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DcnbDataLinkHdr {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "DCNB_DATA_LINK_HDR";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = TABLE_NAME +"_SEQ")
    @SequenceGenerator(sequenceName =  TABLE_NAME +"_SEQ", allocationSize = 1, name = TABLE_NAME +"_SEQ")
    private Long id;

    @Column(name = "KE_HOACH_DC_DTL_ID")
    private Long keHoachDcDtlId;

    @Column(name = "KE_HOACH_DC_HDR_ID")
    private Long keHoachDcHdrId;

    @Column(name = "KE_HOACH_DC_DTL_PARENT_ID")
    private Long keHoachDcDtlParentId;

    @Column(name = "KE_HOACH_DC_HDR_PARENT_ID")
    private Long keHoachDcHdrParentId;
    @Column(name = "QD_CC_ID")
    private Long qdCcId;
    @Column(name = "QD_CC_PARENT_ID")
    private Long qdCcParentId;
    @Column(name = "QD_CTC_ID")
    private Long qdCtcId;
    @Column(name = "TYPE")
    private String type;

    @Transient
    private List<DcnbDataLinkDtl> dataLinkDtls = new ArrayList<>();
}
