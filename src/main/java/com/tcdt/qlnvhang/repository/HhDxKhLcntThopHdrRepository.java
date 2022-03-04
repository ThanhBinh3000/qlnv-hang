package com.tcdt.qlnvhang.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.tcdt.qlnvhang.table.HhDxKhLcntThopHdr;

public interface HhDxKhLcntThopHdrRepository extends BaseRepository<HhDxKhLcntThopHdr, Long> {

	@Transactional()
	@Modifying
	@Query(value = "UPDATE HH_DX_KHLCNT_THOP_HDR SET PHUONG_AN=:trangThai WHERE ID = :idThHdr", nativeQuery = true)
	void updateTongHop(Long idThHdr, String trangThai);

}
