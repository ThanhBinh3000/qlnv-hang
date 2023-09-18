package com.tcdt.qlnvhang.response.feign;

import lombok.Data;

@Data
public class TrangThaiHtResponce {
    private int id;
    private String maDonVi;
    private String tenDonVi;
    private String maVthh;
    private String tenVthh;
    private String loaiVthh;
    private String tenLoaiVthh;
    private String cloaiVthh;
    private String tenCloaiVthh;
    private int slHienThoi;
    private String duDau;
    private String tongNhap;
    private String tongXuat;
    private String nam;
    private int donViTinhId;
}
