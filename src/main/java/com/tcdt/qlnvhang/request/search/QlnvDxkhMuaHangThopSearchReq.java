package com.tcdt.qlnvhang.request.search;

import com.tcdt.qlnvhang.request.BaseRequest;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QlnvDxkhMuaHangThopSearchReq extends BaseRequest {

	@ApiModelProperty(example = "01")
	String pthucBan;

	@ApiModelProperty(example = "MHH001")
	String maHhoa;

	@ApiModelProperty(example = "XXXCT123")
	String soQdKhoach;

}
