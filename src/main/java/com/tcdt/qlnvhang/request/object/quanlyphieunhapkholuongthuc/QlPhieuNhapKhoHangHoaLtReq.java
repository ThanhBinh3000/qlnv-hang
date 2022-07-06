package com.tcdt.qlnvhang.request.object.quanlyphieunhapkholuongthuc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QlPhieuNhapKhoHangHoaLtReq {
    private Long id;

    @NotNull(message = "Không được để trống")
    private Integer stt;

    @NotNull(message = "Không được để trống")
    private String donViTinh;

    @NotNull(message = "Không được để trống")
    private BigDecimal soLuongTrenCt;

    @NotNull(message = "Không được để trống")
    private BigDecimal soLuongThuc;

    @NotNull(message = "Không được để trống")
    private BigDecimal donGia;

    @NotNull(message = "Không được để trống")
    private BigDecimal thanhTien;

    private BigDecimal soChungTu;

    private BigDecimal soThucNhap;

    private String maVatTu;
}
