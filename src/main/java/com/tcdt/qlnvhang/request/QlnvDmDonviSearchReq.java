package com.tcdt.qlnvhang.request;

import com.tcdt.qlnvhang.request.PaggingReq;

import lombok.Data;

import java.util.List;

@Data
public class QlnvDmDonviSearchReq {
	String maDvi;
	String tenDvi;
	String maTinh;
	String maQuan;
	String maPhuong;
	String capDvi;
	String kieuDvi;
	String loaiDvi;
	String trangThai;
	String maDviCha;
	List<String> listMaDvi;
	String type;
	PaggingReq paggingReq;
}
