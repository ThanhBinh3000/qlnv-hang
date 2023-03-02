package com.tcdt.qlnvhang.request.xuathang.bantructiep.hopdong;

import lombok.Data;

import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhHopDongBttDtlReq {
    private Long id;

    private Long idHdr;

    private String maDvi;

    private BigDecimal soLuong;

    private BigDecimal donGiaVat;

    private String diaChi;


    //    phu luc
    private Long idHdDtl;

    @Transient
    private List<XhHopDongBttDviReq> children = new ArrayList<>();


}
