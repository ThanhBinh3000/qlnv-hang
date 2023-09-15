package com.tcdt.qlnvhang.request.xuathang.daugia.hopdong;

import lombok.Data;

import javax.persistence.Transient;
import java.math.BigDecimal;

@Data
public class XhDdiemNhapKhoReq {
    private Long id;
    private Long idDtl;
    private String maDiemKho;
    private String maNhaKho;
    private String maNganKho;
    private String maLoKho;
    private String maDviTsan;
    private BigDecimal tonKho;
    private BigDecimal soLuong;
    private BigDecimal donGia;
    private BigDecimal donGiaTraGia;
    private BigDecimal thanhTien;
    private String donViTinh;
    private String diaDiemKho;
}
