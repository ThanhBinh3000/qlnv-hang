package com.tcdt.qlnvhang.repository.nhaphang.dauthau.hopdong;

import com.tcdt.qlnvhang.repository.BaseRepository;
import com.tcdt.qlnvhang.table.HhPhuLucHd;

import java.util.List;

public interface HhPhuLucRepository extends BaseRepository<HhPhuLucHd, Long> {

	List<HhPhuLucHd> findBySoHd(String soHd);

}
