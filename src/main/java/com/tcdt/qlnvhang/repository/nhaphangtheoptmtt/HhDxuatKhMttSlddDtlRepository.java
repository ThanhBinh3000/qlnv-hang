package com.tcdt.qlnvhang.repository.nhaphangtheoptmtt;

import com.tcdt.qlnvhang.entities.nhaphang.dauthau.kehoachlcnt.dexuatkhlcnt.HhDxKhlcntDsgthauCtiet;
import com.tcdt.qlnvhang.table.nhaphangtheoptt.HhDxuatKhMttSlddDtl;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface HhDxuatKhMttSlddDtlRepository extends JpaRepository<HhDxuatKhMttSlddDtl,Long> {
    List<HhDxuatKhMttSlddDtl> findByIdDiaDiem(Long idDiaDiem);

    void deleteAllByIdDiaDiem(Long idDiaDiem);

    @Transactional
    void deleteAllByIdDiaDiemIn(List<Long> idDiaDiem);

}
