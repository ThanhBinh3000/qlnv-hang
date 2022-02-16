package com.tcdt.qlnvhang.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.tcdt.qlnvhang.table.QlnvDxkhXuatKhacHdr;

public interface QlnvDxkhXuatKhacHdrRepository extends BaseRepository<QlnvDxkhXuatKhacHdr, Long> {
	@Transactional()
	@Modifying
	@Query(value = "UPDATE QLNV_DXKH_XUATKHAC_HDR SET TRANG_THAI=:trangThai WHERE SO_DXUAT IN :soDxuatList", nativeQuery = true)
	void updateTongHop(List<String> soDxuatList, String trangThai);
	
	@EntityGraph(value = "QLNV_DXKH_XUATKHAC_HDR.children")
	Optional<QlnvDxkhXuatKhacHdr> findById(Long id);
}
