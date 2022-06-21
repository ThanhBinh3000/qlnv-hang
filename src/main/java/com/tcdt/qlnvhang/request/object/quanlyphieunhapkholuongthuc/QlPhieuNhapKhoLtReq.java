package com.tcdt.qlnvhang.request.object.quanlyphieunhapkholuongthuc;

import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QlPhieuNhapKhoLtReq {

    private Long id;

    @NotNull(message = "Không được để trống")
    private Long phieuKtClId;

    private String soPhieu;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayLap;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayNhapKho;

    private String nguoiGiaoHang;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime thoiGianGiaoNhan;

    private String taiKhoanNo;
    private String taiKhoanCo;
    private String maDiemKho;
    private String maNhaKho;
    private String maNganKho;
    private String maNganLo;

    private String loaiHinhNhap;

    @NotNull(message = "Không được để trống")
    private String qdgnvnxId;

    private List<QlPhieuNhapKhoHangHoaLtReq> hangHoaList = new ArrayList<>();

    private List<FileDinhKemReq> chungTus;
}
