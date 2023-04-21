package com.tcdt.qlnvhang.request.xuathang.daugia.quyetdinhdieuchinh;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class XhQdDchinhKhBdgPlDtlReq {
    private Long id;

    private Long idPhanLo;

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
