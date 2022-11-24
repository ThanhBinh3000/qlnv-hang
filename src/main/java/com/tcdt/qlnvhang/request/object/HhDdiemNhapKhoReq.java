package com.tcdt.qlnvhang.request.object;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HhDdiemNhapKhoReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;

	Long idHdr;

	@Size(max = 250, message = "Chi cục dự trữ nhà nước không được vượt quá 250 ký tự")
	String maDvi;

	@Size(max = 250, message = "Điểm kho không được vượt quá 250 ký tự")
	String maDiemKho;

	@Size(max = 250, message = "Nhà kho không được vượt quá 250 ký tự")
	String nhaKho;

	BigDecimal soLuong;

	BigDecimal donGia;

	List<HhDdiemNhapKhoVtReq> children;

}
