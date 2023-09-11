package com.tcdt.qlnvhang.service.dieuchuyennoibo;

import com.tcdt.qlnvhang.jwt.CustomUserDetails;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbBBNTBQHdrReq;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbBBNTBQHdrDTO;
import com.tcdt.qlnvhang.service.BaseService;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBBNTBQHdr;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbQuyetDinhDcCHdr;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DcnbBBNTBQHdrService extends BaseService<DcnbBBNTBQHdr, DcnbBBNTBQHdrReq,Long> {

    public Page<DcnbBBNTBQHdrDTO> searchPage(CustomUserDetails currentUser, DcnbBBNTBQHdrReq req) throws Exception;

    List<DcnbBBNTBQHdr> searchList(CustomUserDetails currentUser, DcnbBBNTBQHdrReq objReq);

    public DcnbBBNTBQHdr approve(StatusReq req) throws Exception;
}