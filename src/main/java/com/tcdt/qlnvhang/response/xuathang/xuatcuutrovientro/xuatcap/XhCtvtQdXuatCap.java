package com.tcdt.qlnvhang.response.xuathang.xuatcuutrovientro.xuatcap;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Data
public class XhCtvtQdXuatCap {
    private Long id;
    private String soQdXc;
    private String trichYeu;
    private String trangThai;
    private Date ngayHieuLucQdXc;
    private Date ngayHieuLucQdChuyenXc;
    private String soQdChuyenXc;
    private BigDecimal soLuongThocXc;
    private BigDecimal soLuongGaoXc;
    private String tenTrangThai;
}
