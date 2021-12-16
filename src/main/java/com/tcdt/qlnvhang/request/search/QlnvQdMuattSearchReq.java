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
public class QlnvQdMuattSearchReq extends BaseRequest {
	@ApiModelProperty(example = "SQD12389")
	String soQdinh;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@Past
	Date tuNgayQdinh;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@Past
	Date denNgayQdinh;

	@ApiModelProperty(example = "MHH001")
	String maHhoa;

	@ApiModelProperty(example = "XXXCT123")
	String soQdKh;
	
	@ApiModelProperty(example = Contains.MOI_TAO)
	String trangThai;
	
	@ApiModelProperty(example = "HNO")
	String maDvi;
	
	@ApiModelProperty(example = "MHH001")
	String loaiDchinh;

}
