package com.tcdt.qlnvhang.response.dieuChuyenNoiBo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class DcnbBbNhapDayKhoDtlDto {
    private BigDecimal donGia;
    private BigDecimal thanhTien;
    private BigDecimal soLuong;
}
