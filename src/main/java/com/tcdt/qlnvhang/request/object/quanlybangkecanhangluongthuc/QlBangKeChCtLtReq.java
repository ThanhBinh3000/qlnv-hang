package com.tcdt.qlnvhang.request.object.quanlybangkecanhangluongthuc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QlBangKeChCtLtReq {

    private String maCan;

    private BigDecimal trongLuongBaoBi;

    private BigDecimal trongLuongCaBaoBi;
    private String phanLoai;
    private Long lan;
    private BigDecimal soBaoDem;
}
