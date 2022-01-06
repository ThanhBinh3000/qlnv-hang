package com.tcdt.qlnvhang.request.search;

import com.tcdt.qlnvhang.request.PaggingReq;
import lombok.Data;


@Data
public class QlnvTtinDthauVtuHdrSearchReq {

	String soQdKh;
	String maVtu;
	
	PaggingReq paggingReq;
	
}
