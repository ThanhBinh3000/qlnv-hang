package com.tcdt.qlnvhang.request.xuathang.daugia.hopdong;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhHopDongDtlReq {
    private Long id;
    private Long idHdr;
    private String maDvi;
    private String diaChi;
    private BigDecimal soLuongXuatBan;
    private BigDecimal thanhTienXuatBan;
    //    phu luc
    private Boolean tyPe; // tyPe : 0 hợp đồng hdr ; 1 phụ lục hdr
    private List<XhDdiemNhapKhoReq> children = new ArrayList<>();
}
