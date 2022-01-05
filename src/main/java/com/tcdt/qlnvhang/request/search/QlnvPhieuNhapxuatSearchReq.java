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
public class QlnvPhieuNhapxuatSearchReq extends BaseRequest {
	@ApiModelProperty(example = "SHD0001")
	String soPhieu;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	@Past
	@Temporal(TemporalType.DATE)
	Date tuNgayLap;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	@Past
	@Temporal(TemporalType.DATE)
	Date denNgayLap;

	@ApiModelProperty(example = "MHH001")
	String maHhoa;

	@ApiModelProperty(example = Contains.MOI_TAO)
	String trangThai;

	@ApiModelProperty(example = "HNO")
	String maDvi;

	@ApiModelProperty(example = "SHD0001")
	String soQdinhNhapxuat;

	@ApiModelProperty(example = Contains.PHIEU_NHAP)
	String lhinhNhapxuat;

}
