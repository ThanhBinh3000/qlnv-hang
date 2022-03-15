package com.tcdt.qlnvhang.repository.khlcnt;

import com.tcdt.qlnvhang.entities.khlcnt.DiaDiemNhap;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface DiaDiemNhapRepository extends CrudRepository<DiaDiemNhap, Long> {
	List<DiaDiemNhap> findByGoiThauId(Long goiThauId, Pageable pageable);
	List<DiaDiemNhap> findByGoiThauId(Long goiThauId);
	Integer countByGoiThauId(Long goiThauId);
	List<DiaDiemNhap> findByGoiThauIdIn(Set<Long> goiThauIds);
}
