package com.tcdt.qlnvhang.request.search;

import javax.validation.constraints.NotNull;

import com.tcdt.qlnvhang.util.Contains;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HhDxKhLcntDsChuaThReq {
	@NotNull(message = "Không được để trống")
	@ApiModelProperty(example = "2022")
	String namKhoach;

	@NotNull(message = "Không được để trống")
	@ApiModelProperty(example = Contains.LOAI_VTHH_GAO)
	String loaiVthh;

	@NotNull(message = "Không được để trống")
	@ApiModelProperty(example = Contains.DUYET)
	String trangThai;
}
