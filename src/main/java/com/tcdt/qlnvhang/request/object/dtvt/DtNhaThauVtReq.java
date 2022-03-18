package com.tcdt.qlnvhang.request.object.dtvt;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class DtNhaThauVtReq {
    @NotNull(message = "Không được để trống")
    private Integer stt;

    @NotNull(message = "Không được để trống")
    @Size(max = 500, message = "Tên không được vượt quá 500 ký tự")
    private String ten;

    @NotNull(message = "Không được để trống")
    @Size(max = 500, message = "Địa chỉ không được vượt quá 500 ký tự")
    private String diaChi;

    @NotNull(message = "Không được để trống")
    @Size(max = 20, message = "Số điện thoại không được vượt quá 20 ký tự")
    private String soDienThoai;

    private Integer diem;

    private Integer xepHang;

    @NotNull(message = "Không được để trống")
    private BigDecimal donGiaDuThau;

    @NotNull(message = "Không được để trống")
    private BigDecimal giaDuThau;

    private String lyDo;
}
