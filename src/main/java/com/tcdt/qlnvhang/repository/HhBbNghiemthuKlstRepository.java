package com.tcdt.qlnvhang.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.bbnghiemthubqld.HhBbNghiemthuKlstHdr;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface HhBbNghiemthuKlstRepository extends BaseRepository<HhBbNghiemthuKlstHdr, Long> {

	Optional<HhBbNghiemthuKlstHdr> findFirstBySoBbNtBq(String soBbNtBq);

	Optional<HhBbNghiemthuKlstHdr> findFirstByNamAndMaDvi(Integer nam, String maDvi);

	List<HhBbNghiemthuKlstHdr> findByIdQdGiaoNvNhAndMaDvi(Long idQdGiaoNvNh, String maDvi);


	@Transactional
	@Modifying
	void deleteByIdIn(Collection<Long> ids);
}
