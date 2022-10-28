package com.tcdt.qlnvhang.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;

import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.HhPhuLucHdReq;
import com.tcdt.qlnvhang.request.search.HhPhuLucHdSearchReq;
import com.tcdt.qlnvhang.table.HhPhuLucHd;

public interface HhPhuLucHdService {

	@Transactional(rollbackOn = Exception.class)
	HhPhuLucHd create(HhPhuLucHdReq objReq) throws Exception;

	@Transactional(rollbackOn = Exception.class)
	HhPhuLucHd update(HhPhuLucHdReq objReq) throws Exception;

	HhPhuLucHd detail(String ids) throws Exception;

	@Transactional(rollbackOn = Exception.class)
	HhPhuLucHd approve(StatusReq stReq) throws Exception;

	@Transactional(rollbackOn = Exception.class)
	void delete(IdSearchReq idSearchReq) throws Exception;

	List<HhPhuLucHd> findBySoHd(HhPhuLucHdSearchReq objReq) throws Exception;

	Page<HhPhuLucHd> colection(HhPhuLucHdSearchReq objReq) throws Exception;



}
