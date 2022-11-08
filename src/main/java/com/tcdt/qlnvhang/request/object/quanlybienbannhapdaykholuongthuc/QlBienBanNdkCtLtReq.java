package com.tcdt.qlnvhang.request.object.quanlybienbannhapdaykholuongthuc;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.util.Contains;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QlBienBanNdkCtLtReq {
    private Long id;

    private String soPhieuKtraCl;

    private String soPhieuNhapKho;

    private String soBangKe;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Contains.FORMAT_DATE_STR)
    private Date ngayNhap;

    private BigDecimal soLuong;
}
