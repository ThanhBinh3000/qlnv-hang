package com.tcdt.qlnvhang.response.luukho;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TheKhoCtResponse {
    private String soPhieu;
    private String maKho;
    private String bang;
    private String kieu;
    private int loaiPhieu;
    private LocalDate ngay;
    private BigDecimal slThucTe;
    private BigDecimal slChungTu;
    private Long maBanGhi;
}
