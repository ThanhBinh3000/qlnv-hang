package com.tcdt.qlnvhang.repository.phieuknghiemcluonghang;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.phieuknghiemcl.KquaKnghiem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

@Repository
public interface KquaKnghiemRepository extends CrudRepository<KquaKnghiem, Long> {
//	Page<KquaKnghiem> findByPhieuKnghiemIdOrderBySttAsc(Long phieuKnghiemId, Pageable pageable);
//	List<KquaKnghiem> findByPhieuKnghiemId(Long phieuKnghiemId);
//
	@Transactional
	@Modifying
	void deleteByPhieuKnghiemId(Long phieuKnghiemId);

	List<KquaKnghiem> findByPhieuKnghiemId(Long phieuKnghiemId);


//
//	@Transactional
//	@Modifying
//	void deleteByPhieuKnghiemIdIn(Collection<Long> phieuKnghiemIds);
//
//	@Query("SELECT kq.phieuKnghiemId, COUNT(kq.id) FROM KquaKnghiem kq WHERE kq.phieuKnghiemId IN ?1 " +
//			"GROUP BY kq.phieuKnghiemId")
//	List<Object[]> countByPhieuKnghiemIdIn(Collection<Long> phieuKnghiemIds);
}
