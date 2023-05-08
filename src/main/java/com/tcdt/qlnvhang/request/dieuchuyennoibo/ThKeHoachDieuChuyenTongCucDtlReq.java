package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbKeHoachDcDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.THKeHoachDieuChuyenCucKhacCucDtl;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ThKeHoachDieuChuyenTongCucDtlReq {
    private Long id;

    private Long hdrId;

    private Long thKhDcHdrId;

    private Long thKhDcDtlId;

    private Long keHoachDcHdrId;

    private Long keHoachDcDtlId;

    private Long tongDuToanKp;

    private String maCucNhan;

    private String tenCucNhan;

    private String tenCucDxuat;

    private String maCucDxuat;

    private String soDxuat;

    private LocalDate ngayTrinhDuyetTc;

    private String trichYeu;

    List<THKeHoachDieuChuyenCucKhacCucDtl> thKeHoachDieuChuyenCucKhacCucDtls = new ArrayList<>();

}
