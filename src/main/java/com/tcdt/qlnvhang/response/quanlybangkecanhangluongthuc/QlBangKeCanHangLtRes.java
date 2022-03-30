package com.tcdt.qlnvhang.response.quanlybangkecanhangluongthuc;

import com.tcdt.qlnvhang.response.quanlyphieunhapkholuongthuc.QlPhieuNhapKhoHangHoaLtRes;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
public class QlBangKeCanHangLtRes {
    private Long id;
    private String soBangKe;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayLap;
    private String maDonViLap;
    private Long qlPhieuNhapKhoLtId;
    private String maKhoNganLo;
    private String soKho;
    private String maHang;
    private String tenHang;
    private String donViTinh;
    private String tenNguoiGiaoHang;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime thoiGianGiaoHang;
    private String diaChi;
    private String trangThai;
    private String tenTrangThai;


    private List<QlBangKeChCtLtRes> chiTiets = new ArrayList<>();
}
