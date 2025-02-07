package com.tcdt.qlnvhang.service.quanlyhopdongmuavattu;

import com.tcdt.qlnvhang.request.hopdongmuavattu.QlhdMuaVatTuRequestDTO;
import com.tcdt.qlnvhang.request.hopdongmuavattu.QlhdmvtFilterRequest;
import com.tcdt.qlnvhang.response.quanlyhopdongmuavattu.QlhdMuaVatTuResponseDTO;
import org.springframework.data.domain.Page;

public interface QlhdMuavatTuService {
	QlhdMuaVatTuResponseDTO create(QlhdMuaVatTuRequestDTO request) throws Exception;

	QlhdMuaVatTuResponseDTO update(QlhdMuaVatTuRequestDTO request) throws Exception;

	Page<QlhdMuaVatTuResponseDTO> filter(QlhdmvtFilterRequest request);
}
