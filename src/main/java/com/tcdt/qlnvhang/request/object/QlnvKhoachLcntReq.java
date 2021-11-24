package com.tcdt.qlnvhang.request.object;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QlnvKhoachLcntReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	Long id;

	@NotNull(message = "Không được để trống")
	@Size(max = 2, message = "Loại hàng hóa không được vượt quá 2 ký tự")
	String loaiHanghoa;

	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "Số quyết định không được vượt quá 20 ký tự")
	String soQdinh;

	@NotNull(message = "Không được để trống")
	Date ngayQd;

	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "Số văn bản không được vượt quá 20 ký tự")
	String soVban;

	@NotNull(message = "Không được để trống")
	Date ngayVban;
	
	//String doc;
	
	

	TTinGoiThau doc;
}
