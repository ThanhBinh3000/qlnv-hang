package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBienBanHaoDoiDtl;
import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBienBanTinhKhoDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DcnbBienBanHaoDoiDtlRepository extends JpaRepository<DcnbBienBanHaoDoiDtl, Long> {
    List<DcnbBienBanHaoDoiDtl> findByHdrId(Long id);

    List<DcnbBienBanHaoDoiDtl> findByHdrIdIn(List<Long> listId);
}
