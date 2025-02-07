package com.tcdt.qlnvhang.enums;

public enum QdGiaoNvNhapHangEnum  {
    DU_THAO("00", "Dự Thảo", "Dự Thảo"),
    BAN_HANH("11", "Ban Hành", "Ban Hành"),
    CHO_DUYET_TP("01", "Chờ Duyệt - TP", "Chờ Duyệt"),
    CHO_DUYET_LD_C("02", "Chờ Duyệt - LĐ Cục", "Chờ Duyệt"),
    DA_DUYET("03", "Đã Duyệt", "Đã Duyệt"),
    TU_CHOI_TP("04", "Từ Chối – TP", "Từ Chối"),
    TU_CHOI_LD_C("05", "Từ Chối – LĐ Cục", "Từ Chối"),
    ;

    private final String id;
    private final String ten;
    private final String trangThaiDuyet;

    QdGiaoNvNhapHangEnum(String id, String ten, String trangThaiDuyet) {
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
