package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbKeHoachDcDtl;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class ThKeHoachDieuChuyenNoiBoCucDtlReq {
    private Long id;

    private Long hdrId;

    private String maChiCucDxuat;

    private String tenChiCucDxuat;

    private Long dcKeHoachDcHdrId;

    private Long dcKeHoachDcDtlId;

    private Boolean daXdinhDiemNhap;

    private List<DcnbKeHoachDcDtl> dcnbKeHoachDcDtls = new ArrayList<>();
}
