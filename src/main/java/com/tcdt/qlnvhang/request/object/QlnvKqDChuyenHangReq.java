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
public class QlnvKqDChuyenHangReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayBcao;		
	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Số quyết định điều chỉnh không được vượt quá 50 ký tự")
	@ApiModelProperty(example = "XXXSQD123")
	String soQdinh;	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayQdinh;
	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Mã đơn vị không được vượt quá 50 ký tự")
	@ApiModelProperty(example = "HNO")
	String maDviDi;	
	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Mã đơn vị không được vượt quá 50 ký tự")
	@ApiModelProperty(example = "HNO")
	String maDviDen;	
	@Size(max = 50, message = "Số quyết định nhập hàng không được vượt quá 50 ký tự")
	String soQdinhNhap;
	@Size(max = 50, message = "Số quyết định xuất hàng không được vượt quá 50 ký tự")
	String soQdinhXuat;
	@Size(max = 50, message = "Số phiếu nhập hàng không được vượt quá 50 ký tự")
	String soPhieuNhap;
	@Size(max = 50, message = "Số phiếu xuất hàng không được vượt quá 50 ký tự")
	String soPhieuXuat;	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date tuNgayThien;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date denNgayThien;
	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Mã hàng hóa không được vượt quá 50 ký tự")
	@ApiModelProperty(example = "MHH001")
	String maHhoa;
	@NotNull(message = "Không được để trống")
	@Size(max = 250, message = "Tên hàng hóa không được vượt quá 250 ký tự")
	@ApiModelProperty(example = "Tên hàng hóa 01")
	String tenHhoa;
	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Đơn vị tính không được vượt quá 50 ký tự")
	@ApiModelProperty(example = "KG")
	String dviTinh;
	@ApiModelProperty(example = "0")
	BigDecimal soLuongDi;
	@ApiModelProperty(example = "0")
	BigDecimal soLuongDen;
	@ApiModelProperty(example = "0")
	BigDecimal soChenhLech;	
	@NotNull(message = "Không được để trống")
	@Size(max = 2, message = "Hình thức điều chuyển không được vượt quá 2 ký tự")
	@ApiModelProperty(example = "01")
	String hthucDchuyen;
	@Size(max = 250, message = "Lý do chênh lệch không được vượt quá 250 ký tự")
	@ApiModelProperty(example = "Sáp nhập kho")
	String lyDo;
	@NotNull(message = "Không được để trống")
	@Size(max = 2, message = "Kết quả điều chuyển không được vượt quá 2 ký tự")
	@ApiModelProperty(example = "01")
	String ketQua;
	@Size(max = 250, message = "Lý do từ chối không được vượt quá 250 ký tự")
	String ldoTuchoi;
	@NotNull(message = "Không được để trống")
	@Size(max = 2, message = "Trạng thái không được vượt quá 2 ký tự")
	@ApiModelProperty(example = Contains.MOI_TAO)
	String trangThai;

}
