package com.tcdt.qlnvhang.request.search.quanlyphieunhapkholuongthuc;

import com.tcdt.qlnvhang.request.BaseRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NhPhieuNhapKhoSearchReq extends BaseRequest {

    private Long soPhieu;
    private String soQdNhap;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate tuNgayNhapKho;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate denNgayNhapKho;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate tuNgayTaoPhieu;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate denNgayTaoPhieu;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate tuNgayGiaoNhan;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate denNgayGiaoNhan;

    private String loaiVthh;
}
