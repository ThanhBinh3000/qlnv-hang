package com.tcdt.qlnvhang.repository.khlcnt;

import com.tcdt.qlnvhang.entities.khlcnt.GoiThau;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoiThauRepository extends CrudRepository<GoiThau, Long> {
	List<GoiThau> findByKhLcntId(Long khLcntId, Pageable pageable);
	List<GoiThau> findByKhLcntId(Long khLcntId);
	Integer countByKhLcntId(Long khLcntId);
}
