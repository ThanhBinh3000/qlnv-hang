package com.tcdt.qlnvhang.repository.bbanbangiaomau;

import com.tcdt.qlnvhang.entities.nhaphang.bbanlaymau.BienBanBanGiaoMau;
import com.tcdt.qlnvhang.request.search.BienBanBanGiaoMauSearchReq;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BienBanBanGiaoMauRepositoryCustom {
	List<Object[]> search(BienBanBanGiaoMauSearchReq req, Pageable pageable);

	int countBienBan(BienBanBanGiaoMauSearchReq req);
}
