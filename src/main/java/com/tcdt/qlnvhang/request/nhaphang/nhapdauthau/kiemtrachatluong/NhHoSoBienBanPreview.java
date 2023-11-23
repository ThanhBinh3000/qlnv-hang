package com.tcdt.qlnvhang.request.nhaphang.nhapdauthau.kiemtrachatluong;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class NhHoSoBienBanPreview {

    private String tenBb;
    private String soBienBan;
    private String soHd;
    private String ngayHd;
    private String tenDvi;
    private BigDecimal soLuongNhap;
    private String ngayTao;
    private String tenLoaiVthh;
    private String tenCloaiVthh;
    private String tgianNhap;
    private String diaDiemKiemTra;
    private String noiDung;

    private String ketLuan;


}
