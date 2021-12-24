package com.tcdt.qlnvhang.request.object;

import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QlnvQdKQDGHangDtlReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;
	Long idHdr;
	@Size(max = 50, message = "Đơn vị tài sản không được vượt quá 50 ký tự")
	@ApiModelProperty(example = "SDX123")
	String dvts;
	@NotNull(message = "Mã đơn vị không được để trống")
	@Size(max = 50, message = "Mã đơn vị không được vượt quá 50 ký tự")
	@ApiModelProperty(example = "HNO")
	String maDvi;
	BigDecimal soLuong;
	BigDecimal giaKhoidiem;
	@ApiModelProperty(example = "01")
	String coDviTrung;
	@Size(max = 50, message = "Mã đơn vị trúng thầu không được vượt quá 50 ký tự")
	@ApiModelProperty(example = "TTHAU1")
	String maDviThau;
	@Size(max = 250, message = "Tên đơn vị trúng thầu không được vượt quá2 50 ký tự")
	@ApiModelProperty(example = "Công ty TNHH 1 thành viên ABC")
	String tenDviThau;
	String diaChi;
	BigDecimal giaBothau;
}
