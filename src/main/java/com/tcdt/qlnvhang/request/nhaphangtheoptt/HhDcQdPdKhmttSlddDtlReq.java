package com.tcdt.qlnvhang.request.nhaphangtheoptt;

import lombok.Data;

import javax.persistence.Transient;
import java.math.BigDecimal;

@Data
public class HhDcQdPdKhmttSlddDtlReq {
    private Long id;
    private Long idDiaDiem;
    private String maDvi;
    private String maDiemKho;
    private String diaDiemNhap;
    private BigDecimal soLuong;
    private BigDecimal donGiaVat;
    private BigDecimal donGia;
    private BigDecimal thanhTien;
}
