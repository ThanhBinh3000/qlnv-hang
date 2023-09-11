package com.tcdt.qlnvhang.response.dieuChuyenNoiBo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DcnbBangKeCanHangDtlDto {
    private int stt;

    private String maCan;

    private String soBaoBi;

    private BigDecimal trongLuongCaBaoBi;

}
