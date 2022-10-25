package com.tcdt.qlnvhang.request.object.quanlybienbannhapdaykholuongthuc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QlBienBanNdkCtLtReq {
    private Long id;

    private String soPhieuKtraCl;

    private String phieuNhapKho;

    private String soBangKe;

    private LocalDate ngayNhap;

    private BigDecimal soLuong;
}
