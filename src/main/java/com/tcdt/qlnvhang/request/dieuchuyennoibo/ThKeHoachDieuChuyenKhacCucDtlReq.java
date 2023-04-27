package com.tcdt.qlnvhang.request.dieuchuyennoibo;

import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbKeHoachDcDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbKeHoachDcHdr;
import lombok.Data;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class ThKeHoachDieuChuyenKhacCucDtlReq {

    private Long id;

    private String maCucNhan;

    private String tenCucNhan;

    private String soDxuat;

    private LocalDate ngayDxuat;

    private LocalDate ngayGduyetTc;

    private BigDecimal tongDuToanKp;

    private String trichYeu;

    private Long hdrId;

    private String maChiCucDeXuat;

    private String tenChiCucDxuat;

    private Long dcnbKeHoachDcHdrId;

    private List<DcnbKeHoachDcHdr> dcnbKeHoachDcHdrs = new ArrayList<>();

    private List<DcnbKeHoachDcDtl> dcnbKeHoachDcDtls = new ArrayList<>();
}
