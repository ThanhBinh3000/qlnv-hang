package com.tcdt.qlnvhang.request.nhaphangtheoptt.hopdong.hopdongphuluc;

import lombok.Data;

import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class DiaDiemGiaoNhanMttReq {
    private Long id;

    private Long idHdr;

    private String maDvi;

    private String diaChi;

    private BigDecimal soLuongChiTieu;

    private BigDecimal soLuongKhDd;

    private BigDecimal donGia;

    private BigDecimal donGiaVat;

    private BigDecimal tongSoLuong;

    private BigDecimal tongThanhTien;

    private BigDecimal tongThanhTienVat;

    private BigDecimal soLuong;

    //    phu luc
    private Long idHdDtl;

    @Transient
    private List<DiaDiemGiaoNhanMttCtReq> children = new ArrayList<>();

}
