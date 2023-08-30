package com.tcdt.qlnvhang.entities.xuathang.bantructiep.nhiemvuxuat;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = XhQdNvXhBttDtl.TABLE_NAME)
@Data
public class XhQdNvXhBttDtl {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_QD_NV_XH_BTT_DTL";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhQdNvXhBttDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhQdNvXhBttDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhQdNvXhBttDtl.TABLE_NAME + "_SEQ")
    private Long id;
    private Long idHdr;
    private String maDvi;
    private BigDecimal soLuongChiCuc;
    @Transient
    private String tenDvi;
    @Transient
    private List<XhQdNvXhBttDvi> children = new ArrayList<>();
}
