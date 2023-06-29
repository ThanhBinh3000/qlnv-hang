package com.tcdt.qlnvhang.response.dieuChuyenNoiBo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DcnbPhieuXuatKhoHdrListDTO {
    private Long id;
    private String soPhieuXuatKho;
    private LocalDate ngayXuatKho;
    private BigDecimal soLuong;

    private Long phieuKiemNghiemClId;
    private String soPhieuKiemNghiemCl;
    private Long bangKeCanHangId;
    private String soBangKeCanHang;

    public DcnbPhieuXuatKhoHdrListDTO(Long id, String soPhieuXuatKho, LocalDate ngayXuatKho) {
        this.id = id;
        this.soPhieuXuatKho = soPhieuXuatKho;
        this.ngayXuatKho = ngayXuatKho;
    }
}
