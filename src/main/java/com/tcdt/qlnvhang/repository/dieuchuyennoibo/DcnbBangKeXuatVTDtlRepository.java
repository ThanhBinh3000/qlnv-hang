package com.tcdt.qlnvhang.repository.dieuchuyennoibo;

import com.tcdt.qlnvhang.table.dieuchuyennoibo.DcnbBangKeXuatVTDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DcnbBangKeXuatVTDtlRepository extends JpaRepository<DcnbBangKeXuatVTDtl, Long> {
    List<DcnbBangKeXuatVTDtl> findByHdrId(Long id);

    List<DcnbBangKeXuatVTDtl> findByHdrIdIn(List<Long> listId);
}
