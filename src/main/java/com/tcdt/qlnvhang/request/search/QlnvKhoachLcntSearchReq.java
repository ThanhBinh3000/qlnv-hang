package com.tcdt.qlnvhang.request.search;

import java.util.Date;

import com.tcdt.qlnvhang.request.PaggingReq;

import lombok.Data;

@Data
public class QlnvKhoachLcntSearchReq {
	String loaiHanghoa;
	String maDvi;
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
