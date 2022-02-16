package com.tcdt.qlnvhang.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;

import com.tcdt.qlnvhang.table.QlnvPhieuNXuatBoNganhHdr;

public interface QlnvPhieuNXuatBoNganhHdrRepository extends BaseRepository<QlnvPhieuNXuatBoNganhHdr, Long> {

	@EntityGraph(value = "QLNV_PHIEU_NXUAT_BONGANH_HDR.children")
	Optional<QlnvPhieuNXuatBoNganhHdr> findById(Long id);
}