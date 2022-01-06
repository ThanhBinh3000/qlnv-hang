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
public class QlnvQdDChuyenHangHdrReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Số đề xuất không được vượt quá 50 ký tự")
	@ApiModelProperty(example = "XXXSDX123")
	String soDxuat;
	
	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Số quyết định không được vượt quá 50 ký tự")
	@ApiModelProperty(example = "XXXSQD123")
	String soQdinh;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayQdinh;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Mã đơn vị không được vượt quá 50 ký tự")
	@ApiModelProperty(example = "HNO")
	String maDvi;

	@NotNull(message = "Không được để trống")
	@Size(max = 250, message = "Lý do điều chuyển không được vượt quá 250 ký tự")
	@ApiModelProperty(example = "Sáp nhập kho")
	String ldoDchuyen;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Mã hàng hóa không được vượt quá 50 ký tự")
	@ApiModelProperty(example = "MHH001")
	String maHhoa;

	@NotNull(message = "Không được để trống")
	@Size(max = 250, message = "Tên hàng hóa không được vượt quá 250 ký tự")
	@ApiModelProperty(example = "Tên hàng hóa 01")
	String tenHhoa;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date tuNgayThien;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date denNgayThien;

	@Size(max = 250, message = "Địa điểm không được vượt quá 250 ký tự")
	String diaDiem;

	@NotNull(message = "Không được để trống")
	@Size(max = 2, message = "Hình thức điều chuyển không được vượt quá 2 ký tự")
	@ApiModelProperty(example = "01")
	String hthucDchuyen;

	@Size(max = 250, message = "Lý do từ chối không được vượt quá 250 ký tự")
	String ldoTuchoi;

	@ApiModelProperty(example = "1")
	String isCungThukho;

	@NotNull(message = "Không được để trống")
	@Size(max = 2, message = "Trạng thái không được vượt quá 2 ký tự")
	@ApiModelProperty(example = Contains.MOI_TAO)
	String trangThai;

	private List<QlnvQdDChuyenHangDtlReq> detailListReq;
}
