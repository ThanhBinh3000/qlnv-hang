package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBienBanLayMauDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DcnbBienBanLayMauDtlRepository extends JpaRepository<DcnbBienBanLayMauDtl,Long> {
    List<DcnbBienBanLayMauDtl> findByHdrId(Long id);

    List<DcnbBienBanLayMauDtl> findByHdrIdIn(List<Long> listId);
}
