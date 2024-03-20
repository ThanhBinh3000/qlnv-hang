package com.tcdt.qlnvhang.request.object.vattu.bienbanguihang;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class NhBienBanGuiHangCtReq {
    private Long id;
    private String chucVu;
    private String daiDien;
    private Long bienBanGuiHangId;
    private String loaiBen;
    private String stt;
    private BigDecimal idVirtual;
}
