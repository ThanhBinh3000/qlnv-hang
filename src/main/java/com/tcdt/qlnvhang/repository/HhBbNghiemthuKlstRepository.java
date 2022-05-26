package com.tcdt.qlnvhang.repository;

import java.util.Optional;

import com.tcdt.qlnvhang.table.HhBbNghiemthuKlstHdr;
import org.springframework.stereotype.Repository;

@Repository
public interface HhBbNghiemthuKlstRepository extends BaseRepository<HhBbNghiemthuKlstHdr, Long> {

	Optional<HhBbNghiemthuKlstHdr> findBySoBb(String soBb);

	Optional<HhBbNghiemthuKlstHdr> findFirstByNamAndMaDvi(Integer nam, String maDvi);
}
