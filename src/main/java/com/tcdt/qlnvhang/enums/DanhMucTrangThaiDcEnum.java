package com.tcdt.qlnvhang.enums;

public enum DanhMucTrangThaiDcEnum {
    DU_THAO("00", "Dự thảo", "Dự thảo"),
    CHO_DUYET_TP("01", "Chờ duyệt - TP", "Chờ duyệt"),
    TU_CHOI_TP("02", "Từ chối – TP", "Từ chối"),
    CHO_DUYET_LDC("03", "Chờ duyệt - LĐ Cục", "Chờ duyệt"),
    TU_CHOI_LDC("04", "Từ chối – LĐ Cục", "Từ chối"),
    DA_DUYET_LDC("05", "Đã duyệt - LĐ Cục", "Đã duyệt"),
    CHODUYET_TK("06", "Chờ duyệt - Thủ kho", "Chờ duyệt"),
    CHODUYET_KTVBQ("07", "Chờ duyệt - KTV Bảo quản", "Chờ duyệt"),
    CHODUYET_KT("08", "Chờ duyệt - Kế toán", "Chờ duyệt"),
    TUCHOI_TK("09", "Từ chối - Thủ kho", "Từ chối"),
    TUCHOI_KTVBQ("10", "Từ chối - KTV Bảo quản", "Từ chối"),
    TUCHOI_KT("11", "Từ chối - Kế toán", "Từ chối"),
    DADUYET_TK("12", "Đã duyệt – Thủ kho", "Đã duyệt"),
    DADUYET_KTVBQ("13", "Đã duyệt – KTV Bảo quản", "Đã duyệt"),
    DADUYET_KT("14", "Đã duyệt – Kế toán", "Đã duyệt"),
    CHODUYET_LDCC("15", "Chờ duyệt – LĐ Chi cục", "Chờ duyệt"),
    TUCHOI_LDCC("16", "Từ chối – LĐ Chi cục", "Từ chối"),
    DADUYET_LDCC("17", "Đã duyệt - LĐ Chi cục", "Đã duyệt"),
    CHODUYET_LDV("18", "Chờ duyệt – LĐ Vụ", "Chờ duyệt"),
    TUCHOI_LDV("19", "Từ chối – LĐ Vụ", "Từ chối"),
    DADUYET_LDV("20", "Đã duyệt – LĐ Vụ", "Đã duyệt"),
    CHODUYET_LDTC("21", "Chờ duyệt – LĐ Tổng cục", "Chờ duyệt"),
    TUCHOI_LDTC("22", "Từ chối – LĐ Tổng cục", "Từ chối"),
    DADUYET_LDTC("23", "Đã duyệt – LĐ Tổng cục", "Đã duyệt"),
    DATONGHOP("24", "Đã tổng hợp", "Đã tổng hợp"),
    CHUATAO_QD("25", "Chưa tạo QĐ", "Chưa tạo QĐ"),
    DADUTHAO_QD("26", "Đã dự thảo QĐ", "Đã dự thảo QĐ"),
    DABANHANH_QD("27", "Đã ban hành QĐ", "Đã ban hành QĐ"),
    BAN_HANH("28", "Ban hành", "Ban hành"),


    ;

    private final String id;
    private final String ten;
    private final String trangThaiDuyet;

    DanhMucTrangThaiDcEnum(String id, String ten, String trangThaiDuyet) {
        this.id = id;
        this.ten = ten;
        this.trangThaiDuyet = trangThaiDuyet;
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
        for (QdGiaoNvNhapHangEnum status : QdGiaoNvNhapHangEnum.values()) {
            if (status.getId().equals(id))
                return status.getTen();
        }

        return null;
    }

    public static String getTrangThaiDuyetById(String id) {
        for (QdGiaoNvNhapHangEnum status : QdGiaoNvNhapHangEnum.values()) {
            if (status.getId().equals(id))
                return status.getTrangThaiDuyet();
        }

        return null;
    }
}
