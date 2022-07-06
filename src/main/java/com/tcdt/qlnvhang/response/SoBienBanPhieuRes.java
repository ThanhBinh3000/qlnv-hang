package com.tcdt.qlnvhang.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SoBienBanPhieuRes {
    private Integer so;
    private Integer nam;

    public SoBienBanPhieuRes(Integer so, Integer nam) {
        this.so = so;
        this.nam = nam;
    }
}
