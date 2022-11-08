package com.tcdt.qlnvhang.request.nhaphangtheoptt;

import lombok.Data;

import javax.persistence.Transient;
import java.math.BigDecimal;

@Data
public class HhQdPdKhMttSlddDtlReq {
    private Long id;
    private Long idSldd;
    private String maDvi;
    @Transient
    private String tenDvi;
    private String maDiemKho;
    private String diaDiemKho;
    private BigDecimal soLuongDxmtt;
    private BigDecimal donGiaVat;

}
