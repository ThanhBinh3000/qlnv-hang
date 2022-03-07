package com.tcdt.qlnvhang.repository;

import java.util.Optional;

import com.tcdt.qlnvhang.table.HhQdKhlcntHdr;

public interface HhQdKhlcntHdrRepository extends BaseRepository<HhQdKhlcntHdr, Long> {

	Optional<HhQdKhlcntHdr> findByIdPaHdr(Long idPaHdr);

	Optional<HhQdKhlcntHdr> findBySoQd(String soQd);

}
