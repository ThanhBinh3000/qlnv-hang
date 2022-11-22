package com.tcdt.qlnvhang.request.xuathang.xuattheophuongthucdaugia;

import lombok.Data;

import javax.persistence.Transient;
import java.math.BigDecimal;

@Data
public class XhDxKhBanDauGiaDtlReq {
    private Long id;
    private String maDvi;
    private String tenDvi;
    private String maDiemKho;
    private String diaDiemKho;
    private String maNhaKho;
    private String maNganKho;
    private String maLoKho;
    private String loaiVthh;
    @Transient
    private String tenLoaiVthh;
    private String cloaiVthh;
    @Transient
    private String tenCloaiVthh;
    private String maDviTsan;
    private BigDecimal soLuong;
    private BigDecimal  soLuongChiTieu;
    private BigDecimal soLuongKh;
    private String dviTinh;
    private BigDecimal giaKhongVat;
    private BigDecimal giaKhoiDiem;
    private BigDecimal tienDatTruoc;
    private BigDecimal duDau;
    private Long idPhanLo;
}
