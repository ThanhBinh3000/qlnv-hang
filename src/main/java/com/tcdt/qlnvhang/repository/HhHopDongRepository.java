package com.tcdt.qlnvhang.repository;

import java.util.Optional;

import com.tcdt.qlnvhang.table.HhHopDongHdr;

public interface HhHopDongRepository extends BaseRepository<HhHopDongHdr, Long> {

	Optional<HhHopDongHdr> findBySoHd(String soHd);

}
