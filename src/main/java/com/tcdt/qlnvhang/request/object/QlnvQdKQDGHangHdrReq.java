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
public class QlnvQdKQDGHangHdrReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;
	@Size(max = 50, message = "Số quyết định không được vượt quá 50 ký tự")
	@ApiModelProperty(example = "SQD123")
	String soQdinh;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	Date ngayQdinh;
	@Size(max = 50, message = "Số quyết định phê duyệt kế hoạch bán không được vượt quá 50 ký tự")
	@ApiModelProperty(example = "SQDGCT123")
	String soQdKhoach;
	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Mã hàng hóa không được vượt quá 50 ký tự")
	String maHanghoa;
	@NotNull(message = "Mã đơn vị Không được để trống")
	@Size(max = 50, message = "Mã đơn vị không được vượt quá 50 ký tự")
	@ApiModelProperty(example = "HNO")
	String maDvi;
	@ApiModelProperty(example = "1")
	Integer lanDaugia;
	@NotNull(message = "Không được để trống")
	@Size(max = 2, message = "Trạng thái không được vượt quá 2 ký tự")
	@ApiModelProperty(example = Contains.MOI_TAO)
	String trangThai;
	@Size(max = 250, message = "Lý do từ chối không được vượt quá 250 ký tự")
	String ldoTuchoi;
	private List<QlnvQdKQDGHangDtlReq> detail;
}
