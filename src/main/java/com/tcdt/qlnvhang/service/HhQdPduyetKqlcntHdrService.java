package com.tcdt.qlnvhang.service;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.validation.Valid;

import com.tcdt.qlnvhang.request.object.HhQdKhlcntHdrReq;
import com.tcdt.qlnvhang.response.dauthauvattu.HhQdPduyetKqlcntRes;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import org.springframework.data.domain.Page;

import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.HhQdPduyetKqlcntHdrReq;
import com.tcdt.qlnvhang.request.search.HhQdPduyetKqlcntSearchReq;
import com.tcdt.qlnvhang.table.HhQdPduyetKqlcntHdr;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface HhQdPduyetKqlcntHdrService {

	@Transactional(rollbackOn = Exception.class)
	HhQdPduyetKqlcntHdr create(HhQdPduyetKqlcntHdrReq objReq) throws Exception;

	@Transactional(rollbackOn = Exception.class)
	HhQdPduyetKqlcntHdr update(HhQdPduyetKqlcntHdrReq objReq) throws Exception;

	HhQdPduyetKqlcntHdr detail(String ids) throws Exception;
	HhQdPduyetKqlcntHdr detailBySoQd(String soQd) throws Exception;

	Page<HhQdPduyetKqlcntHdr> colection(HhQdPduyetKqlcntSearchReq objReq) throws Exception;

	@Transactional(rollbackOn = Exception.class)
	HhQdPduyetKqlcntHdr approve(StatusReq stReq) throws Exception;

	@Transactional(rollbackOn = Exception.class)
	void delete(IdSearchReq idSearchReq) throws Exception;
	@Transactional(rollbackOn = Exception.class)
	void deleteMulti(IdSearchReq idSearchReq) throws Exception;

	Page<HhQdPduyetKqlcntHdr> timKiemPage(HhQdPduyetKqlcntSearchReq req, HttpServletResponse response) throws Exception;

//	Page<HhQdPduyetKqlcntRes> timKiemPageCustom(HhQdPduyetKqlcntSearchReq objReq) throws Exception;


	Page<HhQdPduyetKqlcntRes> timKiemPageCustom(HhQdPduyetKqlcntSearchReq req, HttpServletResponse response) throws Exception;

	List<HhQdPduyetKqlcntHdr> timKiemAll(HhQdPduyetKqlcntSearchReq req) throws Exception;

	void exportList(@Valid @RequestBody HhQdPduyetKqlcntSearchReq req, HttpServletResponse response) throws  Exception;
	void exportListHd(@Valid @RequestBody HhQdPduyetKqlcntSearchReq req, HttpServletResponse response) throws  Exception;
	ReportTemplateResponse preview(HhQdPduyetKqlcntHdrReq objReq) throws Exception;
}
