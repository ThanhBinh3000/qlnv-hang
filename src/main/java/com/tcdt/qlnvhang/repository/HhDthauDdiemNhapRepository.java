package com.tcdt.qlnvhang.repository;

import com.tcdt.qlnvhang.table.HhDthauDdiemNhap;
import com.tcdt.qlnvhang.table.HhDthauGthau;

import java.util.List;
import java.util.Optional;

public interface HhDthauDdiemNhapRepository extends BaseRepository<HhDthauDdiemNhap, Long> {

//	Optional<HhDthauGthau> findBySoQd(String soQd);

	List<HhDthauDdiemNhap> findByIdDtGt (Long idGoiThau);


}
