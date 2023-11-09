package com.tcdt.qlnvhang.entities.xuathang.daugia.tochuctrienkhai.thongtin;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = XhTcTtinBdgPlo.TABLE_NAME)
@Data
public class XhTcTtinBdgPlo implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_TC_TTIN_BDG_PLO";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_TC_TTIN_BDG_TAI_SAN_SEQ")
    @SequenceGenerator(sequenceName = "XH_TC_TTIN_BDG_TAI_SAN_SEQ", allocationSize = 1, name = "XH_TC_TTIN_BDG_TAI_SAN_SEQ")
    private Long id;
    private Long idDtl;
    private String maDiemKho;
    private String maNhaKho;
    private String maNganKho;
    private String maLoKho;
    private String maDviTsan;
    private BigDecimal tonKho;
    private BigDecimal soLuongDeXuat;
    private String donViTinh;
    private BigDecimal donGiaDeXuat;
    private BigDecimal donGiaDuocDuyet;
    private BigDecimal soTienDatTruoc;
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
    @Transient
    private BigDecimal thanhTien;
}