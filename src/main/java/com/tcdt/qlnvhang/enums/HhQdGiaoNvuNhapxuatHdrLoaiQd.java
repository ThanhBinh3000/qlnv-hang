package com.tcdt.qlnvhang.enums;

public enum HhQdGiaoNvuNhapxuatHdrLoaiQd {
    NHAP("00", "Nhập"),
    XUAT("01", "Xuất");

    private final String id;
    private final String ten;

    HhQdGiaoNvuNhapxuatHdrLoaiQd(String id, String ten) {
        this.id = id;
        this.ten = ten;
    }

    public String getId() {
        return id;
    }

    public String getTen() {
        return ten;
    }

    public static String getTenById(String id) {
        for (HhQdGiaoNvuNhapxuatHdrLoaiQd status : HhQdGiaoNvuNhapxuatHdrLoaiQd.values()) {
            if (status.getId().equals(id))
                return status.getTen();
        }

        return null;
    }
}
