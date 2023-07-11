package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBbChuanBiKhoDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DcnbBbChuanBiKhoDtlRepository extends JpaRepository<DcnbBbChuanBiKhoDtl, Long> {
    List<DcnbBbChuanBiKhoDtl> findByHdrId(Long id);

    List<DcnbBbChuanBiKhoDtl> findByHdrIdIn(List<Long> listId);

    void deleteAllByHdrId(Long hdrId);
}
