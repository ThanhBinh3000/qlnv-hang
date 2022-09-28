package com.tcdt.qlnvhang.service.nhaphang.bbanlaymau;

import com.tcdt.qlnvhang.entities.nhaphang.bbanlaymau.BienBanLayMau;
import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.bbanlaymau.BienBanLayMauReq;
import com.tcdt.qlnvhang.request.search.BienBanLayMauSearchReq;
import com.tcdt.qlnvhang.response.BaseNhapHangCount;
import com.tcdt.qlnvhang.response.bbanlaymau.BienBanLayMauRes;
import com.tcdt.qlnvhang.service.BaseService;
import com.tcdt.qlnvhang.service.impl.BaseServiceImpl;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.Set;

public interface BienBanLayMauService extends BaseService<BienBanLayMau,BienBanLayMauReq,Long> {
//	Page<BienBanLayMauRes> search(BienBanLayMauSearchReq req) throws Exception;
//	BienBanLayMau create(BienBanLayMauReq req) throws Exception;
//	BienBanLayMau update(BienBanLayMauReq req) throws Exception;
//	boolean updateStatus(StatusReq req) throws Exception;
//	BienBanLayMau detail(Long id) throws Exception;
//	boolean delete(Long id) throws Exception;

//	@Transactional(rollbackOn = Exception.class)
//	boolean deleteMultiple(DeleteReq req) throws Exception;
//
//	boolean exportToExcel(BienBanLayMauSearchReq objReq, HttpServletResponse response) throws Exception;
//
//	Integer getSo() throws Exception;
//
//	BaseNhapHangCount count(Set<String> maDvis) throws Exception;
}
