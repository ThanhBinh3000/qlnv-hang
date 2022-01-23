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
public class QlnvQdLcntHdrReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;
	
	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Số quyết định không được vượt quá 50 ký tự")
	@ApiModelProperty(example = "SHD001")
	String soQdinh;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayQd;
	
	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Loại hàng hóa không được vượt quá 50 ký tự")
	String loaiHanghoa;
	
	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Mã hàng hóa không được vượt quá 50 ký tự")
	@ApiModelProperty(example = "MHH001")
	String maHanghoa;
	
	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Nguồn vốn không được vượt quá 50 ký tự")
	String nguonvon;
	
	String soQdinhGoc;
	
	@Size(max = 50, message = "Lý do từ chối không được vượt quá 50 ký tự")
	String ldoTuchoi;
	
	String loaiQd;
	
	@NotNull(message = "Không được để trống")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date tuNgayThien;
	
	@NotNull(message = "Không được để trống")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date denNgayThien;
	
	@NotNull(message = "Không được để trống")
	String soQdGiaoCtkh;
	
	@Size(max = 2, message = "Lý do từ chối không được vượt quá 2 ký tự")
	@ApiModelProperty(example = "01")
	String loaiDieuChinh;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayQdGoc;
	
	private List<QlnvQdLcntDtlReq> detail;
}
