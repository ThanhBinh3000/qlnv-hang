package com.tcdt.qlnvhang.request.nhaphang.nhapkhac;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class HhDxuatKhNhapKhacDtlReq {
    private Long hdrId;
    private String maCuc;
    private String maChiCuc;
    private String maDiemKho;
    private String maNhaKho;
    private String maNganKho;
    private String maLoKho;
    private String cloaiVthh;
    private BigDecimal slTonKho;
    private BigDecimal slHaoDoiDinhMuc;
    private BigDecimal slDoiThua;
    private BigDecimal donGia;
    private BigDecimal slTonKhoThucTe;
    private BigDecimal slNhap;
    private String dvt;
}
