package com.tcdt.qlnvhang.service.dieuchuyennoibo;

import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbBangKeXuatVTReq;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBangKeXuatVTHdrDTO;
import com.tcdt.qlnvhang.service.BaseService;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBangKeXuatVTHdr;
import org.springframework.data.domain.Page;

public interface DcnbBangKeXuatVTService extends BaseService<DcnbBangKeXuatVTHdr, DcnbBangKeXuatVTReq,Long> {
    public Page<DcnbBangKeXuatVTHdrDTO> searchPage(CustomUserDetails currentUser, DcnbBangKeXuatVTReq req) throws Exception;
    ReportTemplateResponse preview(DcnbBangKeXuatVTReq objReq) throws Exception;
}
