package com.tcdt.qlnvhang.response.dieuChuyenNoiBo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DcnbPhieuNhapKhoHdrListDTO {
    private Long id;
    private String soPhieuNhapKho;
    private Long phieuNhapKhoId;
    private LocalDate ngayNhapKho;
    private BigDecimal soLuong;

    private Long phieuKiemTraClId;
    private String soPhieuKiemTraCl;
    private Long bangKeCanHangId;
    private String soBangKeCanHang;
    private Long bangKeNhapVtId;
    private String soBangKeNhapVt;
    private Long bbCbiKhoId;
    private String soBbCbiKho;

    public DcnbPhieuNhapKhoHdrListDTO(Long id, String soPhieuNhapKho, LocalDate ngayNhapKho) {
        this.id = id;
        this.soPhieuNhapKho = soPhieuNhapKho;
        this.ngayNhapKho = ngayNhapKho;
    }
}
