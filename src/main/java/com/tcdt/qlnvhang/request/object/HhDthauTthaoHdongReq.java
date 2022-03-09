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
public class HhDthauTthaoHdongReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayKy;

	@NotNull(message = "Không được để trống")
	@Size(max = 250, message = "Tên nhà thầu không được vượt quá 250 ký tự")
	@ApiModelProperty(example = "Tên nhà thầu")
	String tenNthau;

	BigDecimal dgiaTrThue;
	Long dgiaVat;
	BigDecimal dgiaSauThue;
	BigDecimal giaHdTrThue;
	BigDecimal giaHdSauThue;
	Long giaHdVat;

	Long idGtHdr;

	private List<FileDinhKemReq> fileDinhKems;
}
