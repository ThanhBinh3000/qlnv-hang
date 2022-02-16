package com.tcdt.qlnvhang.request.object;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QlnvTtinDthauVtuDtl2Req{
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;
	Long idDtl;
	
	@NotNull(message = "Không được để trống")
	@Size(max = 250, message = "Địa chỉ không được vượt quá 250 ký tự")
	String diaChi;
	
	@NotNull(message = "Không được để trống")
	@ApiModelProperty(example = "TDV1")
	@Size(max = 250, message = "Tên đơn vị không được vượt quá 250 ký tự")
	String tenDvi;
	
	@NotNull(message = "Không được để trống")
	BigDecimal soLuong;
	
	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "Số điện thoại không được vượt quá 20 ký tự")
	String soDthoai;
	
	@NotNull(message = "Không được để trống")
	@Size(max = 250, message = "Tên liên hệ không được vượt quá 250 ký tự")
	String tenLhe;
	
	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "Điện thoại liên hệ không được vượt quá 20 ký tự")
	String dthoaiLhe;
	
	@NotNull(message = "Không được để trống")
	BigDecimal giaThau;
}
