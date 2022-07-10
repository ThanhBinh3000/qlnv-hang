package com.tcdt.qlnvhang.request.object.vattu.bangke;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class NhBangKeVtCtReq {
    private Long id;
    private Long bangKeVtId;
    private BigDecimal soLuong;
    private String soSerial;
    private Integer stt;
}
