package com.tcdt.qlnvhang.request.object;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QlnvDxPhuongAnGiaDtlReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;

	@NotNull(message = "Mã cha không được để trống")
	Long idHdr;
	
	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Mã đơn vị không được vượt quá 50 ký tự")
	@ApiModelProperty(example = "HNO")
	String maDvi;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Mã hàng hóa không được vượt quá 50 ký tự")
	@ApiModelProperty(example = "MHH001")
	String maHhoa;

	@NotNull(message = "Không được để trống")
	@Size(max = 250, message = "Tên hàng hóa không được vượt quá 250 ký tự")
	@ApiModelProperty(example = "Tên hàng hóa 01")
	String tenHhoa;

	@Size(max = 2, message = "Loại mua/bán được vượt quá 2 ký tự")
	@ApiModelProperty(example = "01")
	String loaiMban;

	BigDecimal giaDxuat;
}
