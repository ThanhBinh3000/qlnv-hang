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
public class QlnvQdkqLcntReq {

	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;
	
	@NotNull(message = "Không được để trống")
	Long idTtinDthau;
	
	Long idDviNopthau;
	
	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Số quyết định KH không được vượt quá 50 ký tự")
	@ApiModelProperty(example = "SQDKH001")
	String soQdinhKh;
	
	@NotNull(message = "Không được để trống")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayQdKh;
	
	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Số quyết định không được vượt quá 50 ký tự")
	@ApiModelProperty(example = "SQD001")
	String soQdinh;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayQd;
	
	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Loại hàng hóa không được vượt quá 50 ký tự")
	String loaiHanghoa;
	
	@NotNull(message = "Không được để trống")
	BigDecimal donGiaTrcThue;
	
	@NotNull(message = "Không được để trống")
	BigDecimal vat;
	
	@NotNull(message = "Không được để trống")
	@Size(max = 250, message = "Đơn vị trúng thầu không được vượt quá 250 ký tự")
	String dviTrungthau;
	
	@NotNull(message = "Không được để trống")
	BigDecimal giaThau;
	
	@NotNull(message = "Không được để trống")
	BigDecimal donGiaTrung;
	
	@Size(max = 250, message = "Ghi chú không được vượt quá 250 ký tự")
	String ghiChu;
	
	String kqDthau;
	
	@Size(max = 250, message = "Lý do không được vượt quá 250 ký tự")
	String lyDo;
	
	@Size(max = 250, message = "Lý do từ chối không được vượt quá 250 ký tự")
	String ldoTuchoi;
}
