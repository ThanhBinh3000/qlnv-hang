package com.tcdt.qlnvhang.request.object;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

@Data
public class HhPreviewDdiemNhapKhoDTO {
	String tenDvi;
	BigDecimal soLuong;
	BigDecimal tongThanhTien;
//	private List<HhPreviewDdiemNhapKhoVtDTO> children;
}
