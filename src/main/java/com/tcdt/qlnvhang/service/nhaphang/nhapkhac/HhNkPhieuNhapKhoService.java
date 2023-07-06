package com.tcdt.qlnvhang.service.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.HhNkPhieuNhapKhoHdr;
import com.tcdt.qlnvhang.request.nhaphang.nhapkhac.HhNkPhieuNhapKhoHdrReq;
import com.tcdt.qlnvhang.response.dieuChuyenNoiBo.DcnbPhieuNhapKhoHdrListDTO;
import com.tcdt.qlnvhang.response.nhaphang.nhapkhac.HhNkPhieuNhapKhoHdrListDTO;
import com.tcdt.qlnvhang.service.BaseService;

import java.util.List;

public interface HhNkPhieuNhapKhoService extends BaseService<HhNkPhieuNhapKhoHdr, HhNkPhieuNhapKhoHdrReq, Long> {

    List<HhNkPhieuNhapKhoHdrListDTO> searchList(HhNkPhieuNhapKhoHdrReq objReq) throws Exception;

    List<HhNkPhieuNhapKhoHdrListDTO> searchListChung(HhNkPhieuNhapKhoHdrReq objReq) throws Exception;
}