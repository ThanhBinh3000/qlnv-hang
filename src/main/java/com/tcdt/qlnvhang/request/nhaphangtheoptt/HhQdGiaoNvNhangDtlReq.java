package com.tcdt.qlnvhang.request.nhaphangtheoptt;

import lombok.Data;

import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class HhQdGiaoNvNhangDtlReq {
    private Long id;
    private Long idQdHdr;
    private String maDvi;
    private String maDiemKho;
    private String maNhaKho;
    private String maNganKho;
    private String maLoKho;
    private BigDecimal soLuong;
    private BigDecimal soLuongGiao;
    private BigDecimal donGiaVat;
    private BigDecimal thanhTien;
    private String trangThai;

//    private List<HhQdGiaoNvNhDdiemReq> hhQdGiaoNvNhDdiemList = new ArrayList<>();
}
