package com.tcdt.qlnvhang.request.object;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.util.Contains;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QlnvTtinHdongHangDtl2Req {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;

	Long idHdr;

	@Size(max = 2, message = "Loại điều chỉnh không được vượt quá 2 ký tự")
	@ApiModelProperty(example = "00")
	String loaiDchinh;

	@NotNull(message = "Không được để trống")
	@Size(max = 250, message = "Nội dung điều chỉnh không được vượt quá 250 ký tự")
	String ndungDchinh;

	Long soPluc;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Đơn vị tài sản không được vượt quá 50 ký tự")
	String canCu;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Đơn vị tài sản không được vượt quá 50 ký tự")
	String fileName;

	@NotNull(message = "Không được để trống")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayKy;

}
