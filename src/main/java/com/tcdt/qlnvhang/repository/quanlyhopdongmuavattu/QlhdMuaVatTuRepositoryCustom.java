package com.tcdt.qlnvhang.repository.quanlyhopdongmuavattu;

import com.tcdt.qlnvhang.request.hopdongmuavattu.QlhdmvtFilterRequest;
import com.tcdt.qlnvhang.response.quanlyhopdongmuavattu.QlhdMuaVatTuResponseDTO;
import org.springframework.data.domain.Page;

public interface QlhdMuaVatTuRepositoryCustom {
	Page<QlhdMuaVatTuResponseDTO> filter(QlhdmvtFilterRequest req);
}
