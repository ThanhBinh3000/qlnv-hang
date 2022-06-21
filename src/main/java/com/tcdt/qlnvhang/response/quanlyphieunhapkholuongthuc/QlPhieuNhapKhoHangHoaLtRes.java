package com.tcdt.qlnvhang.response.quanlyphieunhapkholuongthuc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QlPhieuNhapKhoHangHoaLtRes {
    private Long id;
    private Integer stt;
    private String maVatTu;
    private String tenVatTu;
    private String donViTinh;
    private BigDecimal soLuongTrenCt;
    private BigDecimal soLuongThucNhap;
    private BigDecimal donGia;
    private BigDecimal thanhTien;
    private String maSo;

}
