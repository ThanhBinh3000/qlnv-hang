package com.tcdt.qlnvhang.service;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import com.tcdt.qlnvhang.table.HhQdKhlcntDsgthau;
import org.springframework.data.domain.Page;

import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.HhQdKhlcntHdrReq;
import com.tcdt.qlnvhang.request.search.HhQdKhlcntSearchReq;
import com.tcdt.qlnvhang.table.HhQdKhlcntHdr;

import java.util.List;

public interface HhQdKhlcntHdrService {

	@Transactional(rollbackOn = Exception.class)
	HhQdKhlcntHdr create(HhQdKhlcntHdrReq objReq) throws Exception;

	@Transactional(rollbackOn = Exception.class)
	HhQdKhlcntHdr update(HhQdKhlcntHdrReq objReq) throws Exception;

	HhQdKhlcntHdr detail(String ids) throws Exception;

	HhQdKhlcntDsgthau detailGoiThau(String ids) throws Exception;

//	Page<HhQdKhlcntHdr> colection(HhQdKhlcntSearchReq objReq) throws Exception;

	@Transactional(rollbackOn = Exception.class)
	HhQdKhlcntHdr approve(StatusReq stReq) throws Exception;

	@Transactional(rollbackOn = Exception.class)
	void delete(IdSearchReq idSearchReq) throws Exception;

	void deleteMulti(IdSearchReq idSearchReq) throws Exception;

	HhQdKhlcntHdr detailNumber(String soQd) throws Exception;

	Page<HhQdKhlcntHdr> colection(HhQdKhlcntSearchReq objReq, HttpServletResponse response) throws Exception;

	void exportList(HhQdKhlcntSearchReq searchReq, HttpServletResponse response) throws Exception;

	void exportToExcel(HhQdKhlcntSearchReq searchReq, HttpServletResponse response) throws Exception;

	Page<HhQdKhlcntHdr> getAllPage(HhQdKhlcntSearchReq objReq) throws Exception;

	List<HhQdKhlcntHdr> getAll(HhQdKhlcntSearchReq objReq) throws Exception;

}
