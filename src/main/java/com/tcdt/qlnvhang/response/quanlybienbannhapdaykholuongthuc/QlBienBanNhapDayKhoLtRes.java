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

    private Long diemKhoId;
    private String maDiemKho;
    private String tenDiemKho;

    private Long nganLoId;
    private String maNganLo;
    private String tenNganLo;

    private Long nhaKhoId;
    private String maNhaKho;
    private String tenNhaKho;

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

    private String trangThaiDuyet;

    private Long qdgnvnxId;

    private String maDvi;
    private String tenDvi;
    private String chungLoaiHangHoa;
    private LocalDate ngayNhapDayKho;
    private List<QlBienBanNdkCtLtRes> chiTiets = new ArrayList<>();
}
