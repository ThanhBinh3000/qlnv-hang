package com.tcdt.qlnvhang.enums;

public enum LoaiThanhPhanThamGiaDauGia {
    KHACH_MOI_CHUNG_KIEN("00"),
    DAU_GIA_VIEN("01"),
    NGUOI_TO_CHUA_THAM_GIA_DAU_GIA("3");

    private final String id;
    LoaiThanhPhanThamGiaDauGia(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
