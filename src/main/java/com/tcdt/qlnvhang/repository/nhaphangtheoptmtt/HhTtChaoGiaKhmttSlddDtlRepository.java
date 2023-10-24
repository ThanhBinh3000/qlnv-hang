package com.tcdt.qlnvhang.repository.nhaphangtheoptmtt;

import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhTtChaoGiaSlddDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HhTtChaoGiaKhmttSlddDtlRepository extends JpaRepository<HhTtChaoGiaSlddDtl,Long> {
    List<HhTtChaoGiaSlddDtl> findAllByIdDiaDiem (Long idDiaDiem);

    List<HhTtChaoGiaSlddDtl> findAllByIdDiaDiemIn (List<Long> ids);

    void deleteAllByIdDiaDiem(Long idDiaDiem);
}
