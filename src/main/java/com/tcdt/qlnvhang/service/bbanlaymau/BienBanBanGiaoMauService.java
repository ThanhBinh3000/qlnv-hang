package com.tcdt.qlnvhang.service.bbanlaymau;

import com.tcdt.qlnvhang.request.StatusReq;
import com.tcdt.qlnvhang.request.object.bbanlaymau.BienBanBanGiaoMauReq;
import com.tcdt.qlnvhang.request.search.BienBanBanGiaoMauSearchReq;
import com.tcdt.qlnvhang.response.bbanlaymau.BienBanBanGiaoMauRes;
import org.springframework.data.domain.Page;

public interface BienBanBanGiaoMauService {
	Page<BienBanBanGiaoMauRes> search(BienBanBanGiaoMauSearchReq req);
	BienBanBanGiaoMauRes create(BienBanBanGiaoMauReq req) throws Exception;
	BienBanBanGiaoMauRes update(BienBanBanGiaoMauReq req) throws Exception;
	boolean updateStatus(StatusReq req) throws Exception;
	BienBanBanGiaoMauRes detail(Long id) throws Exception;
	boolean delete(Long id) throws Exception;
}
