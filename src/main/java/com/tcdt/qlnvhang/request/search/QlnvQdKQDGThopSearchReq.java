package com.tcdt.qlnvhang.request.search;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QlnvQdKQDGThopSearchReq{

	@ApiModelProperty(example = "HNO")
	String maDvi;

	@ApiModelProperty(example = "MHH001")
	String maHhoa;
	
	@ApiModelProperty(example = "1")
	int lanDauGia;

}
