package com.tcdt.qlnvhang.request.object;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QlnvDxkhTlthDtlReq {

	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;

	Long idHdr;
	
	@NotNull(message = "Mã đơn vị không được để trống")
	@Size(max = 50, message = "Mã đơn vị không được vượt quá 50 ký tự")
	@ApiModelProperty(example = "HNO.CC1")
	String maDvi;

	@NotNull(message = "Mã kho không được để trống")
	@Size(max = 50, message = "Mã kho không được vượt quá 50 ký tự")
	@ApiModelProperty(example = "MKXX1")
	String maKho;

	@Size(max = 50, message = "Mã ngăn không được vượt quá 50 ký tự")
	@ApiModelProperty(example = "MNXX1")
	String maNgan;

	@Size(max = 50, message = "Mã lô không được vượt quá 50 ký tự")
	@ApiModelProperty(example = "MLXX1")
	String maLo;

	BigDecimal soLuong;

	@Size(max = 50, message = "Đơn vị tính được vượt quá 50 ký tự")
	@ApiModelProperty(example = "DVT1")
	String dviTinh;
	
	@Size(max = 250, message = "Ghi chú không được vượt quá 250 ký tự")
	@ApiModelProperty(example = "Ghi chu")
	String ghiChu;
	
	@Size(max = 50, message = "Đơn vị tài sản không được vượt quá 50 ký tự")
	@ApiModelProperty(example = "SDX123")
	String dvts;
}
