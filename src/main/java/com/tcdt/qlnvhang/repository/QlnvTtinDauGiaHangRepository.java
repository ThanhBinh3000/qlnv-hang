package com.tcdt.qlnvhang.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;

import com.tcdt.qlnvhang.table.QlnvTtinDauGiaHangHdr;

public interface QlnvTtinDauGiaHangRepository extends BaseRepository<QlnvTtinDauGiaHangHdr, Long> {
	@EntityGraph(value = "QLNV_DAUGIA_HANG_HDR.children")
	Optional<QlnvTtinDauGiaHangHdr> findById(Long id);
}
