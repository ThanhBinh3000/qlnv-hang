package com.tcdt.qlnvhang.request.xuathang.xuatcuutrovientroxuatcap.xuatcap;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class XhCtvtQdXuatCapDtlReq {
    private String noiDung;
    private BigDecimal soLuongXuatCap;
    private String maDviCuc;
    private String tenCuc;
    private BigDecimal tonKhoCuc;
    private BigDecimal soLuongXuatCuc;
    private String maDviChiCuc;
    private String tenChiCuc;
    private BigDecimal tonKhoChiCuc;
    private BigDecimal tonKhoCloaiVthh;
    private String cloaiVthh;
    private String tenCloaiVthh;
    private BigDecimal soLuongXuatChiCuc;
    private String donViTinh;
    private BigDecimal donGiaKhongVat;
    private BigDecimal thanhTien;
}
