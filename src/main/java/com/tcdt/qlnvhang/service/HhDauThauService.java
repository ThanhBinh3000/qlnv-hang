package com.tcdt.qlnvhang.service;

import javax.transaction.Transactional;

import com.tcdt.qlnvhang.request.search.HhQdKhlcntSearchReq;
import com.tcdt.qlnvhang.table.HhDthauNthauDuthau;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.qdpduyetkhlcnt.HhQdKhlcntDtl;
import org.springframework.data.domain.Page;

import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.HhDthauReq;

import java.util.List;

public interface HhDauThauService {

	@Transactional(rollbackOn = Exception.class)
    List<HhDthauNthauDuthau> create(HhDthauReq objReq) throws Exception;
//
	Page<HhQdKhlcntDtl> selectPage(HhQdKhlcntSearchReq objReq) throws Exception;
//
//	List<ThongTinDauThauRes> selectAll(HhDthauSearchReq objReq) throws Exception;
//
//	@Transactional(rollbackOn = Exception.class)
//	HhDthau update(HhDthauReq objReq) throws Exception;
//
    List<HhDthauNthauDuthau> detail(String ids,String loaiVthh) throws Exception;
//
////	Page<HhDthau2> colection(HhDthauSearchReq objReq, HttpServletRequest req) throws Exception;
//
	@Transactional(rollbackOn = Exception.class)
	void approve(HhDthauReq stReq) throws Exception;
//
//	@Transactional(rollbackOn = Exception.class)
//	void delete(IdSearchReq idSearchReq) throws Exception;
//
//	void exportList(@Valid @RequestBody HhDthauSearchReq objReq, HttpServletResponse response) throws  Exception;
}
