package com.tcdt.qlnvhang.request.xuathang.bantructiep.xuatkho.bangkecanhang;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class XhBkeCanHangBttDtlReq {

    @ApiModelProperty(notes = "Bắt buộc set đối với update")
    private Long id;

    private Long idHdr;

    private String maCan;

    private BigDecimal trongLuongBaoBi;

    private BigDecimal trongLuongCaBaoBi;
}
