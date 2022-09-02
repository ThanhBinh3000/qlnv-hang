package com.tcdt.qlnvhang.enums;

public enum NhapXuatHangTrangThaiEnum {
	DUTHAO("00", "Dự Thảo", "Dự Thảo"),
	CHODUYET_TP("01", "Chờ Duyệt - TP", "Chờ Duyệt"),
	TUCHOI_TP("02", "Từ Chối - TP", "Từ Chối"),

	CHODUYET_LDC("03", "Chờ Duyệt - LĐ Cục", "Chờ Duyệt"),
	TUCHOI_LDC("04", "Từ Chối - LĐ Cục", "Từ Chối"),
	DADUYET_LDC("05", "Đã Duyệt - LĐ Cục", "Đã Duyệt"),

	CHODUYET_TK("06", "Chờ Duyệt - Thủ Kho", "Chờ Duyệt"),
	CHODUYET_KTVBQ("07", "Chờ Duyệt - KTV Bảo Quản", "Chờ Duyệt"),
	CHODUYET_KT("08", "Chờ Duyệt - Kế Toán", "Chờ Duyệt"),

	TUCHOI_TK("09", "Từ Chối - Thủ Kho", "Từ Chối"),
	TUCHOI_KTVBQ("10", "Từ Chối - KTV Bảo Quản", "Từ Chối"),
	TUCHOI_KT("11", "Từ Chối - Kế Toán", "Từ Chối"),

	DADUYET_TK("12", "Đã Duyệt - Thủ Kho", "Đã Duyệt"),
	DADUYET_KTVBQ("13", "Đã Duyệt - KTV Bảo Quản", "Đã Duyệt"),
	DADUYET_KT("14", "Đã Duyệt - Kế Toán", "Đã Duyệt"),

	CHODUYET_LDCC("15", "Chờ Duyệt - LĐ Chi Cục", "Chờ Duyệt"),
	TUCHOI_LDCC("16", "Từ Chối - LĐ Chi Cục", "Từ Chối"),
	DADUYET_LDCC("17", "Đã Duyệt - LĐ Chi Cục", "Đã Duyệt"),

	CHODUYET_LDV("18", "Chờ Duyệt - LĐ Vụ", "Chờ Duyệt"),
	TUCHOI_LDV("19", "Từ Chối - LĐ Vụ", "Từ Chối"),
	DADUYET_LDV("20", "Đã Duyệt - LĐ Vụ", "Đã Duyệt"),

	CHODUYET_LDTC("21", "Chờ Duyệt - LĐ Tổng Cục", "Chờ Duyệt"),
	TUCHOI_LDTC("22", "Từ Chối - LĐ Tổng Cục", "Từ Chối"),
	DADUYET_LDTC("23", "Đã Duyệt - LĐ Tổng Cục", "Đã Duyệt"),

	CHUATONGHOP("24", "Chưa Tổng Hợp", "Chưa Tổng Hợp"),
	DATONGHOP("25", "Đã Tổng Hợp", "Đã Tổng Hợp"),
	CHUATAO_QD("26", "Chưa Tạo QĐ", "Chưa Tạo QĐ"),
	DADUTHAO_QD("27", "Đã Dự Thảo QĐ", "Đã Dự Thảo QĐ"),
	DABANHANH_QD("28", "Đã Ban Hành QĐ", "Đã Ban Hành QĐ"),

	BAN_HANH("29", "Ban Hành", "Ban Hành"),
	DAKY("30", "Đã Ký", "Đã Ký"),
	CHUATAOTOTRINH("31", "Chưa Tạo Tờ Trình", "Chưa Tạo Tờ Trình"),
	DATAOTOTRINH("32", "Đã Tạo Tờ Trình", "Đã Tạo Tờ Trình"),
	CHUACAPNHAT("33", "Chưa cập nhật", "Chưa cập nhật"),
	DANGCAPNHAT("34", "Đang cập nhật", "Đang cập nhật"),
	HOANTHANHCAPNHAT("35", "Hoan thành cập nhật", "Hoan thành cập nhật"),
	HUYTHAU("36", "Hủy thầu", "Hủy thầu"),
	TRUNGTHAU("37", "Trúng thầu", "Trúng thầu");


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
