package com.tcdt.qlnvhang.request.object;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class TTinGoiThau {
	String tengoithau;
	String nguonvon;
	String hanghoa;
	BigDecimal tongtien;
	BigDecimal tongluong;
	String madvitinh;
	String tungay;
	String denngay;
	List<DdiemQmoGthau> ddiemqmo;
	String tchuancluong;
	BigDecimal sophanthau;
	String hthuclchon;
	String pthuclchon;
	String ngayphanh;
	String thoihan;
	BigDecimal giaban;
	BigDecimal tienbaolanh;
	BigDecimal tiendambao;
	String ngaydongthau;
	String ngaymohso;
	String loaihdong;
	BigDecimal giatamtinh;
	String madvitamtinh;
	String ghichu;
	List<DsachPthau> dsthau;
}
