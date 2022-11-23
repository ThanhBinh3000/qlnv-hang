package com.tcdt.qlnvhang.request.xuathang.xuattheophuongthucdaugia;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class XhQdPdKhBdgPlDtlReq {
    private Long id;
    private Long idPl;
    private String maDvi;
    private String tenDvi;
    private String maDiemKho;
    private String maNganKho;
    private String maLoKho;
    private String maDviTsan;
    private BigDecimal soLuong;
    private String DviTinh;
    private BigDecimal giaKhongVat;
    private BigDecimal giaKhoiDiem;
    private BigDecimal tienDatTruoc;
}
