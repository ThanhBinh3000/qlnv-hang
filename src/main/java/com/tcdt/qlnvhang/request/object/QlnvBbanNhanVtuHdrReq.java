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
public class QlnvBbanNhanVtuHdrReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Số biên bản không được vượt quá 50 ký tự")
	@ApiModelProperty(example = "SBK001")
	String soBban;

	@NotNull(message = "Không được để trống")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayLap;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Số quyết định không được vượt quá 50 ký tự")
	@ApiModelProperty(example = "SHD001")
	String soQdinh;

	@Size(max = 50, message = "Số biên bản kiểm tra chất lượng không được vượt quá 50 ký tự")
	@ApiModelProperty(example = "SHD001")
	String bbanKtraCluong;

	@Size(max = 50, message = "Số hồ sơ kỹ thuật không được vượt quá 50 ký tự")
	@ApiModelProperty(example = "SHD001")
	String soHsoKthuat;

	@Size(max = 50, message = "Đơn vị nhận Cục không được vượt quá 50 ký tự")
	@ApiModelProperty(example = "SHD001")
	String maDviCuc;

	@Size(max = 50, message = "Đại diện Cục không được vượt quá 50 ký tự")
	@ApiModelProperty(example = "SHD001")
	String ddienCuc;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Đơn vị nhận Chi cục không được vượt quá 50 ký tự")
	@ApiModelProperty(example = "SHD001")
	String maDviCcuc;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Đại diện Chi cục không được vượt quá 50 ký tự")
	@ApiModelProperty(example = "SHD001")
	String ddienCcuc;

	@NotNull(message = "Không được để trống")
	@Size(max = 250, message = "Đơn vị giao không được vượt quá 250 ký tự")
	@ApiModelProperty(example = "SHD001")
	String dviGiao;

	@NotNull(message = "Không được để trống")
	@Size(max = 250, message = "Đại diện đơn vị giao không được vượt quá 250 ký tự")
	@ApiModelProperty(example = "SHD001")
	String ddienGiao;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Mã hàng hóa thực hiện không được vượt quá 50 ký tự")
	String maHhoa;

	@NotNull(message = "Không được để trống")
	@Size(max = 2, message = "Trạng thái không được vượt quá 2 ký tự")
	String trangThai;

	@Size(max = 250, message = "Lý do từ chối không được vượt quá 250 ký tự")
	String ldoTuchoi;

	private List<QlnvBbanNhanVtuDtlReq> detail;
}
