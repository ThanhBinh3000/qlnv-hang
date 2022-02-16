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
public class QlnvKhLcntVtuHdrReq{
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;
	
	@NotNull(message = "Không được để trống")
	@Size(max = 50, message = "Số quyết định không được vượt quá 50 ký tự")
	String soQdinh;

	@NotNull(message = "Không được để trống")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayQd;
	
	@Size(max = 50, message = "Số văn bản không được vượt quá 50 ký tự")
	String soVban;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayVban;
	
	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "Mã vật tư không được vượt quá 20 ký tự")
	String maVtu;
	
	@NotNull(message = "Không được để trống")
	@Size(max = 20, message = "Mã đơn vị tính không được vượt quá 20 ký tự")
	String dviTinh;
	
	@NotNull(message = "Không được để trống")
	Integer soGoiThau;
	
	@Size(max = 250, message = "Mô tả không được vượt quá 250 ký tự")
	String moTa;
	
	@Size(max = 250, message = "Lý do từ chối không được vượt quá 250 ký tự")
	String ldoTuchoi;
	
	private List<QlnvKhLcntVtuDtlReq> detailList;
}
