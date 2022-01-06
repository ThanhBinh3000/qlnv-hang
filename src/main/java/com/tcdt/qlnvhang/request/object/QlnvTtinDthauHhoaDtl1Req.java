package com.tcdt.qlnvhang.request.object;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QlnvTtinDthauHhoaDtl1Req{
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;
	Long idDtl;
	String diaChi;
	String tenDvi;
	BigDecimal soLuong;
	String soDthoai;
	String tenLhe;
	String dthoaiLhe;
	BigDecimal giaThau;
}
