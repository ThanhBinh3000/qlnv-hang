package com.tcdt.qlnvhang.service;

import com.tcdt.qlnvhang.request.QuyetDinhKHLCNTVatTuReq;
import com.tcdt.qlnvhang.response.QuyetDinhKHLCNTVatTuRes;

public interface QuyetDinhKHLCNTGoiThauVatTuService {
	QuyetDinhKHLCNTVatTuRes create (QuyetDinhKHLCNTVatTuReq req) throws Exception;

	QuyetDinhKHLCNTVatTuRes update (QuyetDinhKHLCNTVatTuReq req) throws Exception;

	void delete(Long id) throws Exception;

}
