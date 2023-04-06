package com.tcdt.qlnvhang.service.nhaphang.dauthau.ktracluong.bienbannghiemthubaoquan;

import com.tcdt.qlnvhang.request.search.HhQdNhapxuatSearchReq;
import com.tcdt.qlnvhang.service.BaseService;

import com.tcdt.qlnvhang.request.object.HhBbNghiemthuKlstHdrReq;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.bbnghiemthubqld.HhBbNghiemthuKlstHdr;

import javax.servlet.http.HttpServletResponse;

public interface HhBbNghiemthuKlstHdrService extends BaseService<HhBbNghiemthuKlstHdr,HhBbNghiemthuKlstHdrReq,Long> {

//	@Transactional(rollbackOn = Exception.class)
//	HhBbNghiemthuKlstHdr create(HhBbNghiemthuKlstHdrReq objReq) throws Exception;
//
//	@Transactional(rollbackOn = Exception.class)
//	HhBbNghiemthuKlstHdr update(HhBbNghiemthuKlstHdrReq objReq) throws Exception;
//
//	HhBbNghiemthuKlstHdr detail(String ids) throws Exception;
//
//	Page<HhBbNghiemthuKlstHdr> colection(HhBbNghiemthuKlstSearchReq objReq, HttpServletRequest req) throws Exception;
//
//	@Transactional(rollbackOn = Exception.class)
//	boolean approve(StatusReq stReq) throws Exception;
//
//	@Transactional(rollbackOn = Exception.class)
//	void delete(IdSearchReq idSearchReq) throws Exception;
//
//    boolean exportToExcel(HhBbNghiemthuKlstSearchReq objReq, HttpServletResponse response) throws Exception;
//
//    @org.springframework.transaction.annotation.Transactional
//    boolean deleteMultiple(DeleteReq req) throws Exception;
//
//	Integer getSo() throws Exception;
//
//	BaseNhapHangCount count(Set<String> maDvis);

    void exportBbNtBq(HhQdNhapxuatSearchReq searchReq, HttpServletResponse response) throws Exception;
}
