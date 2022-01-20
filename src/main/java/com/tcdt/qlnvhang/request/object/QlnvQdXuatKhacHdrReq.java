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
public class QlnvQdXuatKhacHdrReq {

	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;
	
	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Số quyết định không được vượt quá 50 ký tự")
	@ApiModelProperty(example = "SQD123")
	String soQdinh;
	
	@NotNull(message = "Không được để trống")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayQdinh;
	
	@NotNull(message = "Không được để trống")
	@ApiModelProperty(example = "00")
	String trangThai;
	
	String ldoTuchoi;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date tuNgayThien;
	
	@NotNull(message = "Không được để trống")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date denNgayThien;
	
	@NotNull(message = "Không được để trống")
	@ApiModelProperty(example = "00")
	String lhinhXuat;
	
	@NotNull(message = "Không được để trống")
	@ApiModelProperty(example = "MH")
	String maHanghoa;
	
	private List<QlnvQdXuatKhacDtlReq> detail;
}
