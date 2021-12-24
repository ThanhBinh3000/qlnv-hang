package com.tcdt.qlnvhang.repository;

import java.util.Optional;

import com.tcdt.qlnvhang.table.QlnvQdMuattHdr;

public interface QlnvQdMuattHdrRepository extends BaseRepository<QlnvQdMuattHdr, Long> {

	QlnvQdMuattHdr findBySoQdinh(String soQdinh);

	Optional<QlnvQdMuattHdr> findBySoQdinhAndTrangThai(String soQdinhGoc, String trangThai);
}
