package com.tcdt.qlnvhang.entities.xuathang.bantructiep.nhiemvuxuat;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "XH_QD_NV_XH_BTT_DVI")
@Data
public class XhQdNvXhBttDvi {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_QD_NV_XH_BTT_DVI";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_QD_NV_XH_BTT_DVI_SEQ")
    @SequenceGenerator(sequenceName = "XH_QD_NV_XH_BTT_DVI_SEQ", allocationSize = 1, name = "XH_QD_NV_XH_BTT_DVI_SEQ")
    private Long id;

    private Long idDtl;

    private String maDiemKho;
    @Transient
    private String tenDiemKho;

    private String maNhaKho;
    @Transient
    private String tenNhaKho;

    private String maNganKho;
    @Transient
    private String tenNganKho;

    private String maLoKho;
    @Transient
    private String tenLoKho;

    private BigDecimal soLuong;
}
