package com.tcdt.qlnvhang.request.object;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.util.Contains;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HhQdGiaoNvuNhapxuatDtlReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;

	Long idHdr;

	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "Mã vật tư hàng hóa không được vượt quá 250 ký tự")
	@ApiModelProperty(example = "MVT01")
	String maVthh;

	@NotNull(message = "Không được để trống")
	@Size(max = 300, message = "Tên vật tư hàng hóa không được vượt quá 300 ký tự")
	@ApiModelProperty(example = "Cục Hà Nội")
	String tenVthh;

	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "Loại nhập xuất không được vượt quá 20 ký tự")
	@ApiModelProperty(example = "01")
	String loaiNx;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date tuNgayThien;

	@NotNull(message = "Không được để trống")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	@ApiModelProperty(example = "2022-09-05")
	Date denNgayThien;

	Double soLuong;

	@Size(max = 20, message = "Đơn vị tính không được vượt quá 20 ký tự")
	@ApiModelProperty(example = "DVT01")
	String donViTinh;

	String maDvi;
}
