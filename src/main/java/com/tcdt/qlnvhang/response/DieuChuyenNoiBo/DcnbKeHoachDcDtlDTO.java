package com.tcdt.qlnvhang.response.DieuChuyenNoiBo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

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
