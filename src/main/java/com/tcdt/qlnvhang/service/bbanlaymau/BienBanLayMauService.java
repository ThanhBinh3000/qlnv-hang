package com.tcdt.qlnvhang.service.bbanlaymau;

import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.bbanlaymau.BienBanLayMauReq;
import com.tcdt.qlnvhang.request.search.BienBanLayMauSearchReq;
import com.tcdt.qlnvhang.response.bbanlaymau.BienBanLayMauRes;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

public interface BienBanLayMauService {
	Page<BienBanLayMauRes> search(BienBanLayMauSearchReq req) throws Exception;
	BienBanLayMauRes create(BienBanLayMauReq req) throws Exception;
	BienBanLayMauRes update(BienBanLayMauReq req) throws Exception;
	boolean updateStatus(StatusReq req) throws Exception;
	BienBanLayMauRes detail(Long id) throws Exception;
	boolean delete(Long id) throws Exception;

	@Transactional(rollbackOn = Exception.class)
	boolean deleteMultiple(DeleteReq req) throws Exception;

	boolean exportToExcel(BienBanLayMauSearchReq objReq, HttpServletResponse response) throws Exception;

	Integer getSo() throws Exception;
}
