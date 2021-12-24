package com.tcdt.qlnvhang.request.object;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QlnvTtinDauGiaHangDtlReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;

	Long idHdr;
	
	@Size(max = 50, message = "SĐơn vị tài sản không được vượt quá 50 ký tự")
	@ApiModelProperty(example = "SDX123")
	String dvts;

	@Size(max = 50, message = "Mã đơn vị không được vượt quá 50 ký tự")
	@ApiModelProperty(example = "HNO")
	String maDvi;

	BigDecimal soLuong;
	
	BigDecimal giaKhoidiem;

	private List<QlnvTtinDauGiaHangDtlCtietReq> detail;
}
