package com.tcdt.qlnvhang.request.search;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.util.Contains;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HhQdNhapxuatSearchReq extends BaseRequest {
	String soQd;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date ngayQd;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date tuNgayQd;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
	Date denNgayQd;

	String maDvi;
	String maVthh;
	String loaiQd;
	Long namNhap;
	String loaiVthh;
	String trichYeu;
	List<String> bienBan;
}
