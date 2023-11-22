package com.tcdt.qlnvhang.request.xuathang.bantructiep.kehoach.pheduyet;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhQdPdKhBttDviReq {
    private Long id;
    private Long idDtl;
    private String maDvi;
    private BigDecimal soLuongChiCuc;
    private String diaChi;
    private BigDecimal soLuongChiTieu;
    private BigDecimal soLuongKhDaDuyet;
    private String donViTinh;
    private Long idQdKqHdr;
    private Boolean isKetQua;
    private BigDecimal thanhTien;
    private BigDecimal tienDuocDuyet;
    List<XhQdPdKhBttDviDtlReq> children = new ArrayList<>();
}