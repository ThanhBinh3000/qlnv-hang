package com.tcdt.qlnvhang.request.search;

import java.util.Date;

import com.tcdt.qlnvhang.request.PaggingReq;

import lombok.Data;

@Data
public class QlnvKhoachLcntSearchReq {
	String loaiHanghoa;
	String hoanghoa;
	String maDvi;
	String soDx;
	String trangThai;
	String tuNgayLap;
	String denNgayLap;
	PaggingReq paggingReq;
}
