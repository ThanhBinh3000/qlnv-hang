package com.tcdt.qlnvhang.request.object;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.util.Contains;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QlnvQdMuattHdrReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;

	@Size(max = 50, message = "Số quyết định không được vượt quá 50 ký tự")
	@ApiModelProperty(example = "SQD123")
	String soQdinh;
	
	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Số quyết định giao chỉ tiêu năm không được vượt quá 50 ký tự")
	@ApiModelProperty(example = "SQDGCT123")
	String soQdKhoach;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Mã hàng hóa không được vượt quá 50 ký tự")
	String maHanghoa;

	@NotNull(message = "Không được để trống")
	@Size(max = 2, message = "Trạng thái không được vượt quá 2 ký tự")
	@ApiModelProperty(example = Contains.MOI_TAO)
	String trangThai;

	@Size(max = 250, message = "Lý do từ chối không được vượt quá 250 ký tự")
	String ldoTuchoi;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date tuNgayThien;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date denNgayThien;

	// Danh cho dieu chinh
	String soQdinhGoc;

	@NotNull(message = "Không được để trống")
	@Size(max = 2, message = "Loại điều chỉnh được vượt quá 2 ký tự")
	@ApiModelProperty(example = "00")
	String loaiDchinh;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayQdGoc;

	private List<QlnvQdMuattDtlReq> detail;
}
