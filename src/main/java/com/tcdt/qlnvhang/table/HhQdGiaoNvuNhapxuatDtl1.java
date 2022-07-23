package com.tcdt.qlnvhang.table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "NH_QD_GIAO_NVU_NHAPXUAT_CT1")
@Data
public class HhQdGiaoNvuNhapxuatDtl1 implements Serializable {


    private static final long serialVersionUID = -6895604378943086007L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QD_GIAO_NVU_NHAPXUAT_CT1_SEQ")
    @SequenceGenerator(sequenceName = "QD_GIAO_NVU_NHAPXUAT_CT1_SEQ", allocationSize = 1, name = "QD_GIAO_NVU_NHAPXUAT_CT1_SEQ")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_hdr")
    @JsonBackReference
    private HhQdGiaoNvuNhapxuatHdr parent;

    @ManyToOne
    @JoinColumn(name = "HOP_DONG_ID")
    private HhHopDongHdr hopDong;

    public Long getParentId() {
        return this.parent.getId();
    }
}
