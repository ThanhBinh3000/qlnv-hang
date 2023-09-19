package com.tcdt.qlnvhang.entities.xuathang.daugia.nhiemvuxuat;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = XhQdGiaoNvXhDtl.TABLE_NAME)
@Data
public class XhQdGiaoNvXhDtl {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_QD_GIAO_NV_XH_DTL";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhQdGiaoNvXhDtl.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhQdGiaoNvXhDtl.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhQdGiaoNvXhDtl.TABLE_NAME + "_SEQ")
    private Long id;
    private Long idHdr;
    private String maDvi;
    private String diaChi;
    private BigDecimal tonKho;
    private BigDecimal soLuongXuatBan;
    @Transient
    private String tenDvi;
    @Transient
    private List<XhQdGiaoNvXhDdiem> children = new ArrayList<>();
}
