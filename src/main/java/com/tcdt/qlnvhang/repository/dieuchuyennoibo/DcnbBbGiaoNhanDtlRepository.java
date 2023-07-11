package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBangKeCanHangDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBbGiaoNhanDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DcnbBbGiaoNhanDtlRepository extends JpaRepository<DcnbBbGiaoNhanDtl, Long> {
    List<DcnbBbGiaoNhanDtl> findByHdrId(Long id);

    List<DcnbBbGiaoNhanDtl> findByHdrIdIn(List<Long> listId);

    void deleteAllByHdrId(Long hdrId);
}
