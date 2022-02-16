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
public class QlnvBbanHaodoiDtlReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;

	Long idHdr;

	@NotNull(message = "Không được để trống")
	BigDecimal soLuong;
	
	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Đơn vị giao không được vượt quá 50 ký tự")
	String dviTinh;
	
	@NotNull(message = "Không được để trống")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date denNgayBquan;
	
	@NotNull(message = "Không được để trống")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date tuNgayBquan;
	
	@NotNull(message = "Không được để trống")
	Double dmucHao;
	
	@NotNull(message = "Không được để trống")
	BigDecimal sluongHao;

}
