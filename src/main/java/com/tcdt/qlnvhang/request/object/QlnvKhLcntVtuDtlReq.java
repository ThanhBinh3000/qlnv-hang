package com.tcdt.qlnvhang.request.object;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QlnvKhLcntVtuDtlReq{
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;
	
	Long khLcntVtuId;
	@Size(max = 50, message = "Tên gói thầu không được vượt quá 50 ký tự")
	String tenGoiThau;
	BigDecimal soLuong;
	BigDecimal donGia;
	NDungGoiThau ndungGoiThau;
	
	private List<QlnvKhLcntVtuDtlCtietReq> detailList;
}
