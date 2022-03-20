package com.tcdt.qlnvhang.enums;

public enum TrangThaiEnum {
	MOI_TAO("00", "Mới tạo"),
	CHO_DUYET("01", "Chờ duyệt"),
	DA_DUYET("02", "Đã duyệt"),
	TU_CHOI("03", "Từ chối");

	private final String ma;
	private final String ten;

	TrangThaiEnum(String ma, String ten) {
		this.ma = ma;
		this.ten = ten;
	}

	public String getMa() {
		return ma;
	}

	public String getTen() {
		return ten;
	}
}
