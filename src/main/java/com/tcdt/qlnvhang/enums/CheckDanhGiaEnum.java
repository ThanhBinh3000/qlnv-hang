package com.tcdt.qlnvhang.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CheckDanhGiaEnum {
    dat(1, "Đạt"),
    khong_dat(0, "Không đạt");

    private final int value;

    private final String description;

    CheckDanhGiaEnum(int value, String description) {
        this.value = (byte) value;
        this.description = description;
    }

    @JsonValue
    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    @JsonCreator
    public static EnumResponse fromValue(int value) {
        for (EnumResponse v : EnumResponse.values()) {
            if (v.getValue() == value) {
                return v;
            }
        }
        return null;
    }
}
