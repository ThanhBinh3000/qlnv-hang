package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbHoSoBienBanDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DcnbHoSoBienBanDtlRepository extends JpaRepository<DcnbHoSoBienBanDtl, Long> {
    List<DcnbHoSoBienBanDtl> findByHoSoKyThuatHdrId(Long id);

}
