package com.tcdt.qlnvhang.request.nhaphangtheoptt;

import lombok.Data;

import javax.persistence.Transient;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
public class HhQdPdKhMttSlddDtlReq {
    //	@NotNull(message = "Không được để trống")
    Long id;

    private String maDvi;

    private String maDiemKho;

    private String diaDiemNhap;

    private BigDecimal donGiaVat;


    private BigDecimal soLuong;

    private BigDecimal donGia;

    private BigDecimal thanhTien;

    private String tenGoiThau;

    private BigDecimal soLuongChiTieu;

    private BigDecimal soLuongKhDd;

    private BigDecimal tongThanhTienVat;

    private BigDecimal tongSoLuong;

    private BigDecimal tongThanhTien;

    private BigDecimal tongDonGia;

    private BigDecimal thanhTienVat;

    private String loaiVthh;

    private String cloaiVthh;

}
