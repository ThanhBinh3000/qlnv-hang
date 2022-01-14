package com.tcdt.qlnvhang.request.search;

import java.util.Date;

import javax.validation.constraints.Past;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.util.Contains;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class QlnvDxkhTlthSearchReq extends BaseRequest {
	@ApiModelProperty(example = "XXXSDX123")
	String soDxuat;

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

	@ApiModelProperty(example = "00")
	String lhinhXuat;

}
