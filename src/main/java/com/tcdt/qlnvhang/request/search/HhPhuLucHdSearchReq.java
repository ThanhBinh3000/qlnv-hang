package com.tcdt.qlnvhang.request.search;

import javax.validation.constraints.NotNull;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.util.Contains;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HhPhuLucHdSearchReq extends BaseRequest {
	@ApiModelProperty(example = "2022")
	String namKhoach;

	@NotNull(message = "Không được để trống")
	@ApiModelProperty(example = Contains.LOAI_VTHH_GAO)
	String loaiVthh;

	String soHd;

	String soPluc;
}
