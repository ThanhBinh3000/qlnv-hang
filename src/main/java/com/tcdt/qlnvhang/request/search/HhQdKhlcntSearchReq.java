package com.tcdt.qlnvhang.request.search;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.util.Contains;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HhQdKhlcntSearchReq extends BaseRequest {

	@ApiModelProperty(example = "2022")
	Integer namKhoach;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_FULL_STR)
	Date tuNgayQd;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_FULL_STR)
	Date denNgayQd;

	@ApiModelProperty(example = Contains.LOAI_VTHH_GAO)
	String loaiVthh;

	String cloaiVthh;

	String soQd;

	String trichYeu;

	Integer lastest;

	String maDvi;

	String trangThaiDtl;

	String trangThaiDt;

	String soQdPdKhlcnt;

	String soQdPdKqlcnt;
}
