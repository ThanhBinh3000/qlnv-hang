package com.tcdt.qlnvhang.request.object;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QlnvDxkhXuatKhacDtlReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;

	@NotNull(message = "Mã cha không được để trống")
	Long idHdr;

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

	Integer soLuong;

	@Size(max = 50, message = "Đơn vị tính không được vượt quá 50 ký tự")
	@ApiModelProperty(example = "DVT1")
	String dviTinh;
	
	@Size(max = 250, message = "Ghi chú không được vượt quá 250 ký tự")
	String ghichu;
}
