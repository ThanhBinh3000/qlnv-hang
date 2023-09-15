package com.tcdt.qlnvhang.util;

public class CheckTypeDieuChuyenNoiBo {

    public static String checkType(String type) {
        if (type.equals("00")) return PathContains.BANG_KE_CAN_HANG_XUAT;
        return PathContains.BANG_KE_CAN_HANG_NHAP;
    }
}
