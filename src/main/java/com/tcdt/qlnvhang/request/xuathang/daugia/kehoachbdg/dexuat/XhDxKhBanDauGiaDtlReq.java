package com.tcdt.qlnvhang.request.xuathang.daugia.kehoachbdg.dexuat;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhDxKhBanDauGiaDtlReq {
    private Long id;

    private Long idHdr;

    private BigDecimal soLuongChiCuc;

    private BigDecimal tienDtruocDxChiCuc;

    private BigDecimal tienDtruocDdChiCuc;

    private String maDvi;

    private String diaChi;

    private String slChiTieu;

    private String slKeHoachDd;

    private List<XhDxKhBanDauGiaPhanLoReq> children = new ArrayList<>();
}
