package com.tcdt.qlnvhang.request.object.quanlybienbannhapdaykholuongthuc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QlBienBanNdkCtLtReq {
    private Long id;

    @NotNull(message = "Không được để trống")
    private BigDecimal soLuong;

    @NotNull(message = "Không được để trống")
    private BigDecimal donGia;

    @NotNull(message = "Không được để trống")
    private BigDecimal thanhTien;

    private String ghiChu;

    @NotNull(message = "Không được để trống")
    private Integer stt;
}
