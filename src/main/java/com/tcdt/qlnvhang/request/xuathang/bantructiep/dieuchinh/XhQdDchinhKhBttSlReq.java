package com.tcdt.qlnvhang.request.xuathang.bantructiep.dieuchinh;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhQdDchinhKhBttSlReq {
    private Long id;
    private Long idDtl;
    private String maDvi;
    private BigDecimal soLuongChiCuc;
    private String diaChi;
    private BigDecimal soLuongChiTieu;
    private BigDecimal soLuongKhDaDuyet;
    private String donViTinh;
    private List<XhQdDchinhKhBttSlDtlReq> children = new ArrayList<>();
}
