package com.tcdt.qlnvhang.request.object;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QlnvBbanNhanVtuDtlReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;

	Long idHdr;

	BigDecimal donGia;
	BigDecimal soLuong;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Mã hàng hóa không được vượt quá 50 ký tự")
	String maHhoa;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Chủng loại không được vượt quá 50 ký tự")
	String kyHieu;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Đơn vị giao không được vượt quá 50 ký tự")
	String dviTinh;

	BigDecimal thanhTien;

	@Size(max = 250, message = "Ghi chú không được vượt quá 250 ký tự")
	String ghiChu;

}
