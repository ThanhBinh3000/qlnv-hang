package com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.dexuat;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

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
    private String maNhaKho;
    private String maNganKho;
    private String maLoKho;
    private String maDviTsan;
    private BigDecimal tonKho;
    private BigDecimal soLuongDeXuat;
    private String donViTinh;
    private BigDecimal donGiaDeXuat;
    private BigDecimal giaKhoiDiemDx;
    private BigDecimal soTienDtruocDx;
    private String loaiVthh;
    private String cloaiVthh;
    @Transient
    private String tenDiemKho;
    @Transient
    private String tenNhaKho;
    @Transient
    private String tenNganKho;
    @Transient
    private String tenLoKho;
    @Transient
    private String tenLoaiVthh;
    @Transient
    private String tenCloaiVthh;
    @Transient
    private BigDecimal donGiaDuocDuyet;
}
