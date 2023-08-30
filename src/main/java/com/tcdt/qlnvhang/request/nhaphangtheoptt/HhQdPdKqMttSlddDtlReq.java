package com.tcdt.qlnvhang.request.nhaphangtheoptt;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class HhQdPdKqMttSlddDtlReq {
    //	@NotNull(message = "Không được để trống")
    private Long id;

    private Long idDiaDiem;

    private String maDiemKho;

    private String diaDiemNhap;

    private BigDecimal soLuong;
    private BigDecimal soLuongHd;

    private BigDecimal donGia;

    private BigDecimal donGiaVat;

}
