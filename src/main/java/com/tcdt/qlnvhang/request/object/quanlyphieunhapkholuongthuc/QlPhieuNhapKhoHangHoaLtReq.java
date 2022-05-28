package com.tcdt.qlnvhang.request.object.quanlyphieunhapkholuongthuc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

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
    private Long soLuongTrenCt;

    @NotNull(message = "Không được để trống")
    private Long soLuongThuc;

    @NotNull(message = "Không được để trống")
    private Long donGia;

    @NotNull(message = "Không được để trống")
    private Long thanhTien;

    private String maVatTu;
}
