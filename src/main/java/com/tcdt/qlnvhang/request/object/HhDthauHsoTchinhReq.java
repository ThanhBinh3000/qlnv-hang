package com.tcdt.qlnvhang.request.object;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HhDthauHsoTchinhReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;

	@NotNull(message = "Không được để trống")
	@Size(max = 250, message = "Tên nhà thầu không được vượt quá 250 ký tự")
	@ApiModelProperty(example = "Tên nhà thầu")
	String ten;

	Long diem;
	Long xepHang;
	BigDecimal dgiaDthau;
	BigDecimal giaDthau;

	Long idGtHdr;
}
