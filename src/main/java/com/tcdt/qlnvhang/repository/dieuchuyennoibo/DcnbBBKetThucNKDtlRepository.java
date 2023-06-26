package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBBKetThucNKDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DcnbBBKetThucNKDtlRepository extends JpaRepository<DcnbBBKetThucNKDtl, Long> {
    void deleteAllByHdrId(Long id);
}
