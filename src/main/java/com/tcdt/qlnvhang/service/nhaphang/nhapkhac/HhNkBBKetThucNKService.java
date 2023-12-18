package com.tcdt.qlnvhang.service.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhNkBBKetThucNKHdr;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhNkBBKetThucNKReq;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhNkBangKeNhapVTReq;
import com.tcdt.qlnvhang.response.nhaphang.nhapkhac.HhNkBBKetThucNKHdrDTO;
import com.tcdt.qlnvhang.response.nhaphang.nhapkhac.HhNkBBKetThucNKHdrListDTO;
import com.tcdt.qlnvhang.service.BaseService;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface HhNkBBKetThucNKService extends BaseService<HhNkBBKetThucNKHdr, HhNkBBKetThucNKReq, Long> {
    public Page<HhNkBBKetThucNKHdrDTO> search(CustomUserDetails currentUser, HhNkBBKetThucNKReq req) throws Exception;

    List<HhNkBBKetThucNKHdrListDTO> searchList(CustomUserDetails currentUser, HhNkBBKetThucNKReq objReq);
    ReportTemplateResponse preview(HhNkBBKetThucNKReq objReq) throws Exception;
}
