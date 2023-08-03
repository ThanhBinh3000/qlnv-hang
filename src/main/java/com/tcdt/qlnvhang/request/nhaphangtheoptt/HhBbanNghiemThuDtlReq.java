package com.tcdt.qlnvhang.request.nhaphangtheoptt;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class HhBbanNghiemThuDtlReq {
    private Long id;
    private Long idHdr;
    private String type;
    private String noiDung;
    private String danhMuc;
    private String nhomHang;
    private String donViTinh;
    private String matHang;
    private String tenMatHang;
    private String donViTinhMh;
    private BigDecimal tongGiaTri;
    private BigDecimal soLuongTrongNam;
    private BigDecimal donGia;
    private BigDecimal thanhTienTrongNam;
    private BigDecimal soLuongNamTruoc;
    private BigDecimal thanhTienNamTruoc;
    private Boolean isParent;
    private String idParent;
}
