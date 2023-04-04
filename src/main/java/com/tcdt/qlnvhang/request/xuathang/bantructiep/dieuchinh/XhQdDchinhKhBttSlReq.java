package com.tcdt.qlnvhang.request.xuathang.bantructiep.dieuchinh;

import lombok.Data;

import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhQdDchinhKhBttSlReq {
    private Long id;

    private Long idDtl;

    private BigDecimal soLuongChiCuc;

    private String maDvi;

    private String diaChi;

    private BigDecimal donGiaVat;

    private BigDecimal soLuongChiTieu;

    private BigDecimal soLuongKh;

    @Transient
    private List<XhQdDchinhKhBttSlDtlReq> children= new ArrayList<>();

}
