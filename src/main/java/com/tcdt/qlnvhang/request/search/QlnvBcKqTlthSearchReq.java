package com.tcdt.qlnvhang.request.search;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ibm.icu.math.BigDecimal;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.util.Contains;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QlnvBcKqTlthSearchReq extends BaseRequest{
	@Size(max = 50, message = "Số quyết định thanh lý, tiêu hủy không được vượt quá 50 ký tự")
	String soQdTlth;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date tuNgayLapBC;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date denNgayLapBC;
	
	@Size(max = 50, message = "Đơn vị DTQG lập BC không được vượt quá 50 ký tự")
	String maDvi;
	
	@Size(max = 2, message = "Loại hình xuất không được vượt quá 2 ký tự")
	String lhinhXuat;
	
	@Size(max = 50, message = "Mã hàng DTQG không được vượt quá 50 ký tự")
	String maHanghoa;
}
