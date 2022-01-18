package com.tcdt.qlnvhang.request.object;

import java.math.BigDecimal;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QlnvQdLcntDtlReq {
	@ApiModelProperty(notes = "Bắt buộc set đối với update")
	private Long id;
	Long idHdr;
	String soDxuat;
	String maDvi;
	BigDecimal soGoithau;
	private List<QlnvQdLcntDtlCtietReq> detail;

}
