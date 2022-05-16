package com.tcdt.qlnvhang.request.search.sokho;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;

import com.tcdt.qlnvhang.util.Contains;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HHSoKhoHdrSearchReq extends BaseRequest{
	String maHhoa;
	String maDvi;
	String maNgan;
	String maLo;
	String tenHhoa;
	Integer nam;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date tuNgayMoSo;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date denNgayMoSo;
}
