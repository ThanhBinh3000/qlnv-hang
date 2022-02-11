package com.tcdt.qlnvhang.request.object;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QlnvQdPhuongAnGiaDtlCtietReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;

	Long idDtl;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Mã đơn vị không được vượt quá 50 ký tự")
	String maDvi;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Mã hàng hóa không được vượt quá 50 ký tự")
	String maHhoa;

	@NotNull(message = "Không được để trống")
	@Size(max = 250, message = "Tên hàng hóa không được vượt quá 250 ký tự")
	String tenHhoa;
	
	@NotNull(message = "Không được để trống")
	@Size(max = 2, message = "Loại mua/bán không được vượt quá 2 ký tự")
	String loaiMban;

	BigDecimal giaDxuattKthue;
	BigDecimal giaDXuatCothue;
	BigDecimal giaDuyetKthue;
	BigDecimal giaDuyetCothue;
}
