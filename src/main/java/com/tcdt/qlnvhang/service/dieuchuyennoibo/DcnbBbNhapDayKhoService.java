package com.tcdt.qlnvhang.service.dieuchuyennoibo;

import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbBbNhapDayKhoHdrReq;
import com.tcdt.qlnvhang.service.BaseService;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBbNhapDayKhoHdr;

import java.util.List;

public interface DcnbBbNhapDayKhoService extends BaseService<DcnbBbNhapDayKhoHdr, DcnbBbNhapDayKhoHdrReq,Long> {

    List<DcnbBbNhapDayKhoHdr> searchList(CustomUserDetails currentUser, DcnbBbNhapDayKhoHdrReq param);

    ReportTemplateResponse preview(DcnbBbNhapDayKhoHdrReq objReq) throws Exception;
    public DcnbBbNhapDayKhoHdr approve(StatusReq req) throws Exception;
}