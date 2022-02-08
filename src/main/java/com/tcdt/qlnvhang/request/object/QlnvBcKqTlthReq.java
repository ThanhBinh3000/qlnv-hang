package com.tcdt.qlnvhang.request.object;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.util.Contains;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QlnvBcKqTlthReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;
	
	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Số quyết định thanh lý, tiêu hủy không được vượt quá 50 ký tự")
	String soQdTlth;
	
	@NotNull(message = "Không được để trống")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayQdTlth;
	
	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Đơn vị DTQG lập BC không được vượt quá 50 ký tự")
	String maDvi;
	
	@NotNull(message = "Không được để trống")
	@Size(max = 2, message = "Loại hình xuất không được vượt quá 2 ký tự")
	String lhinhXuat;
	
	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Mã hàng DTQG không được vượt quá 50 ký tự")
	String maHanghoa;
	
	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Số QĐ xuất hàng không được vượt quá 50 ký tự")
	String soQdXhang;
	
	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Số phiếu xuất hàng không được vượt quá 50 ký tự")
	String soPhieuXhang;
	
	BigDecimal kinhPhi;
	
	BigDecimal tienThu;
	
	@NotNull(message = "Không được để trống")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayThienTu;
	
	@NotNull(message = "Không được để trống")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayThienDen;
	
	
	@Size(max = 50, message = "Số quyết định thanh lý, tiêu hủy không được vượt quá 50 ký tự")
	String ghiChu;
	
	@NotNull(message = "Không được để trống")
	@Size(max = 2, message = "Trạng thái xuất không được vượt quá 2 ký tự")
	String trangThaiXuat;
	
	@Size(max = 250, message = "Lý do từ chối không được vượt quá 250 ký tự")
	String ldoTuchoi;
}
