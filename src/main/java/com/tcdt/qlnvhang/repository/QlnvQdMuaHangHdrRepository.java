package com.tcdt.qlnvhang.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import com.tcdt.qlnvhang.table.QlnvQdMuaHangHdr;

public interface QlnvQdMuaHangHdrRepository extends BaseRepository<QlnvQdMuaHangHdr, Long> {

	QlnvQdMuaHangHdr findBySoQdinh(String soQdinh);

	Optional<QlnvQdMuaHangHdr> findBySoQdinhAndTrangThai(String soQdinhGoc, String trangThai);
	
	@EntityGraph(value = "QLNV_QD_MUA_HANG_HDR.children")
	Optional<QlnvQdMuaHangHdr> findById(Long id);
}
