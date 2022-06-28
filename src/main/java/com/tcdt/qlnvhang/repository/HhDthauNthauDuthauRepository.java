package com.tcdt.qlnvhang.repository;

import com.tcdt.qlnvhang.table.HhDthauGthau;
import com.tcdt.qlnvhang.table.HhDthauNthauDuthau;

import java.util.List;
import java.util.Optional;

public interface HhDthauNthauDuthauRepository extends BaseRepository<HhDthauNthauDuthau, Long> {

//	Optional<HhDthauGthau> findBySoQd(String soQd);

	List<HhDthauNthauDuthau> findByIdDtGt (Long idGoiThau);

	void deleteAllByIdDtGt(Long idGoiThau);



}
