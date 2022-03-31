package com.tcdt.qlnvhang.response.quanlybienbannhapdaykholuongthuc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QlBienBanNdkCtLtRes {
    private Long id;

    private BigDecimal soLuong;

    private BigDecimal donGia;

    private BigDecimal thanhTien;

    private String ghiChu;

    private Integer stt;
}
