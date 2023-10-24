package com.tcdt.qlnvhang.service;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.validation.Valid;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntHdr;
import com.tcdt.qlnvhang.request.object.HhQdKhlcntHdrReq;
import com.tcdt.qlnvhang.request.search.HhDthauSearchReq;
import com.tcdt.qlnvhang.request.search.HhQdKhlcntSearchReq;
import com.tcdt.qlnvhang.table.HhDthauNthauDuthau;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntDtl;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import org.springframework.data.domain.Page;

import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.HhDthauReq;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface HhDauThauService {

	@Transactional(rollbackOn = Exception.class)
    List<HhDthauNthauDuthau> create(HhDthauReq objReq) throws Exception;
    void updateKqLcnt(HhDthauReq objReq) throws Exception;
    void updateGoiThau(HhDthauReq objReq) throws Exception;

	Page<HhQdKhlcntDtl> selectPage(HhQdKhlcntSearchReq objReq) throws Exception;
	Page<HhQdKhlcntHdr> selectPageVt(HhQdKhlcntSearchReq objReq) throws Exception;
//
//	List<ThongTinDauThauRes> selectAll(HhDthauSearchReq objReq) throws Exception;
//
//	@Transactional(rollbackOn = Exception.class)
//	HhDthau update(HhDthauReq objReq) throws Exception;
//
    List<HhDthauNthauDuthau> detail(String ids,String loaiVthh, String type) throws Exception;
    List<HhDthauNthauDuthau> getDanhSachNhaThau() throws Exception;
//
////	Page<HhDthau2> colection(HhDthauSearchReq objReq, HttpServletRequest req) throws Exception;
//
	@Transactional(rollbackOn = Exception.class)
	void approve(HhDthauReq stReq) throws Exception;
//
//	@Transactional(rollbackOn = Exception.class)
//	void delete(IdSearchReq idSearchReq) throws Exception;
//
	void exportList(@Valid @RequestBody HhQdKhlcntSearchReq objReq, HttpServletResponse response) throws  Exception;
	ReportTemplateResponse preview(HhQdKhlcntHdrReq objReq) throws Exception;

}
