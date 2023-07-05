package com.tcdt.qlnvhang.request.suachua;

import com.tcdt.qlnvhang.request.BaseRequest;
import com.tcdt.qlnvhang.request.object.FileDinhKemReq;
import com.tcdt.qlnvhang.table.xuathang.suachuahang.ScDanhSachHdr;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class ScTrinhThamDinhDtlReq {

    private Long id;

    private BigDecimal donGiaDk;

    private String ketQua;


}
