package com.tcdt.qlnvhang.request.object.dtvt;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class DtThuongThaoHopDongVtReq {

    @NotNull(message = "Không được để trống")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate ngayKy;

    @NotNull(message = "Không được để trống")
    private BigDecimal donGiaTruocThue;

    @NotNull(message = "Không được để trống")
    private BigDecimal giaHopDongTruocThue;

    @NotNull(message = "Không được để trống")
    private Double vat;

    @NotNull(message = "Không được để trống")
    private BigDecimal donGiaSauThue;

    @NotNull(message = "Không được để trống")
    private BigDecimal giaHopDongSauThue;
}
