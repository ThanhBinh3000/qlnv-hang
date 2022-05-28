package com.tcdt.qlnvhang.response.quanlyphieunhapkholuongthuc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QlPhieuNhapKhoLtRes {
    private Long id;
    private Long phieuKtClId;
    private Long bbNghiemThuKlId;
    private String soPhieu;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayLap;
    private Long nganLoId;
    private String maNganLo;
    private String tenNganLo;
    private String tenNguoiGiaoNhan;
    private String diaChiGiaoNhan;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime thoiGianGiaoNhan;
    private String taiKhoanNo;
    private String taiKhoanCo;
    private String loaiHinhNhap;
    private String ghiChu;
    private String trangThai;
    private String tenTrangThai;
    private String trangThaiDuyet;
    private String qdgnvnxId;
    private Long diemKhoId;
    private String tenDiemKho;
    private String maDiemKho;
    private Long nhaKhoId;
    private String tenNhaKho;
    private String maNhaKho;
    private String maQhns;
    private LocalDate ngayNhapKho;
    private Long soQdNvuNhang;
    private LocalDate ngayQdNvuNhang;
    private String nguoiGiaoHang;
    private LocalDate ngayTao;
    private List<QlPhieuNhapKhoHangHoaLtRes> hangHoaRes = new ArrayList<>();
}
