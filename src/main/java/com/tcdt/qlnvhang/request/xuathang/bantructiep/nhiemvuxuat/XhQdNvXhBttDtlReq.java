package com.tcdt.qlnvhang.request.xuathang.bantructiep.nhiemvuxuat;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhQdNvXhBttDtlReq {
    private Long id;
    private Long idHdr;
    private String maDvi;
    private BigDecimal soLuong;
    private List<XhQdNvXhBttDviReq> children = new ArrayList<>();
}