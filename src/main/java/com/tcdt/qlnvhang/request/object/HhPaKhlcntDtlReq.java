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
public class HhPaKhlcntDtlReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;

	Long idHdr;

	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "Mã đơn vị không được vượt quá 20 ký tự")
	@ApiModelProperty(example = "HNO")
	String maDvi;

	@NotNull(message = "Không được để trống")
	@Size(max = 250, message = "Tên đơn vị không được vượt quá 250 ký tự")
	@ApiModelProperty(example = "Cục Hà Nội")
	String tenDvi;

	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "Số đề xuất không được vượt quá 20 ký tự")
	@ApiModelProperty(example = "Tên dự án")
	String soDxuat;

	@NotNull(message = "Không được để trống")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayDxuat;

	@NotNull(message = "Không được để trống")
	@Size(max = 250, message = "Tên dự án không được vượt quá 250 ký tự")
	@ApiModelProperty(example = "Tên dự án")
	String tenDuAn;

	BigDecimal soLuong;
	BigDecimal donGia;
	BigDecimal tongTien;
	Long soGthau;

	@NotNull(message = "Không được để trống")
	@Size(max = 4, message = "Năm kế hoạch không được vượt quá 4 ký tự")
	@ApiModelProperty(example = "2022")
	String namKhoach;

	private List<HhKhlcntDsgthauReq> detail;
}
