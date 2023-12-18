package com.tcdt.qlnvhang.service.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhNkBangKeNhapVTHdr;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhNkBangKeNhapVTReq;
import com.tcdt.qlnvhang.request.object.vattu.hosokythuat.NhHoSoKyThuatReq;
import com.tcdt.qlnvhang.response.nhaphang.nhapkhac.HhNkBangKeNhapVTHdrDTO;
import com.tcdt.qlnvhang.service.BaseService;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import org.springframework.data.domain.Page;

public interface HhNkBangKeNhapVTService extends BaseService<HhNkBangKeNhapVTHdr, HhNkBangKeNhapVTReq,Long> {
    public Page<HhNkBangKeNhapVTHdrDTO> searchPage(CustomUserDetails currentUser, HhNkBangKeNhapVTReq req) throws Exception;
    public void approve(CustomUserDetails currentUser, StatusReq statusReq) throws Exception;
    ReportTemplateResponse preview(HhNkBangKeNhapVTReq objReq) throws Exception;
}
