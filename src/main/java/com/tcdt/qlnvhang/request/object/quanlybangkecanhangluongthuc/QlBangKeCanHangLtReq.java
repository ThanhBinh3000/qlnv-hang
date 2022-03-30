package com.tcdt.qlnvhang.request.object.quanlybangkecanhangluongthuc;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
public class QlBangKeCanHangLtReq {
    private Long id;

    @NotNull(message = "Không được để trống")
    private String soBangKe;

    @NotNull(message = "Không được để trống")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayLap;

    @NotNull(message = "Không được để trống")
    private String maDonViLap;

    @NotNull(message = "Không được để trống")
    private Long qlPhieuNhapKhoLtId;

    @NotNull(message = "Không được để trống")
    private String maKhoNganLo;

    @NotNull(message = "Không được để trống")
    private String soKho;

    @NotNull(message = "Không được để trống")
    private String maHang;

    @NotNull(message = "Không được để trống")
    private String tenHang;

    @NotNull(message = "Không được để trống")
    private String donViTinh;

    @NotNull(message = "Không được để trống")
    private String tenNguoiGiaoHang;

    @NotNull(message = "Không được để trống")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime thoiGianGiaoHang;

    @NotNull(message = "Không được để trống")
    private String diaChi;

    private List<QlBangKeChCtLtReq> chiTiets = new ArrayList<>();
}
