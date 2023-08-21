package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBbThuaThieuDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBcKqDcDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DcnbBbThuaThieuDtlRepository extends JpaRepository<DcnbBbThuaThieuDtl, Long> {
    List<DcnbBbThuaThieuDtl> findByHdrId(Long id);
}
