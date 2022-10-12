package com.tcdt.qlnvhang.request.nhaphangtheoptt;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class HhBbanNghiemThuDtlReq {
    private Long id;
    private Long idHdr;
    private String noiDung;
    private String dvt;
    private BigDecimal soLuongTn;
    private BigDecimal donGiaTn;
    private BigDecimal donGiaQt;
    private BigDecimal thanhTienTn;
    private BigDecimal soLuongQt;
    private BigDecimal thanhTienQt;
    private BigDecimal tongGtri;
}
