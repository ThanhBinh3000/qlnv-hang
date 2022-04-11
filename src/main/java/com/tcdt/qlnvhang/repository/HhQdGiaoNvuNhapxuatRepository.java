package com.tcdt.qlnvhang.repository;

import java.util.Optional;

import com.tcdt.qlnvhang.table.HhQdGiaoNvuNhapxuatHdr;

public interface HhQdGiaoNvuNhapxuatRepository extends BaseRepository<HhQdGiaoNvuNhapxuatHdr, Long> {

	Optional<HhQdGiaoNvuNhapxuatHdr> findBySoHd(String soHd);

	Optional<HhQdGiaoNvuNhapxuatHdr> findBySoQd(String soQd);

}
