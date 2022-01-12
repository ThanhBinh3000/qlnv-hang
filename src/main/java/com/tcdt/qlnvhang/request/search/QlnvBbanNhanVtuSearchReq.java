package com.tcdt.qlnvhang.request.search;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Past;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.util.Contains;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QlnvBbanNhanVtuSearchReq extends BaseRequest {
	@ApiModelProperty(example = "SHD0001")
	String soBban;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	@Past
	@Temporal(TemporalType.DATE)
	Date tuNgayLap;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	@Past
	@Temporal(TemporalType.DATE)
	Date denNgayLap;

	@ApiModelProperty(example = "HNO")
	String maDvi;

	@ApiModelProperty(example = "MHH001")
	String maHhoa;

	@ApiModelProperty(example = Contains.MOI_TAO)
	String trangThai;

}
