package com.tcdt.qlnvhang.response.quanlyphieunhapkholuongthuc;

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
public class QlPhieuNhapKhoLtRes {
    private Long id;
    private Long phieuKtClId;
    private Long bbNghiemThuKlId;
    private String soPhieu;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayLap;
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

    private List<QlPhieuNhapKhoHangHoaLtRes> hangHoaRes = new ArrayList<>();
}
