package com.tcdt.qlnvhang.request.nhaphangtheoptt;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class HhQdGiaoNvNhDdiemReq {
    private Long id;
    private Long idDtl;
    private String maCuc;
    private String maChiCuc;
    private String MaDiemKho;
    private String maNhaKho;
    private String maNganKho;
    private String maLoKho;
    private BigDecimal soLuong;
}
