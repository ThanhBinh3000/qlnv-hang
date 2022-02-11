package com.tcdt.qlnvhang.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import com.tcdt.qlnvhang.table.QlnvQdPhuongAnGiaHdr;

public interface QlnvQdPhuongAnGiaHdrRepository extends BaseRepository<QlnvQdPhuongAnGiaHdr, Long> {
	
	@EntityGraph(value = "QLNV_QD_MUA_HANG_HDR.children")
	Optional<QlnvQdPhuongAnGiaHdr> findById(Long id);
}
