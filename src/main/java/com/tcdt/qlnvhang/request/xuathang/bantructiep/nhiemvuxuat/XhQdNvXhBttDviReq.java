package com.tcdt.qlnvhang.request.xuathang.bantructiep.nhiemvuxuat;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class XhQdNvXhBttDviReq {
    private Long id;
    private Long idDtl;
    private String maDiemKho;
    private String maNhaKho;
    private String maNganKho;
    private String maLoKho;
    private BigDecimal soLuongDeXuat;
    private String maDviTsan;
    private BigDecimal tonKho;
    private String donViTinh;
    private BigDecimal donGiaDeXuat;
    private BigDecimal donGiaDuocDuyet;
}
