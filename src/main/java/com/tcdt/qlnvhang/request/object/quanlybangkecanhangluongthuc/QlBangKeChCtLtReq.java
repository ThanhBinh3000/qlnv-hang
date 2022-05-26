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
    private Long id;

    @NotNull(message = "Không được để trống")
    private Integer stt;

    @NotNull(message = "Không được để trống")
    private String maCan;

    @NotNull(message = "Không được để trống")
    private BigDecimal soBaoBi;

    @NotNull(message = "Không được để trống")
    private BigDecimal trongLuongCaBi;
}
