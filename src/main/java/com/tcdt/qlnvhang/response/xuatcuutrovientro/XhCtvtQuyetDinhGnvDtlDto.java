package com.tcdt.qlnvhang.response.xuatcuutrovientro;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class XhCtvtQuyetDinhGnvDtlDto {
    private int stt;
    private String tenChiCuc;
    private BigDecimal soLuong;
    private String loaiVthh;
    private String cloaiVthh;
}
