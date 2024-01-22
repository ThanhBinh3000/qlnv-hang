package com.tcdt.qlnvhang.enums;

public enum KhPagQuyetDinhBtcEnum {
  DU_THAO("00", "Dự Thảo"),
  CHUATAO_QD("26", "Chưa tạo QĐ"),
  DADUTHAO_QD("27", "Đã dự thảo QĐ"),
  DABANHANH_QD("28", "Đã ban hành QĐ"),
  BAN_HANH("29", "Ban Hành");

  private final String id;
  private final String ten;

  KhPagQuyetDinhBtcEnum(String id, String ten) {
    this.id = id;
    this.ten = ten;

  }

  public static String getLabelById(String id) {
    for (KhPagQuyetDinhBtcEnum e : values()) {
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
