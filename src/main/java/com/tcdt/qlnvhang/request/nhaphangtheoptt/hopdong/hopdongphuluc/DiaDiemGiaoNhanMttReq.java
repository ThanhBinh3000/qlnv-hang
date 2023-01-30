package com.tcdt.qlnvhang.request.nhaphangtheoptt.hopdong.hopdongphuluc;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
public class DiaDiemGiaoNhanMttReq {
    private Long id;
    private Long idHdr;
    private String maDvi;
    private BigDecimal soLuong;
    private BigDecimal donGiaVat;
    private BigDecimal thanhTien;
    private Integer stt;
}
