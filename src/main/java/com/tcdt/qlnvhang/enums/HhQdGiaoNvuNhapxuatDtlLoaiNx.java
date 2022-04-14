package com.tcdt.qlnvhang.enums;

public enum HhQdGiaoNvuNhapxuatDtlLoaiNx {

    NHAP_THE_KE_HOACH("00", "Nhập theo kế hoạch"),
    NHAP_DO_DIEU_CHUYEN("01", "Nhập do điều chuyển"),
    XUAT_THEO_KE_HOACH("02", "Xuất theo kế hoạch"),
    XUAT_SUA_CHUA("03", "Xuất sửa chữa"),
    XUAT_THANH_LY("04", "Xuất thanh lý"),
    XUAT_TIEU_HUY("05", "Xuất tiêu hủy"),
    XUAT_CUU_TRO("06", "Xuất cứu trợ"),
    XUAT_DIEU_CHUYEN("07", "Xuất điều chuyển");

    private final String id;
    private final String ten;

    HhQdGiaoNvuNhapxuatDtlLoaiNx(String id, String ten) {
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
        for (HhQdGiaoNvuNhapxuatDtlLoaiNx status : HhQdGiaoNvuNhapxuatDtlLoaiNx.values()) {
            if (status.getId().equals(id))
                return status.getTen();
        }

        return null;
    }
}
