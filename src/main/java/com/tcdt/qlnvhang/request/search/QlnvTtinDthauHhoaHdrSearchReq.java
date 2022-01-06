package com.tcdt.qlnvhang.request.search;

import com.tcdt.qlnvhang.request.PaggingReq;
import lombok.Data;


@Data
public class QlnvTtinDthauHhoaHdrSearchReq {

	String soQdKh;
	String maHhoa;
	
	PaggingReq paggingReq;
	
}
