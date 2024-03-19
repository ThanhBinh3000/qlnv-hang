package com.tcdt.qlnvhang.request.nhaphangtheoptt;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class HhBcanKeHangDtlReq {
    @ApiModelProperty(notes = "Bắt buộc set đối với update")
    private Long id;

    private Long idHdr;

    private String maCan;

    private BigDecimal soBaoBi;

    private BigDecimal trongLuongCaBi;
    private BigDecimal trongLuongBaoBi;
    private String phanLoai;
    private Long lan;
    private BigDecimal soBaoDem;
}
