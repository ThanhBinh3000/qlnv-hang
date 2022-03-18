package com.tcdt.qlnvhang.response.dtvt;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
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
