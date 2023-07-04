package com.tcdt.qlnvhang.request.nhaphang.nhapkhac;

import lombok.Data;

@Data
public class HhNkPhieuKtclCtReq {
    private Long id;
    private Long phieuKtChatLuongId;
    private String tenTchuan;
    private String ketQuaKiemTra;
    private String phuongPhap;
    private String danhGia;
    private String chiSoNhap;
}
