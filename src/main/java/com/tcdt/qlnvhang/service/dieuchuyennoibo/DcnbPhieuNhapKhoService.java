package com.tcdt.qlnvhang.service.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbPhieuNhapKhoHdrReq;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbPhieuNhapKhoHdrDTO;
import com.tcdt.qlnvhang.service.BaseService;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbPhieuNhapKhoHdr;

import java.util.List;

public interface DcnbPhieuNhapKhoService extends BaseService<DcnbPhieuNhapKhoHdr, DcnbPhieuNhapKhoHdrReq,Long> {

    List<DcnbPhieuNhapKhoHdrDTO> searchList(DcnbPhieuNhapKhoHdrReq objReq) throws Exception;
}