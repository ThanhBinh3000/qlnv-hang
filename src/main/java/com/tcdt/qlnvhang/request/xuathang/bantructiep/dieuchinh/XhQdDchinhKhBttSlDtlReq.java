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

    private BigDecimal duDau;

    private BigDecimal soLuong;

    private BigDecimal donGiaDeXuat;

    private BigDecimal donGiaVat;

    private String dviTinh;

}
