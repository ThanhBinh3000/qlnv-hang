package com.tcdt.qlnvhang.request.object.vattu.phieunhapkhotamgui;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class NhPhieuNhapKhoTamGuiCtReq {
    private Long id;
    private Long phieuNkTgId;
    private String maSo;
    private String donViTinh;
    private BigDecimal soLuongChungTu;
    private BigDecimal soLuongThucNhap;
    private BigDecimal donGia;
    private String moTaHangHoa;
}
