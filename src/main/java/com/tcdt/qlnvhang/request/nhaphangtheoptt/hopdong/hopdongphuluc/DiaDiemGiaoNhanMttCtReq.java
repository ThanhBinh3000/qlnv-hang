package com.tcdt.qlnvhang.request.nhaphangtheoptt.hopdong.hopdongphuluc;

import lombok.Data;

import javax.persistence.Transient;
import java.math.BigDecimal;

@Data
public class DiaDiemGiaoNhanMttCtReq {
    private Long id;

    private String maDiemKho;

    private String diaDiemNhap;

    private Long idDiaDiem;

    private BigDecimal soLuong;
    private BigDecimal soLuongHd;

    private BigDecimal donGia;

    private BigDecimal donGiaVat;
}
