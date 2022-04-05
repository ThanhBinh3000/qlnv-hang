package com.tcdt.qlnvhang.repository.bbanlaymau;

import com.tcdt.qlnvhang.entities.bbanlaymau.BienBanLayMau;
import com.tcdt.qlnvhang.request.search.BienBanLayMauSearchReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BienBanLayMauRepositoryCustom {
	Page<BienBanLayMau> search(BienBanLayMauSearchReq req, Pageable pageable);
}
