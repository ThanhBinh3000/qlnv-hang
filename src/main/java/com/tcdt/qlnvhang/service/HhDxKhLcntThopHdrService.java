package com.tcdt.qlnvhang.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;

import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.object.HhDxKhLcntThopHdrReq;
import com.tcdt.qlnvhang.request.search.HhDxKhLcntDsChuaThReq;
import com.tcdt.qlnvhang.request.search.HhDxKhLcntTChiThopReq;
import com.tcdt.qlnvhang.request.search.HhDxKhLcntThopSearchReq;
import com.tcdt.qlnvhang.table.HhDxKhLcntThopDtl;
import com.tcdt.qlnvhang.table.HhDxKhLcntThopHdr;

public interface HhDxKhLcntThopHdrService {

	HhDxKhLcntThopHdr sumarryData(HhDxKhLcntTChiThopReq objReq) throws Exception;

	@Transactional(rollbackOn = Exception.class)
	HhDxKhLcntThopHdr create(HhDxKhLcntThopHdrReq objReq) throws Exception;

	@Transactional(rollbackOn = Exception.class)
	HhDxKhLcntThopHdr update(HhDxKhLcntThopHdrReq objReq) throws Exception;

	HhDxKhLcntThopHdr detail(String ids) throws Exception;

	Page<HhDxKhLcntThopHdr> colection(HhDxKhLcntThopSearchReq objReq) throws Exception;

	@Transactional(rollbackOn = Exception.class)
	void delete(IdSearchReq idSearchReq) throws Exception;

	List<HhDxKhLcntThopDtl> findByStatus(HhDxKhLcntDsChuaThReq objReq) throws Exception;

}
