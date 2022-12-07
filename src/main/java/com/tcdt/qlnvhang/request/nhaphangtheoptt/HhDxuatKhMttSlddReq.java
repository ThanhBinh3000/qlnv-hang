package com.tcdt.qlnvhang.request.nhaphangtheoptt;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class HhDxuatKhMttSlddReq {
    @ApiModelProperty(notes = "Bắt buộc set đối với update")
    private Long id;

    private Long idHdr;

    private String maDvi;

    private String maDiemKho;

    private String diaDiemNhap;

    private BigDecimal soLuongChiTieu;

    private BigDecimal soLuongKhDd;

    private BigDecimal soLuong;

    private BigDecimal donGia;

    private BigDecimal thanhTien;

    private String loaiVthh;

    private String cloaiVthh;

    private BigDecimal donGiaVat;

    private BigDecimal thanhTienVat;

    private BigDecimal tongSoLuong;

    private BigDecimal tongDonGia;

    private BigDecimal tongThanhTien;



    List<HhDxuatKhMttSlddDtlReq> children = new ArrayList<>();
}
