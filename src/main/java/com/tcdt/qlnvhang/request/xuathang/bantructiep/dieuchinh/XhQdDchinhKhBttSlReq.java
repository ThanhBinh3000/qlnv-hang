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

    private String maDvi;

    private BigDecimal soLuong;

    @Transient
    private List<XhQdDchinhKhBttSlDtlReq> children= new ArrayList<>();

}
