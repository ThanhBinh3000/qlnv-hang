package com.tcdt.qlnvhang.response.quanlybangkecanhangluongthuc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QlBangKeChCtLtRes {
    private Long id;
    private Long qlBangKeCanHangLtId;
    private Integer stt;
    private String maCan;
    private BigDecimal soBaoBi;
    private BigDecimal trongLuongCaBi;
}
