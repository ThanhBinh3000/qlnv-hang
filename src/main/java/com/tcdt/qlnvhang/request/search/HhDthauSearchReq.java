package com.tcdt.qlnvhang.request.search;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
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

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayQdTu;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayQdDen;

	@ApiModelProperty(example = Contains.LOAI_VTHH_GAO)
	String loaiVthh;

	String cloaiVthh;

	String maDvi;

	String tenHd;

	String trichYeu;

}
