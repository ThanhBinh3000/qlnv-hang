package com.tcdt.qlnvhang.request.search;

import javax.validation.constraints.NotNull;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.util.Contains;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class HhDthauSearchReq extends BaseRequest {
	@NotNull(message = "Không được để trống")
	@ApiModelProperty(example = "2022")
	Long namKhoach;

	String soQd;

	Date tuNgayQd;

	Date denNgayQd;

	@ApiModelProperty(example = Contains.LOAI_VTHH_GAO)
	String loaiVthh;

	String cloaiVthh;

	String maDvi;

	String tenHd;

	String trichYeu;

}
