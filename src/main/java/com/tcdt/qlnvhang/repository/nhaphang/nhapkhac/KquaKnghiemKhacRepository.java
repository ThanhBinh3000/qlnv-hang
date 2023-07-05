package com.tcdt.qlnvhang.repository.nhaphang.nhapkhac;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kiemtracl.phieuknghiemcl.KquaKnghiem;
import com.tcdt.qlnvhang.entities.nhaphang.nhapkhac.phieuknghiemcl.KquaKnghiemKhac;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface KquaKnghiemKhacRepository extends CrudRepository<KquaKnghiemKhac, Long> {
//	Page<KquaKnghiem> findByPhieuKnghiemIdOrderBySttAsc(Long phieuKnghiemId, Pageable pageable);
//	List<KquaKnghiem> findByPhieuKnghiemId(Long phieuKnghiemId);
//
	@Transactional
	@Modifying
	void deleteByPhieuKnghiemId(Long phieuKnghiemId);

	List<KquaKnghiemKhac> findByPhieuKnghiemId(Long phieuKnghiemId);


//
//	@Transactional
//	@Modifying
//	void deleteByPhieuKnghiemIdIn(Collection<Long> phieuKnghiemIds);
//
//	@Query("SELECT kq.phieuKnghiemId, COUNT(kq.id) FROM KquaKnghiem kq WHERE kq.phieuKnghiemId IN ?1 " +
//			"GROUP BY kq.phieuKnghiemId")
//	List<Object[]> countByPhieuKnghiemIdIn(Collection<Long> phieuKnghiemIds);
}
