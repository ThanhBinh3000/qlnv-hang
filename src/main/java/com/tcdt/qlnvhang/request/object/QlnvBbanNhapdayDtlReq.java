package com.tcdt.qlnvhang.request.object;

import java.math.BigDecimal;

import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QlnvBbanNhapdayDtlReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;

	Long idHdr;

	BigDecimal thanhTien;
	BigDecimal soLuong;
	BigDecimal donGia;

	@Size(max = 250, message = "Ghi chú không được vượt quá 250 ký tự")
	String ghiChu;
}
