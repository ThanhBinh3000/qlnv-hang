package com.tcdt.qlnvhang.repository.nhaphangtheoptmtt;

import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhDcQdPdKhmttSlddDtl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HhDcQdPdKhmttSlddDtlRepository extends JpaRepository<HhDcQdPdKhmttSlddDtl,Long> {
    List<HhDcQdPdKhmttSlddDtl> findAllByIdSldd (Long ids);

    List<HhDcQdPdKhmttSlddDtl> findAllByIdSlddIn (List<Long> ids);
}
