package com.tcdt.qlnvhang.request.object;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.tcdt.qlnvhang.util.Contains;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QlnvTtinChaogiaDtlCtietReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;

	Long idDtl;

	@NotNull(message = "Không được để trống")
	@Size(max = 250, message = "Địa chỉ không được vượt quá 250 ký tự")
	String diaChi;

	@NotNull(message = "Không được để trống")
	@Size(max = 250, message = "Tên đơn vị không được vượt quá 250 ký tự")
	String tenDvi;

	BigDecimal soLuong;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Đơn vị tính không được vượt quá 50 ký tự")
	String dviTinh;

	BigDecimal giaChao;

	@NotNull(message = "Không được để trống")
	@Size(max = 2, message = "Kết quả chào giá không được vượt quá 2 ký tự")
	@ApiModelProperty(example = Contains.TRUNG_THAU)
	String ketQua;
}
