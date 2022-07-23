package com.tcdt.qlnvhang.response.quanlyphieunhapkholuongthuc;

import com.tcdt.qlnvhang.entities.quanlyphieunhapkholuongthuc.NhPhieuNhapKhoCt1;
import com.tcdt.qlnvhang.response.SoBienBanPhieuRes;
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
public class NhPhieuNhapKhoRes extends SoBienBanPhieuRes {
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
    private BigDecimal taiKhoanNo;
    private BigDecimal taiKhoanCo;

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
    private List<NhPhieuNhapKhoCtRes> hangHoaRes = new ArrayList<>();
    private List<NhPhieuNhapKhoCt1> chiTiet1s = new ArrayList<>();
    private Long hoSoKyThuatId;
    private String soBbHoSoKyThuat;
    private String loaiVthh;
    private String maDvi;
    private String tenDvi;
    private String maQhns;

    private List<FileDinhKem> chungTus;
}
