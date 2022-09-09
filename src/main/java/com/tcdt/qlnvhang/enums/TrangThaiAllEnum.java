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
  DA_TAO_TO_TRINH("32", "Đã tạo tờ trình");

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
