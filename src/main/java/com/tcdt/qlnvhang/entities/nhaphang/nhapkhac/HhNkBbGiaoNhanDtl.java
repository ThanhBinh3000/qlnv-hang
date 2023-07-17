package com.tcdt.qlnvhang.entities.nhaphang.nhapkhac;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = HhNkBbGiaoNhanDtl.TABLE_NAME)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HhNkBbGiaoNhanDtl implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "HHNK_BB_GIAO_NHAN_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = HhNkBbGiaoNhanDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = HhNkBbGiaoNhanDtl.TABLE_NAME
            + "_SEQ", allocationSize = 1, name = HhNkBbGiaoNhanDtl.TABLE_NAME + "_SEQ")
    private Long id;
    @Column(name = "HDR_ID", insertable = true, updatable = true)
    private Long hdrId;
    private String hoVaTen;
    private String chucVu;
    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HDR_ID", insertable = false, updatable = false)
    @JsonIgnore
    private HhNkBbGiaoNhanHdr parent;
}
