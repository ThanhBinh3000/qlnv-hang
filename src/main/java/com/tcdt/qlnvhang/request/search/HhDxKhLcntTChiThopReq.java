package com.tcdt.qlnvhang.request.search;

import javax.validation.constraints.NotNull;

import com.tcdt.qlnvhang.util.Contains;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class  HhDxKhLcntTChiThopReq {
	@NotNull(message = "Không được để trống")
	@ApiModelProperty(example = "2022")
	Integer namKhoach;

	@NotNull(message = "Không được để trống")
	@ApiModelProperty(example = Contains.LOAI_VTHH_GAO)
	String loaiVthh;

	@NotNull(message = "Không được để trống")
	@ApiModelProperty(example = Contains.LOAI_VTHH_GAO)
	String cloaiVthh;

	@ApiModelProperty(example = Contains.LOAI_VTHH_GAO)
	String hthucLcnt;

	@ApiModelProperty(example = Contains.LOAI_VTHH_GAO)
	String pthucLcnt;

	@ApiModelProperty(example = Contains.LOAI_VTHH_GAO)
	String loaiHdong;

	@ApiModelProperty(example = Contains.LOAI_VTHH_GAO)
	String nguonVon;
	List<String> listMaDvi;
}
