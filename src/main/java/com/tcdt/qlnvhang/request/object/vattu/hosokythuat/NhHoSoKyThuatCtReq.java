package com.tcdt.qlnvhang.request.object.vattu.hosokythuat;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NhHoSoKyThuatCtReq {
    private Long id;
    private Long hoSoKyThuatId;
    private String loaiDaiDien;
    private String daiDien;
    private Integer stt;
}
