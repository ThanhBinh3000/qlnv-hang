package com.tcdt.qlnvhang.response.xuathang.bbtinhkho;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class XhBienBanTinhKhoRes {
    private Long id;
    private String soQd;
    private String loaiHH;
    private String chungLoaiHH;
    private String tenDvi;
    private String maDvi;
    private String diemKho;
    private String nhaKho;
    private String nganKho;
    private String loKho;
    private String soBienBan;
    private int soLuongNhap;
    private int soLuongXuat;
    private int slConlaiSosach;
    private int slConlaiXuatcuoi;
    private int slThuaConlai;
    private int slThieuConlai;
    private String nguyenNhan;
    private String kienNghi;
    private LocalDate ngayLapPhieu;
    private List<XhBienBanTinhKhoCtRes> ds = new ArrayList<>();
}
