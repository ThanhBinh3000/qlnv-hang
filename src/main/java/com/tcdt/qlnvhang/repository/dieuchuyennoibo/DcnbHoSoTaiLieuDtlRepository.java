package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbHoSoTaiLieuDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DcnbHoSoTaiLieuDtlRepository  extends JpaRepository<DcnbHoSoTaiLieuDtl, Long> {
    List<DcnbHoSoTaiLieuDtl> findByHoSoKyThuatHdrId(Long id);

    List<DcnbHoSoTaiLieuDtl> findByHoSoKyThuatHdrIdIn(List<Long> listId);
}
