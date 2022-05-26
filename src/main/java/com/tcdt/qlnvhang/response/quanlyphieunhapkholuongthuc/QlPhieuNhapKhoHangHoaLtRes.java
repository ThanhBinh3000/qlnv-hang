package com.tcdt.qlnvhang.response.quanlyphieunhapkholuongthuc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QlPhieuNhapKhoHangHoaLtRes {
    private Long id;
    private Integer stt;
    private Long vatTuId;
    private String tenVatTu;
    private String donViTinh;
    private Long soLuongTrenCt;
    private Long soLuongThuc;
    private Long donGia;
    private Long thanhTien;
}
