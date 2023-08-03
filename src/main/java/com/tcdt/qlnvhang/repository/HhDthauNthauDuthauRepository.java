package com.tcdt.qlnvhang.repository;

import com.tcdt.qlnvhang.table.HhDthauNthauDuthau;

import java.util.List;

public interface HhDthauNthauDuthauRepository extends BaseRepository<HhDthauNthauDuthau, Long> {

//	Optional<HhDthauGthau> findBySoQd(String soQd);

	List<HhDthauNthauDuthau> findByIdDtGtAndType (Long idGoiThau, String type);

	void deleteAllByIdDtGt(Long idGoiThau);



}
