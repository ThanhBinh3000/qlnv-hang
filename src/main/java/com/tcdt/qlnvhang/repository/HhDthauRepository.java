package com.tcdt.qlnvhang.repository;

import java.util.Optional;

import com.tcdt.qlnvhang.table.HhDthau;

public interface HhDthauRepository extends BaseRepository<HhDthau, Long> {

	Optional<HhDthau> findBySoQd(String soQd);

}
