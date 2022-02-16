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
public class QlnvPhieuNXuatBoNganhSearchReq extends BaseRequest {
	@ApiModelProperty(example = "XXXSDX123")
	String soPhieu;

	@ApiModelProperty(example = "HNO")
	String maDvi;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	@Past
	Date tuNgayLap;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	@Past
	Date denNgayLap;

	@ApiModelProperty(example = "MHH001")
	String maHhoa;

	@ApiModelProperty(example = "123456")
	String soQdinhNhapxuat;
	
	@ApiModelProperty(example = "01")
	String loaiNhapxuat;
}
