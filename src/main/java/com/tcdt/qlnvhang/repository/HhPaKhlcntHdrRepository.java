package com.tcdt.qlnvhang.repository;

import java.util.Optional;

import com.tcdt.qlnvhang.table.HhPaKhlcntHdr;

public interface HhPaKhlcntHdrRepository extends BaseRepository<HhPaKhlcntHdr, Long> {

	Optional<HhPaKhlcntHdr> findByIdThHdr(Long idThHdr);

}
