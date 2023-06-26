package com.tcdt.qlnvhang.service.dieuchuyennoibo;

import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbBBKetThucNKReq;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBBKetThucNKHdrDTO;
import com.tcdt.qlnvhang.service.BaseService;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBBKetThucNKHdr;
import org.springframework.data.domain.Page;

public interface DcnbBBKetThucNKService extends BaseService<DcnbBBKetThucNKHdr, DcnbBBKetThucNKReq, Long> {
    public Page<DcnbBBKetThucNKHdrDTO> search(CustomUserDetails currentUser, DcnbBBKetThucNKReq req) throws Exception;
}
