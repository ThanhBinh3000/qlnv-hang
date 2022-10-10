package com.tcdt.qlnvhang.repository.nhaphangtheoptmtt;

import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhQdGiaoNvNhDdiem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HhQdGiaoNvNhDdiemRepository extends JpaRepository<HhQdGiaoNvNhDdiem,Long> {
    List<HhQdGiaoNvNhDdiem> findAllByIdDtlIn(List<Long> ids);

    List<HhQdGiaoNvNhDdiem> findAllByIdDtl(Long ids);


}
