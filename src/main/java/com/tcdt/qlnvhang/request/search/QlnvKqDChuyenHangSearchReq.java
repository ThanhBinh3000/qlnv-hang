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
public class QlnvKqDChuyenHangSearchReq extends BaseRequest {
	@ApiModelProperty(example = "XXXSDX123")
	String soQdinh;

	@ApiModelProperty(example = "HNO")
	String maDviDi;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	@Past
	Date tuNgayBcao;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	@Past
	Date denNgayBcao;

	@ApiModelProperty(example = "MHH001")
	String maHhoa;

	@ApiModelProperty(example = "1")
	String hthucDchuyen;
	
	@ApiModelProperty(example = "HNO1")
	String maDviDen;
}
