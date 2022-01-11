package com.tcdt.qlnvhang.request.object;

import java.math.BigDecimal;

import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QlnvBkeNhapvtuDtlReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;

	Long idHdr;

	@Size(max = 50, message = "Số serial không được vượt quá 50 ký tự")
	@ApiModelProperty(example = "SRA0001")
	String soSerial;
	BigDecimal soLuong;

}
