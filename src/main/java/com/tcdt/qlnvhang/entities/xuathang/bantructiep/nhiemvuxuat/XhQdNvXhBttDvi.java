package com.tcdt.qlnvhang.entities.xuathang.bantructiep.nhiemvuxuat;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = XhQdNvXhBttDvi.TABLE_NAME)
@Data
public class XhQdNvXhBttDvi {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_QD_NV_XH_BTT_DVI";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhQdNvXhBttDvi.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhQdNvXhBttDvi.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhQdNvXhBttDvi.TABLE_NAME + "_SEQ")
    private Long id;
    private Long idDtl;
    private String maDiemKho;
    private String maNhaKho;
    private String maNganKho;
    private String maLoKho;
    private BigDecimal soLuongDeXuat;
    private String maDviTsan;
    private BigDecimal tonKho;
    private String donViTinh;
    private BigDecimal donGiaDeXuat;
    private BigDecimal donGiaDuocDuyet;
    @Transient
    private String tenDiemKho;
    @Transient
    private String tenNhaKho;
    @Transient
    private String tenNganKho;
    @Transient
    private String tenLoKho;
}
