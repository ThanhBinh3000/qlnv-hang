package com.tcdt.qlnvhang.request.nhaphang.nhapkhac;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class HhQdPdNhapKhacDtlReq {
    private Long id;
    private Long hdrId;
    private String soDxuat;
    private String maCuc;
    private String maChiCuc;
    private String maDiemKho;
    private String maNhaKho;
    private String maNganKho;
    private String maLoKho;
    private BigDecimal slTonKho;
    private BigDecimal slHaoDoiDinhMuc;
    private BigDecimal slDoiThua;
    private BigDecimal donGia;
}
