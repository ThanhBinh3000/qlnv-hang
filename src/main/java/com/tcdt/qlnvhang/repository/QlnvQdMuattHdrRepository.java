package com.tcdt.qlnvhang.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;

import com.tcdt.qlnvhang.table.QlnvQdMuattHdr;

public interface QlnvQdMuattHdrRepository extends BaseRepository<QlnvQdMuattHdr, Long> {

	QlnvQdMuattHdr findBySoQdinh(String soQdinh);

	Optional<QlnvQdMuattHdr> findBySoQdinhAndTrangThai(String soQdinhGoc, String trangThai);

	@EntityGraph(value = "QLNV_QD_MUATT_HDR.children")
	Optional<QlnvQdMuattHdr> findById(Long id);
}
