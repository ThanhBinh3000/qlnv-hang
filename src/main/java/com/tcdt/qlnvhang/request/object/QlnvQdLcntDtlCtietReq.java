package com.tcdt.qlnvhang.request.object;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QlnvQdLcntDtlCtietReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;
	Long idDtl;
	String maDvi;
	String diaChi;
	BigDecimal soDxuat;
	BigDecimal soDuyet;
	String dviTinh;
	BigDecimal donGia;
	BigDecimal thuesuat;
	BigDecimal thanhtien;
	BigDecimal tongtien;
}
