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
    private LocalDate ngayNhapKho;
}
