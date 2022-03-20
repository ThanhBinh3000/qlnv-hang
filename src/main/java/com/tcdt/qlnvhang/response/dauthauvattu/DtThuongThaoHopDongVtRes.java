package com.tcdt.qlnvhang.response.dauthauvattu;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class DtThuongThaoHopDongVtRes {

    private Long id;
    private LocalDate ngayKy;
    private BigDecimal donGiaTruocThue;
    private BigDecimal giaHopDongTruocThue;
    private Double vat;
    private BigDecimal donGiaSauThue;
    private BigDecimal giaHopDongSauThue;
}
