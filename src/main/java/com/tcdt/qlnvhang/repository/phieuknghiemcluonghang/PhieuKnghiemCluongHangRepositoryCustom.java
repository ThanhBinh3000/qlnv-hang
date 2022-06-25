package com.tcdt.qlnvhang.repository.phieuknghiemcluonghang;

import com.tcdt.qlnvhang.entities.phieuknghiemcluonghang.PhieuKnghiemCluongHang;
import com.tcdt.qlnvhang.request.search.PhieuKnghiemCluongHangSearchReq;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PhieuKnghiemCluongHangRepositoryCustom {
	List<Object[]> search(PhieuKnghiemCluongHangSearchReq req, Pageable pageable);

	int countCtkhn(PhieuKnghiemCluongHangSearchReq req);
}
