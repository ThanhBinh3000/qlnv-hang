package com.tcdt.qlnvhang.service;

import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.StrSearchReq;
import com.tcdt.qlnvhang.request.object.HhQdGiaoNvuNhapxuatHdrReq;
import com.tcdt.qlnvhang.request.search.HhQdNhapxuatSearchReq;
import com.tcdt.qlnvhang.table.HhDviThuchienQdinh;
import com.tcdt.qlnvhang.table.HhQdGiaoNvuNhapxuatHdr;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

public interface HhQdGiaoNvuNhapxuatService {

	@Transactional(rollbackOn = Exception.class)
	HhQdGiaoNvuNhapxuatHdr create(HhQdGiaoNvuNhapxuatHdrReq objReq) throws Exception;

	@Transactional(rollbackOn = Exception.class)
	HhQdGiaoNvuNhapxuatHdr update(HhQdGiaoNvuNhapxuatHdrReq objReq) throws Exception;

	HhQdGiaoNvuNhapxuatHdr detail(String ids) throws Exception;

	Page<HhQdGiaoNvuNhapxuatHdr> colection(HhQdNhapxuatSearchReq objReq, HttpServletRequest req) throws Exception;

	@Transactional(rollbackOn = Exception.class)
	HhQdGiaoNvuNhapxuatHdr approve(StatusReq stReq) throws Exception;

	@Transactional(rollbackOn = Exception.class)
	void delete(IdSearchReq idSearchReq) throws Exception;

	HhQdGiaoNvuNhapxuatHdr findBySoHd(StrSearchReq strSearchReq) throws Exception;

	HhDviThuchienQdinh dviThQdDetail(String ids) throws Exception;

	List<Map<String, String>> listLoaiNx();

	void exportDsQdGNvNx(HhQdNhapxuatSearchReq searchReq, HttpServletResponse response) throws Exception;

	Page<HhQdGiaoNvuNhapxuatHdr> timKiem(HhQdNhapxuatSearchReq objReq) throws Exception;

}
