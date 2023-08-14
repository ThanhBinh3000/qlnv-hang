package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBbThuaThieuKiemKeDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DcnbBbThuaThieuKiemKeDtlRepository extends JpaRepository<DcnbBbThuaThieuKiemKeDtl, Long> {
    List<DcnbBbThuaThieuKiemKeDtl> findByHdrId(Long id);
}
