package com.tcdt.qlnvhang.request.object;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QlnvTtinChaogiaDtlReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;

	Long idHdr;

	@Size(max = 50, message = "Đơn vị tính không được vượt quá 50 ký tự")
	@ApiModelProperty(example = "Kg")
	String dviTinh;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Mã đơn vị không được vượt quá 50 ký tự")
	@ApiModelProperty(example = "HNO")
	String maDvi;

	BigDecimal soLuong;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Đơn vị tài sản không được vượt quá 50 ký tự")
	@ApiModelProperty(example = "DVTS01")
	String dvts;

	private List<QlnvTtinChaogiaDtlCtietReq> detail;
}
