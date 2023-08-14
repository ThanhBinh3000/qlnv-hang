package com.tcdt.qlnvhang.request.xuathang.daugia.quyetdinhdieuchinh;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhQdDchinhKhBdgPlReq {
    private Long id;
    private Long idDcDtl;
    private String maDvi;
    private BigDecimal slChiTieu;
    private BigDecimal tongSlKeHoachDd;
    private BigDecimal tongSlXuatBanDx;
    private BigDecimal tongTienDatTruocDx;
    private String donViTinh;
    private String diaChi;
    private List<XhQdDchinhKhBdgPlDtlReq> children = new ArrayList<>();
}
