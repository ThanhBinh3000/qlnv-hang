package com.tcdt.qlnvhang.service;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;

import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.HhQdPduyetKqlcntHdrReq;
import com.tcdt.qlnvhang.request.search.HhQdPduyetKqlcntSearchReq;
import com.tcdt.qlnvhang.table.HhQdPduyetKqlcntHdr;

public interface HhQdPduyetKqlcntHdrService {

	@Transactional(rollbackOn = Exception.class)
	HhQdPduyetKqlcntHdr create(HhQdPduyetKqlcntHdrReq objReq) throws Exception;

	@Transactional(rollbackOn = Exception.class)
	HhQdPduyetKqlcntHdr update(HhQdPduyetKqlcntHdrReq objReq) throws Exception;

	HhQdPduyetKqlcntHdr detail(String ids) throws Exception;

	Page<HhQdPduyetKqlcntHdr> colection(HhQdPduyetKqlcntSearchReq objReq) throws Exception;

	@Transactional(rollbackOn = Exception.class)
	HhQdPduyetKqlcntHdr approve(StatusReq stReq) throws Exception;

	@Transactional(rollbackOn = Exception.class)
	void delete(IdSearchReq idSearchReq) throws Exception;

}
