package com.tcdt.qlnvhang.request.object;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
public class HhDdiemNhapKhoVtReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;

	String maDvi;

	BigDecimal soLuong;


}
