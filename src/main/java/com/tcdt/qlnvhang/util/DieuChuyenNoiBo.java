package com.tcdt.qlnvhang.util;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import lombok.var;

import java.util.ArrayList;
import java.util.List;

public class DieuChuyenNoiBo {

    public static String checkType(String type) {
        if (type.equals("00")) return PathContains.BANG_KE_CAN_HANG_XUAT;
        return PathContains.BANG_KE_CAN_HANG_NHAP;
    }
    public static String getChiTieuKiemTra(String chiTieuKiemTra){
        var list = Lists.newArrayList(Splitter.on("-*").split(chiTieuKiemTra));
        List<String> nds = new ArrayList<>();
        for (String res : list) {
            String[] nd = res.split("-");
            nds.add(nd[1].trim());
        }
        return String.join(", ", nds);
    }
}
