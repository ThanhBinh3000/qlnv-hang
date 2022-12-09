package com.tcdt.qlnvhang.request.nhaphangtheoptt;

import lombok.Data;

import javax.persistence.Transient;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
public class HhDxuatKhMttSlddDtlReq {

   private Long id;

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

   private BigDecimal thanhTienVat;

   private BigDecimal tongDonGia;

   private String loaiVthh;

   private String cloaiVthh;

}
