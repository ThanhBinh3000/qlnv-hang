package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBbKqDcDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DcnbBbKqDcDtlRepository extends JpaRepository<DcnbBbKqDcDtl, Long> {
    List<DcnbBbKqDcDtl> findByHdrId(Long id);
}
