package com.tcdt.qlnvhang.table.dieuchuyennoibo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcdt.qlnvhang.entities.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = DcnbBbGiaoNhanDtl.TABLE_NAME)
@Getter
@Setter
public class DcnbBbGiaoNhanDtl extends BaseEntity implements Serializable, Cloneable{
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "DCNB_BB_GIAO_NHAN_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = DcnbBbGiaoNhanDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = DcnbBbGiaoNhanDtl.TABLE_NAME
            + "_SEQ", allocationSize = 1, name = DcnbBbGiaoNhanDtl.TABLE_NAME + "_SEQ")
    private Long id;
    @Column(name = "HDR_ID", insertable = true, updatable = true)
    private Long hdrId;
    private String hoVaTen;
    private String chucVu;
    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HDR_ID", insertable = false, updatable = false)
    @JsonIgnore
    private DcnbBbGiaoNhanHdr parent;
}
