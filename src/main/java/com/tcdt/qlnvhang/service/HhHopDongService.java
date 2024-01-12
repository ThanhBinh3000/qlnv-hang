package com.tcdt.qlnvhang.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import com.tcdt.qlnvhang.request.object.HhHopDongDtlReq;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import org.springframework.data.domain.Page;

import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.StrSearchReq;
import com.tcdt.qlnvhang.request.object.HhHopDongHdrReq;
import com.tcdt.qlnvhang.request.search.HhHopDongSearchReq;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.hopdong.HhHopDongHdr;

public interface HhHopDongService {

	@Transactional(rollbackOn = Exception.class)
	HhHopDongHdr create(HhHopDongHdrReq objReq) throws Exception;

	@Transactional(rollbackOn = Exception.class)
	HhHopDongHdr update(HhHopDongHdrReq objReq) throws Exception;

	HhHopDongHdr detail(String ids) throws Exception;

	Page<HhHopDongHdr> colection(HhHopDongSearchReq objReq, HttpServletRequest req) throws Exception;

	@Transactional(rollbackOn = Exception.class)
	HhHopDongHdr approve(StatusReq stReq) throws Exception;

	@Transactional(rollbackOn = Exception.class)
	void delete(IdSearchReq idSearchReq) throws Exception;

	HhHopDongHdr findBySoHd(StrSearchReq strSearchReq) throws Exception;

	Page<HhHopDongHdr> selectPage(HhHopDongSearchReq objReq, HttpServletResponse response) throws Exception;


	void deleteMulti(IdSearchReq idSearchReq) throws Exception;
	void saveSoTienTinhPhat(HhHopDongDtlReq dataMap) throws Exception;

	void exportList(HhHopDongSearchReq objReq, HttpServletResponse response) throws Exception;

	Page<HhHopDongHdr> lookupData(HhHopDongSearchReq req) throws Exception;

	ReportTemplateResponse preview (HhHopDongHdrReq req) throws Exception;
}
