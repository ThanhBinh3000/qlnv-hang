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
public class QlnvPhieuNhapxuatHdrReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Số hợp đồng không được vượt quá 50 ký tự")
	@ApiModelProperty(example = "SHD001")
	String soPhieu;

	@NotNull(message = "Không được để trống")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayLap;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Số quyết định nhập xuất không được vượt quá 50 ký tự")
	@ApiModelProperty(example = "SHD001")
	String soQdinhNhapxuat;

	@NotNull(message = "Loại hình nhập xuất")
	@Size(max = 2, message = "Loại mua bán không được vượt quá 2 ký tự")
	@ApiModelProperty(example = Contains.PHIEU_NHAP)
	String lhinhNhapxuat;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Mã đơn vị ra quyết định không được vượt quá 50 ký tự")
	String maDvi;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Mã hàng hóa thực hiện không được vượt quá 50 ký tự")
	String maHhoa;

	@NotNull(message = "Không được để trống")
	@Size(max = 250, message = "Địa chỉ không được vượt quá 250 ký tự")
	String diaChi;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Mã ngăn không được vượt quá 50 ký tự")
	String maNgan;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Mã lô không được vượt quá 50 ký tự")
	String maLo;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Mã kho không được vượt quá 50 ký tự")
	String maKho;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Tài khoản nợ không được vượt quá 50 ký tự")
	String tkhoanNo;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Tài khoản có không được vượt quá 50 ký tự")
	String tkhoanCo;

	@NotNull(message = "Không được để trống")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayNhan;

	@Size(max = 50, message = "Tên người nhận không được vượt quá 250 ký tự")
	String tenNguoiNhan;

	@Size(max = 20, message = "Loại chứng từ không được vượt quá 20 ký tự")
	String loaiCtu;

	@NotNull(message = "Không được để trống")
	@Size(max = 2, message = "Trạng thái không được vượt quá 2 ký tự")
	String trangThai;

	@Size(max = 250, message = "Lý do từ chối không được vượt quá 250 ký tự")
	String ldoTuchoi;

	private List<QlnvPhieuNhapxuatDtlReq> detail;
}
