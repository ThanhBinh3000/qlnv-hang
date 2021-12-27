package com.tcdt.qlnvhang.request.object;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.util.Contains;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QlnvTtinDauGiaHangHdrReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;
	@ApiModelProperty(example = "1")
	Integer lanDaugia;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayDaugia;
	@Size(max = 250, message = "Tên đại diện không được vượt quá 250 ký tự")
	String tenDaidien;
	@ApiModelProperty(example = "1000000")
	BigDecimal kinhPhi;
	@Size(max = 250, message = "Ghi chú không được vượt quá 250 ký tự")
	String ghiChu;
	@Size(max = 250, message = "Đơn vị tổ chức không được vượt quá 250 ký tự")
	String dviTochuc;
	@Size(max = 250, message = "Nơi tổ chức không được vượt quá 250 ký tự")
	String noiTochuc;
	@NotNull(message = "Mã đơn vị Không được để trống")
	@Size(max = 50, message = "Mã đơn vị  không được vượt quá 50 ký tự")
	String maDvi;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Phương thức bán không được vượt quá 50 ký tự")
	String pthucDaugia;

	@Size(max = 50, message = "Số quyết định giao chỉ tiêu năm không được vượt quá 50 ký tự")
	@ApiModelProperty(example = "SQDGCT123")
	String soQdKhoach;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Mã hàng hóa không được vượt quá 50 ký tự")
	String maHanghoa;

	@NotNull(message = "Không được để trống")
	@Size(max = 2, message = "Trạng thái không được vượt quá 2 ký tự")
	@ApiModelProperty(example = Contains.MOI_TAO)
	String trangThai;

	@Size(max = 250, message = "Lý do từ chối không được vượt quá 250 ký tự")
	String ldoTuchoi;

	private List<QlnvTtinDauGiaHangDtlReq> detail;
}
