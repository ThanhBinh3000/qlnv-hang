package com.tcdt.qlnvhang.request.object.quanlyphieunhapkholuongthuc;

import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.request.object.SoBienBanPhieuReq;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NhPhieuNhapKhoReq extends SoBienBanPhieuReq {

    private Long id;

    private List<Long> phieuKtClIds = new ArrayList<>();
    private Long hoSoKyThuatId;
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
    private Long qdgnvnxId;
    private BigDecimal tongSoLuong;
    private BigDecimal tongSoTien;
    private String loaiVthh;

    private List<NhPhieuNhapKhoCtReq> hangHoaList = new ArrayList<>();

    private List<FileDinhKemReq> chungTus;
}
