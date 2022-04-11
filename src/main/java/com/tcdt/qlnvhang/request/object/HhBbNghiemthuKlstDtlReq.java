package com.tcdt.qlnvhang.request.object;

import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HhBbNghiemthuKlstDtlReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;

	@Size(max = 500, message = "Nội dung không được vượt quá 500 ký tự")
	String noiDung;

	@Size(max = 20, message = "Đơn vị tính không được vượt quá 20 ký tự")
	String dvt;

	Double soLuongTn;
	Double donGiaTn;
	Double thanhTienTn;
	Double soLuongQt;
	Double thanhTienQt;
	Double tongGtri;

	Long idHdr;

}
