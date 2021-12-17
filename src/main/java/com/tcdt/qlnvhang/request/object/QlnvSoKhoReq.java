package com.tcdt.qlnvhang.request.object;

import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QlnvSoKhoReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;
	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Sổ kho không được vượt quá 50 ký tự")
	String soKho;
	Date ngayLap;
	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Mã hàng hóa không được vượt quá 50 ký tự")
	String maHhoa;
	@Size(max = 250, message = "Tên sổ kho không được vượt quá 250 ký tự")
	String tenSo;
	@Size(max = 50, message = "Mã kho không được vượt quá 50 ký tự")
	String maKho;
	@Size(max = 50, message = "Mã ngăn không được vượt quá 50 ký tự")
	String maNgan;
	@Size(max = 50, message = "Mã lô không được vượt quá 50 ký tự")
	String maLo;
	@Size(max = 50, message = "Mã đơn vị không được vượt quá 50 ký tự")
	String maDvi;
	String dviTinh;
	Date ngayTao;
	String nguoiTao;
	Date ngaySua;
	String nguoiSua;
}
