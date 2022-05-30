package com.tcdt.qlnvhang.repository.phieuknghiemcluonghang;

import com.tcdt.qlnvhang.entities.phieuknghiemcluonghang.KquaKnghiem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface KquaKnghiemRepository extends CrudRepository<KquaKnghiem, Long> {
	Page<KquaKnghiem> findByPhieuKnghiemIdOrderBySttAsc(Long phieuKnghiemId, Pageable pageable);
	List<KquaKnghiem> findByPhieuKnghiemId(Long phieuKnghiemId);

	@Transactional
	@Modifying
	void deleteByphieuKnghiemId(Long phieuKnghiemId);
}
