package com.tcdt.qlnvhang.request.object;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class HhDthauNthauDuthauReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;

	private Long idNhaThau;

	@NotNull(message = "Không được để trống")
	@Size(max = 250, message = "Tên nhà thầu không được vượt quá 250 ký tự")
	@ApiModelProperty(example = "Tên nhà thầu")
	String tenNhaThau;

	String mst;

	@Size(max = 500, message = "Địa chỉ nhà thầu không được vượt quá 500 ký tự")
	@ApiModelProperty(example = "Địa chỉ nhà thầu")
	String diaChi;

	@Size(max = 20, message = "Số điện thoại không được vượt quá 20 ký tự")
	@ApiModelProperty(example = "0999999999")
	String sdt;

	BigDecimal soLuong;

	BigDecimal donGia;

	String trangThai;

	String lyDo;

	Long idGtHdr;
}
