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
public class QlnvTtinHdongHangSearchReq extends BaseRequest {
	@ApiModelProperty(example = "SHD0001")
	String soHdong;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@Past
	@Temporal(TemporalType.DATE)
	Date tuNgayHdong;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@Past
	@Temporal(TemporalType.DATE)
	Date denNgayHdong;

	@ApiModelProperty(example = "MHH001")
	String maHhoa;

	@ApiModelProperty(example = Contains.MOI_TAO)
	String tthaiHdong;

	@ApiModelProperty(example = "HNO")
	String maDvi;

	@ApiModelProperty(example = Contains.HD_MUA)
	String loaiHdong;

}
