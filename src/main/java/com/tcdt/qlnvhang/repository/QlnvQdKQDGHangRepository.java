package com.tcdt.qlnvhang.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import com.tcdt.qlnvhang.table.QlnvQdKQDGHangHdr;

public interface QlnvQdKQDGHangRepository extends BaseRepository<QlnvQdKQDGHangHdr, Long> {	
	@EntityGraph(value = "QLNV_QD_KQDG_HANG_HDR.children")
	Optional<QlnvQdKQDGHangHdr> findById(Long id);
}
