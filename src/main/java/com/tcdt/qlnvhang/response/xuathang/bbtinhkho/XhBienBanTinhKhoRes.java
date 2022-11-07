package com.tcdt.qlnvhang.response.xuathang.bbtinhkho;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
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
    private String maDiemKho;
    private String maNhaKho;
    private String maNganKho;
    private String maLoKho;
    private String soBienBan;
    private double soLuongNhap;
    private double soLuongXuat;
    private double slConlaiSosach;
    private double slConlaiXuatcuoi;
    private double slThuaConlai;
    private double slThieuConlai;
    private String nguyenNhan;
    private String kienNghi;
    private Date ngayLapPhieu;
    private String trangThai;
    private String tenTrangThai;
    private String trangThaiDuyet;
    private List<XhBienBanTinhKhoCtRes> ds = new ArrayList<>();
}
