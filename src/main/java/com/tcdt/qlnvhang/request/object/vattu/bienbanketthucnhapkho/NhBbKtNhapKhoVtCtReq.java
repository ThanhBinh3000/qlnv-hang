package com.tcdt.qlnvhang.request.object.vattu.bienbanketthucnhapkho;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class NhBbKtNhapKhoVtCtReq {
    private Long id;
    private Integer stt;
    private BigDecimal soLuong;
    private BigDecimal donGia;
    private BigDecimal thanhTien;
    private String ghiChu;
    private Long bbKtNhapKhoId;
}
