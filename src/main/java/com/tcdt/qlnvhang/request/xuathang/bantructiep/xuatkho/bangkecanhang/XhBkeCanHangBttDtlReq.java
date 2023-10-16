package com.tcdt.qlnvhang.request.xuathang.bantructiep.xuatkho.bangkecanhang;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class XhBkeCanHangBttDtlReq {
    private Long id;
    private Long idHdr;
    private String maCan;
    private String soBaoBi;
    private BigDecimal trongLuongCaBi;
}