package com.tcdt.qlnvhang.request.search;

import java.util.Date;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.util.Contains;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class QlnvQdTlthSearchReq extends BaseRequest {
	
	@ApiModelProperty(example = "XXXSDX123")
	String soQdinh;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayQdinh;
	
	@ApiModelProperty(example = "MHH001")
	String maHhoa;

	@ApiModelProperty(example = "00")
	String lhinhXuat;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date tuNgayQdinh;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date denNgayQdinh;
	
    @Size(max = 20, message = "Mã đơn vị không được vượt quá 20 ký tự")
    String maDvi;
}
