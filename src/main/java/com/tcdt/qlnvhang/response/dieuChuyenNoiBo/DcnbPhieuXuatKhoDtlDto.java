package com.tcdt.qlnvhang.response.dieuChuyenNoiBo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class DcnbPhieuXuatKhoDtlDto {
    private int stt;
    private String tenLoaiVthh;
    private String maSo;
    private String donViTinh;
    private BigDecimal slDcThucTe;
    private BigDecimal duToanKinhPhiDc;
    private BigDecimal kinhPhiDcTt;
}
