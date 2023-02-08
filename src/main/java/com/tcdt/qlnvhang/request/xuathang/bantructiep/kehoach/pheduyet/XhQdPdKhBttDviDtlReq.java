package com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.pheduyet;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class XhQdPdKhBttDviDtlReq {
    private Long id;

    private Long idDvi;

    private String maDiemKho;

    private String maNhaKho;

    private String maNganKho;

    private String maLoKho;

    private String maDviTsan;

    private BigDecimal duDau;

    private BigDecimal soLuong;

    private BigDecimal donGiaDeXuat;

    private BigDecimal donGiaVat;

    private BigDecimal dviTinh;
}
