package com.tcdt.qlnvhang.request.nhaphangtheoptt.hopdong.hopdongphuluc;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcdt.qlnvhang.util.Contains;
import lombok.Data;

import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class DiaDiemGiaoNhanMttReq {
    private Long id;

    private Long idHdr;

    private String maDvi;

    private String diaChi;
    private String soPhuLuc;

    private BigDecimal soLuongChiTieu;

    private BigDecimal soLuongKhDd;

    private BigDecimal donGia;

    private BigDecimal donGiaVat;

    private BigDecimal tongSoLuong;

    private BigDecimal tongThanhTien;

    private BigDecimal tongThanhTienVat;

    private BigDecimal soLuong;
    private BigDecimal soLuongHd;
    private Date ngayPduyet;
    private Date ngayHlucPhuLuc;
    private Date thoiGianDuKienSauDc;
    private String tgianThienHdSauDc;
    private String noiDungPl;
    private String noiDungDc;
    private String ghiChu;
    private String trangThaiPhuLuc;

    //    phu luc
    private Long idHdDtl;

    @Transient
    private List<DiaDiemGiaoNhanMttCtReq> children = new ArrayList<>();

}
