package com.tcdt.qlnvhang.request.search;

import com.tcdt.qlnvhang.request.PaggingReq;
import lombok.Data;


@Data
public class QlnvQdLcntHdrDchinhSearchReq {

	String soQdinh;
	String soQdGiaoCtkh;
	String soQdinhGoc;
	String tuNgayQd;
	String denNgayQd;
	String maHanghoa;
	String loaiHanghoa;
	String loaiDieuChinh;
	
	PaggingReq paggingReq;
	
}
