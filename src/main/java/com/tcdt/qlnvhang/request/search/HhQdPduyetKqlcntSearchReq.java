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
public class HhQdPduyetKqlcntSearchReq extends BaseRequest {
	//@NotNull(message = "Không được để trống")
	@ApiModelProperty(example = "2022")
	String namKhoach;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date tuNgayQd;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date denNgayQd;

	@ApiModelProperty(example = Contains.LOAI_VTHH_GAO)
	String loaiVthh;
	String cloaiVthh;

	@ApiModelProperty(example = "2022")
	String soQd;

	@ApiModelProperty(example = "2022")
	String maDvi;

	String trichYeu;

	String soQdPdKhlcnt;
	String soHd;
	String tenHd;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_FULL_STR)
	Date tuNgayKy;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_TIME_FULL_STR)
	Date denNgayKy;

	String tuNgayKyStr;
	String denNgayKyStr;
	String trangThaiHd;
	Boolean hdChuaBanHanh;

}
