package com.tcdt.qlnvhang.response.nhaphang.nhapkhac;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class HhNkPhieuNhapKhoHdrListDTO {
    private Long id;
    private String soPhieuNhapKho;
    private LocalDate ngayNhapKho;
    private BigDecimal soLuong;

    private Long phieuKiemTraClId;
    private String soPhieuKiemTraCl;
    private Long bangKeCanHangId;
    private String soBangKeCanHang;
    private Long bangKeVtId;
    private String soBangKeVt;
    private Long bbCbiKhoId;
    private String soBbCbiKho;

    public HhNkPhieuNhapKhoHdrListDTO(Long id, String soPhieuNhapKho, LocalDate ngayNhapKho) {
        this.id = id;
        this.soPhieuNhapKho = soPhieuNhapKho;
        this.ngayNhapKho = ngayNhapKho;
    }
}
