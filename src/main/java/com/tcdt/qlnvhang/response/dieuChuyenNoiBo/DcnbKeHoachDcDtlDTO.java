package com.tcdt.qlnvhang.response.dieuChuyenNoiBo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DcnbKeHoachDcDtlDTO {
    private String maLoKho;
    private String tenLoKho;

    public DcnbKeHoachDcDtlDTO(String maLoKho, String tenLoKho) {
        this.maLoKho = maLoKho;
        this.tenLoKho = tenLoKho;
    }
}
