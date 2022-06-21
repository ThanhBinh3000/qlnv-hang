package com.tcdt.qlnvhang.response.quanlyphieunhapkholuongthuc;

import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.FileDinhKem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
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
    private String soPhieuKtCl;
    private Long qdgnvnxId;
    private String soQuyetDinhNhap;

    private String soPhieu;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayLap;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime thoiGianGiaoNhan;
    private String taiKhoanNo;
    private String taiKhoanCo;

    private String trangThai;
    private String tenTrangThai;
    private String trangThaiDuyet;

    private String tenDiemKho;
    private String maDiemKho;
    private String tenNhaKho;
    private String maNhaKho;
    private String tenNganKho;
    private String maNganKho;
    private String tenNganLo;
    private String maNganLo;

    private String loaiHinhNhap;

    private LocalDate ngayNhapKho;
    private String nguoiGiaoHang;

    private BigDecimal tongSoLuong;
    private BigDecimal tongSoTien;
    private String tongSoLuongBangChu;
    private String tongSoTienBangChu;
    private List<QlPhieuNhapKhoHangHoaLtRes> hangHoaRes = new ArrayList<>();

    private List<FileDinhKem> chungTus;
}
