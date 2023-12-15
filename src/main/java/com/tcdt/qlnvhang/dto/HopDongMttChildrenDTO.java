package com.tcdt.qlnvhang.dto;

import com.tcdt.qlnvhang.table.nhaphangtheoptt.*;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.hopdong.hopdongphuluc.HopDongMttHdr;
import lombok.Data;

import javax.persistence.Transient;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class HopDongMttChildrenDTO {
    private Long id;
    private Long idQdPdKq;
    private String maDvi;
    private String tenDvi;
    private String diaChi;
    private String tenGoiThau;
    private BigDecimal soLuongChiTieu;
    private BigDecimal soLuongKhDd;
    private BigDecimal donGia;
    private BigDecimal donGiaVat;
    private BigDecimal tongSoLuong;
    private BigDecimal tongThanhTien;
    private BigDecimal tongThanhTienVat;
    private BigDecimal soLuong;
    private BigDecimal soLuongHd;
    private String tongThanhTienStr;
    private Long idQdHdr;
    private String maDiemKho;
    private String maNhaKho;
    private String maNganKho;
    private String maLoKho;
    private BigDecimal soLuongGiao;
    private BigDecimal thanhTien;
    private String trangThai;
    private String tenTrangThai;
    private String tenDiemKho;
    private String tenNhaKho;
    private String tenNganKho;
    private String tenLoKho;
    private List<HopDongMttChildrenDtlDTO> children = new ArrayList<>();
}
