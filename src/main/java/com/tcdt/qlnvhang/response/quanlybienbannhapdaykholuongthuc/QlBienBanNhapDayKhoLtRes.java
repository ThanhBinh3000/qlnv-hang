package com.tcdt.qlnvhang.response.quanlybienbannhapdaykholuongthuc;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QlBienBanNhapDayKhoLtRes {
    private Long id;
    private String soBienBan;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayLap;

    private String maDonViLap;

    private String maKhoNganLo;

    private String maHang;

    private String tenHang;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayBatDauNhap;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayKetThucNhap;

    private String thuKho;

    private String kyThuatVien;

    private String keToan;

    private String thuTruong;

    private String trangThai;

    private String tenTrangThai;

    private List<QlBienBanNdkCtLtRes> chiTiets = new ArrayList<>();
}
