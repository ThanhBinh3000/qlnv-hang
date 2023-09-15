package com.tcdt.qlnvhang.service;

import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.IdSearchReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.StrSearchReq;
import com.tcdt.qlnvhang.request.object.HhQdGiaoNvuNhapxuatHdrReq;
import com.tcdt.qlnvhang.request.search.HhQdNhapxuatSearchReq;
import com.tcdt.qlnvhang.response.BaseNhapHangCount;
import com.tcdt.qlnvhang.table.HhDviThuchienQdinh;
import com.tcdt.qlnvhang.entities.nhaphang.dauthau.nhiemvunhap.NhQdGiaoNvuNhapxuatHdr;
import com.tcdt.qlnvhang.table.ReportTemplateResponse;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

public interface HhQdGiaoNvuNhapxuatService {

	@Transactional(rollbackOn = Exception.class)
    NhQdGiaoNvuNhapxuatHdr create(HhQdGiaoNvuNhapxuatHdrReq objReq) throws Exception;

	@Transactional(rollbackOn = Exception.class)
    NhQdGiaoNvuNhapxuatHdr update(HhQdGiaoNvuNhapxuatHdrReq objReq) throws Exception;

	NhQdGiaoNvuNhapxuatHdr detail(String ids) throws Exception;

	Page<NhQdGiaoNvuNhapxuatHdr> colection(HhQdNhapxuatSearchReq objReq, HttpServletRequest req) throws Exception;

    @Transactional(rollbackOn = Exception.class)
    boolean updateStatus(StatusReq req) throws Exception;

    @Transactional(rollbackOn = Exception.class)
	void delete(IdSearchReq idSearchReq) throws Exception;
	void deleteMulti(IdSearchReq idSearchReq) throws Exception;

	NhQdGiaoNvuNhapxuatHdr findBySoHd(StrSearchReq strSearchReq) throws Exception;

	HhDviThuchienQdinh dviThQdDetail(String ids) throws Exception;

	List<Map<String, String>> listLoaiNx();

	void exportDsQdGNvNx(HhQdNhapxuatSearchReq searchReq, HttpServletResponse response) throws Exception;
	void exportBbNtBq(HhQdNhapxuatSearchReq searchReq, HttpServletResponse response) throws Exception;

	Page<NhQdGiaoNvuNhapxuatHdr> timKiem(HhQdNhapxuatSearchReq objReq) throws Exception;

	BaseNhapHangCount count() throws Exception;

	@Transactional(rollbackOn = Exception.class)
	boolean deleteMultiple(DeleteReq req) throws Exception;

	Page<NhQdGiaoNvuNhapxuatHdr> searchPage(HhQdNhapxuatSearchReq req) throws Exception;
	List<NhQdGiaoNvuNhapxuatHdr> layTatCaQdGiaoNvNh(HhQdNhapxuatSearchReq req) throws Exception;

	void updateDdiemNhap(HhQdGiaoNvuNhapxuatHdrReq req) throws Exception;

	ReportTemplateResponse preview(HhQdGiaoNvuNhapxuatHdrReq hhQdGiaoNvuNhapxuatHdrReq) throws Exception;
}
