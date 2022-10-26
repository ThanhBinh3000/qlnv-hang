package com.tcdt.qlnvhang.request.nhaphangtheoptt;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class HhDcQdPduyetKhmttSlddReq {
    private Long id;
    private Long idDxKhmtt;
    private Long idDcKhmtt;
    private String maDvi;
    private String tenDvi;
    private String maDiemKho;
    private String diaDiemKho;
    private BigDecimal soLuongCtieu;
    private BigDecimal soLuongKhDd;
    private BigDecimal soLuongDxmtt;
    private BigDecimal donGiaVat;
    private BigDecimal thanhTien;
}
