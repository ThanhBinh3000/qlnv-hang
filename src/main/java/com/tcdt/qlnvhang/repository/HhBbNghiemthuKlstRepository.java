package com.tcdt.qlnvhang.repository;

import java.util.Optional;

import com.tcdt.qlnvhang.table.HhBbNghiemthuKlstHdr;

public interface HhBbNghiemthuKlstRepository extends BaseRepository<HhBbNghiemthuKlstHdr, Long> {

	Optional<HhBbNghiemthuKlstHdr> findBySoBb(String soBb);

}
