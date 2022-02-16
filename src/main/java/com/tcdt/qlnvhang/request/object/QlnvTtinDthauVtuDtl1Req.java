package com.tcdt.qlnvhang.request.object;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QlnvTtinDthauVtuDtl1Req{
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;
	Long idDtl;
	
	@NotNull(message = "Không được để trống")
	@ApiModelProperty(example = "MDV1")
	@Size(max = 50, message = "Mã đơn vị không được vượt quá 50 ký tự")
	String maDvi;
	
	@NotNull(message = "Không được để trống")
	@ApiModelProperty(example = "TDV1")
	@Size(max = 250, message = "Tên đơn vị không được vượt quá 250 ký tự")
	String tenDvi;
	
	@NotNull(message = "Không được để trống")
	BigDecimal soLuong;
}
