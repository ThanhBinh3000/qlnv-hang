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

	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "Số phụ lục không được vượt quá 20 ký tự")
	String soPluc;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayKy;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayHluc;

	String veViec;

	Integer tgianThienHdTrc;

	Integer tgianThienHdDc;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date tuNgayHlucTrc;

	@NotNull(message = "Không được để trống")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date tuNgayHlucDc;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date denNgayHlucTrc;

	@NotNull(message = "Không được để trống")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date denNgayHlucDc;

	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "Số hợp đồng không được vượt quá 20 ký tự")
	String soHd;

	String tenHd;

	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "Nội dung điều chỉnh không được vượt quá 20 ký tự")
	String noiDung;

	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "Loại vật tư hàng hóa không được vượt quá 20 ký tự")
	@ApiModelProperty(example = "00")
	String loaiVthh;

	@NotNull(message = "Không được để trống")
	String cloaiVthh;

	String ghiChu;

	private List<HhHopDongDtlReq> detail;

	private List<FileDinhKemReq> fileDinhKems;

}
