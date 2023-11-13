package com.tcdt.qlnvhang.request.object;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

@Data
public class HhPreviewHopDongDtlDTO {
	BigDecimal soLuong;
	BigDecimal tongThanhTien;
	String tenDvi;
	private List<HhPreviewDdiemNhapKhoDTO> children;


}
