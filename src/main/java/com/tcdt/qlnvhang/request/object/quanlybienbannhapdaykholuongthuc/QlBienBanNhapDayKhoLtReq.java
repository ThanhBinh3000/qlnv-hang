package com.tcdt.qlnvhang.request.object.quanlybienbannhapdaykholuongthuc;


import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.jni.Local;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QlBienBanNhapDayKhoLtReq {
    private Long id;

    @NotNull(message = "Không được để trống")
    private String soBienBan;

    @NotNull(message = "Không được để trống")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayLap;

    private LocalDate ngayNhapDayKho;

    @NotNull(message = "Không được để trống")
    private String maDonVi;

    private String maQhns;

    @NotNull(message = "Không được để trống")
    private String maKhoNganLo;

    @NotNull(message = "Không được để trống")
    private String maHang;

    @NotNull(message = "Không được để trống")
    private String tenHang;

    @NotNull(message = "Không được để trống")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayBatDauNhap;

    @NotNull(message = "Không được để trống")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayKetThucNhap;

    @NotNull(message = "Không được để trống")
    private String thuKho;

    @NotNull(message = "Không được để trống")
    private String kyThuatVien;

    @NotNull(message = "Không được để trống")
    private String keToan;

    @NotNull(message = "Không được để trống")
    private String thuTruong;

    private String chungLoaiHh;

    private String maDiemKho;
    private String maNhaKho;

    private List<QlBienBanNdkCtLtReq> chiTiets = new ArrayList<>();

    private List<FileDinhKemReq> fileDinhKems;
}
