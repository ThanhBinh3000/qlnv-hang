package com.tcdt.qlnvhang.response.DieuChuyenNoiBo;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class BienBanNghiemThuLanDauDTO {
    private Long id;
    private String soBbNtBq;

    public BienBanNghiemThuLanDauDTO(Long id, String soBbNtBq ) {
        this.id = id;
        this.soBbNtBq = soBbNtBq;

    }
}
