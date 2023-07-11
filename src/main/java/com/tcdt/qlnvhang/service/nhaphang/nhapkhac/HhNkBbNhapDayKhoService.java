package com.tcdt.qlnvhang.service.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhNkBbNhapDayKhoHdr;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhNkBbNhapDayKhoHdrReq;
import com.tcdt.qlnvhang.response.nhaphang.nhapkhac.HhNkBbNhapDayKhoHdrDTO;
import com.tcdt.qlnvhang.service.BaseService;

import java.util.List;

public interface HhNkBbNhapDayKhoService extends BaseService<HhNkBbNhapDayKhoHdr, HhNkBbNhapDayKhoHdrReq, Long> {

    List<HhNkBbNhapDayKhoHdrDTO> searchList(CustomUserDetails currentUser, HhNkBbNhapDayKhoHdrReq param);
}