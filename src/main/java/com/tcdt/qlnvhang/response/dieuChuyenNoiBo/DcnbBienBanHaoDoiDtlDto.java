package com.tcdt.qlnvhang.response.dieuChuyenNoiBo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class DcnbBienBanHaoDoiDtlDto {
    private int stt;
    private String thoiGianBaoQuan;
    private BigDecimal soLuongHaoTheoDinhMuc;
    private Double slBaoQuan;
    private Double dinhMucHaoHut;
    private Double slHao;
}
