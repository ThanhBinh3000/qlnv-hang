package com.tcdt.qlnvhang.entities.xuathang.daugia.nhiemvuxuat;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = XhQdGiaoNvXhDdiem.TABLE_NAME)
@Data
public class XhQdGiaoNvXhDdiem {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_QD_GIAO_NV_XH_DDIEM";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = XhQdGiaoNvXhDdiem.TABLE_NAME + "_SEQ")
    @SequenceGenerator(sequenceName = XhQdGiaoNvXhDdiem.TABLE_NAME + "_SEQ", allocationSize = 1, name = XhQdGiaoNvXhDdiem.TABLE_NAME + "_SEQ")
    private Long id;
    private Long idDtl;
    private String maDiemKho;
    private String diaDiemKho;
    private String maNhaKho;
    private String maNganKho;
    private String maLoKho;
    private String maDviTsan;
    private BigDecimal tonKho;
    private BigDecimal soLuong;
    private BigDecimal donGia;
    private BigDecimal thanhTien;
    private String donViTinh;
    @Transient
    private String tenDiemKho;
    @Transient
    private String tenNhaKho;
    @Transient
    private String tenNganKho;
    @Transient
    private String tenLoKho;
}

