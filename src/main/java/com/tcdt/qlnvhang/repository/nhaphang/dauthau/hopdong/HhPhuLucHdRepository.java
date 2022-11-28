package com.tcdt.qlnvhang.repository.nhaphang.dauthau.hopdong;

import java.util.Optional;

import com.tcdt.qlnvhang.repository.BaseRepository;
import com.tcdt.qlnvhang.table.HhPhuLucHd;

public interface HhPhuLucHdRepository extends BaseRepository<HhPhuLucHd, Long> {

	Optional<HhPhuLucHd> findBySoPluc(String soPluc);

}
