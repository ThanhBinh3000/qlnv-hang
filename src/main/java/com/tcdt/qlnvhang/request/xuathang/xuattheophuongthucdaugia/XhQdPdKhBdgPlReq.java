package com.tcdt.qlnvhang.request.xuathang.xuattheophuongthucdaugia;

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


   private String maNhaKho;
    private String maDiemKho;
    private String maNganKho;
    private String maLoKho;
    private String maDviTsan;
    private BigDecimal soLuong;
    private String DviTinh;
    private BigDecimal giaKhongVat;
    private BigDecimal giaKhoiDiem;
    private BigDecimal tienDatTruoc;
    private BigDecimal soLuongChiTieu;
    private BigDecimal soLuongKh;

    List<XhQdPdKhBdgPlDtlReq> children =new ArrayList<>();
}
