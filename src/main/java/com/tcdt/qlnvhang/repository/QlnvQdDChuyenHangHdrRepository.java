package com.tcdt.qlnvhang.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import com.tcdt.qlnvhang.table.QlnvQdDChuyenHangHdr;

public interface QlnvQdDChuyenHangHdrRepository extends BaseRepository<QlnvQdDChuyenHangHdr, Long> {

	@EntityGraph(value = "QLNV_QD_CHUYEN_HANG_HDR.children")
	Optional<QlnvQdDChuyenHangHdr> findById(Long id);
}