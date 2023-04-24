package com.tcdt.qlnvhang.enums;

public enum LoaiDieuChuyenKeHoach {
    GIUA_2_CHI_CUC_TRONG_1_CUC("00"),
    GIUA_2_CUC_DTNN_KV("01"),
    TAT_CA("02");

    private final String id;
    LoaiDieuChuyenKeHoach(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
