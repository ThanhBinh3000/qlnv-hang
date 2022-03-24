package com.tcdt.qlnvhang.service;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;

import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.HhHopDongHdrReq;
import com.tcdt.qlnvhang.request.search.HhHopDongSearchReq;
import com.tcdt.qlnvhang.table.HhHopDongHdr;

public interface HhHopDongService {

	@Transactional(rollbackOn = Exception.class)
	HhHopDongHdr create(HhHopDongHdrReq objReq) throws Exception;

	@Transactional(rollbackOn = Exception.class)
	HhHopDongHdr update(HhHopDongHdrReq objReq) throws Exception;

	HhHopDongHdr detail(String ids) throws Exception;

	Page<HhHopDongHdr> colection(HhHopDongSearchReq objReq) throws Exception;

	@Transactional(rollbackOn = Exception.class)
	HhHopDongHdr approve(StatusReq stReq) throws Exception;

	@Transactional(rollbackOn = Exception.class)
	void delete(IdSearchReq idSearchReq) throws Exception;

}
