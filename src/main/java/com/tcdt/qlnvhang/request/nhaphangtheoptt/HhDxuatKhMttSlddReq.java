package com.tcdt.qlnvhang.request.nhaphangtheoptt;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class HhDxuatKhMttSlddReq {
    @ApiModelProperty(notes = "Bắt buộc set đối với update")
    private Long id;

    private String tenGoiThau;

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

    private String dvt;
    private String cloaiVthh;
    private String tenCloaiVthh;
    private BigDecimal donGiaTdVat;

    List<HhDxuatKhMttSlddDtlReq> children = new ArrayList<>();
}
