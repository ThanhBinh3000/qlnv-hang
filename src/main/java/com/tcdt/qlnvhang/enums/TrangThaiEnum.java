package com.tcdt.qlnvhang.enums;

public enum TrangThaiEnum {
	DU_THAO("00", "Dự Thảo", "Dự Thảo"),
	LANH_DAO_DUYET("01", "Lãnh Đạo Duyệt", "Lãnh Đạo Duyệt"),
	BAN_HANH("02", "Ban Hành", "Ban Hành"),
	TU_CHOI("03", "Từ Chối", "Từ Chối"),
	DU_THAO_TRINH_DUYET("04", "Dự Thảo", "Trình Duyệt");

	private final String ma;
	private final String ten;
	private final String trangThaiDuyet;

	TrangThaiEnum(String ma, String ten, String trangThaiDuyet) {
		this.ma = ma;
		this.ten = ten;
		this.trangThaiDuyet= trangThaiDuyet;
	}

	public String getMa() {
		return ma;
	}

	public String getTen() {
		return ten;
	}

	public String getTrangThaiDuyet() {
		return trangThaiDuyet;
	}

	public static String getTen(String ma) {
		for (TrangThaiEnum e : TrangThaiEnum.values()) {
			if (e.getMa().equals(ma))
				return e.getTen();
		}

		return null;
	}

	public static String getTrangThaiDuyetById(String ma) {
		for (TrangThaiEnum status : TrangThaiEnum.values()) {
			if (status.getMa().equals(ma))
				return status.getTrangThaiDuyet();
		}

		return null;
	}
}
