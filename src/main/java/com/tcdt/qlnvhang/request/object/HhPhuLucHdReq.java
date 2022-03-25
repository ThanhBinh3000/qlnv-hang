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
public class HhPhuLucHdReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;

	Long idHdr;

	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "Số phụ lục không được vượt quá 20 ký tự")
	String soPluc;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayKy;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayHluc;
	String veViec;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date tuNgayTrcDc;

	@NotNull(message = "Không được để trống")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date tuNgaySauDc;

	long soNgayTrcDc;
	long soNgaySauDc;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date denNgayTrcDc;

	@NotNull(message = "Không được để trống")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date denNgaySauDc;

	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "Số hợp đồng không được vượt quá 20 ký tự")
	String soHd;

	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "Nội dung điều chỉnh không được vượt quá 20 ký tự")
	String noiDungDc;

	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "Loại vật tư hàng hóa không được vượt quá 20 ký tự")
	@ApiModelProperty(example = "00")
	String loaiVthh;

	private List<HhDdiemNhapKhoReq> detail;
	private List<FileDinhKemReq> fileDinhKems;

}
