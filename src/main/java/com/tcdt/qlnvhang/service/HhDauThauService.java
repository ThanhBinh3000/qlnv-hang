package com.tcdt.qlnvhang.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.validation.Valid;

import com.tcdt.qlnvhang.response.dauthauvattu.ThongTinDauThauRes;
import com.tcdt.qlnvhang.table.HhDthauNthauDuthau;
import com.tcdt.qlnvhang.table.HhQdKhlcntDtl;
import org.springframework.data.domain.Page;

import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.HhDthauReq;
import com.tcdt.qlnvhang.request.search.HhDthauSearchReq;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface HhDauThauService {

	@Transactional(rollbackOn = Exception.class)
    List<HhDthauNthauDuthau> create(HhDthauReq objReq) throws Exception;
//
//	Page<ThongTinDauThauRes> selectPage(HhDthauSearchReq objReq) throws Exception;
//
//	List<ThongTinDauThauRes> selectAll(HhDthauSearchReq objReq) throws Exception;
//
//	@Transactional(rollbackOn = Exception.class)
//	HhDthau update(HhDthauReq objReq) throws Exception;
//
    List<HhDthauNthauDuthau> detail(String ids) throws Exception;
//
////	Page<HhDthau2> colection(HhDthauSearchReq objReq, HttpServletRequest req) throws Exception;
//
	@Transactional(rollbackOn = Exception.class)
	void approve(StatusReq stReq) throws Exception;
//
//	@Transactional(rollbackOn = Exception.class)
//	void delete(IdSearchReq idSearchReq) throws Exception;
//
//	void exportList(@Valid @RequestBody HhDthauSearchReq objReq, HttpServletResponse response) throws  Exception;
}
