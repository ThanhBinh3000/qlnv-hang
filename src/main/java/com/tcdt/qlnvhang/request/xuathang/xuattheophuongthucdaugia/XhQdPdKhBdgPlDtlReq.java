package com.tcdt.qlnvhang.request.xuathang.xuattheophuongthucdaugia;

import lombok.Data;

import javax.persistence.Transient;
import java.math.BigDecimal;

@Data
public class XhQdPdKhBdgPlDtlReq {
    private Long id;
    private Long idPhanLo;
    private String maDvi;
    @Transient
    private String tenDvi;
    private String maDiemKho;
    @Transient
    private String tenDiemKho;
    private String maNhaKho;
    @Transient
    private String tenNhakho;
    private String maNganKho;
    @Transient
    private String tenNganKho;
    private String maLoKho;
    @Transient
    private String tenLoKho;
    private String loaiVthh;
    @Transient
    private String tenLoaiVthh;
    private String cloaiVthh;
    @Transient
    private String tenCloaiVthh;
    private String maDviTsan;
    private BigDecimal duDau;
    private BigDecimal soLuong;
    private BigDecimal giaKhongVat;
    private BigDecimal giaKhoiDiem;
    private BigDecimal donGiaVat;
    private BigDecimal giaKhoiDiemDduyet;
    private BigDecimal tienDatTruoc;


    private BigDecimal tienDatTruocDduyet;
    private BigDecimal  soLuongChiTieu;
    private BigDecimal soLuongKh;
    private String dviTinh;
    private BigDecimal tongSoLuong;
    private BigDecimal tongTienDatTruoc;
    private BigDecimal tongTienDatTruocDd;

}
