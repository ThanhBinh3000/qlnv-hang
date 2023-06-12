package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBienBanHaoDoiDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBienBanHaoDoiTtDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DcnbBienBanHaoDoiTtDtlRepository extends JpaRepository<DcnbBienBanHaoDoiTtDtl, Long> {
    List<DcnbBienBanHaoDoiTtDtl> findByHdrId(Long id);

    List<DcnbBienBanHaoDoiTtDtl> findByHdrIdIn(List<Long> listId);
}
