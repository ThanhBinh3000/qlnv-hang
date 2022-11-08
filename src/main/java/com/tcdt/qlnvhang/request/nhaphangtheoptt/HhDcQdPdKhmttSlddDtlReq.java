package com.tcdt.qlnvhang.request.nhaphangtheoptt;

import lombok.Data;

import javax.persistence.Transient;
import java.math.BigDecimal;

@Data
public class HhDcQdPdKhmttSlddDtlReq {
    private Long id;
    private Long idSldd;
    private String maDvi;
    @Transient
    private String tenDvi;
    private String maDiemKho;
    @Transient
    private String tenDiemKho;
    private String diaDiemKho;
    private BigDecimal soLuongDxmtt;
    private BigDecimal donGiaVat;
}
