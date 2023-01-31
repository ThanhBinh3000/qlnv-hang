package com.tcdt.qlnvhang.request.xuathang.daugia.kehoachbdg.dexuat;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhDxKhBanDauGiaDtlReq {
    private Long id;
    private Long idHdr;
    private String maDvi;
    private BigDecimal soLuong;
    private String diaChi;
    private List<XhDxKhBanDauGiaPhanLoReq> children = new ArrayList<>();
}
