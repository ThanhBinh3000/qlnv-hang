package com.tcdt.qlnvhang.request.object;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QlnvQdMuattDtlCtietReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;

	Long idDtl;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Mã đơn vị không được vượt quá 50 ký tự")
	String maDvi;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Mã đơn vị không được vượt quá 50 ký tự")
	String dvts;

	BigDecimal soDxuat;
	BigDecimal soDuyet;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Đơn vị tính không được vượt quá 50 ký tự")
	String dviTinh;

	BigDecimal donGia;
	BigDecimal giaDuyetKthue;
	BigDecimal giaDuyetCothue;
	BigDecimal tongTien;
}
