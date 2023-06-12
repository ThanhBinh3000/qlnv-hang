package com.tcdt.qlnvhang.service.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbBbChuanBiKhoHdrReq;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBbChuanBiKhoHdrDTO;
import com.tcdt.qlnvhang.service.BaseService;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBbChuanBiKhoHdr;
import org.springframework.data.domain.Page;

public interface DcnbBbChuanBiKhoService extends BaseService<DcnbBbChuanBiKhoHdr, DcnbBbChuanBiKhoHdrReq,Long> {
    public Page<DcnbBbChuanBiKhoHdrDTO> search(DcnbBbChuanBiKhoHdrReq req) throws Exception;
}