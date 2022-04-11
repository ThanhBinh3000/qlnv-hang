package com.tcdt.qlnvhang.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.data.domain.Page;

import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.object.HhPaKhlcntHdrReq;
import com.tcdt.qlnvhang.request.search.HhPaKhLcntDsChuaQdReq;
import com.tcdt.qlnvhang.request.search.HhPaKhlcntSearchReq;
import com.tcdt.qlnvhang.table.HhPaKhlcntHdr;

public interface HhPaKhlcntHdrService {

	@Transactional(rollbackOn = Exception.class)
	HhPaKhlcntHdr create(HhPaKhlcntHdrReq objReq) throws Exception;

	@Transactional(rollbackOn = Exception.class)
	HhPaKhlcntHdr update(HhPaKhlcntHdrReq objReq) throws Exception;

	HhPaKhlcntHdr detail(String ids) throws Exception;

	Page<HhPaKhlcntHdr> colection(HhPaKhlcntSearchReq objReq) throws Exception;

	List<HhPaKhlcntHdr> dsChuaQd(HhPaKhLcntDsChuaQdReq objReq) throws Exception;

	void delete(@Valid IdSearchReq idSearchReq) throws Exception;

	void exportToExcel(@Valid HhPaKhlcntSearchReq objReq, HttpServletResponse response) throws Exception;
}
