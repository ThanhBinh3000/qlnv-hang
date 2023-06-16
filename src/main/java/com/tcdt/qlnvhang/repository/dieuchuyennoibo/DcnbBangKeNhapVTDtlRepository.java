package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBangKeCanHangDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBangKeNhapVTDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DcnbBangKeNhapVTDtlRepository extends JpaRepository<DcnbBangKeNhapVTDtl, Long> {
    List<DcnbBangKeNhapVTDtl> findByHdrId(Long id);

    List<DcnbBangKeNhapVTDtl> findByHdrIdIn(List<Long> listId);
}
