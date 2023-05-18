package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import lombok.Data;

import javax.persistence.Column;
import java.math.BigDecimal;

@Data
public class DcnbBangKeCanHangDtlReq {

    private Long id;

    private Long hdrId;

    private String maCan;

    private BigDecimal soBaoBi;

    private BigDecimal trongLuongCaBaoBi;
}
