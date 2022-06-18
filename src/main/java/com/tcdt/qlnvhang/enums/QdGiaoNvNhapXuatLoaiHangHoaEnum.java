package com.tcdt.qlnvhang.enums;

public enum QdGiaoNvNhapXuatLoaiHangHoaEnum {
    LUONG_THUC("00", "Lương Thực"),
    MUOI("01", "Muối"),
    VAT_TU("02", "Vật tư, thiết bị"),
    THOC("03", "Thóc"),
    GAO("04", "Gạo");
    private final String value;
    private final String ten;

    QdGiaoNvNhapXuatLoaiHangHoaEnum(String value, String ten) {
        this.value = value;
        this.ten = ten;
    }

    public String getValue() {
        return value;
    }

    public String getTen() {
        return ten;
    }

    public static String getTenById(String id) {
        for (QdGiaoNvNhapXuatLoaiHangHoaEnum status : QdGiaoNvNhapXuatLoaiHangHoaEnum.values()) {
            if (status.getValue().equals(id))
                return status.getTen();
        }

        return null;
    }
}
