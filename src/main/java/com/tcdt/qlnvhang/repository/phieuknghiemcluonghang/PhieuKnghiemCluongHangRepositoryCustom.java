package com.tcdt.qlnvhang.repository.phieuknghiemcluonghang;

import com.tcdt.qlnvhang.entities.phieuknghiemcluonghang.PhieuKnghiemCluongHang;
import com.tcdt.qlnvhang.request.search.PhieuKnghiemCluongHangSearchReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PhieuKnghiemCluongHangRepositoryCustom {
	Page<PhieuKnghiemCluongHang> search(PhieuKnghiemCluongHangSearchReq req, Pageable pageable);
}
