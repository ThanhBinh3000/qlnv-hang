package com.tcdt.qlnvhang.request.object.bbanlaymau;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class BienBanLayMauCtReq {
    private String loaiDaiDien;
    private String daiDien;
    private String tenLoaiDaiDien;
}
