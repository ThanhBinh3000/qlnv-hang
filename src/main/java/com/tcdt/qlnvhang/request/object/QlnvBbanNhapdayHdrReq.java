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
public class QlnvBbanNhapdayHdrReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Số biên bản không được vượt quá 50 ký tự")
	@ApiModelProperty(example = "SBB001")
	String soBban;

	@NotNull(message = "Không được để trống")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayLap;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Số quyết định không được vượt quá 50 ký tự")
	@ApiModelProperty(example = "SQD001")
	String soQdinh;

	@NotNull(message = "Không được để trống")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayBdauNhap;

	@NotNull(message = "Không được để trống")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayKthucNhap;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Mã đơn vị ra quyết định không được vượt quá 50 ký tự")
	String maDvi;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Tên thủ kho không được vượt quá 50 ký tự")
	String thuKho;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Tên kỹ thuật viên bảo quản không được vượt quá 50 ký tự")
	String kthuatVien;
	
	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Tên kế toán trưởng không được vượt quá 50 ký tự")
	String keToan;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Tên thủ trưởng không được vượt quá 50 ký tự")
	String thuTruong;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Mã kho không được vượt quá 50 ký tự")
	String maKho;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Mã ngăn không được vượt quá 50 ký tự")
	String maNgan;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Mã lô không được vượt quá 50 ký tự")
	String maLo;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Mã hàng hóa không được vượt quá 50 ký tự")
	String maHhoa;

	@NotNull(message = "Không được để trống")
	@Size(max = 2, message = "Trạng thái không được vượt quá 2 ký tự")
	@ApiModelProperty(example = Contains.MOI_TAO)
	String trangThai;

	@Size(max = 250, message = "Lý do từ chối không được vượt quá 250 ký tự")
	String ldoTuchoi;

	private List<QlnvBbanNhapdayDtlReq> detail;
}
