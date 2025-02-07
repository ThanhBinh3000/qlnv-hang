package com.tcdt.qlnvhang.request.nhaphangtheoptt;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class HhDcDiaDiemGiaoNhanHangReq {
    private Long id;
    private Long idHdPluc;
    private String maDvi;
    private String tenDvi;
    private String diaChi;
    private BigDecimal soLuong;
    private BigDecimal donGiaVat;
    private BigDecimal thanhTien;

}
