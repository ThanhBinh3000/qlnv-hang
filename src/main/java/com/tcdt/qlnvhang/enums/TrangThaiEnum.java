package com.tcdt.qlnvhang.enums;

public enum TrangThaiEnum {
	DU_THAO("00", "Dự Thảo", "Dự Thảo"),
	LANH_DAO_DUYET("01", "Lãnh Đạo Duyệt", "Lãnh Đạo Duyệt"),
	BAN_HANH("02", "Ban Hành", "Ban Hành"),
	TU_CHOI("03", "Từ Chối", "Từ Chối"),
	DU_THAO_TRINH_DUYET("04", "Dự Thảo", "Trình Duyệt");

	private final String id;
	private final String ten;
	private final String trangThaiDuyet;

	TrangThaiEnum(String id, String ten, String trangThaiDuyet) {
		this.id = id;
		this.ten = ten;
		this.trangThaiDuyet= trangThaiDuyet;
	}

	public String getId() {
		return id;
	}

	public String getTen() {
		return ten;
	}

	public String getTrangThaiDuyet() {
		return trangThaiDuyet;
	}

	public static String getTenById(String id) {
		for (TrangThaiEnum e : TrangThaiEnum.values()) {
			if (e.getId().equals(id))
				return e.getTen();
		}

		return null;
	}

	public static String getTrangThaiDuyetById(String id) {
		for (TrangThaiEnum status : TrangThaiEnum.values()) {
			if (status.getId().equals(id))
				return status.getTrangThaiDuyet();
		}

		return null;
	}
}
