package com.tcdt.qlnvhang.request.object;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
public class HhDviThQdDtlReq {
    @ApiModelProperty(notes = "Bắt buộc set đối với update")
    private Long id;

    Long idHdr;

    @NotNull(message = "Không được để trống")
    private Integer stt;

    @NotNull(message = "Không được để trống")
    private String maKhoNganLo;

    @NotNull(message = "Không được để trống")
    private String tenKhoNganLo;

    @NotNull(message = "Không được để trống")
    private BigDecimal soLuong;

    @NotNull(message = "Không được để trống")
    private String thuKhoPhuTrach;

}
