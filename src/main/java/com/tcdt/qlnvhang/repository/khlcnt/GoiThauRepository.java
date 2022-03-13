package com.tcdt.qlnvhang.repository.khlcnt;

import com.tcdt.qlnvhang.entities.khlcnt.GoiThau;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface GoiThauRepository extends CrudRepository<GoiThau, Long> {
	List<GoiThau> findByKhLcntIdIn(Set<Long> ids);
}
