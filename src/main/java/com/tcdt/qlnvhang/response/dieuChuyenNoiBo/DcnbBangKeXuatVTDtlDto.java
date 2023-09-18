package com.tcdt.qlnvhang.response.dieuChuyenNoiBo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class DcnbBangKeXuatVTDtlDto {
    private int stt;
    private String soSerial;
    private BigDecimal soLuong;
}
