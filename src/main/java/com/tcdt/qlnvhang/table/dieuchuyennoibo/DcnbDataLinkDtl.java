package com.tcdt.qlnvhang.table.dieuchuyennoibo;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = DcnbDataLinkDtl.TABLE_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DcnbDataLinkDtl {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "DCNB_DATA_LINK_DTL";

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
