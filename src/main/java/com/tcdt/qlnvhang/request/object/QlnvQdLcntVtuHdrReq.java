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
public class QlnvQdLcntVtuHdrReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;
	
	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Số quyết định không được vượt quá 50 ký tự")
	@ApiModelProperty(example = "SQD001")
	String soQdinh;
	
	@NotNull(message = "Không được để trống")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayQd;
	
	String soQdKh;
	
	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Loại hàng hóa không được vượt quá 50 ký tự")
	String loaiHanghoa;
	
	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Mã hàng hóa không được vượt quá 50 ký tự")
	@ApiModelProperty(example = "MHH001")
	String maHanghoa;
	
	@Size(max = 50, message = "TC kĩ thuật không được vượt quá 50 ký tự")
	String tcKiThuat;
	
	String soQdinhGoc;
	
	@Size(max = 250, message = "Lý do từ chối không được vượt quá 250 ký tự")
	String ldoTuchoi;
	
	@NotNull(message = "Không được để trống")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayDx;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayQdGoc;
	
	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Số đề xuất không được vượt quá 50 ký tự")
	String soDxuat;
	
	@Size(max = 50, message = "Loại điều chỉnh không được vượt quá 50 ký tự")
	String loaiDieuChinh;
	
	String loaiQd;
	
	private List<QlnvQdLcntVtuDtlReq> detail;
}
