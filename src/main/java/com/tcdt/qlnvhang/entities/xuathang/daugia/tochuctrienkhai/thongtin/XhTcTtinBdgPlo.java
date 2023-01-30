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

    private BigDecimal donGiaDeXuat;

    private BigDecimal donGiaVat;

    private BigDecimal duDau;

    private String dviTinh;

    private String maDviTsan;

    private String maDiemKho;

    private String maNhaKho;

    private String maNganKho;

    private String maLoKho;

    private BigDecimal soLuong;

    private Integer soLanTraGia;

    private BigDecimal donGiaTraGia;

    private String toChucCaNhan;

    @Transient
    private String tenDiemKho;
    @Transient
    private String tenNhaKho;
    @Transient
    private String tenNganKho;
    @Transient
    private String tenLoKho;

}
