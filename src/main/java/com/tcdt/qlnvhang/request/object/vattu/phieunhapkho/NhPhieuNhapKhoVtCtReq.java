package com.tcdt.qlnvhang.request.object.vattu.phieunhapkho;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class NhPhieuNhapKhoVtCtReq {
    private Long id;
    private Long phieuNkTgId;
    private String maSo;
    private String donViTinh;
    private BigDecimal soLuongChungTu;
    private BigDecimal soLuongThucNhap;
    private BigDecimal donGia;
    private BigDecimal thanhTien;
    private String vthh;
    private Integer stt;
}
