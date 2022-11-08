package com.tcdt.qlnvhang.response.xuathang.bbhaodoi;

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
public class XhBienBanHaoDoiRes {
    private Long id;
    private String soBienBanTinhKho;
    private String soQd;
    private String tenDvi;
    private String maDvi;
    private String soBienBan;
    private String diemKho;
    private String nhaKho;
    private String nganKho;
    private String loKho;
    private Date ngayNhap;
    private Date ngayXuat;
    private double slHaoThanhly;
    private double slHaoThucte;
    private double tileThucte;
    private double tileThanhly;
    private double soLuongNhap;
    private double soLuongXuat;
    private double slVuotDm;
    private double tileVuotDm;
    private double slDuoiDm;
    private double tileDuoiDm;
    private String nguyenNhan;
    private String kienNghi;
    private Date ngayLapPhieu;
    private String trangThai;
    private String tenTrangThai;
    private String trangThaiDuyet;
    private List<XhBienBanHaoDoiCtRes> ds = new ArrayList<>();
}
