package com.tcdt.qlnvhang.entities.xuathang.daugia.tochuctrienkhai.thongtin;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = XhTcTtinBdgPlo.TABLE_NAME)
public class XhTcTtinBdgPlo implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_TC_TTIN_BDG_PLO";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_TC_TTIN_BDG_TAI_SAN_SEQ")
    @SequenceGenerator(sequenceName = "XH_TC_TTIN_BDG_TAI_SAN_SEQ", allocationSize = 1, name = "XH_TC_TTIN_BDG_TAI_SAN_SEQ")
    private Long id;

    private Long idTtinDtl;

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

    private String maDviTsan;

    private BigDecimal tonKho;

    private BigDecimal soLuongDeXuat;

    private String donViTinh;

    private BigDecimal donGiaDuocDuyet;

    private BigDecimal donGiaDeXuat;

    private Integer soLanTraGia;

    private BigDecimal donGiaTraGia;

    private String toChucCaNhan;
}
