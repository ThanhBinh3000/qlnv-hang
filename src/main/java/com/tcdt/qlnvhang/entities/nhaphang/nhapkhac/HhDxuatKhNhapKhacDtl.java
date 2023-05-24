package com.tcdt.qlnvhang.entities.nhaphang.nhapkhac;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = HhDxuatKhNhapKhacDtl.TABLE_NAME)
@Data
public class HhDxuatKhNhapKhacDtl {
    public static final String TABLE_NAME = "HH_DX_KHNK_DTL";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HH_DX_KHNK_DTL_SEQ")
    @SequenceGenerator(sequenceName = "HH_DX_KHNK_DTL_SEQ", allocationSize = 1, name = "HH_DX_KHNK_DTL_SEQ")
    private Long id;
    private Long hdrId;
    private Long parentId;
    private Long dviLevel;
    private String maDvi;
    @Transient
    private String tenDvi;
    @Transient
    private String tenCloaiVthh;
    private String cloaiVthh;
    private BigDecimal slTonKho;
    private BigDecimal slHaoDoiDinhMuc;
    private BigDecimal slDoiThua;
    private BigDecimal donGia;
}
