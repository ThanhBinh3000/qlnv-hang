package com.tcdt.qlnvhang.request.xuathang.quyetdinhgiaonhiemvuxuat;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class XhQdGiaoNvXhDdiemReq {
    private Long id;
    private Long idDtl;
    private String maDiemKho;
    private String diaDiemKho;
    private String maNhaKho;
    private String maNganKho;
    private String maLoKho;
    private String maDviTsan;
    private BigDecimal tonKho;
    private BigDecimal soLuong;
    private BigDecimal donGia;
    private BigDecimal thanhTien;
    private String donViTinh;
    private Long idBbTinhKho;
    private String soBbTinhKho;
    private LocalDate ngayLapBbTinhKho;
    private Integer namNhap;
}