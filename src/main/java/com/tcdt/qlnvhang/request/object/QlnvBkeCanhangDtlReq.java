package com.tcdt.qlnvhang.request.object;

import java.math.BigDecimal;

import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QlnvBkeCanhangDtlReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;

	Long idHdr;

	@Size(max = 50, message = "Đơn vị tính không được vượt quá 50 ký tự")
	@ApiModelProperty(example = "Kg")
	String maCan;
	BigDecimal soLuong;
	BigDecimal trongLuong;

}
