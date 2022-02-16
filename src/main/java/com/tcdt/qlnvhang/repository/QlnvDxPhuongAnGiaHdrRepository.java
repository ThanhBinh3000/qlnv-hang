package com.tcdt.qlnvhang.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.tcdt.qlnvhang.table.QlnvDxPhuongAnGiaHdr;

public interface QlnvDxPhuongAnGiaHdrRepository extends BaseRepository<QlnvDxPhuongAnGiaHdr, Long> {

	@EntityGraph(value = "QLNV_DX_PAN_GIA_HDR.children")
	Optional<QlnvDxPhuongAnGiaHdr> findById(Long id);
	
	@Transactional()
	@Modifying
	@Query(value = "UPDATE QLNV_DX_PAN_GIA_HDR SET TRANG_THAI=:trangThai WHERE SO_DXUAT IN :soDxuatList", nativeQuery = true)
	void updateTongHop(List<String> soDxuatList, String trangThai);
}