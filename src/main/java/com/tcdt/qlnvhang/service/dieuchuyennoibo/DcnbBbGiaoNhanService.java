package com.tcdt.qlnvhang.service.dieuchuyennoibo;

import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbBbGiaoNhanHdrReq;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBbGiaoNhanHdrDTO;
import com.tcdt.qlnvhang.service.BaseService;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBbGiaoNhanHdr;
import org.springframework.data.domain.Page;

public interface DcnbBbGiaoNhanService extends BaseService<DcnbBbGiaoNhanHdr, DcnbBbGiaoNhanHdrReq,Long> {
    public Page<DcnbBbGiaoNhanHdrDTO> searchPage(CustomUserDetails currentUser, DcnbBbGiaoNhanHdrReq req) throws Exception;

    ReportTemplateResponse preview(DcnbBbGiaoNhanHdrReq objReq) throws Exception;
    public DcnbBbGiaoNhanHdr approve(StatusReq req) throws Exception;
}