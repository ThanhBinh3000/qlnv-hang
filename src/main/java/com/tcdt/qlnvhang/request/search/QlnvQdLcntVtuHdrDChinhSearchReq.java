package com.tcdt.qlnvhang.request.search;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.util.Contains;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
public class QlnvQdLcntVtuHdrDChinhSearchReq extends BaseRequest {

	String soQdinh;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date tuNgayQd;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date denNgayQd;
	String maHanghoa;
	String loaiHanghoa;
	String loaiDieuChinh;
	String loaiQd;
	String maDvi;
	String soQdKh;
}
