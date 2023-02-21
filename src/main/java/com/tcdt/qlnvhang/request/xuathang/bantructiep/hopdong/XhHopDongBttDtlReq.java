package com.tcdt.qlnvhang.request.xuathang.bantructiep.hopdong;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class XhHopDongBttDtlReq {
    private Long id;

    private Long idHdr;

    private String maDvi;

    private BigDecimal soLuong;

    private BigDecimal donGiaVat;


}
