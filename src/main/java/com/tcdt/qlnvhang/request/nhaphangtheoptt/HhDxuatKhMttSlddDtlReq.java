package com.tcdt.qlnvhang.request.nhaphangtheoptt;

import lombok.Data;

import javax.persistence.Transient;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
public class HhDxuatKhMttSlddDtlReq {
    //	@NotNull(message = "Không được để trống")
   private Long id;

    private Integer soLuong;

    //	@NotNull(message = "Không được để trống")
    @Size(max = 250, message = "Tên đơn vị không được vượt quá 250 ký tự")
    private String tenDvi;

    //	@NotNull(message = "Không được để trống")
    @Size(max = 50, message = "Mã đơn vị không được vượt quá 50 ký tự")
    private String maDvi;

    private String maDiemKho;

    private Integer donGia;

    private String diaDiemNhap;
}
