package com.tcdt.qlnvhang.request.object.bbanlaymau;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class BienBanLayMauCtReq {
    private Long id;
    private Long bbLayMauId;
    private String loaiDaiDien;
    private String daiDien;
}
