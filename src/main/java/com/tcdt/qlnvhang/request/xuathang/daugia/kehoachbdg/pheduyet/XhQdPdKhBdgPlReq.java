package com.tcdt.qlnvhang.request.xuathang.daugia.kehoachbdg.pheduyet;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhQdPdKhBdgPlReq {
    private Long id;
    private Long idDtl;
    private String maDvi;
    private BigDecimal slChiTieu;
    private BigDecimal tongSlKeHoachDd;
    private BigDecimal tongSlXuatBanDx;
    private BigDecimal tongTienDatTruocDx;
    private String donViTinh;
    private String diaChi;
    private BigDecimal soTienDuocDuyet;
    private BigDecimal soTienDtruocDduyet;
    List<XhQdPdKhBdgPlDtlReq> children = new ArrayList<>();
}