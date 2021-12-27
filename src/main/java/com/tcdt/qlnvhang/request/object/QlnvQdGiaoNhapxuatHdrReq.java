package com.tcdt.qlnvhang.request.object;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.contraints.CompareDate;
import com.tcdt.qlnvhang.util.Contains;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@CompareDate(before = "tuNgayThien", after = "denNgayThien", message = "Đến ngày thực hiện phải lớn hơn Từ ngày thực hiện")
public class QlnvQdGiaoNhapxuatHdrReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Số hợp đồng không được vượt quá 50 ký tự")
	@ApiModelProperty(example = "SHD001")
	String soHdong;

	@NotNull(message = "Không được để trống")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayKy;

	@NotNull(message = "Không được để trống")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayHluc;

	@NotNull(message = "Không được để trống")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayHetHluc;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Số quyết định không được vượt quá 50 ký tự")
	String soQdinh;

	@NotNull(message = "Không được để trống")
	@Size(max = 250, message = "Tên quyết định không được vượt quá 250 ký tự")
	String tenQdinh;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Mã hàng hóa thực hiện không được vượt quá 50 ký tự")
	String maHhoa;

	@NotNull(message = "Không được để trống")
	@Size(max = 2, message = "Loại hình nhập xuất thực hiện không được vượt quá 2 ký tự")
	String lhinhNhapxuat;

	@NotNull(message = "Không được để trống")
	@Size(max = 2, message = "Tình trạng hợp đồng không được vượt quá 2 ký tự")
	String loaiHdong;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Mã đơn vị ra quyết định không được vượt quá 50 ký tự")
	String maDvi;

	BigDecimal soLuong;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Đơn vị tính không được vượt quá 50 ký tự")
	String dviTinh;

	@NotNull(message = "Không được để trống")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	@CompareDate(before = "tuNgayThien", after = "ngayHluc", message = "Từ ngày thực hiện phải lớn hơn Ngày hiệu lực")
	Date tuNgayThien;

	@NotNull(message = "Không được để trống")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date denNgayThien;

	@NotNull(message = "Không được để trống")
	@Size(max = 2, message = "Trạng thái không được vượt quá 2 ký tự")
	String trangThai;

	@Size(max = 250, message = "Lý do từ chối không được vượt quá 250 ký tự")
	String ldoTuchoi;

	private List<QlnvQdGiaoNhapxuatDtlReq> detail;
}
