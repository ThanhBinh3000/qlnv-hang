package com.tcdt.qlnvhang.request.xuathang.bantructiep.hopdong;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhHopDongBttDtlReq {
    private Long id;
    private Long idHdr;
    private BigDecimal soLuongChiCuc;
    private BigDecimal soLuongBanTrucTiepHd;
    private String maDvi;
    private String diaChi;

    //phu luc
    private Long idHdDtl;
    private List<XhHopDongBttDviReq> children = new ArrayList<>();
}
