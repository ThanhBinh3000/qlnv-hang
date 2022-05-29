package com.tcdt.qlnvhang.response.quanlybangkecanhangluongthuc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QlBangKeCanHangLtRes {
    private Long id;
    private String soBangKe;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayLap;
    private String maDonViLap;
    private Long qlPhieuNhapKhoLtId;
    private String soKho;
    private String maHang;
    private String tenHang;
    private String donViTinh;
    private String tenNguoiGiaoHang;
    private String tenDonViLap;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime thoiGianGiaoHang;
    private String diaChi;
    private String trangThai;
    private String tenTrangThai;
    private String trangThaiDuyet;
    private String soHd;

    private Long diemKhoId;
    private String maDiemKho;
    private String tenDiemKho;

    private Long nganLoId;
    private String maNganLo;
    private String tenNganLo;

    private Long nhaKhoId;
    private String maNhaKho;
    private String tenNhaKho;

    private Long qdgnvnxId;
    private String maThuKho;
    private String maLhKho;
    private String diaChiNguoiGiao;
    private LocalDate ngayNhapXuat;
    private List<QlBangKeChCtLtRes> chiTiets = new ArrayList<>();
}
