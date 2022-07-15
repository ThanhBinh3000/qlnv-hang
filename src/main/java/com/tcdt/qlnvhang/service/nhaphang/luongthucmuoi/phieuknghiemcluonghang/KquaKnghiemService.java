package com.tcdt.qlnvhang.service.nhaphang.luongthucmuoi.phieuknghiemcluonghang;

import com.tcdt.qlnvhang.request.object.phieuknghiemcluonghang.KquaKnghiemReq;
import com.tcdt.qlnvhang.response.phieuknghiemcluonghang.KquaKnghiemRes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface KquaKnghiemService {
	Page<KquaKnghiemRes> list(Long phieuKnghiemId, Pageable pageable);
	void update(Long phieuKnghiemId, List<KquaKnghiemReq> list);

	void deleteByPhieuKnghiemId(Long phieuKnghiemId);

	void deleteByPhieuKnghiemIdIn(Collection<Long> phieuKnghiemIds);

	Map<Long, Long> countKqByPhieuKnghiemId(Collection<Long> phieuKnhiemIds);
}
