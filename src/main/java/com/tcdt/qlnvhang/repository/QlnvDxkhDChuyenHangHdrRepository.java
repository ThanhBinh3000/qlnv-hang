package com.tcdt.qlnvhang.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import com.tcdt.qlnvhang.table.QlnvDxkhDChuyenHangHdr;

public interface QlnvDxkhDChuyenHangHdrRepository extends BaseRepository<QlnvDxkhDChuyenHangHdr, Long> {

	@EntityGraph(value = "QLNV_DXKH_CHUYEN_HANG_HDR.children")
	Optional<QlnvDxkhDChuyenHangHdr> findById(Long id);
}