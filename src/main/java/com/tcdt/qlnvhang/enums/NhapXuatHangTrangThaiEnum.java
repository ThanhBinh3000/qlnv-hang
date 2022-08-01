package com.tcdt.qlnvhang.enums;

public enum NhapXuatHangTrangThaiEnum {
	DU_THAO("00", "Dự Thảo", "Dự Thảo"),
	CHO_DUYET_TP("01", "Chờ Duyệt - TP", "Chờ Duyệt"),
	CHO_DUYET_THU_KHO("02", "Chờ Duyệt - Thủ Kho", "Chờ Duyệt"),
	CHO_DUYET_KE_TOAN("03", "Chờ Duyệt - Kế Toán", "Chờ Duyệt"),
	CHO_DUYET_KTV_BAO_QUAN("04", "Chờ Duyệt - KTV Bảo Quản", "Chờ Duyệt"),
	CHO_DUYET_CAN_BO_KE_TOAN("05", "Chờ Duyệt - Cán Bộ Kế Toán", "Chờ Duyệt"),
	CHO_DUYET_TP_KTBQ("06", "Chờ Duyệt - TP KTBQ", "Chờ Duyệt"),
	CHO_DUYET_LD_CHI_CUC("07", "Chờ Duyệt - LĐ Chi Cục", "Chờ Duyệt"),
	CHO_DUYET_TP_KH_QLHDT("08", "Chờ Duyệt - TP KH&QLHDT", "Chờ Duyệt"),
	CHO_DUYET_LD_CUC("09", "Chờ Duyệt - LĐ Cục", "Chờ Duyệt"),
	TU_CHOI_TP("10", "Từ Chối - TP", "Từ Chối"),
	TU_CHOI_THU_KHO("11", "Từ Chối - Thủ Kho", "Từ Chối"),
	TU_CHOI_KE_TOAN("12", "Từ Chối - Kế Toán", "Từ Chối"),
	TU_CHOI_KTV_BAO_QUAN("13", "Từ Chối - KTV Bảo Quản", "Từ Chối"),
	TU_CHOI_CAN_BO_KE_TOAN("14", "Từ Chối - KTV Bảo Quản", "Từ Chối"),
	TU_CHOI_TP_KTBQ("15", "Từ Chối - TP KTBQ", "Từ Chối"),
	TU_CHOI_LD_CHI_CUC("16", "Từ Chối - LĐ Chi Cục", "Từ Chối"),
	TU_CHOI_TP_KH_QLHDT("17", "Từ Chối - TP KH&QLHDT", "Từ Chối"),
	TU_CHOI_LD_CUC("18", "Từ Chối - LĐ Cục", "Từ Chối"),
	DA_DUYET("19", "Đã Duyệt", "Đã Duyệt"),
	BAN_HANH("20", "Ban Hành", "Ban Hành");

	private final String id;
	private final String ten;
	private final String trangThaiDuyet;

	NhapXuatHangTrangThaiEnum(String id, String ten, String trangThaiDuyet) {
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
		for (NhapXuatHangTrangThaiEnum e : NhapXuatHangTrangThaiEnum.values()) {
			if (e.getId().equals(id))
				return e.getTen();
		}

		return null;
	}

	public static String getTrangThaiDuyetById(String id) {
		for (NhapXuatHangTrangThaiEnum status : NhapXuatHangTrangThaiEnum.values()) {
			if (status.getId().equals(id))
				return status.getTrangThaiDuyet();
		}

		return null;
	}
}
