package com.tcdt.qlnvhang.entities.xuathang.bantructiep.tochuctrienkhai.ketqua;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "XH_KQ_BTT_DDIEM")
@Data
public class XhKqBttDdiem implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "XH_KQ_BTT_DDIEM";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "XH_KQ_BTT_DDIEM_SEQ")
    @SequenceGenerator(sequenceName = "XH_KQ_BTT_DDIEM_SEQ", allocationSize = 1, name = "XH_KQ_BTT_DDIEM_SEQ")
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

    private String donViTinh;

    private BigDecimal donGiaDeXuat;

    private BigDecimal donGiaDuocDuyet;

    private String loaiVthh;
    @Transient
    private String tenLoaiVthh;

    private String cloaiVthh;
    @Transient
    private String tenCloaiVthh;

    @Transient
    private List<XhKqBttTchuc> children = new ArrayList<>();

}
