package com.tcdt.qlnvhang.repository;

import java.util.Optional;

import com.tcdt.qlnvhang.table.HhQdPduyetKqlcntHdr;

public interface HhQdPduyetKqlcntHdrRepository extends BaseRepository<HhQdPduyetKqlcntHdr, Long> {

	Optional<HhQdPduyetKqlcntHdr> findBySoQd(String canCu);

}
