package com.tcdt.qlnvhang.repository.bandaugia.thongbaobandaugiakhongthanh;

import com.tcdt.qlnvhang.request.bandaugia.thongbaobandaugiakhongthanh.BhThongBaoBdgKtSearchReq;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BhThongBaoBdgKtRepositoryCustom {
	List<Object[]> search(BhThongBaoBdgKtSearchReq req, Pageable pageable);

	int count(BhThongBaoBdgKtSearchReq req);
}
