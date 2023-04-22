package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbKeHoachDcDtl;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ThKeHoachDieuChuyenTongCucDtlReq {
    private Long id;

    private Long hdrId;

    private Long keHoachDcHdrId;

    private Long keHoachDcDtlId;

    private String maCucDxuatDc;

    private String tenCucDxuatDc;

    private String maCucNhanDc;

    private String tenCucNhanDc;

    private String soDxuat;

    private Date ngayDuyetTc;

    private Long duToanKp;

    private String trichYeu;

    private List<DcnbKeHoachDcDtl> dcnbKeHoachDcDtls = new ArrayList<>();
}
