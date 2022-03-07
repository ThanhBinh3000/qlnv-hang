package com.tcdt.qlnvhang.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.tcdt.qlnvhang.table.HhPaKhlcntHdr;

public interface HhPaKhlcntHdrRepository extends BaseRepository<HhPaKhlcntHdr, Long> {

	Optional<HhPaKhlcntHdr> findByIdThHdr(Long idThHdr);

	@Transactional()
	@Modifying
	@Query(value = "UPDATE HH_PA_KHLCNT_HDR SET QUYET_DINH=:trangThai WHERE ID = :idQdHdr", nativeQuery = true)
	void updateTongHop(Long idQdHdr, String trangThai);

}
