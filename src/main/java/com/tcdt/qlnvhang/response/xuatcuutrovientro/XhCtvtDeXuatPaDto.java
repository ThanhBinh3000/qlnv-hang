package com.tcdt.qlnvhang.response.xuatcuutrovientro;


import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class XhCtvtDeXuatPaDto {
    private String tenDvi;
    private BigDecimal soLuong;
}
