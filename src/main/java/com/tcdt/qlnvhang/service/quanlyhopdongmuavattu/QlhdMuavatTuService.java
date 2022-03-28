package com.tcdt.qlnvhang.service.quanlyhopdongmuavattu;

import com.tcdt.qlnvhang.request.quanlyhopdongmuavattu.QlhdMuaVatTuRequestDTO;
import com.tcdt.qlnvhang.response.quanlyhopdongmuavattu.QlhdMuaVatTuResponseDTO;

public interface QlhdMuavatTuService {
	QlhdMuaVatTuResponseDTO create(QlhdMuaVatTuRequestDTO request) throws Exception;
}
