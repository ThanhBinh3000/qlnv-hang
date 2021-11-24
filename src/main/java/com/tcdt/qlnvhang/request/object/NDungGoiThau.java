package com.tcdt.qlnvhang.request.object;

import java.util.List;

import lombok.Data;

@Data
public class NDungGoiThau {
	String tengoithau;
	String soluong;
	String sohieu;
	String soluongton;
	String tongluong;
	String dongia;
	String vat;
	String dongiagoithau;
	String thanhtien;
	List<DdiemQmoGthau> ddiemqmo;
	String hthuclchon;
	String pthuclchon;
	String nguonvon;
	String loaihdong;
	String ngaydongthau;
	String ngaymohso;
	String ngaypduyet;
	String ngaytochuc;
	String ngaythuchien;
}
