package com.tcdt.qlnvhang.service.dieuchuyennoibo;

import com.tcdt.qlnvhang.request.dieuchuyennoibo.DcnbPhieuNhapKhoHdrReq;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbPhieuNhapKhoHdrListDTO;
import com.tcdt.qlnvhang.service.BaseService;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbPhieuNhapKhoHdr;

import java.util.List;

public interface DcnbPhieuNhapKhoService extends BaseService<DcnbPhieuNhapKhoHdr, DcnbPhieuNhapKhoHdrReq,Long> {

    List<DcnbPhieuNhapKhoHdrListDTO> searchList(DcnbPhieuNhapKhoHdrReq objReq) throws Exception;

    List<DcnbPhieuNhapKhoHdrListDTO> searchListChung(DcnbPhieuNhapKhoHdrReq objReq) throws Exception;

    ReportTemplateResponse preview(DcnbPhieuNhapKhoHdrReq objReq) throws Exception;;
}