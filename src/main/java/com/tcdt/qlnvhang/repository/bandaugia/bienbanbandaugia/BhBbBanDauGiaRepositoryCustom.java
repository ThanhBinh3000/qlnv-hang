package com.tcdt.qlnvhang.repository.bandaugia.bienbanbandaugia;

import com.tcdt.qlnvhang.request.bandaugia.bienbanbandaugia.BhBbBanDauGiaSearchReq;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BhBbBanDauGiaRepositoryCustom {
	List<Object[]> search(BhBbBanDauGiaSearchReq req, Pageable pageable);

	int count(BhBbBanDauGiaSearchReq req);
}
