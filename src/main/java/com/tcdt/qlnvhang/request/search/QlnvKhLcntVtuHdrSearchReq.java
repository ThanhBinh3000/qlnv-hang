package com.tcdt.qlnvhang.request.search;


import java.util.Date;

import com.tcdt.qlnvhang.request.PaggingReq;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class QlnvKhLcntVtuHdrSearchReq {

	String maVtu;
	String soQdinh;
	Date ngayQd;
	String soVban;
	Date ngayVban;
	String nguoiTao;
	String trangThai;
	String tuNgayLap;
	String denNgayLap;
	PaggingReq paggingReq;
}
