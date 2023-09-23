package com.tcdt.qlnvhang.request.search;

import java.util.Date;

import javax.validation.constraints.Past;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.util.Contains;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QlnvQdKQDGHangSearchReq extends BaseRequest {

	@ApiModelProperty(example = "HNO")
	String maDvi;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date tuNgay;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date denNgay;

	@ApiModelProperty(example = "MHH001")
	String maHhoa;

	@ApiModelProperty(example = "XXXCT123")
	String soQdinh;

	String loaiVthh;
	String cloaiVthh;

	String maKho;

}
