package com.tcdt.qlnvhang.entities.xuathang.bantructiep.nhiemvuxuat;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "XH_QD_NV_XH_BTT_DTL")
@Data
public class XhQdNvXhBttDtl {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_QD_NV_XH_BTT_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_QD_NV_XH_BTT_DTL_SEQ")
    @SequenceGenerator(sequenceName = "XH_QD_NV_XH_BTT_DTL_SEQ", allocationSize = 1, name = "XH_QD_NV_XH_BTT_DTL_SEQ")
    private Long id;

    private Long idQdHdr;

    private String maDvi;
    @Transient
    private String tenDvi;

    private BigDecimal soLuongChiCuc;

    @Transient
    private List<XhQdNvXhBttDvi> children = new ArrayList<>();
}
