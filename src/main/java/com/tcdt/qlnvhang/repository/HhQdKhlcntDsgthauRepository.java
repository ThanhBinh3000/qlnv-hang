package com.tcdt.qlnvhang.repository;

import com.tcdt.qlnvhang.table.HhQdKhlcntDsgthau;

import java.util.List;

public interface HhQdKhlcntDsgthauRepository extends BaseRepository<HhQdKhlcntDsgthau, Long> {

    List<HhQdKhlcntDsgthau> findByIdQdHdr(Long idQdKhlcntHdr);

}
