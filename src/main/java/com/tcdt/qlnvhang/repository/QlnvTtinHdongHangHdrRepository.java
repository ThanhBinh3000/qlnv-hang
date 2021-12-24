package com.tcdt.qlnvhang.repository;

import java.util.Optional;

import com.tcdt.qlnvhang.table.QlnvTtinHdongHangHdr;

public interface QlnvTtinHdongHangHdrRepository extends BaseRepository<QlnvTtinHdongHangHdr, Long> {

	Optional<QlnvTtinHdongHangHdr> findBySoHdong(String soHdong);

}
