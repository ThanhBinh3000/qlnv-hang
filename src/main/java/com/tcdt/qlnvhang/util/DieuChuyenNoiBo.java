package com.tcdt.qlnvhang.util;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.tcdt.qlnvhang.enums.CheckDanhGiaEnum;
import lombok.var;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

public class DieuChuyenNoiBo {

    public static String checkType(String type) {
        if (type.equals("00")) return PathContains.BANG_KE_CAN_HANG_XUAT;
        return PathContains.BANG_KE_CAN_HANG_NHAP;
    }
    public static List<String> getDataNew(String chiTieuKiemTra){
        var list = Lists.newArrayList(Splitter.on("-*").split(chiTieuKiemTra));
        List<String> nds = new ArrayList<>();
        for (String res : list) {
            String[] nd = res.split("\\*");
            nds.add(nd.length > 1 ? nd[1].trim() : "");
        }
        return nds;
    }

    public static String getData(String chiTieuKiemTra){
        var list = Lists.newArrayList(Splitter.on("-*").split(chiTieuKiemTra));
        List<String> nds = new ArrayList<>();
        for (String res : list) {
            String[] nd = res.split("\\*");
            nds.add(nd[1].trim());
        }
        return String.join(", ", nds);
    }
    public static String checkDanhGia(Long danhGia) {
        if (!ObjectUtils.isEmpty(danhGia)) {
            if (danhGia == CheckDanhGiaEnum.dat.getValue()) return CheckDanhGiaEnum.dat.getDescription();
            return CheckDanhGiaEnum.khong_dat.getDescription();
        }
        return "";
    }
}
