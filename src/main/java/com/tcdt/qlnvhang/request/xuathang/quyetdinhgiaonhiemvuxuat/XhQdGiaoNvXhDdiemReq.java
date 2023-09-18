package com.tcdt.qlnvhang.request.xuathang.quyetdinhgiaonhiemvuxuat;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class XhQdGiaoNvXhDdiemReq {
    private Long id;
    private Long idDtl;
    private String maDiemKho;
    private String diaDiemKho;
    private String maNhaKho;
    private String maNganKho;
    private String maLoKho;
    private String maDviTsan;
    private BigDecimal tonKho;
    private BigDecimal soLuong;
    private BigDecimal donGia;
    private BigDecimal thanhTien;
    private String donViTinh;
}
