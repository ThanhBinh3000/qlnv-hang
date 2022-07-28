package com.tcdt.qlnvhang.request.bandaugia.quyetdinhpheduyetketquabandaugia;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class BhQdPheDuyetKqbdgCtReq {
    private Long id;
    private BigDecimal donGiaTrungDauGia;
    private String trungDauGia;
}
