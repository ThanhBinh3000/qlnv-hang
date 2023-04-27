package com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.dexuat;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "XH_DX_KH_BAN_DAU_GIA_PHAN_LO ")
@Data
public class XhDxKhBanDauGiaPhanLo implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_DX_KH_BAN_DAU_GIA_PHAN_LO ";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_DX_KH_BAN_DAU_GIA_PL_SEQ")
    @SequenceGenerator(sequenceName = "XH_DX_KH_BAN_DAU_GIA_PL_SEQ", allocationSize = 1, name = "XH_DX_KH_BAN_DAU_GIA_PL_SEQ")
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

    private String maDviTsan;

    private BigDecimal tonKho;

    private BigDecimal soLuongDeXuat;

    private BigDecimal donGiaDeXuat;

    @Transient
    private BigDecimal donGiaDuocDuyet;

    private String donViTinh;

    private String loaiVthh;
    @Transient
    private String tenLoaiVthh;

    private String cloaiVthh;
    @Transient
    private String tenCloaiVthh;
}
