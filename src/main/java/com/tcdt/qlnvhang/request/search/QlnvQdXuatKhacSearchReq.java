package com.tcdt.qlnvhang.request.search;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Past;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.util.Contains;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class QlnvQdXuatKhacSearchReq extends BaseRequest {
	@ApiModelProperty(example = "SDX123")
	String soQdinh;
	
	@ApiModelProperty(example = "MH")
	String maHhoa;
	
	@ApiModelProperty(example = "00")
	String lhinhXuat;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	@Past
	@Temporal(TemporalType.DATE)
	Date tuNgayLap;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	@Past
	@Temporal(TemporalType.DATE)
	Date denNgayLap;
	
	@ApiModelProperty(example = "00")
	String trangThai;
	
	@ApiModelProperty(example = "HNO")
	String maDvi;
}
