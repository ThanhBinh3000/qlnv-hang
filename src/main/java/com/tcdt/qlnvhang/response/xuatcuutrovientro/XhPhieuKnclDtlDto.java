package com.tcdt.qlnvhang.response.xuatcuutrovientro;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class XhPhieuKnclDtlDto {
    private int stt;
    private String chiTieuCl;
    private String chiSoCl;
    private String ketQua;
    private String phuongPhap;
    private String danhGia;
}
