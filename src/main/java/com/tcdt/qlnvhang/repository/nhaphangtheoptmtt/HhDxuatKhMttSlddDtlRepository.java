package com.tcdt.qlnvhang.repository.nhaphangtheoptmtt;

import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhDxuatKhMttSlddDtl;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface HhDxuatKhMttSlddDtlRepository extends JpaRepository<HhDxuatKhMttSlddDtl,Long> {
    List<HhDxuatKhMttSlddDtl> findAllByIdDtl(Long idDtl);

    void deleteAllByIdDtl(Long idDtl);

    @Transactional
    void deleteAllByIdDtlIn(List<Long> idDtl);

}
