package com.tcdt.qlnvhang.repository.phieuknghiemcluonghang;

import com.tcdt.qlnvhang.entities.phieuknghiemcluonghang.KquaKnghiem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KquaKnghiemRepository extends CrudRepository<KquaKnghiem, Long> {
	Page<KquaKnghiem> findByPhieuKnghiemId(Long phieuKnghiemId, Pageable pageable);
	List<KquaKnghiem> findByPhieuKnghiemId(Long phieuKnghiemId);
}
