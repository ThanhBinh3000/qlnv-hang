package com.tcdt.qlnvhang.service.nhaphang.bbanlaymau;

import com.tcdt.qlnvhang.request.DeleteReq;
import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.bbanlaymau.BienBanBanGiaoMauReq;
import com.tcdt.qlnvhang.request.search.BienBanBanGiaoMauSearchReq;
import com.tcdt.qlnvhang.response.BaseNhapHangCount;
import com.tcdt.qlnvhang.response.bbanlaymau.BienBanBanGiaoMauRes;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.Set;

public interface BienBanBanGiaoMauService {
	Page<BienBanBanGiaoMauRes> search(BienBanBanGiaoMauSearchReq req) throws Exception;
	BienBanBanGiaoMauRes create(BienBanBanGiaoMauReq req) throws Exception;
	BienBanBanGiaoMauRes update(BienBanBanGiaoMauReq req) throws Exception;
	boolean updateStatus(StatusReq req) throws Exception;
	BienBanBanGiaoMauRes detail(Long id) throws Exception;
	boolean delete(Long id) throws Exception;

    @Transactional(rollbackOn = Exception.class)
    boolean deleteMultiple(DeleteReq req) throws Exception;

    boolean exportToExcel(BienBanBanGiaoMauSearchReq objReq, HttpServletResponse response) throws Exception;

	Integer getSo() throws Exception;

	BaseNhapHangCount count(Set<String> maDvis) throws Exception;
}
