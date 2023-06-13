package com.tcdt.qlnvhang.table.dieuchuyennoibo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = DcnbDataLinkHdr.TABLE_NAME)
@Getter
@Setter
public class DcnbDataLinkDtl {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "DCNB_DATA_LINK_HDR";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = TABLE_NAME +"_SEQ")
    @SequenceGenerator(sequenceName =  TABLE_NAME +"_SEQ", allocationSize = 1, name = TABLE_NAME +"_SEQ")
    private Long id;
    @Column(name = "HDR_ID")
    private Long hdrId;
    @Column(name = "LINK_ID")
    private Long linkId;
    @Column(name = "TYPE")
    private String type;

}
