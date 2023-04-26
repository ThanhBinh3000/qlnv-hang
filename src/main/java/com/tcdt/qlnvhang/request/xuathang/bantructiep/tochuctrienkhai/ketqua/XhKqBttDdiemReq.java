package com.tcdt.qlnvhang.request.xuathang.bantructiep.tochuctrienkhai.ketqua;

import lombok.Data;

import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhKqBttDdiemReq {
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

    private String loaiVthh;

    private String cloaiVthh;

    @Transient
    private List<XhKqBttTchucReq> children = new ArrayList<>();
}
