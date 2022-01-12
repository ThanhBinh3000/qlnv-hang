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
public class QlnvBkeNhapvtuHdrReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Số bảng kê không được vượt quá 50 ký tự")
	@ApiModelProperty(example = "SBK001")
	String soBke;

	@NotNull(message = "Không được để trống")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayLap;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Số hợp đồng không được vượt quá 50 ký tự")
	@ApiModelProperty(example = "SHD001")
	String soHdong;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Mã đơn vị ra quyết định không được vượt quá 50 ký tự")
	String maDvi;

	@NotNull(message = "Không được để trống")
	@Size(max = 2, message = "Loại bảng kê không được vượt quá 2 ký tự")
	@ApiModelProperty(example = Contains.BK_NHAP)
	String loaiBke;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Tên người giao hàng không được vượt quá 50 ký tự")
	String tenNguoiGiao;

	@NotNull(message = "Không được để trống")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayGiao;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Mã kho không được vượt quá 50 ký tự")
	String maKho;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Sổ kho không được vượt quá 50 ký tự")
	String soKho;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Mã lô không được vượt quá 50 ký tự")
	String maLo;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Mã ngăn không được vượt quá 50 ký tự")
	String maNgan;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Mã hàng hóa thực hiện không được vượt quá 50 ký tự")
	String maHhoa;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Đơn vị tính không được vượt quá 50 ký tự")
	String dviTinh;

	@NotNull(message = "Không được để trống")
	@Size(max = 2, message = "Trạng thái không được vượt quá 2 ký tự")
	String trangThai;

	@Size(max = 250, message = "Lý do từ chối không được vượt quá 250 ký tự")
	String ldoTuchoi;

	private List<QlnvBkeNhapvtuDtlReq> detail;
}
