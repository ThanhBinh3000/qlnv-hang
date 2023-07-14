package com.tcdt.qlnvhang.service.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhNkBbGiaoNhanHdr;
import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhNkBbGiaoNhanHdrReq;
import com.tcdt.qlnvhang.response.nhaphang.nhapkhac.HhNkBbGiaoNhanHdrDTO;
import com.tcdt.qlnvhang.service.BaseService;
import org.springframework.data.domain.Page;

public interface HhNkBbGiaoNhanService extends BaseService<HhNkBbGiaoNhanHdr, HhNkBbGiaoNhanHdrReq,Long> {
    public Page<HhNkBbGiaoNhanHdrDTO> searchPage(CustomUserDetails currentUser, HhNkBbGiaoNhanHdrReq req) throws Exception;

}