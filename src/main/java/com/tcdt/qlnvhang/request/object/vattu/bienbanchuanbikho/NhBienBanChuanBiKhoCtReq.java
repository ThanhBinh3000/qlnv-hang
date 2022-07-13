package com.tcdt.qlnvhang.request.object.vattu.bienbanchuanbikho;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class NhBienBanChuanBiKhoCtReq {
    private Long id;
    private Long bbChuanBiKhoId;
    private String noiDung;
    private String donViTinh;
    private BigDecimal soLuongTrongNam;
    private BigDecimal donGiaTrongNam;
    private BigDecimal thanhTienTrongNam;
    private BigDecimal soLuongQt;
    private BigDecimal thanhTienQt;
    private BigDecimal tongGiaTri;
}
