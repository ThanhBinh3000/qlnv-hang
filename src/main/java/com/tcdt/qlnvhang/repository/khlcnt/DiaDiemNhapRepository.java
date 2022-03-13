package com.tcdt.qlnvhang.repository.khlcnt;

import com.tcdt.qlnvhang.entities.khlcnt.DiaDiemNhap;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface DiaDiemNhapRepository extends CrudRepository<DiaDiemNhap, Long> {
	List<DiaDiemNhap> findByGoiThauIdIn(Set<Long> ids);
}
