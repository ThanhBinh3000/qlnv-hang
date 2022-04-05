package com.tcdt.qlnvhang.service.phieuknghiemcluonghang;

import com.tcdt.qlnvhang.request.object.phieuknghiemcluonghang.KquaKnghiemReq;
import com.tcdt.qlnvhang.response.phieuknghiemcluonghang.KquaKnghiemRes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface KquaKnghiemService {
	Page<KquaKnghiemRes> list(Long phieuKnghiemId, Pageable pageable);
	void update(Long phieuKnghiemId, List<KquaKnghiemReq> list);
}
