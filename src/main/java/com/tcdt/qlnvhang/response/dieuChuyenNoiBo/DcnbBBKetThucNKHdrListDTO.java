package com.tcdt.qlnvhang.response.dieuChuyenNoiBo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DcnbBBKetThucNKHdrListDTO {
    private Long id;
    private Long qdDcCucId;
    private String soBBKtNH;
}
