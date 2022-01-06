package com.tcdt.qlnvhang.request.search;

import com.tcdt.qlnvhang.request.PaggingReq;
import lombok.Data;


@Data
public class QlnvQdLcntHdrSearchReq {

	String soQdinh;
	String soQdGiaoCtkh;
	String tuNgayQd;
	String denNgayQd;
	String maHanghoa;
	String loaiHanghoa;
	
	PaggingReq paggingReq;
	
}
