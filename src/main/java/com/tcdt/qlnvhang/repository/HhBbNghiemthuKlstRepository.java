package com.tcdt.qlnvhang.repository;

import java.util.Collection;
import java.util.Optional;

import com.tcdt.qlnvhang.table.HhBbNghiemthuKlstHdr;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface HhBbNghiemthuKlstRepository extends BaseRepository<HhBbNghiemthuKlstHdr, Long> {

	Optional<HhBbNghiemthuKlstHdr> findFirstBySoBb(String soBb);

	Optional<HhBbNghiemthuKlstHdr> findFirstByNamAndMaDvi(Integer nam, String maDvi);

	@Transactional
	@Modifying
	void deleteByIdIn(Collection<Long> ids);
}
