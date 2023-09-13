package com.tcdt.qlnvhang.service.dieuchuyennoibo;

import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbBangKeNhapVTReq;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBangKeNhapVTHdrDTO;
import com.tcdt.qlnvhang.service.BaseService;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBangKeNhapVTHdr;
import org.springframework.data.domain.Page;

public interface DcnbBangKeNhapVTService extends BaseService<DcnbBangKeNhapVTHdr, DcnbBangKeNhapVTReq,Long> {
    public Page<DcnbBangKeNhapVTHdrDTO> searchPage(CustomUserDetails currentUser, DcnbBangKeNhapVTReq req) throws Exception;
    public void approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception;
    ReportTemplateResponse preview(DcnbBangKeNhapVTReq objReq) throws Exception;
}
