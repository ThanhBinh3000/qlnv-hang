package com.tcdt.qlnvhang.service.dieuchuyennoibo;

import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbBBNTBQHdrReq;
import com.tcdt.qlnvhang.service.BaseService;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBBNTBQHdr;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbQuyetDinhDcCHdr;
import org.springframework.data.domain.Page;

public interface DcnbBBNTBQHdrService extends BaseService<DcnbBBNTBQHdr, DcnbBBNTBQHdrReq,Long> {

    public Page<DcnbQuyetDinhDcCHdr> searchPage(CustomUserDetails currentUser, DcnbBBNTBQHdrReq req) throws Exception;
}