package com.tcdt.qlnvhang.repository;

import com.tcdt.qlnvhang.table.HhDthauGthau;

import java.util.Optional;

public interface HhDthauGthauRepository extends BaseRepository<HhDthauGthau, Long> {

	Optional<HhDthauGthau> findBySoQd(String soQd);

	Optional<HhDthauGthau> findByIdGoiThau (Long idGoiThau);



}
