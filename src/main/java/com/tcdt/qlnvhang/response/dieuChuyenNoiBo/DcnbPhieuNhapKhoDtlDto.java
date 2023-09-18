package com.tcdt.qlnvhang.response.dieuChuyenNoiBo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DcnbPhieuNhapKhoDtlDto {
    int stt;
    private String maSo;
    private String noiDung;
    private String dviTinh;
    private BigDecimal soLuongTheoChungTu;
    private BigDecimal soLuongThucNhap;
    private BigDecimal donGia;
    private BigDecimal thanhTien;

}
