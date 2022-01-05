package com.tcdt.qlnvhang.request.object;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QlnvPhieuNhapxuatDtlReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;

	Long idHdr;

	@Size(max = 50, message = "Đơn vị tính không được vượt quá 50 ký tự")
	@ApiModelProperty(example = "Kg")
	String dviTinh;

	BigDecimal soLuongCtu;

	BigDecimal soLuongThucte;

	BigDecimal donGia;

	BigDecimal tongTien;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Mã hàng hóa không được vượt quá 50 ký tự")
	@ApiModelProperty(example = "DKH001")
	String maHhoa;

}
