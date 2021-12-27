package com.tcdt.qlnvhang.repository;

import java.util.Optional;

import com.tcdt.qlnvhang.table.QlnvQdGiaoNhapxuatHdr;

public interface QlnvQdGiaoNhapxuatHdrRepository extends BaseRepository<QlnvQdGiaoNhapxuatHdr, Long> {

	Optional<QlnvQdGiaoNhapxuatHdr> findBySoHdong(String soHdong);

}
