package com.tcdt.qlnvhang.enums;
public enum LoaiDaiDienEnum {
	TONG_CUC("1"),
	CUC("2"),
	CHI_CUC("3"),
	BEN_GIAO("4");

	private final String id;
	LoaiDaiDienEnum(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
}
