package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBienBanTinhKhoDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DcnbBienBanTinhKhoDtlRepository extends JpaRepository<DcnbBienBanTinhKhoDtl, Long> {
    List<DcnbBienBanTinhKhoDtl> findByHdrId(Long id);

    List<DcnbBienBanTinhKhoDtl> findByHdrIdIn(List<Long> listId);
}
