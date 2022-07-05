package com.tcdt.qlnvhang.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.response.SoBienBanPhieuRes;
import org.springframework.data.domain.Page;

import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.HhBbNghiemthuKlstHdrReq;
import com.tcdt.qlnvhang.request.search.HhBbNghiemthuKlstSearchReq;
import com.tcdt.qlnvhang.table.HhBbNghiemthuKlstHdr;

public interface HhBbNghiemthuKlstHdrService {

	@Transactional(rollbackOn = Exception.class)
	HhBbNghiemthuKlstHdr create(HhBbNghiemthuKlstHdrReq objReq) throws Exception;

	@Transactional(rollbackOn = Exception.class)
	HhBbNghiemthuKlstHdr update(HhBbNghiemthuKlstHdrReq objReq) throws Exception;

	HhBbNghiemthuKlstHdr detail(String ids) throws Exception;

	Page<HhBbNghiemthuKlstHdr> colection(HhBbNghiemthuKlstSearchReq objReq, HttpServletRequest req) throws Exception;

	@Transactional(rollbackOn = Exception.class)
	boolean approve(StatusReq stReq) throws Exception;

	@Transactional(rollbackOn = Exception.class)
	void delete(IdSearchReq idSearchReq) throws Exception;

    boolean exportToExcel(HhBbNghiemthuKlstSearchReq objReq, HttpServletResponse response) throws Exception;

    @org.springframework.transaction.annotation.Transactional
    boolean deleteMultiple(DeleteReq req) throws Exception;

    SoBienBanPhieuRes getSo() throws Exception;
}
