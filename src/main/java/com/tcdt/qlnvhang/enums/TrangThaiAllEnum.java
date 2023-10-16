package com.tcdt.qlnvhang.enums;

public enum TrangThaiAllEnum {
    DU_THAO("00", "Dự thảo"),
    CHO_DUYET_TP("01", "Chờ duyệt - TP"),
    TU_CHOI_TP("02", "Từ chối - TP"),
    CHO_DUYET_LDC("03", "Chờ duyệt - LĐ Cục"),
    TU_CHOI_LDC("04", "Từ chối - LĐ Cục"),
    DA_DUYET_LDC("05", "Đã duyệt - LĐ Cục"),
    CHO_DUYET_TK("06", "Chờ duyệt - Thủ kho"),
    CHO_DUYET_KTVBQ("07", "Chờ duyệt - KTV Bảo quản"),
    CHO_DUYET_KT("08", "Chờ duyệt - Kế toán"),
    TU_CHOI_TK("09", "Từ chối - Thủ kho"),
    TU_CHOI_KTVBQ("10", "Từ chối - KTV Bảo quản"),
    TU_CHOI_KT("11", "Từ chối - Kế toán"),
    DA_DUYET_TK("12", "Đã duyệt – Thủ kho"),
    DA_DUYET_KTVBQ("13", "Đã duyệt – KTV Bảo quản"),
    DA_DUYET_KT("14", "Đã duyệt – Kế toán"),
    CHO_DUYET_LDCC("15", "Chờ duyệt – LĐ Chi cục"),
    TU_CHOI_LDCC("16", "Từ chối – LĐ Chi cục"),
    DA_DUYET_LDCC("17", "Đã duyệt - LĐ Chi cục"),
    CHO_DUYET_LDV("18", "Chờ duyệt – LĐ Vụ"),
    TU_CHOI_LDV("19", "Từ chối – LĐ Vụ"),
    DA_DUYET_LDV("20", "Đã duyệt – LĐ Vụ"),
    CHO_DUYET_LDTC("21", "Chờ duyệt – LĐ Tổng cục"),
    TU_CHOI_LDTC("22", "Từ chối – LĐ Tổng cục"),
    DA_DUYET_LDTC("23", "Đã duyệt – LĐ Tổng cục"),
    CHUA_TONG_HOP("24", "Chưa tổng hợp"),
    DA_TONG_HOP("25", "Đã tổng hợp"),
    CHUA_TAO_QD("26", "Chưa tạo QĐ"),
    DA_DU_THAO_QD("27", "Đã dự thảo QĐ"),
    DA_BAN_HANH_QD("28", "Đã ban hành QĐ"),
    BAN_HANH("29", "Ban hành"),
    DA_KY("30", "Đã ký"),
    CHUA_TAO_TO_TRINH("31", "Chưa tạo tờ trình"),
    DA_TAO_TO_TRINH("32", "Đã tạo tờ trình"),
    //   CHUA_CAP_NHAT("32", "Đã tạo tờ trình");
    CHUA_CAP_NHAT("33", "Chưa cập nhật"),
    DANG_CAP_NHAT("34", "Đang cập nhật"),
    HOAN_THANH_CAP_NHAT("35", "Hoàn thành cập nhật"),
    HUY_THAU("36", "Hủy thầu"),
    TRUNG_THAU("37", "Trúng thầu"),
    CHODUYET_TBP_TVQT("38", "Chờ duyệt -TBP TVQT"),
    TUCHOI_TBP_TVQT("39", "Từ chối -TBP TVQT"),
    THANH_CONG("40", "Thành công"),
    THAT_BAI("41", "Thất bại"),
    TRUOT_THAU("42", "Trượt thầu"),
    CHUA_THUC_HIEN("43", "Chưa thực hiện"),
    DANG_THUC_HIEN("44", "Đang thực hiện"),
    DA_HOAN_THANH("45", "Đã hoàn thành"),
    DA_NGHIEM_THU("46", "Đã nghiệm thu"),
    CHO_DUYET_TBP_KTBQ("47", "Chờ duyệt Phòng kỹ thuật bảo quản"),
    TU_CHOI_TBP_KTBQ("48", "Từ chối Phòng kỹ thuật bảo quản"),
    CHO_DUYET_BTC("49", "Chờ duyệt - BTC"),
    TU_CHOI_BTC("50", "Từ chối - BTC"),
    DA_DUYET_BTC("51", "Đã duyệt - BTC"),
    DA_TAO_CBV("52", "Đã tạo cán bộ Vụ"),
    DA_DUYET_CBV("53", "Đã duyệt cán bộ Vụ"),
    TU_CHOI_CBV("54", "Từ chối cán bộ Vụ"),
    YC_CHICUC_PHANBO_DC("59", "Y/c Chi cục xác định điểm nhập ĐC"),
    DA_PHANBO_DC_CHODUYET_TBP_TVQT("60", "Đã xác định điểm nhập, chờ duyệt – TBP TVQT"),
    DA_PHANBO_DC_TUCHOI_TBP_TVQT("61", "Đã xác định điểm nhập, từ chối – TBP TVQT"),
    DA_PHANBO_DC_CHODUYET_LDCC("62", "Đã xác định điểm nhập, chờ duyệt – LĐ Chi cục"),
    DA_PHANBO_DC_TUCHOI_LDCC("63", "Đã xác định điểm nhập, từ chối – LĐ Chi cục"),
    DA_PHANBO_DC_DADUYET_LDCC("64", "Đã xác định điểm nhập, đã duyệt – LĐ Chi cục"),
    DA_PHANBO_DC_CHODUYET_TP("65", "Đã xác định điểm nhập, chờ duyệt – TP"),
    DA_PHANBO_DC_TUCHOI_TP("66", "Đã xác định điểm nhập, từ chối – TP"),
    DA_PHANBO_DC_CHODUYET_LDC("67", "Đã xác định điểm nhập, chờ duyệt – LĐ Cục"),
    DA_PHANBO_DC_TUCHOI_LDC("68", "Đã xác định điểm nhập, từ chối – LĐ Cục"),
    DA_PHANBO_DC_DADUYET_LDC("69", "Đã xác định điểm nhập, đã duyệt – LĐ Cục"),
    CHUA_CHOT("71", "Chưa chốt"),
    DA_CHOT("72", "Đã chốt"),
    DA_THANH_LY("73", "Đã thanh lý"),
    DA_XUAT_KHO("74", "Đã xuất kho"),
    DA_TIEU_HUY("75", "Đã tiêu hủy"),
    GUI_DUYET("76", "Gửi duyệt"),
    DANG_DUYET_CB_VU("77", "Đang duyệt - CB Vụ"),
    DANG_NHAP_DU_LIEU("78", "Đang nhập dữ liệu"),
    TU_CHOI_BAN_HANH_QD("79", "Từ chối ban hành QĐ"),
    DA_LAP("80", "Đã lập"),
    CHUATAO_KH("81", "Chưa tạo KH"),
    DADUTHAO_KH("82", "Đã dự thảo KH"),
    DAGUIDUYET_KH("83", "Đã gửi duyệt KH");

    private final String id;
    private final String ten;

    TrangThaiAllEnum(String id, String ten) {
        this.id = id;
        this.ten = ten;

    }

    public static String getLabelById(String id) {
        for (TrangThaiAllEnum e : values()) {
            if (e.id.equals(id)) {
                return e.ten;
            }
        }
        return null;
    }

    public String getId() {
        return id;
    }

    public String getTen() {
        return ten;
    }

}
