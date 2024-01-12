package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class DcnbQuyetDinhDcTcDtlPreviewReq {
    private String tenCuc;
    private String soToTrinh;
    private BigDecimal tongDuToanKinhPhi;
    private List<DcnbQuyetDinhDcTcRowPreviewReq> listData;
}
