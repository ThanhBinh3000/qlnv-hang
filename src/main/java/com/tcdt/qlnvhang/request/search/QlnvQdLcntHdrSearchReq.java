package com.tcdt.qlnvhang.request.search;


import java.util.Date;

import com.tcdt.qlnvhang.request.PaggingReq;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class QlnvQdLcntHdrSearchReq {

	String soQdinh;
	String soQdKh;
	String tuNgayQd;
	String denNgayQd;
	String maHanghoa;
	String loaiHanghoa;
	PaggingReq paggingReq;
	
}
