package com.tcdt.qlnvhang.request.object.sokho;

import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.util.Contains;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class HhSoKhoHdrReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Mã hàng hóa không được vượt quá 50 ký tự")
	String maHhoa;

	@NotNull(message = "Không được để trống")
	@Size(max = 255, message = "Tên hàng hóa không được vượt quá 255 ký tự")
	String tenHhoa;

	@NotNull(message = "Không được để trống")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayMoSo;

	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Mã ngăn không được vượt quá 50 ký tự")
	String maNgan;
	@Size(max = 50, message = "Mã lô không được vượt quá 50 ký tự")
	String maLo;
	@Size(max = 50, message = "Mã đơn vị không được vượt quá 50 ký tự")
	String maDvi;
	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Mã đơn vị tính không được vượt quá 50 ký tự")
	String dviTinh;

	@NotNull(message = "Không được để trống")
	@Size(max = 100, message = "Tên thủ kho không được vượt quá 100 ký tự")
	String thuKho;

	@NotNull(message = "Không được để trống")
	@Size(max = 100, message = "Tên kế toán không được vượt quá 100 ký tự")
	String keToan;

	@NotNull(message = "Không được để trống")
	@Size(max = 100, message = "Tên thủ trưởng đơn vị không được vượt quá 100 ký tự")
	String ttruongDvi;

	@NotNull(message = "Không được để trống")
	Integer nam;

}
