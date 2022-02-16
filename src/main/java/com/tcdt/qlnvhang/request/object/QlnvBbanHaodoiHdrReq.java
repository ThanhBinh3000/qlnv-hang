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
public class QlnvBbanHaodoiHdrReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Số biên bản không được vượt quá 50 ký tự")
	@ApiModelProperty(example = "SBB001")
	String soBban;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Mã đơn vị ra quyết định không được vượt quá 50 ký tự")
	String maDvi;
	
	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Mã kho không được vượt quá 50 ký tự")
	String maKho;
	
	@NotNull(message = "Không được để trống")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayLap;

	@NotNull(message = "Không được để trống")
	@Size(max = 02, message = "Mã hàng hóa không được vượt quá 02 ký tự")
	String maHhoa;
	
	@NotNull(message = "Không được để trống")
	@Size(max = 250, message = "Tên hàng hóa không được vượt quá 250 ký tự")
	String tenHhoa;
	
	@Size(max = 50, message = "Mã lô không được vượt quá 50 ký tự")
	String maLo;
	
	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Mã thủ kho không được vượt quá 50 ký tự")
	String maThukho;
	
	@Size(max = 250, message = "Địa điểm không được vượt quá 250 ký tự")
	String diaDiem;
	
	@Size(max = 50, message = "Mã ngăn không được vượt quá 50 ký tự")
	String maNgan;
	
	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Tên kỹ thuật viên bảo quản không được vượt quá 50 ký tự")
	String kthuatVien;
	
	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Tên kế toán trưởng không được vượt quá 50 ký tự")
	String keToan;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Thủ kho không được vượt quá 50 ký tự")
	String thuKho;
	
	@NotNull(message = "Không được để trống")
	BigDecimal sluongNhap;
	
	@NotNull(message = "Không được để trống")
	BigDecimal sluongXuat;
	
	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Đơn vị tính không được vượt quá 50 ký tự")
	String dviTinh;
	
	@NotNull(message = "Không được để trống")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayKthucNhap;
	
	@NotNull(message = "Không được để trống")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayKthucXuat;
	
	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Số biên bản không được vượt quá 50 ký tự")
	String soBbanTinhkho;
	
	@NotNull(message = "Không được để trống")
	BigDecimal sluongHaoTte;
	
	@NotNull(message = "Không được để trống")
	Double tleHaoTte;
	
	@NotNull(message = "Không được để trống")
	BigDecimal sluongHaoTly;
	
	@NotNull(message = "Không được để trống")
	Double tleHaoTly;
	
	@NotNull(message = "Không được để trống")
	BigDecimal sluongHaoTrendm;
	
	@NotNull(message = "Không được để trống")
	Double tleHaoTrendm;
	
	@NotNull(message = "Không được để trống")
	BigDecimal sluongHaoDuoidm;
	
	@NotNull(message = "Không được để trống")
	Double tleHaoDuoidm;
	
	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Tên thủ trưởng không được vượt quá 50 ký tự")
	String thuTruong;

	@Size(max = 250, message = "Lý do từ chối không được vượt quá 250 ký tự")
	String ldoTuchoi;
	
	@Size(max = 250, message = "Nguyên nhân không được vượt quá 250 ký tự")
	String nguyenNhan;
	
	@Size(max = 250, message = "Kiến nghị không được vượt quá 250 ký tự")
	String kienNghi;
	
	private List<QlnvBbanHaodoiDtlReq> detail;
}
