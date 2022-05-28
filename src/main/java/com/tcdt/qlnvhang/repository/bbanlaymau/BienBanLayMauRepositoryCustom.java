package com.tcdt.qlnvhang.repository.bbanlaymau;

import com.tcdt.qlnvhang.entities.bbanlaymau.BienBanLayMau;
import com.tcdt.qlnvhang.request.search.BienBanLayMauSearchReq;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BienBanLayMauRepositoryCustom {
	List<BienBanLayMau> search(BienBanLayMauSearchReq req, Pageable pageable);

	int countBienBan(BienBanLayMauSearchReq req);
}
