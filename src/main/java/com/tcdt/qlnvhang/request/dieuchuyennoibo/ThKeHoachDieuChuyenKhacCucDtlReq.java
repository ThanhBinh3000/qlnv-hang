package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import lombok.Data;


import java.time.LocalDate;

@Data
public class ThKeHoachDieuChuyenKhacCucDtlReq {

    private Long id;

    private String maCucNhan;

    private String tenCucNhan;

    private String soDxuat;

    private LocalDate ngayDxuat;

    private LocalDate ngayGduyetTc;

    private Long tongDuToanKp;

    private String trichYeu;

    private Long hdrId;

    private Long dcnbKeHoachDcHdrId;
}
