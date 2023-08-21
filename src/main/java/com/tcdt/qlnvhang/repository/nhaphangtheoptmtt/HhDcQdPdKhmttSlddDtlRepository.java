package com.tcdt.qlnvhang.repository.nhaphangtheoptmtt;

import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhDcQdPdKhmttSlddDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HhDcQdPdKhmttSlddDtlRepository extends JpaRepository<HhDcQdPdKhmttSlddDtl,Long> {
    List<HhDcQdPdKhmttSlddDtl> findAllByIdDiaDiem (Long idDiaDiem);

    List<HhDcQdPdKhmttSlddDtl> findAllByIdDiaDiemIn (List<Long> ids);

    void deleteAllByIdDiaDiem(Long idDiaDiem);
}
