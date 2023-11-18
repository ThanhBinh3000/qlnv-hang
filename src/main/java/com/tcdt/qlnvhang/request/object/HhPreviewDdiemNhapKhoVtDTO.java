package com.tcdt.qlnvhang.request.object;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class HhPreviewDdiemNhapKhoVtDTO {
	String tenDvi;
	BigDecimal soLuong;
	BigDecimal tongThanhTien;


}
