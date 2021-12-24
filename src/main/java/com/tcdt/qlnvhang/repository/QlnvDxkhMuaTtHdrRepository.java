package com.tcdt.qlnvhang.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.tcdt.qlnvhang.table.QlnvDxkhMuaTtHdr;

public interface QlnvDxkhMuaTtHdrRepository extends BaseRepository<QlnvDxkhMuaTtHdr, Long> {

	@Transactional()
	@Modifying
	@Query(value = "UPDATE QLNV_DXKH_MUA_TT_HDR SET TRANG_THAI=:trangThai WHERE SO_DXUAT IN :soDxuatList", nativeQuery = true)
	void updateTongHop(List<String> soDxuatList, String trangThai);
	
	@EntityGraph(value = "QLNV_DXKH_MUA_TT_HDR.children")
	Optional<QlnvDxkhMuaTtHdr> findById(Long id);
}
