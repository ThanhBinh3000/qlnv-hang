package com.tcdt.qlnvhang.request.xuathang.bantructiep.dieuchinh;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class XhQdDchinhKhBttSlDtlReq {
    private Long id;
    private Long idSl;
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
}
