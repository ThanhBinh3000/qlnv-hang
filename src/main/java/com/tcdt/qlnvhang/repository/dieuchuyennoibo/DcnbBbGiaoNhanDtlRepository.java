package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBangKeCanHangDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBbGiaoNhanDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DcnbBbGiaoNhanDtlRepository extends JpaRepository<DcnbBbGiaoNhanDtl, Long> {
    List<DcnbBangKeCanHangDtl> findByHdrId(Long id);

    List<DcnbBangKeCanHangDtl> findByHdrIdIn(List<Long> listId);

    void deleteAllByHdrId(Long hdrId);
}
