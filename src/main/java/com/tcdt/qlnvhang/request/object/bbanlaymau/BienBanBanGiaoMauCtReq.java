package com.tcdt.qlnvhang.request.object.bbanlaymau;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class BienBanBanGiaoMauCtReq {
    private Long id;
    private Long bbLayMauId;
    private String loaiDaiDien;
    private String daiDien;
}
