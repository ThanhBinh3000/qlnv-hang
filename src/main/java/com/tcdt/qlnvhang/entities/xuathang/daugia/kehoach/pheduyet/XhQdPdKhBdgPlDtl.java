package com.tcdt.qlnvhang.entities.xuathang.daugia.kehoach.pheduyet;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "XH_QD_PD_KH_BDG_PL_DTL")
@Data
public class XhQdPdKhBdgPlDtl implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_QD_PD_KH_BDG_PL_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_QD_PD_KH_BDG_PL_DTL_SEQ")
    @SequenceGenerator(sequenceName = "XH_QD_PD_KH_BDG_PL_DTL_SEQ", allocationSize = 1, name = "XH_QD_PD_KH_BDG_PL_DTL_SEQ")
    private Long id;
    private Long idPhanLo;
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
    private BigDecimal donGiaDuocDuyet;
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
}
