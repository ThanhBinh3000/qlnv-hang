package com.tcdt.qlnvhang.request.object.quanlyphieunhapkholuongthuc;

import com.tcdt.qlnvhang.entities.quanlyphieunhapkholuongthuc.QlPhieuNhapKhoHangHoaLt;
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

    @NotNull(message = "Không được để trống")
    private Long bbNghiemThuKlId;

    @NotNull(message = "Không được để trống")
    private String soPhieu;

    @NotNull(message = "Không được để trống")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayLap;

    private String maNganLo;

    private String tenNganLo;

    @NotNull(message = "Không được để trống")
    private String tenNguoiGiaoNhan;

    @NotNull(message = "Không được để trống")
    private String diaChiGiaoNhan;

    @NotNull(message = "Không được để trống")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime thoiGianGiaoNhan;

    @NotNull(message = "Không được để trống")
    private String taiKhoanNo;

    @NotNull(message = "Không được để trống")
    private String taiKhoanCo;

    @NotNull(message = "Không được để trống")
    private String loaiHinhNhap;

    private String ghiChu;

    private List<QlPhieuNhapKhoHangHoaLtReq> hangHoaList = new ArrayList<>();
}
