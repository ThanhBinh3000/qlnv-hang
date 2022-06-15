package com.tcdt.qlnvhang.request.object;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.util.Contains;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HhDxuatKhLcntGaoDtlReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;

	Long idHdr;
	
	@NotNull(message = "Không được để trống")
	@Size(max = 500, message = "Tên dự án không được vượt quá 500 ký tự")
	@ApiModelProperty(example = "Tên dự án")
	String tenDuAn;
	
	BigDecimal tongMucDt;
	
//	@NotNull(message = "Không được để trống")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date tuNgayThHien;

//	@NotNull(message = "Không được để trống")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date denNgayThHien;
	
//	@NotNull(message = "Không được để trống")
//	@Size(max = 20, message = "Tiêu chuẩn chất lượng không được vượt quá 20 ký tự")
	@ApiModelProperty(example = "Tiêu chuẩn")
	String tchuanCluong;
	
//	@NotNull(message = "Không được để trống")
//	@Size(max = 20, message = "Nguồn vốn không được vượt quá 20 ký tự")
	@ApiModelProperty(example = "Nguồn vốn")
	String nguonVon;
	
//	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "Hình thức lựa chọn nhà thầu không được vượt quá 20 ký tự")
	@ApiModelProperty(example = "Hình thức")
	String hthucLcnt;
	
//	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "Phương thức lựa chọn nhà thầu không được vượt quá 20 ký tự")
	@ApiModelProperty(example = "Phương thức")
	String pthucLcnt;
	
//	@NotNull(message = "Không được để trống")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date tgianTbao;
	
//	@NotNull(message = "Không được để trống")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date tgianDongThau;
	
//	@NotNull(message = "Không được để trống")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date tgianMoThau;
	
	@Size(max = 20, message = "Loại hợp đồng không được vượt quá 20 ký tự")
	@ApiModelProperty(example = "Loại hợp đồng")
	String loaiHdong;
	
	@NotNull(message = "Không được để trống")
	Integer tgianThHienHd;
	
//	@NotNull(message = "Không được để trống")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	@Temporal(TemporalType.DATE)
	Date tgianNhapHang;
	
	@Size(max = 20, message = "Bảo lãnh đấu thầu không được vượt quá 20 ký tự")
	@ApiModelProperty(example = "Bảo lãnh")
	String blanhDthau;
	
	@Size(max = 500, message = "Ghi chú không được vượt quá 500 ký tự")
	String ghiChu;

//	@NotNull(message = "Không được để trống")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	@Temporal(TemporalType.DATE)
	Date tgianPhatHanh;

	BigDecimal donGia;

	String maDvi;

	String dienGiai;

}
