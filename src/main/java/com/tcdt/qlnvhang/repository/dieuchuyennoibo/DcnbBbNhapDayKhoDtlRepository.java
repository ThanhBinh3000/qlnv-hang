package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBbNhapDayKhoDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DcnbBbNhapDayKhoDtlRepository extends JpaRepository<DcnbBbNhapDayKhoDtl, Long> {
    List<DcnbBbNhapDayKhoDtl> findByHdrId(Long id);

    List<DcnbBbNhapDayKhoDtl> findByHdrIdIn(List<Long> listId);

    void deleteAllByHdrId(Long id);
}
