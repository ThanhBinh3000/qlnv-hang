package com.tcdt.qlnvhang.repository;

import java.util.Optional;

import com.tcdt.qlnvhang.table.HhPhuLucHd;

public interface HhPhuLucHdRepository extends BaseRepository<HhPhuLucHd, Long> {

	Optional<HhPhuLucHd> findBySoPluc(String soPluc);

}
