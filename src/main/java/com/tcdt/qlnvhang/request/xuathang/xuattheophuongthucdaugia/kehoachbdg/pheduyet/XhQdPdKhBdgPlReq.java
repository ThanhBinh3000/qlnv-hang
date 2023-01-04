package com.tcdt.qlnvhang.request.xuathang.xuattheophuongthucdaugia.kehoachbdg.pheduyet;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class XhQdPdKhBdgPlReq {
    @ApiModelProperty(notes = "Bắt buộc set đối với update")
    private Long id;
   private Long idHdr;
   private String maDvi;
    private String maDiemKho;
    private String diaDiemKho;
    private String maNhaKho;
    private String maNganKho;
    private String maLoKho;
    private String loaiVthh;
    private String cloaiVthh;
    private String maDviTsan;
    private BigDecimal duDau;
    private BigDecimal soLuong;
    private BigDecimal giaKhongVat;
    private BigDecimal giaKhoiDiem;
    private BigDecimal donGiaVat;
    private BigDecimal giaKhoiDiemDduyet;
    private BigDecimal tienDatTruoc;
    private BigDecimal tienDatTruocDduyet;
    private BigDecimal soLuongChiTieu;
    private BigDecimal soLuongKh;

    private BigDecimal tongSoLuong;

    private BigDecimal tongTienDatTruoc;

    private BigDecimal tongTienDatTruocDd;
    private String dviTinh;


    List<XhQdPdKhBdgPlDtlReq> children =new ArrayList<>();
}
